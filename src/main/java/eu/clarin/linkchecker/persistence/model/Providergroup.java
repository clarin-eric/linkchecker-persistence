package eu.clarin.linkchecker.persistence.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "contexts")
@EqualsAndHashCode(exclude = "contexts")
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
@Table(indexes = {@Index(columnList = "name", unique = true)})
public class Providergroup {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @NonNull
   private final String name;
   
   @OneToMany(mappedBy = "providergroup")
   private List<Context> contexts;

}
