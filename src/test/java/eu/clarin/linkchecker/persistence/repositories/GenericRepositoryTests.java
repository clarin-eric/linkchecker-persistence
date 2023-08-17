/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repositories;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.linkchecker.persistence.model.Providergroup;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.repository.GenericRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
@SpringBootTest
public class GenericRepositoryTests extends RepositoryTests {
   
   @Autowired
   GenericRepository gRep;
   
   @Test
   @Transactional
   void findAllUrl() {
      
      
      
      IntStream.range(0, 5).forEach(i -> uRep.save(new Url("http://www.wowasa.com" +i, "wowasa.com", true)));      
      //native query
      Stream<Tuple> stream = gRep.findAll("select * from url", true);       
      assertEquals(5, stream.count());
      // jpql query
      stream = gRep.findAll("select u from Url u", false);
      assertEquals(5, stream.count());
   }
   
   @Test
   @Transactional
   void findAllProvidergroup() {
      
      IntStream.range(0, 5).forEach(i -> pRep.save(new Providergroup("pg" +i)));      
      // native query
      Stream<Tuple> stream = gRep.findAll("select * from providergroup", true);       
      assertEquals(5, stream.count());      
      // jpql query
      stream = gRep.findAll("select p from Providergroup p", false);       
      assertEquals(5, stream.count());      
   }
}
