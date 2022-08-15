package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.model.User;
import eu.clarin.cmdi.cpa.model.Context;
import eu.clarin.cmdi.cpa.model.Providergroup;
import eu.clarin.cmdi.cpa.model.Role;
import eu.clarin.cmdi.cpa.model.Url;
import eu.clarin.cmdi.cpa.model.UrlContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

@SpringBootTest
class ContextRepositoryTests extends RepositoryTests {

   @Test
   @Transactional
   void deleteWithoutContext() {

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      User user = usRep.save(new User("wowasa", "########", Role.ADMIN));

      Context contextWith = cRep.save(new Context("origin1", null, null, user));

      cRep.save(new Context("origin2", null, null, user));
      
      UrlContext urlContext = new UrlContext(url, contextWith, LocalDateTime.now(), true);
      urlContext.setActive(true);     

      ucRep.save(urlContext);

      assertEquals(2, cRep.count());

      cRep.deleteWithoutContext();

      assertEquals(1, cRep.count());

   }

   @Test
   void findByOriginAndProvidergroupAndExpectedMimeTypeAndClient() {

      User client = usRep.save(new User("wowasa", "########", Role.ADMIN));

      cRep.save(new Context("origin1", null, null, client));

      Providergroup providergroup = pRep.save(new Providergroup("wowasa's pg"));

      Context context = new Context("origin1", providergroup, "application/pdf", client);

      cRep.save(context);

      assertEquals(2, cRep.count());

      assertTrue(
            cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndUser("origin1", null, "some mime type", client).isEmpty());

      assertFalse(cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndUser("origin1", null, null, client).isEmpty());

      assertFalse(cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndUser("origin1", providergroup,
            "application/pdf", client).isEmpty());

   }

}
