package eu.clarin.cmdi.cpa.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.clarin.cmdi.cpa.model.*;
import eu.clarin.cmdi.cpa.repository.HistoryRepository;
import eu.clarin.cmdi.cpa.repository.StatusRepository;

@Service
@Transactional
public class StatusService {
   @Autowired
   StatusRepository sRep;
   @Autowired
   HistoryRepository hRep;

   
   @Transactional
   public void save(Status status) {
      
      Status oldStatus;
      
      if((oldStatus = sRep.findByUrl(status.getUrl())) != null) { //save record to history
         
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
      }
      
      sRep.save(status); //insert or update
   }
}
