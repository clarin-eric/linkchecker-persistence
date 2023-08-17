/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;

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
      
      return (isNative?em.createNativeQuery(query, Tuple.class):em.createQuery(query, Tuple.class)).getResultStream();   
   }
}
