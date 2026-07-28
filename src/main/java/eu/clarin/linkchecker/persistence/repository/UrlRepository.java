package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.model.UrlCount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {

    Optional<Url> findByName(String name);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Url u WHERE u NOT IN (SELECT DISTINCT uc.url FROM UrlContext uc)")
    void deleteByUrlContextsIsEmpty();

    long countByUrlContextsActive(boolean active);

    @Query("SELECT COUNT(*) FROM Url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON p.name=:providergroupName")
    long countByProvidergroupName(@Param("providergroupName") String providergroupName);

    @Query("SELECT DISTINCT COUNT(u.id) FROM Url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON p.name=:providergroupName")
    long countDistinctByProvidergroupName(@Param("providergroupName") String providergroupName);

    @Query(
            """
                    SELECT new eu.clarin.linkchecker.persistence.model.UrlCount(p.name, COUNT(u), COUNT(DISTINCT u))
                            FROM Url u
                                    JOIN u.urlContexts uc
                                            JOIN uc.context c
                                                    JOIN c.providergroup p
                                                            WHERE uc.active = true
                                                                    GROUP BY p.id
                    """
    )
    Stream<UrlCount> aggregateCountUrl();

}
