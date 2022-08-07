package eu.clarin.cmdi.cpa.model;

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

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
@Table(name="context", indexes = {@Index(columnList = "origin, providergroup_id, expectedMimeType, user_id", unique = true)})
public class Context {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private final String origin;
   
   @OneToOne(optional = true)
   @JoinColumn(name = "providergroup_id", referencedColumnName = "id")
   private final Providergroup providergroup;
   
   private final String expectedMimeType;
   
   @OneToOne
   @JoinColumn(name = "user_id", referencedColumnName = "id")
   private final User user;
   
   @OneToMany
   @JoinColumn(name = "context_id", referencedColumnName = "id")
   private List<UrlContext> urlContexts = new ArrayList<UrlContext>();
   

}
