package eu.clarin.cmdi.cpa.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.cmdi.cpa.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
   
   
   public Optional<Client> findByUsername(String username);

}
