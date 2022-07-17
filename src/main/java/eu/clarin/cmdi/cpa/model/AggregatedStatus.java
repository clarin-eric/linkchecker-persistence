package eu.clarin.cmdi.cpa.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.lang.Nullable;

import eu.clarin.cmdi.cpa.utils.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
public class AggregatedStatus {
   
   @EmbeddedId
   private AggregatedStatusId id;
   
   @Column(name = "name", insertable = false, updatable = false)
   private final String providergroupName;
   @Column(insertable = false, updatable = false)
   @Enumerated(EnumType.STRING)
   private final Category category;
   
   @Nullable
   private Double avgDuration;
   @Nullable
   private Long maxDuration;
   
   private Long number;

}
