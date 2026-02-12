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
