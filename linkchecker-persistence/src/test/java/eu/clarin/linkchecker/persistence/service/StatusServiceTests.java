package eu.clarin.linkchecker.persistence.service;

import eu.clarin.linkchecker.persistence.model.History;
import eu.clarin.linkchecker.persistence.model.Status;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.repository.HistoryRepository;
import eu.clarin.linkchecker.persistence.repository.StatusRepository;
import eu.clarin.linkchecker.persistence.repository.UrlRepository;
import eu.clarin.linkchecker.persistence.utils.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StatusServiceTests {

    @Autowired
    UrlRepository uRep;
    @Autowired
    StatusRepository sRep;
    @Autowired
    HistoryRepository hRep;


    @Autowired
    private StatusService sService;


    @Test
    void save() {

        Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));

        sService.save(new Status(url, Category.Broken, "", LocalDateTime.now()));

        assertEquals(1, sRep.count());
        assertEquals(0, hRep.count());

        //saving a record for same url copies old record to history
        sService.save(new Status(url, Category.Broken, "", LocalDateTime.now()));

        assertEquals(1, sRep.count());
        assertEquals(1, hRep.count());

    }

    @Test
    void saveWithDBInconsistency() {

        Url url = uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true));
        LocalDateTime now = LocalDateTime.now();

        hRep.save(new History(url, Category.Broken, now));

        sService.save(new Status(url, Category.Broken, "", now));

        assertEquals(1, sRep.count());
        assertEquals(1, hRep.count());
    }


    @Test
    void getStatus() {

        IntStream
                .range(0, 100)
                .forEach(i -> {
                    Url url = uRep.save(new Url("http://www.wowasa.com/page" + i, "www.wowasa.com", true));
                    sService.save(new Status(url, Category.Broken, "", LocalDateTime.now()));
                });

        assertEquals(0, sService.getStatus("http://www.wowasa.com/page100").size());
        assertEquals(1, sService.getStatus("http://www.wowasa.com/page100", "http://www.wowasa.com/page0").size());
        assertEquals(2, sService.getStatus("http://www.wowasa.com/page1", "http://www.wowasa.com/page0").size());
    }

    @AfterEach
    void deleteAll() {
        sRep.deleteAll();
        hRep.deleteAll();
        uRep.deleteAll();
    }

}
