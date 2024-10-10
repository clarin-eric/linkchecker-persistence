package eu.clarin.linkchecker.persistence.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StatusService {
    @Autowired
    UrlRepository uRep;
    @Autowired
    StatusRepository sRep;
    @Autowired
    HistoryRepository hRep;
    @Autowired
    StatusDetailRepository sdRep;


    public void save(Status status) {

        if (status.getUrl().getPriority() > 0) { //de-prioritization when status update
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

            // the try/catch block was necessary since we had frequently some records which were already
            // copied from status history table
            try {
                hRep.save(history);
            }
            catch (Exception e) {
                log.error("constraint violation while attempting to copy a record more than once from status to history table");
            }

            status.setId(oldStatus.getId());
        });

        sRep.save(status); //insert or update
    }

    public Map<String, Status> getStatus(String... urlNames) {

        final Map<String, Status> map = new HashMap<String, Status>();

        Arrays.stream(urlNames).forEach(urlName -> {

            sRep.findByUrlName(PIDUtils.getActionableLinkForPid(urlName))
                    .ifPresent(status -> map.put(urlName, status));

        });

        return map;

    }

    public Stream<StatusDetail> findAllDetail(Category category) {
        return sdRep.findAllByCategory(category.name());
    }

    public Stream<StatusDetail> findAllDetail(String providergroupname, Category category) {
        return sdRep.findAllByProvidergroupnameAndCategory(providergroupname, category.name());
    }
}
