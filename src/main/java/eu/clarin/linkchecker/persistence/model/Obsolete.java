package eu.clarin.linkchecker.persistence.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import eu.clarin.linkchecker.persistence.utils.Category;
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
   private LocalDateTime checkingDate;
   
   @NonNull
   @Enumerated(EnumType.STRING)
   private Category category;   
   @NonNull
   private String message;   
   @NonNull
   private LocalDateTime deletionDate;

}
