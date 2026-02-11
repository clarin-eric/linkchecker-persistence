package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.History;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.utils.Category;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HistoryRepositoryTests extends RepositoryTests {

    @Test
    void save() {

        History history = new History(uRep.save(new Url("http://www.wowasa.com", "www.wowasa.com", true)), Category.Broken, LocalDateTime.now());

        hRep.save(history);

        assertEquals(1, hRep.count());
    }

    @Test
    @Transactional
    void deleteByCheckingDateBefore() {

        LocalDateTime now = LocalDateTime.now();

        IntStream.range(0, 100)
                .forEach(i -> hRep.save(new History(uRep.save(new Url("http://www.wowasa.com" + i, "www.wowasa.com" + i, true)), Category.Broken, now.minusDays(i))));

        hRep.deleteByCheckingDateBefore(now.minusDays(50).plusSeconds(1));
        assertEquals(50, hRep.count());
    }
}
