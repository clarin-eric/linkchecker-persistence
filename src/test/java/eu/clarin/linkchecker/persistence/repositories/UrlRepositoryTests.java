package eu.clarin.linkchecker.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import eu.clarin.linkchecker.persistence.model.Client;
import eu.clarin.linkchecker.persistence.model.Context;
import eu.clarin.linkchecker.persistence.model.Role;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.model.UrlContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootTest
class UrlRepositoryTests extends RepositoryTests{
   

	@Test
	void save() {
	   
	   // the URL mustn't be null
	   assertThrows(NullPointerException.class, () -> uRep.save(new Url(null, null, null)));
	   
	   // saving
	   Url url = new Url("http://www.wowasa.com", "www.wowasa.com", true);
	   uRep.save(url);
	   assertEquals(1, uRep.count());
	   
	   // the same URL mustn't be saved twice
	   assertThrows(DataAccessException.class, () -> uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true)));
	   
	   // deleting
	   uRep.delete(url);
	   assertEquals(0, uRep.count());
	}
	
	@Transactional
	@Test
	void getNextUrlsToCheck() {
	   
	   final Random random = new Random();
	   
	   final String[] groupKeys = {"key1", "key2", "key3"};
	   final Url[] urls = new Url[100];
	   
	   final Client client = usRep.save(new Client("wowasa", "xxxxxxxx", Role.ADMIN));
	   
	   final Context context = cRep.save(new Context("origin", null, client));
	   
	   IntStream.range(0, 100).forEach(i -> {
	      
	      urls[i] = new Url("http://www.wowasa.com?page=" +i, groupKeys[random.nextInt(3)], true);
	      
	      uRep.save(urls[i]);
	      
	      UrlContext urlContext = new UrlContext(urls[i], context, LocalDateTime.now().minusDays(100 +i), true);
	      urlContext.setActive(true);
	      
	      ucRep.save(urlContext);
	      
	   });
	   
	   try(Stream<Url> stream = uRep.getNextUrlsToCheck(100, LocalDateTime.now())){
	      assertEquals(100, stream.count());
	   }
	   
	   IntStream.range(0, 1).forEach(i -> {
	       try(Stream<Url> stream = uRep.getNextUrlsToCheck(100, LocalDateTime.now())){
	            assertEquals(Arrays.stream(urls).filter(url -> url.getGroupKey().equals(groupKeys[i])).count(), stream.filter(url -> url.getGroupKey().equals(groupKeys[i])).count());
	         }
	   });
	}
	
	@Test
	void countByUrlContextActive() {
	   
	   Client client = usRep.save(new Client("wowasa", "xxxxxxxx", Role.ADMIN));
	   Context context = cRep.save(new Context("origin", null, client));
	   
	   IntStream.range(0, 6).forEach(i -> {	
	      
	      UrlContext urlContext = new UrlContext(uRep.save(new Url("http://www.wowasa.com?page=" +i, "www.wowasa.com", true)), context, LocalDateTime.now(), true);
	      urlContext.setActive(true);
	      
	      ucRep.save(urlContext);
	   });
      IntStream.range(6, 10).forEach(i -> {  
         
         UrlContext urlContext = new UrlContext(uRep.save(new Url("http://www.wowasa.com?page=" +i, "www.wowasa.com", true)), context, LocalDateTime.now(), true);
         urlContext.setActive(false);
         
         ucRep.save(urlContext);
      });	   
	   assertEquals(6, uRep.countByUrlContextsActive(true));
	   assertEquals(4, uRep.countByUrlContextsActive(false));
	}
}
