package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.AggregatedStatus;
import eu.clarin.linkchecker.persistence.model.AggregatedStatusId;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface AggregatedStatusRepository extends CrudRepository<AggregatedStatus, AggregatedStatusId> {

    Stream<AggregatedStatus> findAllByProvidergroupName(String providergroupName);
}
