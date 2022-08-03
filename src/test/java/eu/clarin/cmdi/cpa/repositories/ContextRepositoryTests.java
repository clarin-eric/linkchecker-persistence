package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.model.Client;
import eu.clarin.cmdi.cpa.model.Context;
import eu.clarin.cmdi.cpa.model.Providergroup;
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

      Client client = clRep.save(new Client("wowasa", "devnull@wowasa.com", "########"));

      Context contextWith = cRep.save(new Context("origin1", null, null, client));

      cRep.save(new Context("origin2", null, null, client));
      
      UrlContext urlContext = new UrlContext(url, contextWith);
      urlContext.setIngestionDate(LocalDateTime.now());
      urlContext.setActive(true);
      

      ucRep.save(urlContext);

      assertEquals(2, cRep.count());

      cRep.deleteWithoutContext();

      assertEquals(1, cRep.count());

   }

   @Test
   void findByOriginAndProvidergroupAndExpectedMimeTypeAndClient() {

      Client client = clRep.save(new Client("wowasa", "devnull@wowasa.com", "########"));

      cRep.save(new Context("origin1", null, null, client));

      Providergroup providergroup = pRep.save(new Providergroup("wowasa's pg"));

      Context context = new Context("origin1", providergroup, "application/pdf", client);

      cRep.save(context);

      assertEquals(2, cRep.count());

      assertTrue(
            cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndClient("origin1", null, "some mime type", client).isEmpty());

      assertFalse(cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndClient("origin1", null, null, client).isEmpty());

      assertFalse(cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndClient("origin1", providergroup,
            "application/pdf", client).isEmpty());

   }

}
