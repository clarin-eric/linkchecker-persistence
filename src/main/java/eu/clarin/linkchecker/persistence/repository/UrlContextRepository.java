package eu.clarin.linkchecker.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;

import eu.clarin.linkchecker.persistence.model.Context;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.model.UrlContext;

public interface UrlContextRepository extends CrudRepository<UrlContext, Long> {
   
   public Optional<UrlContext> findByUrlAndContextAndExpectedMimeType(Url url, Context context, String expectedMimeType);
   @Modifying()
   @Transactional()
   @Query(
         value = """
               INSERT INTO url_context(url_id, context_id, expected_mime_type, active, ingestion_date)
               VALUES(:urlId, :contextId, :expectedMimeType, TRUE, :ingestionDate)
               ON DUPLICATE KEY UPDATE active=TRUE, ingestion_date=:ingestionDate
            """,
         nativeQuery = true
      )
   public void insertOrUpdate(@Param("urlId") Long urlId, @Param("contextId") Long contextId, @Param("expectedMimeType") String expectedMimeType, @Param("ingestionDate") LocalDateTime ingestionDate);
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("UPDATE UrlContext uc SET uc.active = false WHERE uc.active = true AND uc.ingestionDate < ?1")
   public void deactivateOlderThan(LocalDateTime dateTime);
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM UrlContext uc WHERE uc.ingestionDate < :ingestionDate") 
   public void deleteByIngestionDateBefore(@Param("ingestionDate") LocalDateTime ingestionDate);

}
