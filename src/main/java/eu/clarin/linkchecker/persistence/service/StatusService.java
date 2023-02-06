package eu.clarin.linkchecker.persistence.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.clarin.cmdi.vlo.PIDUtils;
import eu.clarin.linkchecker.persistence.model.*;
import eu.clarin.linkchecker.persistence.repository.HistoryRepository;
import eu.clarin.linkchecker.persistence.repository.StatusRepository;

@Service
@Transactional
public class StatusService {
   @Autowired
   StatusRepository sRep;
   @Autowired
   HistoryRepository hRep;

   
   @Transactional
   public void save(Status status) {
      
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
}
