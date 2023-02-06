package eu.clarin.linkchecker.persistence.repository;

import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.AggregatedStatus;
import eu.clarin.linkchecker.persistence.model.AggregatedStatusId;

public interface AggregatedStatusRepository extends CrudRepository<AggregatedStatus, AggregatedStatusId> {
   
   Stream<AggregatedStatus> findAllByProvidergroupName(String providergroupName);
   
}
