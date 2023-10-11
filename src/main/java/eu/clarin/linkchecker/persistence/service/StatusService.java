package eu.clarin.linkchecker.persistence.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.clarin.cmdi.vlo.PIDUtils;
import eu.clarin.linkchecker.persistence.model.*;
import eu.clarin.linkchecker.persistence.repository.HistoryRepository;
import eu.clarin.linkchecker.persistence.repository.StatusDetailRepository;
import eu.clarin.linkchecker.persistence.repository.StatusRepository;
import eu.clarin.linkchecker.persistence.repository.UrlRepository;
import eu.clarin.linkchecker.persistence.utils.Category;

@Service
@Transactional
public class StatusService {
   @Autowired
   UrlRepository uRep;
   @Autowired
   StatusRepository sRep;
   @Autowired
   HistoryRepository hRep;
   @Autowired
   StatusDetailRepository sdRep;

   
   @Transactional
   public void save(Status status) {
      
      if(status.getUrl().getPriority() > 0) { //de-prioritization when status update
         status.getUrl().setPriority(0);
         
         uRep.save(status.getUrl());
      }
      
      sRep.findByUrl(status.getUrl()).ifPresent(oldStatus -> {//save record to history
         History history = new History(oldStatus.getUrl(), oldStatus.getCategory(), oldStatus.getCheckingDate());
         history.setMethod(oldStatus.getMethod());
         history.setStatusCode(oldStatus.getStatusCode());
         history.setMessage(oldStatus.getMessage());
         history.setContentType(oldStatus.getContentType());
         history.setDuration(oldStatus.getDuration());
         history.setContentLength(oldStatus.getContentLength());
         history.setRedirectCount(oldStatus.getRedirectCount());
         
         hRep.save(history);
         
         status.setId(oldStatus.getId());
      }); 
      
      sRep.save(status); //insert or update
   }
   
   @Transactional
   public Map<String, Status> getStatus(String... urlNames){
      
      final Map<String, Status> map = new HashMap<String, Status>();
      
      Arrays.stream(urlNames).forEach(urlName -> {
         
         sRep.findByUrlName(PIDUtils.getActionableLinkForPid(urlName))
         .ifPresent(status -> map.put(urlName, status));

      });     
      
      return map;
   
   }
   @Transactional
   public Stream<StatusDetail> findAllDetail(Category category){
      return sdRep.findAllByCategory(category.name());
   }
   @Transactional
   public Stream<StatusDetail> findAllDetail(String providergroupname, Category category){
      return sdRep.findAllByProvidergroupnameAndCategory(providergroupname, category.name());
   }
}
