package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.ProviderGroup;

public interface ProviderGroupRepository extends CrudRepository<ProviderGroup, Long> {
   
   @Nullable
   public ProviderGroup findByName(String name); 
   
   @Query(
         value = "DELETE FROM providerGroup WHERE id NOT IN (SELECT providerGroup_id from context)",
         nativeQuery = true
      )
   public void deleteWithoutContext();

}
