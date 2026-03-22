package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.*;
import eu.clarin.linkchecker.persistence.utils.Category;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusDetailRepositoryTests extends RepositoryTests {

    @Test
    public void findByProvidergroupnameAndCategory(){

        LocalDateTime fixedDateTime = LocalDateTime.now();

        Client client = usRep.save(new Client("wowasa", "xxxxxxxxxxxxxxxx", Role.ADMIN));

        Providergroup providergroup1 = pRep.save(new Providergroup("pg1"));
        Providergroup providergroup2 = pRep.save(new Providergroup("pg2"));

        Context context1 = cRep.save(new Context("c1", providergroup1, client));
        Context context2 = cRep.save(new Context("c2", providergroup2, client));

        IntStream.range(0, 10).forEach(i -> {

            Url url = uRep.save(new Url("http://www.wowasa.com/page" + i, "www.wowasa.com", true));

            sRep.save(new Status(url, Category.Ok, "", fixedDateTime));

            if(i % 2 == 0) {
                ucRep.save(new UrlContext(url, context1, fixedDateTime, true));
            }
            else  {
                ucRep.save(new UrlContext(url, context2, fixedDateTime, true));
            }

        });

        assertEquals(5, sdRep.findByProvidergroupnameAndCategory("pg1", Category.Ok).count());


    }

}


