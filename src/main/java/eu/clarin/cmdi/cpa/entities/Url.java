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

import lombok.Data;

@Data
@Entity
@Table(name="url", indexes = {@Index(columnList = "url", unique = true)})
public class Url {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   private String url;
   
   @OneToMany
   @JoinColumn(name = "url_id", referencedColumnName = "id")
   private Set<UrlContext> urlContexts;

}
