/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import java.util.stream.Stream;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;

import org.hibernate.jpa.AvailableHints;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Component;


/**
 *
 */
@Component
@Scope("prototype")
public class GenericRepositoryImpl implements GenericRepository {
   
   @PersistenceContext
   EntityManager em;
 
   @SuppressWarnings("unchecked")
   @Override
   public Stream<Tuple> findAll(String query, boolean isNative) {
      
      return (isNative?em.createNativeQuery(query, Tuple.class):em.createQuery(query, Tuple.class))
            .setHint(AvailableHints.HINT_FETCH_SIZE, "1")
            .setHint(AvailableHints.HINT_CACHEABLE, "false")
            .setHint(AvailableHints.HINT_READ_ONLY, "true")
            .getResultStream();   
   }
}
