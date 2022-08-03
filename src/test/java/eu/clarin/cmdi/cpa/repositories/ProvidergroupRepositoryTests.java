package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.model.Client;
import eu.clarin.cmdi.cpa.model.Context;
import eu.clarin.cmdi.cpa.model.Providergroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import javax.transaction.Transactional;

@SpringBootTest
class ProvidergroupRepositoryTests extends RepositoryTests{

	@Test
	void findByName() {
	   
	   pRep.save(new Providergroup("wowasa's pg"));
	   
	   assertEquals(1, pRep.count());
	   assertFalse(pRep.findByName("wowasa's pg").isEmpty());
	   assertTrue(pRep.findByName("other's pg").isEmpty());
	}
	
	@Transactional
	@Test
	void deleteWithoutContext() {
	   
	   Providergroup providergroup = pRep.save(new Providergroup("wowasa's pg"));
	   pRep.save(new Providergroup("other's pg"));
	   
	   Client client = clRep.save(new Client("wowasa", "devnull@wowasa.com", "xxxxxxxx"));
	   Context context = new Context("origin1", providergroup, null, client);

	   cRep.save(context);
	   
	   assertEquals(2, pRep.count());
	   assertEquals(1, cRep.count());
	   
	  pRep.deleteWithoutContext();
	  assertEquals(1, pRep.count());
	   
	}

}
