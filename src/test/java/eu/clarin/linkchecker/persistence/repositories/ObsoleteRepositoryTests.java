/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repositories;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import eu.clarin.linkchecker.persistence.model.Obsolete;
import eu.clarin.linkchecker.persistence.utils.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
@SpringBootTest
public class ObsoleteRepositoryTests extends RepositoryTests {
   
   @Test
   @Transactional
   void deleteByCheckingDateBefore() {
      
      LocalDateTime now = LocalDateTime.now();
      
      IntStream.range(0, 100)         
         .forEach(i -> {
            Obsolete obs = new Obsolete("http://www.wowasa.com", Category.Ok, "", now);
            
            obs.setCheckingDate(now.minusDays(i));           
            oRep.save(obs);
         });
      
      
      oRep.deleteByCheckingDateBefore(now.minusDays(50).plusSeconds(1));
      assertEquals(50, oRep.count());      
   }
}
