package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.model.Client;
import eu.clarin.cmdi.cpa.model.Context;
import eu.clarin.cmdi.cpa.model.Providergroup;
import eu.clarin.cmdi.cpa.model.Url;
import eu.clarin.cmdi.cpa.model.UrlContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

@SpringBootTest
class ContextRepositoryTests extends RepositoryTests {

   @Test
   @Transactional
   void deleteWithoutContext() {

      Url url = uRep.save(new Url("http://www.wowasa.com"));

      Client client = clRep.save(new Client("wowasa", "devnull@wowasa.com", "########"));

      Context contextWith = cRep.save(new Context("origin1", client));

      cRep.save(new Context("origin2", client));
      
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

      cRep.save(new Context("origin1", client));

      Providergroup providergroup = pRep.save(new Providergroup("wowasa's pg"));

      Context context = new Context("origin1", client);
      context.setProvidergroup(providergroup);
      context.setExpectedMimeType("application/pdf");
      cRep.save(context);

      assertEquals(2, cRep.count());

      assertNull(
            cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndClient("origin1", null, "some mime type", client));

      assertNotNull(cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndClient("origin1", null, null, client));

      assertNotNull(cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndClient("origin1", providergroup,
            "application/pdf", client));

   }

}
