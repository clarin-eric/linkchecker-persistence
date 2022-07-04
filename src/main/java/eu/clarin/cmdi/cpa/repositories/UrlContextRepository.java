package eu.clarin.cmdi.cpa.repositories;

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
   
   @Query(
         value = "UPDATE url_context uc SET uc.active = false WHERE active = true AND timestampdiff(day, uc.ingestionDate, now()) > :periodInDays", 
         nativeQuery = true
      )
   @Modifying
   public void deactivateAfter(int periodInDays);
   
   @Query(
         value = "DELETE FROM url_context WHERE timestampdiff(day, ingestionDate, now()) > ?1",
         nativeQuery = true
      )
   @Modifying   
   public void deleteByPeriod(int periodInDays);

}
