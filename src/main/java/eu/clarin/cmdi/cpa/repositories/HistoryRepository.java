package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import eu.clarin.cmdi.cpa.entities.History;

public interface HistoryRepository extends PagingAndSortingRepository<History, Long> {
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM History h WHERE h.url IS NULL")
   public void deleteWithoutContext();
}
