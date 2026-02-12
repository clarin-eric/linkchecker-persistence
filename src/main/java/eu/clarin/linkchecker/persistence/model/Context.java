package eu.clarin.linkchecker.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
   
   @OneToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "client_id")
   private final Client client;
   
   @OneToMany(mappedBy = "context",  fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
   private Set<UrlContext> urlContexts;
   

}
