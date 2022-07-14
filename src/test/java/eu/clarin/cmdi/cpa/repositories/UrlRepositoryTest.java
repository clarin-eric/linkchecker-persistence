package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import eu.clarin.cmdi.cpa.entities.Client;
import eu.clarin.cmdi.cpa.entities.Context;
import eu.clarin.cmdi.cpa.entities.Url;
import eu.clarin.cmdi.cpa.entities.UrlContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.transaction.Transactional;


@SpringBootTest
class UrlRepositoryTest extends RepositoryTests{
   

	@Test
	void save() {
	   
	   // the URL mustn't be null
	   assertThrows(NullPointerException.class, () -> uRep.save(new Url(null)));
	   
	   // saving
	   Url url = new Url("http://www.wowasa.com");
	   uRep.save(url);
	   assertEquals(1, uRep.count());
	   
	   // the same URL mustn't be saved twice
	   assertThrows(DataAccessException.class, () -> uRep.save(new Url("http://www.wowasa.com")));
	   
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
	   
	   final Client client = clRep.save(new Client("devnull@wowasa.com", "xxxxxxxx"));
	   
	   final Context context = cRep.save(new Context("origin", client));
	   
	   IntStream.range(0, 100).forEach(i -> {
	      
	      urls[i] = new Url("http://www.wowasa.com?page=" +i);
	      
	      urls[i].setGroupKey(groupKeys[random.nextInt(3)]);
	      urls[i].setValid(true);
	      
	      uRep.save(urls[i]);
	      
	      ucRep.save(new UrlContext(urls[i], context, LocalDateTime.now().minusDays(100 +i), true));
	      
	   });
	   
	   try(Stream<Url> stream = uRep.getNextUrlsToCheck(100)){
	      assertEquals(100, stream.count());
	   }
	   
	   IntStream.range(0, 1).forEach(i -> {
	       try(Stream<Url> stream = uRep.getNextUrlsToCheck(100)){
	            assertEquals(Arrays.stream(urls).filter(url -> url.getGroupKey().equals(groupKeys[i])).count(), stream.filter(url -> url.getGroupKey().equals(groupKeys[i])).count());
	         }
	   });
	}
}
