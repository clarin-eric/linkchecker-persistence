package eu.clarin.linkchecker.persistence.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.Context;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.model.UrlContext;

public interface UrlContextRepository extends CrudRepository<UrlContext, Long> {
   
   public Optional<UrlContext> findByUrlAndContextAndExpectedMimeType(Url url, Context context, String expectedMimeType);
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("UPDATE UrlContext uc SET uc.active = false WHERE uc.active = true AND uc.ingestionDate < ?1")
   public void deactivateOlderThan(LocalDateTime dateTime);
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM UrlContext uc WHERE uc.ingestionDate < ?1") 
   public void deleteOlderThan(LocalDateTime dateTime);

}
