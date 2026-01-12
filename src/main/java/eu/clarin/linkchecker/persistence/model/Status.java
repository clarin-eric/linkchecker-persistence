package eu.clarin.linkchecker.persistence.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
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
   
   @OneToOne(fetch = FetchType.LAZY)
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
