package eu.clarin.linkchecker.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@ToString(exclude = "contexts")
@EqualsAndHashCode(exclude = "contexts")
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Entity
@Table(indexes = {@Index(columnList = "name", unique = true)})
public class Providergroup {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @NonNull
   private final String name;
   
   @OneToMany(mappedBy = "providergroup", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
   private Set<Context> contexts;

}
