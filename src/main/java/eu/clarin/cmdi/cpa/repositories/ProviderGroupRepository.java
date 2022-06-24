package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.ProviderGroup;

public interface ProviderGroupRepository extends CrudRepository<ProviderGroup, Long> {
   
   @Nullable
   public ProviderGroup findByName(String name); 

}
