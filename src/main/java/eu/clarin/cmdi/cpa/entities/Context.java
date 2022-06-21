package eu.clarin.cmdi.cpa.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="context", indexes = {@Index(columnList = "record, providerGroup_id, expectedMimeType, source", unique = true)})
public class Context {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private String source;
   
   private String record;
   
   @OneToOne(optional = true)
   @JoinColumn(name = "providerGroup_id", referencedColumnName = "id")
   private ProviderGroup providerGroup;
   
   private String expectedMimeType;
   
   @OneToMany
   @JoinColumn(name = "context_id", referencedColumnName = "id")
   private Set<UrlContext> urlContexts;

}
