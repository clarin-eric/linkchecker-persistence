package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

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
      hRep.deleteAll();
      sRep.deleteAll();
      ucRep.deleteAll();
      cRep.deleteAll();
      pRep.deleteAll();
      clRep.deleteAll();
      uRep.deleteAll();
   }

}
