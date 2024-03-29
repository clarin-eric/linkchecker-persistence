/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repositories;

import java.util.List;
import java.util.stream.IntStream;


import jakarta.persistence.Tuple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
      List<Tuple> list = gRep.findAll("select * from url", true);       
      assertEquals(5, list.size());
      // jpql query
      list = gRep.findAll("select u from Url u", false);
      assertEquals(5, list.size());
   }
   
   @Test
   @Transactional
   void findAllProvidergroup() {
      
      IntStream.range(0, 5).forEach(i -> pRep.save(new Providergroup("pg" +i)));      
      // native query
      List<Tuple> list = gRep.findAll("select * from providergroup", true);       
      assertEquals(5, list.size());      
      // jpql query
      list = gRep.findAll("select p from Providergroup p", false);       
      assertEquals(5, list.size());      
   }
}
