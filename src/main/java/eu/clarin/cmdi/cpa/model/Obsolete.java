package eu.clarin.cmdi.cpa.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import eu.clarin.cmdi.cpa.utils.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Entity
public class Obsolete {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private String urlName;
   
   private String userName;
   
   private String origin;
   
   private String prividergroupName;
   
   private String expectedMimeType;
   
   private String method;
   private Integer statusCode;
   private String contentType;
   private Long contentLength;
   private Integer duration;
   private Integer redirectCount;
   @NonNull
   @Enumerated(EnumType.STRING)
   private final Category category;   
   @NonNull
   private final String message;   
   @NonNull
   private final LocalDateTime checkingDate;

}
