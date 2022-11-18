package eu.clarin.linkchecker.persistence.service;

import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   public Map<String, Status> getStatus(String... urlString){
      
      return sRep
            .findAllByUrlNameIn(urlString)
            .collect(Collectors.toMap(status -> status.getUrl().getName(), status -> status));
   
   }
}
