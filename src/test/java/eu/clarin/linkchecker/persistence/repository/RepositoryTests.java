package eu.clarin.linkchecker.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class RepositoryTests {

    @Autowired
    protected UrlRepository uRep;
    @Autowired
    protected StatusRepository sRep;
    @Autowired
    protected HistoryRepository hRep;
    @Autowired
    protected UrlContextRepository ucRep;
    @Autowired
    protected ContextRepository cRep;
    @Autowired
    protected ProvidergroupRepository pRep;
    @Autowired
    protected ClientRepository usRep;
    @Autowired
    protected ObsoleteRepository oRep;
    @Autowired
    protected AggregatedStatusRepository asRep;
    @Autowired
    protected StatusDetailRepository sdRep;
}
