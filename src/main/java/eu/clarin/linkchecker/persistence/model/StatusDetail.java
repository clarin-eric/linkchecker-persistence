/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.Data;


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
