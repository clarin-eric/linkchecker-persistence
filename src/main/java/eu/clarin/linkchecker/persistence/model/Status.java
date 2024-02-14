package eu.clarin.linkchecker.persistence.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

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
@Table(name = "status", indexes = {@Index(columnList = "url_id", unique = true), @Index(columnList = "category", unique = false)})
public class Status {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;   
   private String method;
   private Integer statusCode;
   private String contentType;
   private Long contentLength;
   private Integer duration;

   private Integer redirectCount;
   
   @OneToOne(cascade = CascadeType.REMOVE)
   @JoinColumn(name = "url_id")
   @NonNull
   private final Url url;
   @NonNull
   @Enumerated(EnumType.STRING)
   private final Category category;   
   @NonNull
   private final String message;   
   @NonNull
   private final LocalDateTime checkingDate;
   
}
