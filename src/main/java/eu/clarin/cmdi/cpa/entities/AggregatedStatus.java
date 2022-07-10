package eu.clarin.cmdi.cpa.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import eu.clarin.cmdi.cpa.utils.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
@Table(name = "aggregatedStatus")
public class AggregatedStatus {
   
   @EmbeddedId
   private AggregatedStatusId id;
   
   @Column(name = "name")
   private final String providerGroupName;
   
   private final Category category;
   
   private Long avgDuration;
   
   private Long maxDuration;
   
   private Long number;

}
