package eu.clarin.linkchecker.persistence.repository;

import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.AggregatedStatus;

public interface AggregatedStatusRepository extends CrudRepository<AggregatedStatus, Long> {
   
   Stream<AggregatedStatus> findAllByProvidergroupName(String providergroupName);
   
}
