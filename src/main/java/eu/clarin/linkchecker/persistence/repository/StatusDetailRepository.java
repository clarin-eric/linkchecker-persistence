/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import java.util.stream.Stream;

import jakarta.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.StatusDetail;
import eu.clarin.linkchecker.persistence.model.StatusDetailId;


/**
 *
 */
public interface StatusDetailRepository extends CrudRepository<StatusDetail, StatusDetailId>{
   
   @Query(
      value = """
         SELECT NULL AS order_nr, s.*, u.name AS urlname, p.name AS providergroupname, c.origin, uc.expected_mime_type
            FROM status s 
            INNER JOIN url u ON s.url_id = u.id 
            INNER JOIN url_context uc ON uc.url_id = u.id
            INNER JOIN context c ON c.id = uc.context_id
            INNER JOIN providergroup p ON p.id = c.providergroup_id
            WHERE s.category = ?1
            AND uc.active = true       
            """,
      nativeQuery = true
   )
   @QueryHints(value = {
         @QueryHint(name = org.hibernate.jpa.AvailableHints.HINT_FETCH_SIZE, value = "1"), 
         @QueryHint(name = org.hibernate.jpa.AvailableHints.HINT_CACHEABLE, value = "false"),
         @QueryHint(name = org.hibernate.jpa.AvailableHints.HINT_READ_ONLY, value = "true")
   })
   Stream<StatusDetail> findAllByCategory(String categoryName);
   @Query(
      value = """
         SELECT NULL AS order_nr, s.*, u.name AS urlname, p.name AS providergroupname, c.origin, uc.expected_mime_type
            FROM status s 
            INNER JOIN url u ON s.url_id = u.id 
            INNER JOIN url_context uc ON uc.url_id = u.id
            INNER JOIN context c ON c.id = uc.context_id
            INNER JOIN providergroup p ON p.id = c.providergroup_id
            WHERE s.category = ?2
            AND p.name = ?1
            AND uc.active = true       
            """,
      nativeQuery = true
   )
   @QueryHints(value = {
         @QueryHint(name = org.hibernate.jpa.AvailableHints.HINT_FETCH_SIZE, value = "1"), 
         @QueryHint(name = org.hibernate.jpa.AvailableHints.HINT_CACHEABLE, value = "false"),
         @QueryHint(name = org.hibernate.jpa.AvailableHints.HINT_READ_ONLY, value = "true")
   })
   Stream<StatusDetail> findAllByProvidergroupnameAndCategory(String providergroupname, String categoryName);
   
   Stream<StatusDetail> findByOrderNrLessThanEqual(Long orderNr);

}
