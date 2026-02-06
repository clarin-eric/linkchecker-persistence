package eu.clarin.linkchecker.persistence.repositories;

import eu.clarin.linkchecker.persistence.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SpringBootTest
class UrlContextRepositoryTests extends RepositoryTests {

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
                    UrlContext urlContext = new UrlContext(url, context, now.minusDays(i).minusHours(1), true);
                    urlContext.setExpectedMimeType("mimetype-" + i);

                    ucRep.save(urlContext);
                });

        ucRep.deleteByIngestionDateBefore(now.minusDays(50));

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

                    UrlContext urlContext = new UrlContext(url, context, now.minusDays(i).minusHours(1), true);
                    urlContext.setExpectedMimeType("mimetype-" + i);

                    ucRep.save(urlContext);
                });

        ucRep.deactivateOlderThan(now.minusDays(50));

        assertEquals(50, StreamSupport.stream(ucRep.findAll().spliterator(), false).filter(UrlContext::getActive).count());
    }

    @Test
    @Transactional
    void updateIngestionDate() {
        LocalDateTime fixedDateTime = LocalDateTime.now().minusHours(1);

        Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

        Client client = usRep.save(new Client("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

        Providergroup providergroup1 = pRep.save(new Providergroup("pg1"));
        Providergroup providergroup2 = pRep.save(new Providergroup("pg2"));

        Context context1 = cRep.save(new Context("c1", providergroup1, client));
        Context context2 = cRep.save(new Context("c2", providergroup2, client));

        // creating 20 url-contexts for context1, therefrom 10 active and 10 non-active
        IntStream.range(0, 20)
                .forEach(i -> {

                    UrlContext urlContext = new UrlContext(url, context1, fixedDateTime.minusDays(1), i%2 == 0);
                    urlContext.setExpectedMimeType("mimetype-" + i);

                    ucRep.save(urlContext);
                });
        // creating 20 url-contexts for context2, therefrom 10 active and 10 non-active
        IntStream.range(0, 20)
                .forEach(i -> {

                    UrlContext urlContext = new UrlContext(url, context2, fixedDateTime.minusDays(1), i%2 == 0);
                    urlContext.setExpectedMimeType("mimetype-" + i);

                    ucRep.save(urlContext);
                });
        // altogether 40 inserted
        assertEquals(40, ucRep.count());
        // since inserted with ingestion date (fixedDateTime - 1 day), all 40 have an ingestion date before fixedDateTime
        assertEquals(40, StreamSupport.stream(ucRep.findAll().spliterator(), false).filter(uc -> uc.getIngestionDate().isBefore(fixedDateTime)).count());
        // we update the ingestionDate to now(), which is after fixedDateTime
        ucRep.updateIngestionDate(Set.of("pg1"));
        // 30 entries remain unchanged
        assertEquals(30, StreamSupport.stream(ucRep.findAll().spliterator(), false).filter(uc -> uc.getIngestionDate().isBefore(fixedDateTime)).count());
        // but for the 10 active url-contexts of providergroup1 (with context1) we set ingestionDate to now() > fixedDateTime
        assertEquals(10, StreamSupport.stream(ucRep.findAll().spliterator(), false).filter(uc -> uc.getIngestionDate().isAfter(fixedDateTime)).count());
    }
}
