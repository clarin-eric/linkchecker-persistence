package eu.clarin.linkchecker.persistence.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import eu.clarin.linkchecker.persistence.model.Client;
import eu.clarin.linkchecker.persistence.model.History;
import eu.clarin.linkchecker.persistence.model.Obsolete;
import eu.clarin.linkchecker.persistence.model.Role;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.repositories.RepositoryTests;
import eu.clarin.linkchecker.persistence.service.LinkService;
import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
class LinkServiceTests extends RepositoryTests{
  
   
   @Autowired
   LinkService lService;
   
   //private LinkService lService;

	@Test
	@Transactional
	void save() {
      
      Client client = usRep.save(new Client("wowasa", UUID.randomUUID().toString(), Role.ADMIN));
      
      IntStream.range(0, 3).forEach(i -> {
         
         lService.save(client, "http://www.wowasa.com?page=0", "origin0", null, "application/xml");
         
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
      assertEquals(3, cRep.count());
      assertEquals(1, pRep.count());
      
      lService.save(client, "http://www.wowasa.com?page=1", "origin1", "pg0", "application/xml");
      
      assertEquals(2, uRep.count());
      assertEquals(5, ucRep.count());
      assertEquals(3, cRep.count());
      assertEquals(1, pRep.count());
      
	}
	
	@Test
	void multithreadedSave() {
	   
	   Client client = usRep.save(new Client("wowasa", "xxxxxxxx", Role.ADMIN));
	   
	   ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(30);
	   
	   IntStream.range(0, 30).forEach(threadNr -> {
	      
	        executor.submit(() -> {
	           
	           IntStream.range(0, 30).forEach(loopNr -> {
	              lService.save(
	                    client, 
	                    "http://www.wowasa.com?thread=" + threadNr + "&loop=" +loopNr,
	                    "origin" + (loopNr%3), 
	                    "pg" + (loopNr%5),
	                    "application/xml");
	           });
	         });
	      
	   });
	   while(executor.getActiveCount() > 0) {
	      try {
            Thread.sleep(5000);
         }
         catch (InterruptedException e) {
            
            log.error("", e);
         }
	   }
	   
	   assertEquals(900, uRep.count());
	   assertEquals(5, pRep.count());
	   assertEquals(15, cRep.count());
	   
	}
   
   @Test
   void deactivateLinksOlderThan() {
      
      Client client = usRep.save(new Client("wowasa", "xxxxxxxx", Role.ADMIN));
      
      IntStream.range(0, 15).forEach(i -> {
         lService.save(client, "http://www.wowasa.com?page=" +i, "origin1", "pg0", "application/xml", LocalDateTime.now().minusDays(i));
      });
      
      assertEquals(15, uRep.countByUrlContextsActive(true));
      
      lService.deactivateLinksOlderThan(7);
      
      assertEquals(7, uRep.countByUrlContextsActive(true));
      assertEquals(8, uRep.countByUrlContextsActive(false));
      
   }
   
   @Test
   void deleteLinksOlderThan() {
      
      Client client = usRep.save(new Client("wowasa", "xxxxxxxx", Role.ADMIN));
      
      IntStream.range(0, 100).forEach(i -> {
         lService.save(client, "http://www.wowasa.com?page=" +i, "origin1", "pg0", "application/xml", LocalDateTime.now().minusDays(i));
      });
      
      assertEquals(100, uRep.countByUrlContextsActive(true));  
      
      lService.deleteLinksOderThan(30);
      
      assertEquals(30, uRep.countByUrlContextsActive(true));  
           
   }
   
   @Test
   void purgeHistory() {
      
      LocalDateTime now = LocalDateTime.now();
      
      IntStream.range(0, 100)
         .forEach(i -> hRep.save(new History(uRep.save(new Url("http://www.wowasa.com" +i, "www.wowasa.com" +i, true)), Category.Broken, now.minusDays(i))));
      
      lService.purgeHistory(50);
      assertEquals(50, hRep.count());      
   }
   
   @Test
   void purgeObsolete() {
      
      LocalDateTime now = LocalDateTime.now();
      
      IntStream.range(0, 100)         
         .forEach(i -> {
            Obsolete obs = new Obsolete("http://www.wowasa.com", Category.Ok, "", now);
            
            obs.setCheckingDate(now.minusDays(i));           
            oRep.save(obs);
         });
      
      
      lService.purgeObsolete(50);
      assertEquals(50, oRep.count());            
   }
}
