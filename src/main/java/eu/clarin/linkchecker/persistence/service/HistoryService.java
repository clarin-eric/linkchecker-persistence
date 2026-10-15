package eu.clarin.linkchecker.persistence.service;

import eu.clarin.linkchecker.persistence.model.History;
import eu.clarin.linkchecker.persistence.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
/*
The main purpose of this service is to encapsulate the save in an own transaction to prevent the rollback
of the enclosing transaction in case of a constraint violation
 */
public class HistoryService {

    @Autowired
    private HistoryRepository hRep;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(History history){

        hRep.save(history);
    }
}
