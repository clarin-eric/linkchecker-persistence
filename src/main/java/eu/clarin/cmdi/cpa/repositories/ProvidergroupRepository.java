package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.Providergroup;

public interface ProvidergroupRepository extends CrudRepository<Providergroup, Long> {
   
   @Nullable
   public Providergroup findByName(String name); 
   
   @Modifying
   @Query("DELETE FROM Providergroup p WHERE p NOT IN (SELECT c.providergroup FROM Context c)")
   public void deleteWithoutContext();

}