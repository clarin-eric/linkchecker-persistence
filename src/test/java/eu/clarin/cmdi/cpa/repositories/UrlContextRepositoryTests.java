package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.model.User;
import eu.clarin.cmdi.cpa.model.Context;
import eu.clarin.cmdi.cpa.model.Role;
import eu.clarin.cmdi.cpa.model.Url;
import eu.clarin.cmdi.cpa.model.UrlContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

@SpringBootTest
class UrlContextRepositoryTests extends RepositoryTests{

   @Test
   void save() {

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      User user = usRep.save(new User("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), null, null, user));
      
      UrlContext urlContext = new UrlContext(url, context, LocalDateTime.now(), true);
      urlContext.setActive(true);

      ucRep.save(urlContext);

      assertEquals(1, ucRep.count());

   }

   @Test
   @Transactional
   void deleteOlderThan() {

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      User user = usRep.save(new User("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), null, null, user));
      
      UrlContext urlContext = new UrlContext(url, context, LocalDateTime.now().minusDays(7), false);
      urlContext.setActive(true);

      ucRep.save(urlContext);

      ucRep.deleteOlderThan(LocalDateTime.now().minusDays(8));

      assertEquals(1, ucRep.count());

      ucRep.deleteOlderThan(LocalDateTime.now().minusDays(6));

      assertEquals(0, ucRep.count());

   }

   @Test
   @Transactional
   void deactivateOlderThan() {

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      User user = usRep.save(new User("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), null, null, user));
      
      UrlContext urlContext = new UrlContext(url, context, LocalDateTime.now().minusDays(7), true);
      urlContext.setActive(true);

      ucRep.save(urlContext);

      ucRep.deactivateOlderThan(LocalDateTime.now().minusDays(8));

      assertEquals(true, ucRep.findByUrlAndContext(url, context).get().getActive());

      ucRep.deactivateOlderThan(LocalDateTime.now().minusDays(6));

      assertEquals(1, ucRep.count());
      assertEquals(false, ucRep.findByUrlAndContext(url, context).get().getActive());

   }
}
