package eu.clarin.linkchecker.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import lombok.*;
import org.springframework.lang.Nullable;

import eu.clarin.linkchecker.persistence.utils.Category;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Entity
@IdClass(AggregatedStatusId.class)
public class AggregatedStatus {

   @Id
   private final String providergroupName;
   @Enumerated(EnumType.STRING)
   @Id
   private final Category category;   

   private final Double avgDuration;

   private final Integer maxDuration;
   
   private final Long number;
   
   private final Long numberWithDuration;

}
