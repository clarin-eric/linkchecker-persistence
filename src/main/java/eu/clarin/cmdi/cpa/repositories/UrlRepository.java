package eu.clarin.cmdi.cpa.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.clarin.cmdi.cpa.entities.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {
   
   public Optional<Url> findById(Long id);
   
   public Optional<Url> findByUrl(String url);

}
