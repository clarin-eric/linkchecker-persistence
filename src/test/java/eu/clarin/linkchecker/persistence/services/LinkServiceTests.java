package eu.clarin.linkchecker.persistence.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;

import eu.clarin.linkchecker.persistence.model.Client;
import eu.clarin.linkchecker.persistence.model.Role;
import eu.clarin.linkchecker.persistence.repositories.RepositoryTests;
import eu.clarin.linkchecker.persistence.service.LinkService;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
class LinkServiceTests extends RepositoryTests{
  
   
   @Autowired
   ApplicationContext ctx;

	@Test
	void save() {
	   
	   LinkService lService = ctx.getBean(LinkService.class);
      
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
	   
	   LinkService lService = ctx.getBean(LinkService.class);
	   
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
	void savePerOrigin() {
	   
	     LinkService lService = ctx.getBean(LinkService.class);
	      
        Client client = usRep.save(new Client("wowasa", UUID.randomUUID().toString(), Role.ADMIN));
        
        Collection<Pair<String,String>> urlMimes = IntStream
              .range(0, 5)
              .mapToObj(i -> Pair.of("http://www.wowasa.com?page=" +i, "application/jpg"))
              .collect(Collectors.toList());
        
        IntStream.range(0, 3).forEach(originNr -> {
           
           lService.savePerOrigin(client, "pg1", "origin" + originNr, urlMimes);
           
        });	
        
        assertEquals(5, uRep.count());
        assertEquals(1, pRep.count());
        assertEquals(3, cRep.count());
	}
	
	@Test
   void multithreadedSavePerOrigin() {
	   
      LinkService lService = ctx.getBean(LinkService.class);
      
      Client client = usRep.save(new Client("wowasa", UUID.randomUUID().toString(), Role.ADMIN));
      
      Collection<Pair<String,String>> urlMimes = IntStream
            .range(0, 5)
            .mapToObj(i -> Pair.of("http://www.wowasa.com?page=" +i, "application/jpg"))
            .collect(Collectors.toList());
      
      ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(30);
      
      IntStream.range(0, 30).forEach(threadNr -> {
         
         executor.submit(() -> {
            lService.savePerOrigin(client, "pg1", "origin" + threadNr, urlMimes);
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
      
      assertEquals(5, uRep.count());
      assertEquals(1, pRep.count());
      assertEquals(30, cRep.count());
	}
   
   @Test
   void deactivateLinksOlderThan() {
      
      LinkService lService = ctx.getBean(LinkService.class);
      
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
      
      LinkService lService = ctx.getBean(LinkService.class);
      
      Client client = usRep.save(new Client("wowasa", "xxxxxxxx", Role.ADMIN));
      
      IntStream.range(0, 100).forEach(i -> {
         lService.save(client, "http://www.wowasa.com?page=" +i, "origin1", "pg0", "application/xml", LocalDateTime.now().minusDays(i));
      });
      
      assertEquals(100, uRep.countByUrlContextsActive(true));  
      
      lService.deleteLinksOderThan(30);
      
      assertEquals(30, uRep.countByUrlContextsActive(true));  
           
   }
}
