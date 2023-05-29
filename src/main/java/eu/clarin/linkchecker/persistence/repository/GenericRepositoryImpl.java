/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import java.lang.reflect.Type;
import java.util.stream.Stream;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Scope("prototype")
public class GenericRepositoryImpl<T> implements GenericRepository<T> {
   
   @PersistenceContext
   EntityManager em;
 
   @SuppressWarnings("unchecked")
   @Override
   public Stream<T> findAll(String nativeQuery) {
 
         return em.createNativeQuery(nativeQuery)
               .getResultStream();

   }
}
