package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlRepositoryTests extends RepositoryTests {


    @Test
    void save() {

        // the URL mustn't be null
        assertThrows(NullPointerException.class, () -> uRep.save(new Url(null, null, null)));

        // saving
        Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

        assertEquals(1, uRep.count());

        // the same URL mustn't be saved twice
        assertThrows(DataAccessException.class, () -> uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true)));
    }

    @Test
    void countByUrlContextActive() {

        Client client = usRep.save(new Client("wowasa", "xxxxxxxx", Role.ADMIN));
        Context context = cRep.save(new Context("origin", null, client));

        IntStream.range(0, 6).forEach(i -> {

            UrlContext urlContext = new UrlContext(uRep.save(new Url("http://www.wowasa.com?page=" + i, "www.wowasa.com", true)), context, LocalDateTime.now(), true);
            urlContext.setActive(true);

            ucRep.save(urlContext);
        });
        IntStream.range(6, 10).forEach(i -> {

            UrlContext urlContext = new UrlContext(uRep.save(new Url("http://www.wowasa.com?page=" + i, "www.wowasa.com", true)), context, LocalDateTime.now(), true);
            urlContext.setActive(false);

            ucRep.save(urlContext);
        });
        assertEquals(6, uRep.countByUrlContextsActive(true));
        assertEquals(4, uRep.countByUrlContextsActive(false));
    }

    @Test
    @Transactional
    void aggregateCountUrl(){

        Client client = usRep.save(new Client("wowasa1", "pw", Role.USER));

        Providergroup pg = pRep.save(new Providergroup("pg1"));

        Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

        IntStream.range(0, 10).forEach(i -> {

            Context context = cRep.save(new Context("context" + i, pg, client));

            ucRep.save(new UrlContext(url, context, LocalDateTime.now(), true));
        });
        IntStream.range(10, 12).forEach(i -> {

            Context context = cRep.save(new Context("context" + i, pg, client));

            ucRep.save(new UrlContext(url, context, LocalDateTime.now(), false));
        });

        assertEquals(1, uRep.aggregateCountUrl().count());
        uRep.aggregateCountUrl().findFirst().ifPresent(urlCount -> {
            assertEquals(10, urlCount.count());
            assertEquals(1, urlCount.distinctCount());
        });

    }
}
