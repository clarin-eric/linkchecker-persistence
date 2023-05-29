/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repositories;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

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
   ApplicationContext ctx;
   
   @Test
   @Transactional
   public void findAllUrl() {
      
      
      
      IntStream.range(0, 5).forEach(i -> uRep.save(new Url("http://www.wowasa.com" +i, "wowasa.com", true)));      

      GenericRepository<Url> gRep = ctx.getBean(GenericRepository.class);
      Stream<Url> stream = gRep.findAll("select * from url");       
      assertEquals(5, stream.count());
      
   }
   
   @Test
   @Transactional
   public void findAllProvidergroup() {
      
      IntStream.range(0, 5).forEach(i -> pRep.save(new Providergroup("pg" +i)));      

      GenericRepository<Providergroup> gRep = ctx.getBean(GenericRepository.class);
      Stream<Providergroup> stream = gRep.findAll("select * from providergroup"); 
      
      assertEquals(5, stream.count());      
   }
}
