package eu.clarin.linkchecker.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDetailId implements Serializable {
   
   private static final long serialVersionUID = 1L;

   private Long id;

   private String providergroupname;

   private String origin;

}
