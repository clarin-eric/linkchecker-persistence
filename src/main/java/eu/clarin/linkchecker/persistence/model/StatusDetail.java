/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.model;

import eu.clarin.linkchecker.persistence.utils.Category;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


/**
 *
 */
@Data
@Entity
@IdClass(StatusDetailId.class)
public class StatusDetail {
   

   @Id
   private Long id;
   @Id
   private String providergroupname;
   @Id
   private String origin;
     
   private Long orderNr;
   
   private String urlname;

   private String method;

   private Integer statusCode;

   @Enumerated(EnumType.STRING)
   private Category category;   

   private String message;   

   private LocalDateTime checkingDate;

   private String contentType;
   
   private String expectedMimeType;

   private Long contentLength;

   private Integer duration;

   private Integer redirectCount;  

}
