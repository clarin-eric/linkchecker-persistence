package eu.clarin.cmdi.cpa.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
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
