package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.Context;
import eu.clarin.cmdi.cpa.entities.Url;
import eu.clarin.cmdi.cpa.entities.UrlContext;

public interface UrlContextRepository extends CrudRepository<UrlContext, Long> {
   
   @Nullable
   UrlContext findByUrlAndContext(Url url, Context context);

}
