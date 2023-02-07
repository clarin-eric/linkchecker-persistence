package eu.clarin.linkchecker.persistence.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   
   public void save(Client client, String urlString, String origin, String providerGroupName, String expectedMimeType) {
      save(client, urlString, origin, providerGroupName, expectedMimeType, LocalDateTime.now());
   }
   
   @Transactional
   public void save(Client client, String urlName, String origin, String providergroupName, String expectedMimeType, LocalDateTime ingestionDate) {
      
      urlName = urlName.trim();
      
      ValidationResult validation = UrlValidator.validate(urlName);
      
      Url url = getUrl(urlName, validation, ingestionDate);      
      
      Providergroup providergroup = (providergroupName == null)?null: getProvidergroup(providergroupName);
      
      Context context = getContext(origin, providergroup, client);
         
      getUrlContext(url, context, expectedMimeType, ingestionDate);          

   }
   
   private synchronized Url getUrl(String urlName, ValidationResult validation, LocalDateTime ingestionDate) {
      
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

   
   private synchronized Providergroup getProvidergroup(String providergroupName) {
      
      return pRep.findByName(providergroupName)
               .orElseGet(() -> pRep.save(new Providergroup(providergroupName)));
      
   }
   
   private synchronized Context getContext(String origin, Providergroup providergroup, Client client){
      
      return cRep.findByOriginAndProvidergroupAndClient(origin, providergroup, client)
               .orElseGet(() -> cRep.save(new Context(origin, providergroup, client)));
      
   }
   
   private synchronized UrlContext getUrlContext(Url url, Context context, String expectedMimeType, LocalDateTime ingestionDate) {
      
      return ucRep.findByUrlAndContextAndExpectedMimeType(url, context, expectedMimeType)
               .or(() -> Optional.of(new UrlContext(url, context, ingestionDate, true)))
               .map(urlContext -> {
                  urlContext.setExpectedMimeType(expectedMimeType);
                  urlContext.setIngestionDate(ingestionDate);
                  urlContext.setActive(true);
               
                  return ucRep.save(urlContext);
               })
               .get();

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
      ucRep.deleteOlderThan(oldestDate);
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
   
   
   @Transactional
   public List<Url> getUrlsToCheck(int maxNum, int maxNumPerGroup, LocalDateTime lastestCheck){
      
      return (List<Url>) uRep.getNextUrlsToCheck(maxNumPerGroup, lastestCheck).limit(maxNum).collect(Collectors.toList());
   }
}
