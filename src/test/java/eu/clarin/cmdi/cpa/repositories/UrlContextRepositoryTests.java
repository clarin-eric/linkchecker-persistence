package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.entities.Client;
import eu.clarin.cmdi.cpa.entities.Context;
import eu.clarin.cmdi.cpa.entities.Url;
import eu.clarin.cmdi.cpa.entities.UrlContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

@SpringBootTest
class UrlContextRepositoryTests extends RepositoryTests{

   @Test
   void save() {

      Url url = uRep.save(new Url("http://www.wowasa.com"));

      Client client = clRep.save(new Client("clarin@wowasa.com", "xxxxxxxxxxxxxxxx"));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), client));

      ucRep.save(new UrlContext(url, context, LocalDateTime.now(), true));

      assertEquals(1, ucRep.count());

   }

   @Test
   @Transactional
   void deleteOlderThan() {

      Url url = uRep.save(new Url("http://www.wowasa.com"));

      Client client = clRep.save(new Client("clarin@wowasa.com", "xxxxxxxxxxxxxxxx"));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), client));

      ucRep.save(new UrlContext(url, context, LocalDateTime.now().minusDays(7), true));

      ucRep.deleteOlderThan(LocalDateTime.now().minusDays(8));

      assertEquals(1, ucRep.count());

      ucRep.deleteOlderThan(LocalDateTime.now().minusDays(6));

      assertEquals(0, ucRep.count());

   }

   @Test
   @Transactional
   void deactivateOlderThan() {

      Url url = uRep.save(new Url("http://www.wowasa.com"));

      Client client = clRep.save(new Client("clarin@wowasa.com", "xxxxxxxxxxxxxxxx"));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), client));

      ucRep.save(new UrlContext(url, context, LocalDateTime.now().minusDays(7), true));

      ucRep.deactivateOlderThan(LocalDateTime.now().minusDays(8));

      assertEquals(true, ucRep.findByUrlAndContext(url, context).getActive());

      ucRep.deactivateOlderThan(LocalDateTime.now().minusDays(6));

      assertEquals(1, ucRep.count());
      assertEquals(false, ucRep.findByUrlAndContext(url, context).getActive());

   }
}
