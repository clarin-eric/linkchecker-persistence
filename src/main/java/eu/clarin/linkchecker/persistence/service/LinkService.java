package eu.clarin.linkchecker.persistence.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import eu.clarin.linkchecker.persistence.model.*;
import eu.clarin.linkchecker.persistence.repository.ContextRepository;
import eu.clarin.linkchecker.persistence.repository.HistoryRepository;
import eu.clarin.linkchecker.persistence.repository.ProvidergroupRepository;
import eu.clarin.linkchecker.persistence.repository.StatusRepository;
import eu.clarin.linkchecker.persistence.repository.UrlContextRepository;
import eu.clarin.linkchecker.persistence.repository.UrlRepository;
import eu.clarin.linkchecker.persistence.utils.Category;
import eu.clarin.linkchecker.persistence.utils.UrlValidator;
import eu.clarin.linkchecker.persistence.utils.UrlValidator.ValidationResult;
import lombok.extern.slf4j.Slf4j;

@Service
@Scope("prototype")
@Slf4j
public class LinkService {   
   
   @Autowired
   private UrlRepository uRep;
   @Autowired
   private UrlContextRepository ucRep;
   @Autowired
   private ContextRepository cRep;
   @Autowired
   private ProvidergroupRepository pRep;
   @Autowired
   private StatusService sService;
   @Autowired
   private StatusRepository sRep;
   @Autowired
   private HistoryRepository hRep;
   
   private Map<String,Providergroup> providergroupMap = new ConcurrentHashMap<String,Providergroup>();
   //locks
   private Set<String> urlLock = ConcurrentHashMap.newKeySet();
   private Set<String> contextLock = ConcurrentHashMap.newKeySet();
   
   
   public void save(Client client, String urlString, String origin, String providerGroupName, String expectedMimeType) {
      save(client, urlString, origin, providerGroupName, expectedMimeType, LocalDateTime.now());
   }
   
   @Transactional(isolation = Isolation.READ_UNCOMMITTED)
   public void savePerOrigin(Client client, String providergroupName, String origin, Collection<Pair<String,String>> urlMimes) {
      log.trace("insert or update providergroup"); 
      Providergroup providergroup = (providergroupName == null)?null: getProvidergroup(providergroupName);     
      log.trace("insert or update context");
      Context context = getContext(origin, providergroup, client);
      
      urlMimes.forEach(urlMime -> {
         String urlName = urlMime.getFirst().trim();
         
         ValidationResult validation = UrlValidator.validate(urlName);
         log.trace("insert or update url");
         Url url = getUrl(urlName, validation, LocalDateTime.now());        
         log.trace("insert or update url_context");   
         insertOrUpdateUrlContext(url.getId(), context.getId(), urlMime.getSecond(), LocalDateTime.now());                         
      });
      log.trace("done insert or update url_context"); 
   }
   
   @Transactional(isolation = Isolation.READ_UNCOMMITTED)
   public void savePerOrigin(Client client, String providergroupName, String origin, Collection<Pair<String,String>> urlMimes, int priority) {
      log.trace("insert or update providergroup"); 
      Providergroup providergroup = (providergroupName == null)?null: getProvidergroup(providergroupName);     
      log.trace("insert or update context");
      Context context = getContext(origin, providergroup, client);
      
      urlMimes.forEach(urlMime -> {
         String urlName = urlMime.getFirst().trim();
         
         ValidationResult validation = UrlValidator.validate(urlName);
         log.trace("insert or update url");
         Url url = getUrl(urlName, validation, LocalDateTime.now(), priority);        
         log.trace("insert or update url_context");   
         insertOrUpdateUrlContext(url.getId(), context.getId(), urlMime.getSecond(), LocalDateTime.now());                         
      });
      log.trace("done insert or update url_context"); 
   }
   
   @Transactional
   public void save(Client client, String urlName, String origin, String providergroupName, String expectedMimeType, LocalDateTime ingestionDate) {
      
      urlName = urlName.trim();
      
      ValidationResult validation = UrlValidator.validate(urlName);
      
      log.trace("insert or update url");
      Url url = getUrl(urlName, validation, ingestionDate);      
      log.trace("insert or update providergroup");      
      Providergroup providergroup = (providergroupName == null)?null: getProvidergroup(providergroupName);
      log.trace("insert or update context");
      Context context = getContext(origin, providergroup, client);
      log.trace("insert or update url_context");   
      insertOrUpdateUrlContext(url.getId(), context.getId(), expectedMimeType, ingestionDate);          
      log.trace("done insert or update url_context");   
   }
   
   private Url getUrl(String urlName, ValidationResult validation, LocalDateTime ingestionDate) {
         try {
            while(!this.urlLock.add(urlName)) {
               
               try {
                  Thread.sleep(1000);
               }
               catch (InterruptedException e) {
                  
                  log.error("InterruptedException while waiting for unlock");
               }
            }
            return uRep.findByName(urlName)
               .orElseGet(() -> {
                  Url url = uRep.save(new  Url(urlName, validation.getHost(), validation.isValid()));
                  
                  if(!validation.isValid()) { //create a status entry if Url is not valid
                     Status status = new Status(url, Category.Invalid_URL, validation.getMessage(), ingestionDate);               
                     sService.save(status);
                  }
                  
                  return url;            
               });
         }
         finally{
            this.urlLock.remove(urlName);
         }
   }
   
   private Url getUrl(String urlName, ValidationResult validation, LocalDateTime ingestionDate, int priority) {
      try {
         while(!this.urlLock.add(urlName)) {
            
            try {
               Thread.sleep(1000);
            }
            catch (InterruptedException e) {
               
               log.error("InterruptedException while waiting for unlock");
            }
         }
         return uRep.findByName(urlName)
            .map(u -> {
               u.setPriority(priority);
               
               return uRep.save(u);
            })   
            .orElseGet(() -> {
               
               Url url = new  Url(urlName, validation.getHost(), validation.isValid());
               url.setPriority(priority);
                     
               url = uRep.save(url);
               
               if(!validation.isValid()) { //create a status entry if Url is not valid
                  Status status = new Status(url, Category.Invalid_URL, validation.getMessage(), ingestionDate);               
                  sService.save(status);
               }
               
               return url;            
            });
      }
      finally{
         this.urlLock.remove(urlName);
      }
   }

   private Providergroup getProvidergroup(String providergroupName) {

      return this.providergroupMap.computeIfAbsent(providergroupName,  
            k-> pRep.findByName(providergroupName).orElseGet(() ->  pRep.save(new Providergroup(providergroupName))));
   }
   
   private Context getContext(String origin, Providergroup providergroup, Client client){

      try {
         while(!this.contextLock.add(origin)) {
            
            try {
               Thread.sleep(1000);
            }
            catch (InterruptedException e) {
               
               log.error("InterruptedException while waiting for unlock");
            }
         }
         
         return cRep.findByOriginAndProvidergroupAndClient(origin, providergroup, client)
                  .orElseGet(() -> cRep.save(new Context(origin, providergroup, client)));
         
      }
      finally {
         
         this.contextLock.remove(origin);
      }
   }
   
   private void insertOrUpdateUrlContext(Long urlId, Long contextId, String expectedMimeType, LocalDateTime ingestionDate) {
      ucRep.insertOrUpdate(urlId, contextId, expectedMimeType, ingestionDate);
   }
   
   @Transactional
   public void deactivateLinksOlderThan(int periodInDays) {
      
      ucRep.deactivateOlderThan(LocalDateTime.now().minusDays(periodInDays));
   }
   
   @Transactional
   public void deleteLinksOderThan(int periodInDays) {
      
      LocalDateTime oldestDate = LocalDateTime.now().minusDays(periodInDays);
      
      log.info("multi step deletion of links older then {} days (date: {})", periodInDays, oldestDate);
      
      int step = 1;
      
      log.info("step {}: saving status records", step);
      sRep.saveStatusLinksOlderThan(oldestDate);
      log.info("step {}: done", step++);
      
      log.info("step {}: saving history records", step);
      hRep.saveHistoryLinksOlderThan(oldestDate);
      log.info("step {}: done", step++);
      
      log.info("step {}: deleting url_context records", step);
      ucRep.deleteByIngestionDateBefore(oldestDate);
      log.info("step {}: done", step++);
      
      log.info("step {}: deleting url records with delete cascade to status and history records", step);
      uRep.deleteWithoutContext();
      log.info("step {}: done", step++);
      
      log.info("step {}: deleting context records", step);
      cRep.deleteWithoutContext();
      log.info("step {}: done", step++);
      
      log.info("step {}: deleting providerGroup records", step);
      pRep.deleteWithoutContext();
      log.info("step {}: done", step);      
   }
   
   
   @Transactional(readOnly = true)
   public List<Url> getUrlsToCheck(int groupLimit, int limit, LocalDateTime lastestCheck){
      
      try(Stream<Url> stream = uRep.getNextUrlsToCheck(groupLimit, lastestCheck)){
         return stream.limit(limit).collect(Collectors.toList());
      }
   }
}
