package eu.clarin.cmdi.cpa.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import eu.clarin.cmdi.cpa.entities.AggregatedStatus;
import eu.clarin.cmdi.cpa.entities.Client;
import eu.clarin.cmdi.cpa.entities.Context;
import eu.clarin.cmdi.cpa.entities.Providergroup;
import eu.clarin.cmdi.cpa.entities.Status;
import eu.clarin.cmdi.cpa.entities.Url;
import eu.clarin.cmdi.cpa.entities.UrlContext;
import eu.clarin.cmdi.cpa.utils.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;


@SpringBootTest
class AggregatedStatusRepositoryTests extends RepositoryTests{
   
   @Autowired
   private AggregatedStatusRepository aRep;
   
   private Map<String, Vector<Status>> statusMap = new HashMap<String, Vector<Status>>();
   
   @BeforeEach
   void init() {
      
      Random random = new Random();
      
      final Vector<Url> urls = new Vector<Url>();
      
      final Providergroup[] providergroups = {pRep.save(new Providergroup("wowasa's pg")), pRep.save(new Providergroup("other's pg"))};
      
      final Client client = clRep.save(new Client("devnull@wowasa.com", "xxxxxxxx"));
      
      final Context[] contexts = new Context[10];
      IntStream.range(0, 10).forEach(i -> {
         contexts[i] = new Context("context" + i, client);
         contexts[i].setProvidergroup(providergroups[random.nextInt(2)]);
         cRep.save(contexts[i]);
      });
      

      

      
      IntStream.range(1, 100).forEach( i-> {
         
         urls.add(uRep.save(new Url("http://www.wowasa.com?page=" + i)));  
         
         Context context = contexts[random.nextInt(10)];
         
         ucRep.save(new UrlContext(urls.lastElement(), context, LocalDateTime.now(), true));
         
         
         Status status = new Status(urls.lastElement(), Category.values()[random.nextInt(Category.values().length)], "", LocalDateTime.now());
         status.setDuration(random.nextInt(15000));
         status.setByteSize((long) random.nextInt(Integer.MAX_VALUE));
         
         sRep.save(status);
         
         statusMap.computeIfAbsent(context.getProvidergroup().getName(), name -> new Vector<Status>()).add(status);
         
      });  
   }
	
   @Transactional
	@Test
	void findAllByProviderGroupName() {
	   
      Stream.of("wowasa's pg", "other's pg").forEach(pgName -> {
         
   	   try(Stream<AggregatedStatus> stream = aRep.findAllByProvidergroupName(pgName)){   
   	      stream.forEach(ag -> {
   	         assertEquals(
   	               this.statusMap.get(pgName).stream().filter(s -> s.getCategory() == ag.getCategory()).count(),
   	               ag.getNumber()
                  );
   	      });  	      
   	   }
      });	   
	}
}
