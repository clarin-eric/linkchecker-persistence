package eu.clarin.cmdi.cpa.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.cmdi.cpa.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
   
   
   public Optional<User> findByName(String username);

}
