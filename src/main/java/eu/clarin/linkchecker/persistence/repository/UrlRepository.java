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
   
   public Optional<Url> findByName(String name);
   
   @Modifying
   @Query("DELETE FROM Url u WHERE u.urlContexts IS EMPTY")
   public void deleteWithoutContext();
   
   @Query(
         value = """
            SELECT id, name, group_key, valid FROM 
               (SELECT ROW_NUMBER() OVER (PARTITION BY u.group_key ORDER BY s.checking_date) AS order_Nr, u.id, u.name, u.group_key, u.valid, s.checking_date 
               FROM url u 
               LEFT JOIN status s ON s.url_id = u.id 
               INNER JOIN url_context uc ON u.id = uc.url_id 
               WHERE u.valid=true 
               AND uc.active = true 
               AND (s.checking_date IS NULL OR s.checking_date < ?2)
               ORDER BY u.group_key, s.recheck DESC, s.checking_date) tab1 
               WHERE order_nr <= ?1 
               ORDER by tab1.checking_date    
               """,
          nativeQuery = true     
      )
   public Stream<Url> getNextUrlsToCheck(int maxPerGroup, LocalDateTime maximalCheckingDate);
   
   public long countByUrlContextsActive(boolean active);
   
   @Query("SELECT COUNT(*) FROM Url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON p.name=?1")
   public long countByProvidergroupName(String providergroupName);
   
   @Query("SELECT DISTINCT COUNT(*) FROM Url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON p.name=?1")
   public long countDinstinctByProvidergroupName(String providergroupName);

}
