package eu.clarin.linkchecker.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.linkchecker.persistence.model.Client;
import eu.clarin.linkchecker.persistence.model.Context;
import eu.clarin.linkchecker.persistence.model.Providergroup;
import eu.clarin.linkchecker.persistence.model.Role;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.model.UrlContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

@SpringBootTest
class ContextRepositoryTests extends RepositoryTests {

   @Test
   @Transactional
   void deleteWithoutContext() {

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      Client client = usRep.save(new Client("wowasa", "########", Role.ADMIN));

      Context contextWith = cRep.save(new Context("origin1", null, client));

      cRep.save(new Context("origin2", null, client));
      
      UrlContext urlContext = new UrlContext(url, contextWith, LocalDateTime.now(), true);
      urlContext.setActive(true);     

      ucRep.save(urlContext);

      assertEquals(2, cRep.count());

      cRep.deleteWithoutContext();

      assertEquals(1, cRep.count());

   }

   @Test
   void findByOriginAndProvidergroupAndClient() {

      Client client = usRep.save(new Client("wowasa", "########", Role.ADMIN));

      cRep.save(new Context("origin1", null, client));

      Providergroup providergroup = pRep.save(new Providergroup("wowasa's pg"));

      Context context = new Context("origin1", providergroup, client);

      cRep.save(context);

      assertEquals(2, cRep.count());

      assertFalse(cRep.findByOriginAndProvidergroupAndClient("origin1", null, client).isEmpty());

      assertFalse(cRep.findByOriginAndProvidergroupAndClient("origin1", providergroup, client).isEmpty());

   }

}
