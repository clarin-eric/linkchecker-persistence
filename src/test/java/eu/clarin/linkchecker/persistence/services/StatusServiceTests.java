package eu.clarin.linkchecker.persistence.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import eu.clarin.linkchecker.persistence.model.Status;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.repositories.RepositoryTests;
import eu.clarin.linkchecker.persistence.service.StatusService;
import eu.clarin.linkchecker.persistence.utils.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootTest
class StatusServiceTests extends RepositoryTests{
  
   
   @Autowired
   private StatusService sService;

   @Transactional
	@Test
	void save() {
      
      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));
      
      sService.save(new Status(url, Category.Broken, "", LocalDateTime.now()));
      
      assertEquals(1, sRep.count());
      assertEquals(0, hRep.count());
      
      //saving a record for same url copies old record to history
      sService.save(new Status(url, Category.Broken, "", LocalDateTime.now()));
      
      assertEquals(1, sRep.count());
      assertEquals(1, hRep.count());
      
	}
   
   @Test
   void getStatus() {
      
      IntStream
         .range(0, 100)
         .forEach(i -> {
               Url url = uRep.save(new Url("http://www.wowasa.com/page" +i, "www.wowasa.com", true));
               sService.save(new Status(url, Category.Broken, "", LocalDateTime.now()));
            });
      
      assertEquals(0, sService.getStatus("http://www.wowasa.com/page100").size());
      assertEquals(1, sService.getStatus("http://www.wowasa.com/page100", "http://www.wowasa.com/page0").size());
      assertEquals(2, sService.getStatus("http://www.wowasa.com/page1", "http://www.wowasa.com/page0").size());
   }

}
