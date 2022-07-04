package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import eu.clarin.cmdi.cpa.entities.Url;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {
   
   @Nullable
   public Url findByUrl(String url);
   
   @Query(
         value = "DELETE FROM url WHERE id NOT IN (SELECT url_id from url_context)",
         nativeQuery = true
      )
   public void deleteWithoutContext();

}
