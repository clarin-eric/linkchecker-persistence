package eu.clarin.linkchecker.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.linkchecker.persistence.model.Client;
import eu.clarin.linkchecker.persistence.model.Context;
import eu.clarin.linkchecker.persistence.model.Role;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.model.UrlContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

@SpringBootTest
class UrlContextRepositoryTests extends RepositoryTests{

   @Test
   void save() {

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      Client client = usRep.save(new Client("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), null, client));
      
      UrlContext urlContext = new UrlContext(url, context, LocalDateTime.now(), true);
      urlContext.setActive(true);

      ucRep.save(urlContext);

      assertEquals(1, ucRep.count());

   }

   @Test
   @Transactional
   void deleteOlderThan() {

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      Client client = usRep.save(new Client("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), null, client));
      
      UrlContext urlContext = new UrlContext(url, context, LocalDateTime.now().minusDays(7), false);
      urlContext.setActive(true);

      ucRep.save(urlContext);

      ucRep.deleteByIngestionDateBefore(LocalDateTime.now().minusDays(8));

      assertEquals(1, ucRep.count());

      ucRep.deleteByIngestionDateBefore(LocalDateTime.now().minusDays(6));

      assertEquals(0, ucRep.count());

   }

   @Test
   @Transactional
   void deactivateOlderThan() {

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      Client client = usRep.save(new Client("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), null, client));
      
      UrlContext urlContext = new UrlContext(url, context, LocalDateTime.now().minusDays(7), true);
      urlContext.setActive(true);

      ucRep.save(urlContext);

      ucRep.deactivateOlderThan(LocalDateTime.now().minusDays(8));

      assertEquals(true, ucRep.findByUrlAndContextAndExpectedMimeType(url, context, null).get().getActive());

      ucRep.deactivateOlderThan(LocalDateTime.now().minusDays(6));

      assertEquals(1, ucRep.count());
      assertEquals(false, ucRep.findByUrlAndContextAndExpectedMimeType(url, context, null).get().getActive());
   }
}
