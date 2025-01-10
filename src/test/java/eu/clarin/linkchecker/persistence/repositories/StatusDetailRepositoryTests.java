/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import eu.clarin.linkchecker.persistence.model.Client;
import eu.clarin.linkchecker.persistence.model.Context;
import eu.clarin.linkchecker.persistence.model.StatusDetail;
import eu.clarin.linkchecker.persistence.model.Providergroup;
import eu.clarin.linkchecker.persistence.model.Role;
import eu.clarin.linkchecker.persistence.model.Status;
import eu.clarin.linkchecker.persistence.model.Url;
import eu.clarin.linkchecker.persistence.model.UrlContext;
import eu.clarin.linkchecker.persistence.repository.StatusDetailRepository;
import eu.clarin.linkchecker.persistence.utils.Category;

/**
 *
 */
@SpringBootTest
public class StatusDetailRepositoryTests extends RepositoryTests{
   
   @Autowired
   private StatusDetailRepository lRep;
   
   private final Map<String, Vector<Status>> statusMap = new HashMap<String, Vector<Status>>();
   
   @BeforeEach
   void init() {
      
      Random random = new Random();
      
      final Vector<Url> urls = new Vector<Url>();
      
      final Providergroup[] providergroups = {pRep.save(new Providergroup("wowasa's pg")), pRep.save(new Providergroup("other's pg"))};
      
      final Client client = usRep.save(new Client("wowasa", "xxxxxxxx", Role.ADMIN));
      
      final Context[] contexts = new Context[10];
      IntStream.range(0, 10).forEach(i -> {
         contexts[i] = new Context("context" + i, providergroups[random.nextInt(2)], client);
         cRep.save(contexts[i]);
      });
      
      IntStream.range(1, 100).forEach( i-> {
         
         urls.add(uRep.save(new Url("http://www.wowasa.com?page=" + i, "www.wowasa.com", true)));  
         
         Context context = contexts[random.nextInt(10)];
         
         UrlContext urlContext = new UrlContext(urls.lastElement(), context, LocalDateTime.now(), true);
         urlContext.setActive(true);
         
         ucRep.save(urlContext);
         
         
         Status status = new Status(urls.lastElement(), Category.values()[random.nextInt(Category.values().length)], "", LocalDateTime.now());
         status.setDuration(random.nextInt(15000));
         status.setContentLength((long) random.nextInt(Integer.MAX_VALUE));
         
         sRep.save(status);
         
         statusMap.computeIfAbsent(context.getProvidergroup().getName(), name -> new Vector<Status>()).add(status);
         
      }); 
   }
   

   @Transactional(readOnly = true)
   @Test
   void findAllByOrderNrLessThanEqual() {
      
      this.statusMap.forEach((providergroup, statusVec) -> {
         
         Map<Category,List<Status>> statusMap = statusVec.stream().collect(Collectors.groupingBy(Status::getCategory));
         
         statusMap.forEach((category, statusList) -> {
            
            try(Stream<StatusDetail> stream = lRep.findByOrderNrLessThanEqual(100L)){
               
               assertEquals(
                     statusList.size(), 
                     stream.filter(lStatus -> (lStatus.getProvidergroupname().equals(providergroup) && lStatus.getCategory() == category)).count());
            }
         });
         
      });
   }
}
