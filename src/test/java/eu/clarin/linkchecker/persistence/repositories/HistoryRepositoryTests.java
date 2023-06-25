package eu.clarin.linkchecker.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.linkchecker.persistence.model.History;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.utils.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

@SpringBootTest
class HistoryRepositoryTests extends RepositoryTests {

	@Test
	void save() {
	   
	   History history = new History(uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true)), Category.Broken, LocalDateTime.now());
	   
	   hRep.save(history);
	   
	   assertEquals(1, hRep.count());
	}
	
	@Test
	@Transactional
	void deleteByCheckingDateBefore() {
	   
	   LocalDateTime now = LocalDateTime.now();
	   
	   IntStream.range(0, 100)
	      .forEach(i -> hRep.save(new History(uRep.save(new Url("http://www.wowasa.com" +i, "www.wowasa.com" +i, true)), Category.Broken, now.minusDays(i))));
	   
	   hRep.deleteByCheckingDateBefore(now.minusDays(50).plusSeconds(1));
      assertEquals(50, hRep.count()); 
	}
}
