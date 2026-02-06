package eu.clarin.linkchecker.persistence.repositories;

import eu.clarin.linkchecker.persistence.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

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

    @AfterEach
    void cleanUp() {

        ucRep.deleteAll();
        cRep.deleteAll();
        pRep.deleteAll();
        usRep.deleteAll();
        oRep.deleteAll();
        hRep.deleteAll();
        sRep.deleteAll();
        uRep.deleteAll();
    }
}
