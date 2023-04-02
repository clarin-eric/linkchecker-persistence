package eu.clarin.linkchecker.persistence.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
