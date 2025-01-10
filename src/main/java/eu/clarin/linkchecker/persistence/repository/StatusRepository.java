package eu.clarin.linkchecker.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.Status;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.utils.Category;


public interface StatusRepository extends CrudRepository<Status, Long> {
   
   
   Optional<Status> findByUrl(Url url);
   
   Optional<Status> findByUrlName(String name);
   
   Stream<Status> findAllByUrlNameIn(String... names);
   
   Stream<Status> findAllByCategory(Category category);
   
   Stream<Status> findAllByUrlUrlContextsContextClientName(String name);
   
   Stream<Status> findAllByUrlUrlContextsContextClientNameAndUrlUrlContextsContextOrigin(String name, String origin);
   
   
   @Query("SELECT s FROM Status s JOIN s.url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON uc.active = true AND p.name=?1 AND s.category=?2")
   Stream<Status> findAllByProvidergroupAndCategory(String providergroupName, Category category);
   
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
