package eu.clarin.cmdi.cpa.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.Context;
import eu.clarin.cmdi.cpa.entities.Url;
import eu.clarin.cmdi.cpa.entities.UrlContext;

public interface UrlContextRepository extends CrudRepository<UrlContext, Long> {
   
   @Nullable
   public UrlContext findByUrlAndContext(Url url, Context context);
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("UPDATE UrlContext uc SET uc.active = false WHERE uc.active = true AND uc.ingestionDate < ?1")
   public void deactivateOlderThan(LocalDateTime dateTime);
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM UrlContext uc WHERE uc.ingestionDate < ?1") 
   public void deleteOlderThan(LocalDateTime dateTime);

}
