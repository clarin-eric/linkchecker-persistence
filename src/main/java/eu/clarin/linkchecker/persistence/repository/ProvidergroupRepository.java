package eu.clarin.linkchecker.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.Providergroup;

public interface ProvidergroupRepository extends CrudRepository<Providergroup, Long> {
   
   public Optional<Providergroup> findByName(String name); 
   
   public void deleteByContextsIsEmpty();

}
