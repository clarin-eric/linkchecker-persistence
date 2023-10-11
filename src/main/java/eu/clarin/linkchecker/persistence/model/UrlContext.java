package eu.clarin.linkchecker.persistence.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "url_context", indexes = @Index(columnList = "url_id, context_id, expectedMimeType", unique = true))
public class UrlContext {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @ManyToOne
   @JoinColumn(name = "url_id")
   @NonNull
   private final Url url;
   
   @ManyToOne
   @JoinColumn(name = "context_id")
   @NonNull
   private final Context context;
   
   private String expectedMimeType;
   @NonNull
   private LocalDateTime ingestionDate;
   @NonNull
   private Boolean active;

}
