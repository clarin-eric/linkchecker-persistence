package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.entities.AggregatedStatus;

public interface AggregatedStatusRepository extends CrudRepository<AggregatedStatus, Long> {
   
   @Nullable
   AggregatedStatus findByProviderGroupName(String providerGroupName);
   
}
