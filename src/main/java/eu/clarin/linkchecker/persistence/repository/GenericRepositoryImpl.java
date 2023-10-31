/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;

import java.util.List;

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
   public List<Tuple> findAll(String query, boolean isNative) {
      
      return (isNative?em.createNativeQuery(query, Tuple.class):em.createQuery(query, Tuple.class))
            .getResultList();
   }
}
