package eu.clarin.linkchecker.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.clarin.linkchecker.persistence.model.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {
   
   Optional<Url> findByName(String name);
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM Url u WHERE u NOT IN (SELECT DISTINCT uc.url FROM UrlContext uc)")
   void deleteByUrlContextsIsEmpty();
   
   @Query(
         value = """
            SELECT id, name, group_key, valid, priority FROM 
               (SELECT ROW_NUMBER() OVER (PARTITION BY u.group_key ORDER BY u.priority DESC, s.checking_date) AS order_Nr, u.id, u.name, u.group_key, u.valid, u.priority, s.checking_date 
               FROM url u 
               LEFT JOIN status s ON s.url_id = u.id 
               WHERE u.valid=true 
               AND u.id IN (SELECT uc.url_id FROM url_context uc WHERE uc.active = true) 
               AND (s.checking_date IS NULL OR s.checking_date < ?2)
               ORDER BY u.group_key, u.priority DESC, s.checking_date) tab1
            WHERE order_nr <= ?1 
            ORDER by tab1.priority DESC, tab1.checking_date    
            """,
          nativeQuery = true     
      )
   Stream<Url> getNextUrlsToCheck(int groupLimit, LocalDateTime maximalCheckingDate);
   
   long countByUrlContextsActive(boolean active);
   
   @Query("SELECT COUNT(*) FROM Url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON p.name=?1")
   long countByProvidergroupName(String providergroupName);
   
   @Query("SELECT DISTINCT COUNT(u.id) FROM Url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON p.name=?1")
   long countDistinctByProvidergroupName(String providergroupName);

}
