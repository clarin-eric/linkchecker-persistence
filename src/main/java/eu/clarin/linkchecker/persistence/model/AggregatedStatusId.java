package eu.clarin.linkchecker.persistence.model;

import java.io.Serializable;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregatedStatusId implements Serializable {
   
   private static final long serialVersionUID = 1L;

   private String providergroupName;
   @Enumerated(EnumType.STRING)
   private Category category;
}
