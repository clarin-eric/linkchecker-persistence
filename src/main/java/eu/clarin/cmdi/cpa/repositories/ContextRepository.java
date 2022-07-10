package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.Client;
import eu.clarin.cmdi.cpa.entities.Context;
import eu.clarin.cmdi.cpa.entities.Providergroup;

public interface ContextRepository extends CrudRepository<Context, Long> {
   
   @Nullable
   public Context findByOriginAndProvidergroupAndExpectedMimeTypeAndClient(String origin, Providergroup providergroup, String expectedMimeType, Client client); 
   
   @Modifying
   @Query("DELETE FROM Context c WHERE c.urlContexts IS EMPTY")
   public void deleteWithoutContext();
}
