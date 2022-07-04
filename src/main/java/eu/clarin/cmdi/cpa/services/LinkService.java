package eu.clarin.cmdi.cpa.services;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.clarin.cmdi.cpa.entities.*;
import eu.clarin.cmdi.cpa.repositories.ContextRepository;
import eu.clarin.cmdi.cpa.repositories.HistoryRepository;
import eu.clarin.cmdi.cpa.repositories.ObsoleteRepository;
import eu.clarin.cmdi.cpa.repositories.ProviderGroupRepository;
import eu.clarin.cmdi.cpa.repositories.StatusRepository;
import eu.clarin.cmdi.cpa.repositories.UrlContextRepository;
import eu.clarin.cmdi.cpa.repositories.UrlRepository;
import eu.clarin.cmdi.cpa.utils.Category;
import eu.clarin.cmdi.cpa.utils.UrlValidator;
import eu.clarin.cmdi.cpa.utils.UrlValidator.ValidationResult;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
public class LinkService {
   

   
   @Autowired
   private UrlRepository uRep;
   @Autowired
   private StatusRepository sRep;
   @Autowired
   private HistoryRepository hRep;
   @Autowired
   private UrlContextRepository ucRep;
   @Autowired
   private ContextRepository cRep;
   @Autowired
   private ProviderGroupRepository pRep;
   @Autowired
   private StatusService sService;
   @Autowired
   private ObsoleteRepository oRep;
   
   public void save(String urlString, String origin, String providerGroupName, String expectedMimeType, String source) {
      
      Url url;
      
      ValidationResult validation = UrlValidator.validate(urlString);
      
      if((url = uRep.findByUrl(urlString)) == null) {
         url = new Url(urlString, validation.getHost(), validation.isValid());
         
         uRep.save(url);
      }
      
      ProviderGroup providerGroup;
      
      if((providerGroup = pRep.findByName(providerGroupName)) == null) {
         providerGroup = new ProviderGroup();
         providerGroup.setName(providerGroupName);
         pRep.save(providerGroup);
      }
      
      Context context;
      
      if((context = cRep.findByOriginAndProviderGroupAndExpectedMimeTypeAndSource(origin, providerGroup, expectedMimeType, source)) == null) {
         context = new Context();
         context.setOrigin(origin);
         context.setProviderGroup(providerGroup);
         context.setExpectedMimeType(expectedMimeType);
         context.setSource(source);
         cRep.save(context);
      }
      
      UrlContext urlContext;
      
      if((urlContext = ucRep.findByUrlAndContext(url, context)) == null) {
         urlContext = new UrlContext();
         urlContext.setUrl(url);
         urlContext.setContext(context);
      }
      
      urlContext.setActive(true);
      urlContext.setIngestionDate(new Timestamp(System.currentTimeMillis()));
      
      ucRep.save(urlContext);
      
      if(!validation.isValid()) { //create a status entry if Url is not valid
         Status status = new Status(Category.Invalid_URL, url);
         status.setMessage(validation.getMessage());
         
         sService.save(status);
      }
   }
   
   public void deactivateLinksAfter(int periodInDays) {
      
      ucRep.deactivateAfter(periodInDays);
   }
   
   public void deleteLinksAfter(int periodInDays) {
      
      log.info("multi step deletion of links older then {} days", periodInDays);
      
      int step = 1;
      
      log.info("step {}: saving status records", step);
      oRep.saveStatusLinksOlderThan(periodInDays);
      log.info("step {}: done", step++);
      
      log.info("step {}: saving history records", step);
      oRep.saveHistoryLinksOlderThan(periodInDays);
      log.info("step {}: done", step++);
      
      log.info("step {}: deleting url_context records", step);
      ucRep.deleteByPeriod(periodInDays);
      log.info("step {}: done", step++);
      
      log.info("step {}: deleting history records", step);
      hRep.deleteWithoutContext();
      log.info("step {}: done", step++);
      
      log.info("step {}: deleting status records", step);
      sRep.deleteWithoutContext();
      log.info("step {}: done", step++);
      
      log.info("step {}: deleting url records", step);
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
