package eu.clarin.cmdi.cpa.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Index;
import javax.persistence.JoinColumn;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
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
   
   @OneToMany
   @JoinColumn(name = "url_id", referencedColumnName = "id")
   private List<UrlContext> urlContexts = new ArrayList<UrlContext>();

}
