package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.Status;
import eu.clarin.cmdi.cpa.entities.Url;

public interface StatusRepository extends PagingAndSortingRepository<Status, Long> {
   
   @Nullable
   public Status findByUrl(Url url);

}
