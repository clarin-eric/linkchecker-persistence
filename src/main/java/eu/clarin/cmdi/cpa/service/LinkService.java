package eu.clarin.cmdi.cpa.service;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.clarin.cmdi.cpa.model.*;
import eu.clarin.cmdi.cpa.repository.ClientRepository;
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
   @Autowired
   private ClientRepository clRep;
   
   public void save(Client client, String urlString, String origin, String providerGroupName, String expectedMimeType) {
      save(client, urlString, origin, providerGroupName, expectedMimeType, LocalDateTime.now());
   }
   
   @Transactional
   public void save(Client client, String urlName, String origin, String providerGroupName, String expectedMimeType, LocalDateTime ingestionDate) {
      
      urlName = urlName.trim();
      
      Url url;
      
      ValidationResult validation = UrlValidator.validate(urlName);
      
      if((url = uRep.findByName(urlName)) == null) {
         url = new Url(urlName);
         url.setGroupKey(validation.getHost());
         url.setValid(validation.isValid());
         
         uRep.save(url);
      }
      
      Providergroup providerGroup = null;
     
      synchronized(this) {
         if(providerGroupName != null && (providerGroup = pRep.findByName(providerGroupName)) == null) {
            providerGroup = new Providergroup(providerGroupName);
            pRep.save(providerGroup);
         }
      }
      
      Context context;
     
      synchronized(this) {
         if((context = cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndClient(origin, providerGroup, expectedMimeType, client)) == null) {
            context = new Context(origin, client);
            context.setProvidergroup(providerGroup);
            context.setExpectedMimeType(expectedMimeType);
   
            cRep.save(context);
         }
      }
      
      UrlContext urlContext;
      
      synchronized(this) {
         if((urlContext = ucRep.findByUrlAndContext(url, context)) == null) {
            urlContext = new UrlContext(url, context);
         }
         urlContext.setIngestionDate(ingestionDate);
         urlContext.setActive(true);
         
         ucRep.save(urlContext);
      }
      
      if(!validation.isValid()) { //create a status entry if Url is not valid
         Status status = new Status(url, Category.Invalid_URL, validation.getMessage(), ingestionDate);
         
         sService.save(status);
      }
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
