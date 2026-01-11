package eu.clarin.linkchecker.persistence.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "urlContexts")
@EqualsAndHashCode(exclude = "urlContexts")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Entity
@Table(name="context", indexes = {@Index(columnList = "origin, providergroup_id, client_id", unique = true)})
public class Context {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private final String origin;
   
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "providergroup_id")
   private final Providergroup providergroup;
   
   @OneToOne
   @JoinColumn(name = "client_id")
   private final Client client;
   
   @OneToMany(mappedBy = "context")
   private List<UrlContext> urlContexts = new ArrayList<UrlContext>();
   

}
