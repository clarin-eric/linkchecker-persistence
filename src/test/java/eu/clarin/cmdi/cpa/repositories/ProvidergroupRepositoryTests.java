package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.entities.Client;
import eu.clarin.cmdi.cpa.entities.Context;
import eu.clarin.cmdi.cpa.entities.Providergroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.transaction.Transactional;

@SpringBootTest
class ProvidergroupRepositoryTests extends RepositoryTests{

	@Test
	void findByName() {
	   
	   pRep.save(new Providergroup("wowasa's pg"));
	   
	   assertEquals(1, pRep.count());
	   assertNotNull(pRep.findByName("wowasa's pg"));
	   assertNull(pRep.findByName("other's pg"));
	}
	
	@Transactional
	@Test
	void deleteWithoutContext() {
	   
	   Providergroup providergroup = pRep.save(new Providergroup("wowasa's pg"));
	   pRep.save(new Providergroup("other's pg"));
	   
	   Client client = clRep.save(new Client("devnull@wowasa.com", "xxxxxxxx"));
	   Context context = new Context("origin1", client);
	   context.setProvidergroup(providergroup);
	   cRep.save(context);
	   
	   assertEquals(2, pRep.count());
	   assertEquals(1, cRep.count());
	   
	  pRep.deleteWithoutContext();
	  assertEquals(1, pRep.count());
	   
	}

}
