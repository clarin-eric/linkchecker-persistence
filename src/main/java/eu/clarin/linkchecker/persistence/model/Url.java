package eu.clarin.linkchecker.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@ToString(exclude = {"status", "urlContexts"})
@EqualsAndHashCode(exclude = {"status", "urlContexts"})
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
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
   
   @OneToOne(mappedBy = "url", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
   private Status status;
   
   @OneToOne(mappedBy = "url", fetch = FetchType.LAZY,   cascade = CascadeType.ALL)
   private History history;

   @OneToMany(mappedBy = "url", fetch =  FetchType.LAZY, cascade = CascadeType.ALL)
   private Set<UrlContext> urlContexts;

}
