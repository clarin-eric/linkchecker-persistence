package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import eu.clarin.cmdi.cpa.entities.History;

public interface HistoryRepository extends PagingAndSortingRepository<History, Long> {

}
