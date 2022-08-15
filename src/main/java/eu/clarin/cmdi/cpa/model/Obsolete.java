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
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
public class Obsolete {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @NonNull
   private String urlName;
   
   private String clientName;
   
   private String origin;
   
   private String providergroupName;
   
   private String expectedMimeType;
   
   private String method;
   private Integer statusCode;
   private String contentType;
   private Long contentLength;
   private Integer duration;
   private Integer redirectCount;
   @NonNull
   @Enumerated(EnumType.STRING)
   private Category category;   
   @NonNull
   private String message;   
   @NonNull
   private LocalDateTime checkingDate;

}
