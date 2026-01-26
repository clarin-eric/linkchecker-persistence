package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.AggregatedStatus;
import eu.clarin.linkchecker.persistence.model.Status;
import eu.clarin.linkchecker.persistence.model.StatusDetail;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.utils.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;


public interface StatusRepository extends CrudRepository<Status, Long> {
   
   
   Optional<Status> findByUrl(Url url);
   
   Optional<Status> findByUrlName(String name);
   
   Stream<Status> findAllByUrlNameIn(String... names);
   
   Stream<Status> findAllByCategory(Category category);
   
   Stream<Status> findAllByUrlUrlContextsContextClientName(String name);
   
   Stream<Status> findAllByUrlUrlContextsContextClientNameAndUrlUrlContextsContextOrigin(String name, String origin);
   
   
   @Query("SELECT s FROM Status s JOIN s.url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON uc.active = true AND p.name= :providergroupName AND s.category= :category")
   Stream<Status> findAllByProvidergroupAndCategory(@Param("providergroupName") String providergroupName, @Param("category") Category category);

   @Query(
        """
        SELECT new eu.clarin.linkchecker.persistence.model.AggregatedStatus(p.name, s.category, avg(s.duration), max(s.duration), count(u.id), sum(CASE WHEN s.duration IS NULL THEN 1 ELSE 0 END))
                FROM Providergroup p
                        JOIN p.contexts c
                                JOIN c.urlContexts uc
                                        JOIN uc.url u
                                                JOIN u.status s
                                                        WHERE uc.active = true
                                                                GROUP BY p.id, s.category
        """
   )
   Stream<AggregatedStatus> findAggregatedStatus();

   @Query(
        """
            SELECT new eu.clarin.linkchecker.persistence.model.StatusDetail(
                        p.name, 
                        c.origin, 
                        u.name, 
                        s.method, 
                        s.statusCode, 
                        s.category, 
                        s.message, 
                        s.checkingDate, 
                        s.contentType, 
                        uc.expectedMimeType, 
                        s.contentLength, 
                        s.duration, 
                        s.redirectCount
                    )
                    FROM Providergroup p
                            JOIN p.contexts c
                                    JOIN c.urlContexts uc
                                            JOIN uc.url u
                                                    JOIN u.status s
                                                            WHERE uc.active = true
                                                                    AND p.name = :providergroupName
                                                                        AND s.category = :category
                                                                                ORDER BY s.checkingDate DESC 
        """
   )
   Stream<StatusDetail> findStatusDetail(@Param("providergroupName") String providergroupName, @Param("category") Category  category);

    @Query(
            """
                SELECT new eu.clarin.linkchecker.persistence.model.StatusDetail(
                            p.name,
                            c.origin,
                            u.name,
                            s.method,
                            s.statusCode,
                            s.category,
                            s.message,
                            s.checkingDate,
                            s.contentType,
                            uc.expectedMimeType,
                            s.contentLength,
                            s.duration,
                            s.redirectCount
                        )
                        FROM Providergroup p
                                JOIN p.contexts c
                                        JOIN c.urlContexts uc
                                                JOIN uc.url u
                                                        JOIN u.status s
                                                                    WHERE uc.active = true
                                                                            AND s.category = :category
                                                                                    ORDER BY s.checkingDate DESC
            """
    )
    Stream<StatusDetail> findStatusDetail(@Param("category") Category  category);
   
   @Query(
         value = """
                INSERT INTO obsolete (url_name, client_name, providergroup_name, origin, expected_mime_type, ingestion_date, status_code, message, category, method, content_type, content_length, duration, checking_date, redirect_count, deletion_date) 
                SELECT u.name, cl.name, p.name, c.origin, uc.expected_mime_type, uc.ingestion_date, s.status_code, s.message, s.category, s.method, s.content_type, s.content_length, s.duration, s.checking_date, s.redirect_count, NOW() 
                FROM url_context uc 
                INNER JOIN (url u) 
                ON u.id=uc.url_id 
                INNER JOIN (context c) 
                ON c.id=uc.context_id 
                INNER JOIN providergroup p 
                ON p.id=c.providergroup_id 
                INNER JOIN status s 
                ON s.url_id=u.id 
                INNER JOIN client cl 
                ON cl.id=c.client_id 
                WHERE uc.ingestion_date < ?1
               """, 
         nativeQuery = true
      )
   @Modifying
   void saveStatusLinksOlderThan(LocalDateTime dateTime);

}
