package eu.clarin.linkchecker.persistence.repositories;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

import eu.clarin.linkchecker.persistence.repository.ClientRepository;
import eu.clarin.linkchecker.persistence.repository.ContextRepository;
import eu.clarin.linkchecker.persistence.repository.HistoryRepository;
import eu.clarin.linkchecker.persistence.repository.ObsoleteRepository;
import eu.clarin.linkchecker.persistence.repository.ProvidergroupRepository;
import eu.clarin.linkchecker.persistence.repository.StatusRepository;
import eu.clarin.linkchecker.persistence.repository.UrlContextRepository;
import eu.clarin.linkchecker.persistence.repository.UrlRepository;

public class RepositoryTests {
   
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
   
   @AfterEach
   void cleanUp() {

      ucRep.deleteAll();
      cRep.deleteAll();
      pRep.deleteAll();
      usRep.deleteAll();
      uRep.deleteAll();
      hRep.deleteAll();
      sRep.deleteAll();
      oRep.deleteAll();
   }
}
