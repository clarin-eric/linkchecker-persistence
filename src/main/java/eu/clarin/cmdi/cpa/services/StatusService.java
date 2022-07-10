package eu.clarin.cmdi.cpa.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.clarin.cmdi.cpa.entities.*;
import eu.clarin.cmdi.cpa.repositories.HistoryRepository;
import eu.clarin.cmdi.cpa.repositories.StatusRepository;

@Service
@Transactional
public class StatusService {
   @Autowired
   StatusRepository sRep;
   @Autowired
   HistoryRepository hRep;
   
   public void save(Status status) {
      
      Status oldStatus;
      
      if((oldStatus = sRep.findByUrl(status.getUrl())) != null) { //save record to history
         
         History history = new History(oldStatus.getCategory(), oldStatus.getUrl());
         history.setMethod(oldStatus.getMethod());
         history.setStatus(oldStatus.getStatusCode());
         history.setMessage(oldStatus.getMessage());
         history.setContentType(oldStatus.getContentType());
         history.setDuration(oldStatus.getDuration());
         history.setByteSize(oldStatus.getByteSize());
         history.setRedirectCount(oldStatus.getRedirectCount());
         history.setCheckingDate(oldStatus.getCheckingDate());
         
         hRep.save(history);
         
         status.setId(oldStatus.getId());
      }
      
      sRep.save(status); //insert or update
   }
}
