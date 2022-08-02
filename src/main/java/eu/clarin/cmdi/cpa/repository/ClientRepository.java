package eu.clarin.cmdi.cpa.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
   
   @Nullable
   public Client findByUsernameAndToken(String username, String token);

}
