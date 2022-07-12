package eu.clarin.cmdi.cpa.services;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.clarin.cmdi.cpa.entities.*;
import eu.clarin.cmdi.cpa.repositories.ClientRepository;
import eu.clarin.cmdi.cpa.repositories.ContextRepository;
import eu.clarin.cmdi.cpa.repositories.ObsoleteRepository;
import eu.clarin.cmdi.cpa.repositories.ProvidergroupRepository;
import eu.clarin.cmdi.cpa.repositories.UrlContextRepository;
import eu.clarin.cmdi.cpa.repositories.UrlRepository;
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
   private ObsoleteRepository oRep;
   @Autowired
   private ClientRepository clRep;
   
   @Transactional
   public void save(String email, String token, String urlString, String origin, String providerGroupName, String expectedMimeType, String source) {
      
      Client client = clRep.findByEmailAndToken(email, token);
      
      Url url;
      
      ValidationResult validation = UrlValidator.validate(urlString);
      
      if((url = uRep.findByUrl(urlString)) == null) {
         url = new Url(urlString);
         url.setGroupKey(validation.getHost());
         url.setValid(validation.isValid());
         
         uRep.save(url);
      }
      
      Providergroup providerGroup;
      
      if((providerGroup = pRep.findByName(providerGroupName)) == null) {
         providerGroup = new Providergroup(providerGroupName);
         pRep.save(providerGroup);
      }
      
      
      Context context;
      
      if((context = cRep.findByOriginAndProvidergroupAndExpectedMimeTypeAndClient(origin, providerGroup, expectedMimeType, client)) == null) {
         context = new Context(origin, client);
         context.setProvidergroup(providerGroup);
         context.setExpectedMimeType(expectedMimeType);

         cRep.save(context);
      }
      
      UrlContext urlContext;
      
      if((urlContext = ucRep.findByUrlAndContext(url, context)) == null) {
         urlContext = new UrlContext(url, context, LocalDateTime.now(), true);
      }
      
      ucRep.save(urlContext);
      
      if(!validation.isValid()) { //create a status entry if Url is not valid
         Status status = new Status(url, Category.Invalid_URL, validation.getMessage(), LocalDateTime.now());
         
         sService.save(status);
      }
   }
   
   public void deactivateLinksAfter(int periodInDays) {
      
      ucRep.deactivateOlderThan(LocalDateTime.now().minusDays(periodInDays));
   }
   
   @Transactional
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
