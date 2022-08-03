package eu.clarin.cmdi.cpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import eu.clarin.cmdi.cpa.model.Client;
import eu.clarin.cmdi.cpa.model.Context;
import eu.clarin.cmdi.cpa.model.Providergroup;

public interface ContextRepository extends CrudRepository<Context, Long> {
   
   public Optional<Context> findByOriginAndProvidergroupAndExpectedMimeTypeAndClient(String origin, Providergroup providergroup, String expectedMimeType, Client client); 
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM Context c WHERE c.urlContexts IS EMPTY")
   public void deleteWithoutContext();
}
