package eu.clarin.cmdi.cpa.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "url_context")
public class UrlContext {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @ManyToOne
   @JoinColumn(name = "url_id", referencedColumnName = "id")
   @NonNull
   private Url url;
   
   @ManyToOne
   @JoinColumn(name = "context_id", referencedColumnName = "id")
   @NonNull
   private Context context;
   
   private Timestamp ingestionDate;
   
   private Boolean active;

}
