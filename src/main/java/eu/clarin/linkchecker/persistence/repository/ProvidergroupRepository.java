package eu.clarin.linkchecker.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.Providergroup;

public interface ProvidergroupRepository extends CrudRepository<Providergroup, Long> {
   
   public Optional<Providergroup> findByName(String name); 
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM Providergroup p WHERE p NOT IN (SELECT DISTINCT c.providergroup FROM Context c)")
   public void deleteByContextsIsEmpty();

}
