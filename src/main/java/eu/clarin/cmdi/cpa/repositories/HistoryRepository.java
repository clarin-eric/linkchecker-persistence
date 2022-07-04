package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import eu.clarin.cmdi.cpa.entities.History;

public interface HistoryRepository extends PagingAndSortingRepository<History, Long> {
   
   @Query(
         value = "DELETE FROM history WHERE url_id NOT IN (SELECT url_id from url_context)",
         nativeQuery = true
      )
   public void deleteWithoutContext();
}
