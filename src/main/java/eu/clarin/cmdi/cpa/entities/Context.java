package eu.clarin.cmdi.cpa.entities;

import java.util.ArrayList;
import java.util.List;

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
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="context", indexes = {@Index(columnList = "origin, providerGroup_id, expectedMimeType, source", unique = true)})
public class Context {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private String origin;
   
   @OneToOne(optional = true)
   @JoinColumn(name = "providerGroup_id", referencedColumnName = "id")
   private ProviderGroup providerGroup;
   
   private String expectedMimeType;
   
   private String source;
   
   @OneToMany
   @JoinColumn(name = "context_id", referencedColumnName = "id")
   private List<UrlContext> urlContexts = new ArrayList<UrlContext>();

}
