package eu.clarin.linkchecker.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import org.springframework.lang.Nullable;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
@IdClass(AggregatedStatusId.class)
public class AggregatedStatus {
   
   @Column(name = "name", insertable = false, updatable = false)
   @Id
   private final String providergroupName;
   @Column(insertable = false, updatable = false)
   @Enumerated(EnumType.STRING)
   @Id
   private final Category category;   
   @Nullable
   private Double avgDuration;
   @Nullable
   private Long maxDuration;
   
   private Long numberId;
   
   private Long numberDuration;

}
