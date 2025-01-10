package eu.clarin.linkchecker.persistence.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
   
   
   Optional<Client> findByName(String name);

}
