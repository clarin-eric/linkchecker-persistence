package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.model.User;
import eu.clarin.cmdi.cpa.model.Context;
import eu.clarin.cmdi.cpa.model.Providergroup;
import eu.clarin.cmdi.cpa.model.Role;

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
	   
	   User user = usRep.save(new User("wowasa", "xxxxxxxx", Role.ADMIN));
	   Context context = new Context("origin1", providergroup, null, user);

	   cRep.save(context);
	   
	   assertEquals(2, pRep.count());
	   assertEquals(1, cRep.count());
	   
	  pRep.deleteWithoutContext();
	  assertEquals(1, pRep.count());
	   
	}

}
