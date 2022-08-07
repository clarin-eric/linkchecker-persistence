package eu.clarin.cmdi.cpa.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import eu.clarin.cmdi.cpa.model.Status;
import eu.clarin.cmdi.cpa.model.Url;
import eu.clarin.cmdi.cpa.utils.Category;


public interface StatusRepository extends PagingAndSortingRepository<Status, Long> {
   
   
   public Optional<Status> findByUrl(Url url);  
   
   public Optional<Status> findByUrlName(String name);
   
   public Stream<Status> findAllByUrlNameIn(String... names);
   
   public Page<Status> findAllByCategory(Category category, Pageable pageable);
   
   @Query("SELECT s FROM Status s JOIN s.url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON p.name=?1 AND s.category=?2")
   public Page<Status> findAllByProvidergroupAndCategory(String providerGroupName, Category category, Pageable pageable);
   
   @Query(
         value = "INSERT INTO obsolete (url_name, user_name, providergroup_name, origin, expected_mime_type, ingestion_date, status_code, message, category, method, content_type, content_length, duration, checking_date, redirect_count, deletion_date) "
               + "SELECT u.name, us.name, p.name, c.origin, c.expected_mime_type, uc.ingestion_date, s.status_code, s.message, s.category, s.method, s.content_type, s.content_length, s.duration, s.checking_date, s.redirect_count, NOW() "
               + "FROM url_context uc "
               + "INNER JOIN (url u) "
               + "ON u.id=uc.url_id "
               + "INNER JOIN (context c) "
               + "ON c.id=uc.context_id "
               + "INNER JOIN providergroup p "
               + "ON p.id=c.providergroup_id "
               + "INNER JOIN status s "
               + "ON s.url_id=u.id "
               + "INNER JOIN appuser us "
               + "ON us.id=c.user_id "
               + "WHERE uc.ingestion_date < ?1", 
         nativeQuery = true
      )
   @Modifying
   public void saveStatusLinksOlderThan(int periodOfDays);

}
