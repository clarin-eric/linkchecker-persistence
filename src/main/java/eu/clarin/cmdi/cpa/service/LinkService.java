package eu.clarin.cmdi.cpa.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.clarin.cmdi.cpa.model.*;
import eu.clarin.cmdi.cpa.repository.ContextRepository;
import eu.clarin.cmdi.cpa.repository.HistoryRepository;
import eu.clarin.cmdi.cpa.repository.ProvidergroupRepository;
import eu.clarin.cmdi.cpa.repository.StatusRepository;
import eu.clarin.cmdi.cpa.repository.UrlContextRepository;
import eu.clarin.cmdi.cpa.repository.UrlRepository;
import eu.clarin.cmdi.cpa.utils.Category;
import eu.clarin.cmdi.cpa.utils.UrlValidator;
import eu.clarin.cmdi.cpa.utils.UrlValidator.ValidationResult;
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
   
   public void save(User client, String urlString, String origin, String providerGroupName, String expectedMimeType) {
      save(client, urlString, origin, providerGroupName, expectedMimeType, LocalDateTime.now());
   }
   
   @Transactional
   public void save(User user, String urlName, String origin, String providergroupName, String expectedMimeType, LocalDateTime ingestionDate) {
      
      urlName = urlName.trim();
      
      ValidationResult validation = UrlValidator.validate(urlName);
      
      Url url = getUrl(urlName, validation);      
      
      Providergroup providergroup = (providergroupName == null)?null: getProvidergroup(providergroupName);
      
      Context context = getContext(origin, providergroup, expectedMimeType, user);
         
      getUrlContext(url, context, ingestionDate);      

      
      if(!validation.isValid()) { //create a status entry if Url is not valid
         Status status = new Status(url, Category.Invalid_URL, validation.getMessage(), ingestionDate);
         
         sService.save(status);
      }
   }
   
   private synchronized Url getUrl(String urlName, ValidationResult validation) {
      
      return uRep.findByName(urlName)
         .orElseGet(() -> uRep.save(new  Url(urlName, validation.getHost(), validation.isValid())));
   
   }
   
   private synchronized Providergroup getProvidergroup(String providergroupName) {
      
      return pRep.findByName(providergroupName)
               .orElseGet(() -> pRep.save(new Providergroup(providergroupName)));
      
   }
   
   private synchronized Context getContext(String origin, Providergroup providergroup, String expectedMimeType, User user){
      
      return cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndUser(origin, providergroup, expectedMimeType, user)
               .orElseGet(() -> cRep.save(new Context(origin, providergroup, expectedMimeType, user)));
      
   }
   
   private synchronized UrlContext getUrlContext(Url url, Context context, LocalDateTime ingestionDate) {
      
      return ucRep.findByUrlAndContext(url, context)
               .or(() -> Optional.of(new UrlContext(url, context, ingestionDate, true)))
               .map(urlContext -> {
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
      
      log.info("multi step deletion of links older then {} days", periodInDays);
      
      int step = 1;
      
      log.info("step {}: saving status records", step);
      sRep.saveStatusLinksOlderThan(periodInDays);
      log.info("step {}: done", step++);
      
      log.info("step {}: saving history records", step);
      hRep.saveHistoryLinksOlderThan(periodInDays);
      log.info("step {}: done", step++);
      
      log.info("step {}: deleting url_context records", step);
      ucRep.deleteOlderThan(LocalDateTime.now().minusDays(periodInDays));
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
}
