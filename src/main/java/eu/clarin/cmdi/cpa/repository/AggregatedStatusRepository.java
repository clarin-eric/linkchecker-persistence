package eu.clarin.cmdi.cpa.repository;

import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.cmdi.cpa.model.AggregatedStatus;

public interface AggregatedStatusRepository extends CrudRepository<AggregatedStatus, Long> {
   
   Stream<AggregatedStatus> findAllByProvidergroupName(String providergroupName);
   
}
