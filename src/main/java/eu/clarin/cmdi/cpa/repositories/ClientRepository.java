package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
   
   @Nullable
   public Client findByEmailAndToken(String email, String token);

}
