package eu.clarin.linkchecker.persistence.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"status", "urlContexts"})
@EqualsAndHashCode(exclude = {"status", "urlContexts"})
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
@Table(name="url", indexes = {@Index(columnList = "name", unique = true)})
public class Url {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @NonNull
   private final String name;
   
   private final String groupKey;
   
   private final Boolean valid;
   
   private int priority;
   
   @OneToOne(mappedBy = "url", fetch = FetchType.LAZY)
   @PrimaryKeyJoinColumn
   private Status status;
   
   @OneToOne(mappedBy = "url", fetch = FetchType.LAZY)
   @PrimaryKeyJoinColumn
   private History history;

   @OneToMany
   @JoinColumn(name = "url_id", referencedColumnName = "id")
   private List<UrlContext> urlContexts = new ArrayList<UrlContext>();

}
