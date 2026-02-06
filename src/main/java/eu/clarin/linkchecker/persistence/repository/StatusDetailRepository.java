package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.StatusDetail;
import eu.clarin.linkchecker.persistence.model.StatusDetailId;
import eu.clarin.linkchecker.persistence.utils.Category;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface StatusDetailRepository extends Repository<StatusDetail, StatusDetailId> {



    @Query(
            """
            SELECT new StatusDetail(p.name, c.origin, uc.expectedMimeType, u.name, s.method, s.statusCode, s.category, s.message, s.checkingDate, s.contentType, s.contentLength, s.duration, s.redirectCount)
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
    Stream<StatusDetail> findByProvidergroupnameAndCategory(@Param("providergroupName") String providergroupName, @Param("category") Category category);

    @Query(
            """
            SELECT new StatusDetail(p.name, c.origin, uc.expectedMimeType, u.name, s.method, s.statusCode, s.category, s.message, s.checkingDate, s.contentType, s.contentLength, s.duration, s.redirectCount)
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
    Stream<StatusDetail> findByCategory(@Param("category") Category category);
}
