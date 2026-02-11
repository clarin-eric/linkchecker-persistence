package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.*;
import eu.clarin.linkchecker.persistence.utils.Category;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AggregatedStatusRepositoryTests extends RepositoryTests {

    @Test
    @Transactional
    void findAggregatedStatus(){

        Client client = usRep.save(new Client("wowasa1", "pw", Role.USER));

        Providergroup pg1 = pRep.save(new Providergroup("pg1"));
        Providergroup pg2 = pRep.save(new Providergroup("pg2"));

        Context c1 = cRep.save(new Context("file", pg1, client));
        Context c2 = cRep.save(new Context("file", pg2, client));

        // 10 urls for pg1, Category.Ok, all with duration
        IntStream.range(0, 10).forEach(i -> {
            Url url = uRep.save(new Url("http://www.wowasa.com/page" + i, "www.wowasa.com", true));

            ucRep.save(new UrlContext(url, c1, LocalDateTime.now(), true));

            Status status = new Status(url, Category.Ok, "", LocalDateTime.now());
            status.setDuration(1 + i);

            sRep.save(status);
        });
        // 10 urls for pg1, Category.Undetermined, only half with duration
        IntStream.range(10, 20).forEach(i -> {
            Url url = uRep.save(new Url("http://www.wowasa.com/page" + i, "www.wowasa.com", true));

            ucRep.save(new UrlContext(url, c1, LocalDateTime.now(), true));

            Status status = new Status(url, Category.Undetermined, "", LocalDateTime.now());
            if(i%2 == 0) {
                status.setDuration(1 + i);
            }

            sRep.save(status);
        });
        // 10 non-active urls for pg1, Category.Ok
        IntStream.range(20, 30).forEach(i -> {
            Url url = uRep.save(new Url("http://www.wowasa.com/page" + i, "www.wowasa.com", true));

            ucRep.save(new UrlContext(url, c2, LocalDateTime.now(), false));

            sRep.save(new Status(url, Category.Ok, "", LocalDateTime.now()));
        });
        // 10 urls for pg1, Category.Blocked_By_Robots_txt
        IntStream.range(30, 40).forEach(i -> {
            Url url = uRep.save(new Url("http://www.wowasa.com/page" + i, "www.wowasa.com", true));

            ucRep.save(new UrlContext(url, c2, LocalDateTime.now(), true));

            sRep.save(new Status(url, Category.Blocked_By_Robots_txt, "", LocalDateTime.now()));
        });
        // only three lines, since the non-active urls from pg2 are ignored
        assertEquals(3, asRep.findAll().count());

        asRep.findAll()
                .filter(aggregatedStatus -> aggregatedStatus.getCategory().equals(Category.Ok) && aggregatedStatus.getProvidergroupName().equals("pg1"))
                .findFirst().ifPresent(aggregatedStatus -> {
                    assertEquals(10, aggregatedStatus.getMaxDuration());
                    assertEquals(5.5, aggregatedStatus.getAvgDuration());
                    assertEquals(10, aggregatedStatus.getNumber());
                    assertEquals(10, aggregatedStatus.getNumberWithDuration());
                });
        // only 5 with duration
        asRep.findAll()
                .filter(aggregatedStatus -> aggregatedStatus.getCategory().equals(Category.Undetermined) && aggregatedStatus.getProvidergroupName().equals("pg1"))
                .findFirst().ifPresent(aggregatedStatus -> {
                    assertEquals(10, aggregatedStatus.getNumber());
                    assertEquals(5, aggregatedStatus.getNumberWithDuration());
                });
    }
}
