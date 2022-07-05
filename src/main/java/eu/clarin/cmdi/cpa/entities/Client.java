package eu.clarin.cmdi.cpa.entities;

import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
public class Client {
   
   private Long id;
   
   private final String email;
   
   private final String token;
   
   private Long quota; 

}
