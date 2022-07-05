package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.ProviderGroup;

public interface ProviderGroupRepository extends CrudRepository<ProviderGroup, Long> {
   
   @Nullable
   public ProviderGroup findByName(String name); 
   
   @Modifying
   @Query("DELETE FROM ProviderGroup p WHERE p NOT IN (SELECT c.providerGroup FROM Context c)")
   public void deleteWithoutContext();

}
