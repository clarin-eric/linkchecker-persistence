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
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

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
      
      LocalDateTime now = LocalDateTime.now();

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      Client client = usRep.save(new Client("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), null, client));
      
      IntStream.range(0, 100)
         .forEach(i -> {
            
            UrlContext urlContext = new UrlContext(url, context, now.minusDays(i), false);
            urlContext.setActive(true);
            urlContext.setExpectedMimeType("mimetype-" + i);

            ucRep.save(urlContext);           
         });

      ucRep.deleteByIngestionDateBefore(LocalDateTime.now().minusDays(50));

      assertEquals(50, ucRep.count());
   }

   @Test
   @Transactional
   void deactivateOlderThan() {
      
      LocalDateTime now = LocalDateTime.now();

      Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

      Client client = usRep.save(new Client("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

      Context context = cRep.save(new Context("upload" + System.currentTimeMillis(), null, client));
      
      IntStream.range(0, 100)
      .forEach(i -> {
         
         UrlContext urlContext = new UrlContext(url, context, now.minusDays(i), false);
         urlContext.setActive(true);
         urlContext.setExpectedMimeType("mimetype-" + i);

         ucRep.save(urlContext);           
      });

      ucRep.deactivateOlderThan(LocalDateTime.now().minusDays(50));

      assertEquals(50, StreamSupport.stream(ucRep.findAll().spliterator(), false).filter(UrlContext::getActive).count());
   }
}
