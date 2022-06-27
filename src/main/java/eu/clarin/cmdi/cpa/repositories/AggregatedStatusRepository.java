package eu.clarin.cmdi.cpa.repositories;

import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.cmdi.cpa.entities.AggregatedStatus;

public interface AggregatedStatusRepository extends CrudRepository<AggregatedStatus, Long> {
   
   Stream<AggregatedStatus> findAllByProviderGroupName(String providerGroupName);
   
}
