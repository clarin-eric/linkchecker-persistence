package eu.clarin.linkchecker.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.Client;
import eu.clarin.linkchecker.persistence.model.Context;
import eu.clarin.linkchecker.persistence.model.Providergroup;

public interface ContextRepository extends CrudRepository<Context, Long> {
   
   Optional<Context> findByOriginAndProvidergroupAndClient(String origin, Providergroup providergroup, Client client);
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM Context c WHERE c NOT IN (SELECT uc.context FROM UrlContext uc)")
   void deleteWithoutContext();
}
