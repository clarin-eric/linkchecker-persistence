package eu.clarin.cmdi.cpa.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.entities.Client;
import eu.clarin.cmdi.cpa.repositories.ClientRepository;
import eu.clarin.cmdi.cpa.repositories.RepositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.transaction.Transactional;

@SpringBootTest
class LinkServiceTests extends RepositoryTests{
  
   
   @Autowired
   private LinkService lService;

   @Transactional
	@Test
	void save() {
      
      Client client = clRep.save(new Client("devnull@wowasa.com", "xxxxxxxx"));
      
      IntStream.range(0, 3).forEach(i -> {
         
         lService.save(client, "http://www.wowasa.com?page=0", "origin0", null, null);
         
      });
      
      assertEquals(1, uRep.count());
      assertEquals(1, ucRep.count());
      assertEquals(1, cRep.count());
      assertEquals(0, pRep.count());
      
      lService.save(client, "http://www.wowasa.com?page=0", "origin1", null, null);
      
      assertEquals(1, uRep.count());
      assertEquals(2, ucRep.count());
      assertEquals(2, cRep.count());
      assertEquals(0, pRep.count());
      
      lService.save(client, "http://www.wowasa.com?page=0", "origin1", "pg0", null);
      
      assertEquals(1, uRep.count());
      assertEquals(3, ucRep.count());
      assertEquals(3, cRep.count());
      assertEquals(1, pRep.count());
      
      lService.save(client, "http://www.wowasa.com?page=0", "origin1", "pg0", "application/xml");
      
      assertEquals(1, uRep.count());
      assertEquals(4, ucRep.count());
      assertEquals(4, cRep.count());
      assertEquals(1, pRep.count());
      
      lService.save(client, "http://www.wowasa.com?page=1", "origin1", "pg0", "application/xml");
      
      assertEquals(2, uRep.count());
      assertEquals(5, ucRep.count());
      assertEquals(4, cRep.count());
      assertEquals(1, pRep.count());
      
	}
   
   @Test
   void deactivateLinksAfter() {
      
      Client client = clRep.save(new Client("devnull@wowasa.com", "xxxxxxxx"));
      
      IntStream.range(0, 15).forEach(i -> {
         lService.save(client, "http://www.wowasa.com?page=" +i, "origin1", "pg0", "application/xml", LocalDateTime.now().minusDays(i));
      });
      
      assertEquals(15, uRep.countByUrlContextsActive(true));
      
      lService.deactivateLinksOlderThan(7);
      
      assertEquals(7, uRep.countByUrlContextsActive(true));
      assertEquals(8, uRep.countByUrlContextsActive(false));
      
   }

}
