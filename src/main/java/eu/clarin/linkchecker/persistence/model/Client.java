package eu.clarin.linkchecker.persistence.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Entity
@Table(indexes = {@Index(columnList = "name", unique = true)})
public class Client {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @NonNull
   private String name;
   @NonNull
   private String password;   
   
   private String email;
   
   private Long quota; 
   @NonNull
   @Enumerated(EnumType.STRING)
   private Role role;
   
   private Boolean enabled;

}
