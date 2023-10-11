package eu.clarin.linkchecker.persistence.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.Data;


@Data
public class AggregatedStatusId implements Serializable {
   
   private static final long serialVersionUID = 1L;

   @Column(name = "name")
   private String providergroupName;
   @Enumerated(EnumType.STRING)
   private Category category;
}
