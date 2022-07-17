package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

import eu.clarin.cmdi.cpa.repository.ClientRepository;
import eu.clarin.cmdi.cpa.repository.ContextRepository;
import eu.clarin.cmdi.cpa.repository.HistoryRepository;
import eu.clarin.cmdi.cpa.repository.ProvidergroupRepository;
import eu.clarin.cmdi.cpa.repository.StatusRepository;
import eu.clarin.cmdi.cpa.repository.UrlContextRepository;
import eu.clarin.cmdi.cpa.repository.UrlRepository;

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
   protected ClientRepository clRep;
   
   @AfterEach
   void cleanUp() {

      ucRep.deleteAll();
      cRep.deleteAll();
      pRep.deleteAll();
      clRep.deleteAll();
      uRep.deleteAll();
      hRep.deleteAll();
      sRep.deleteAll();
   }

}
