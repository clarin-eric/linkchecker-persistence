/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.model;

import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.Data;


/**
 *
 */
@Data
@Entity
public class LatestStatus {
   
   @EmbeddedId
   private LatestStatusId latestStatusId;
   
   private String urlname;
   
   private String method;
   
   private Integer statusCode;   
   @Enumerated(EnumType.STRING)
   private Category category;   

   private String message;   

   private LocalDateTime checkingDate;
   
   private String contentType;
   
   private Long contentLength;
   
   private Integer duration;

   private Integer redirectCount;  

}
