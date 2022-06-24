package eu.clarin.cmdi.cpa.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Index;
import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="url", indexes = {@Index(columnList = "url", unique = true)})
public class Url {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @NonNull
   private String url;
   
   @OneToMany
   @JoinColumn(name = "url_id", referencedColumnName = "id")
   private Set<UrlContext> urlContexts;

}
