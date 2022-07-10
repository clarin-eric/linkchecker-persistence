package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import eu.clarin.cmdi.cpa.entities.Url;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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
}
