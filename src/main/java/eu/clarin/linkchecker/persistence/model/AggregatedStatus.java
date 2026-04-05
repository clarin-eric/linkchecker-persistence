package eu.clarin.linkchecker.persistence.model;

import eu.clarin.linkchecker.persistence.utils.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

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
