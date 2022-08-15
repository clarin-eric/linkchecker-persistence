package eu.clarin.cmdi.cpa.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name="history", indexes = {@Index(columnList = "url_id, checkingDate", unique = true)})
public class History {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private String method;
   
   private Integer statusCode;
   
   private String contentType;
   
   private Long contentLength;
   
   private Integer duration;

   
   private String message;
   
   private Integer redirectCount;
   
   @OneToOne(cascade = CascadeType.REMOVE)
   @JoinColumn(name = "url_id")
   private final Url url;
   
   @NonNull
   private final Category category;
   
   @NonNull 
   private final LocalDateTime checkingDate;
}
