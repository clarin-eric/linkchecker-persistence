package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
   
   
   Optional<Client> findByName(String name);

}
