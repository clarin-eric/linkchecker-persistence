package eu.clarin.cmdi.cpa.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.model.Providergroup;

public interface ProvidergroupRepository extends CrudRepository<Providergroup, Long> {
   
   @Nullable
   public Providergroup findByName(String name); 
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM Providergroup p WHERE p NOT IN (SELECT c.providergroup FROM Context c)")
   public void deleteWithoutContext();

}
