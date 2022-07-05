package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.cmdi.cpa.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

}
