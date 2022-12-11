package eu.clarin.linkchecker.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.Data;


@Data
@Embeddable
public class LatestStatusId implements Serializable {
   
   private static final long serialVersionUID = 1L;


   private Long id;

   private String providergroupname;
   
   private String origin;
}
