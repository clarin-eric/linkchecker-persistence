package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.Context;
import eu.clarin.cmdi.cpa.entities.ProviderGroup;

public interface ContextRepository extends CrudRepository<Context, Long> {
   
   @Nullable
   public Context findByOriginAndProviderGroupAndExpectedMimeTypeAndSource(String origin, ProviderGroup providerGroup, String expectedMimeType, String source); 
   
   @Modifying
   @Query("DELETE FROM Context c WHERE c.urlContexts IS EMPTY")
   public void deleteWithoutContext();
}
