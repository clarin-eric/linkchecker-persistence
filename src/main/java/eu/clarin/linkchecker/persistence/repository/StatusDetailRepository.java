/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.StatusDetail;
import eu.clarin.linkchecker.persistence.model.StatusDetailId;
import eu.clarin.linkchecker.persistence.utils.Category;

/**
 *
 */
public interface StatusDetailRepository extends CrudRepository<StatusDetail, StatusDetailId>{
   
   public Stream<StatusDetail> findAllByCategory(Category category);
   
   public Stream<StatusDetail> findAllByProvidergroupnameAndCategory(String providergroupname, Category category);
   
   public Stream<StatusDetail> findByOrderNrLessThanEqual(Long orderNr);

}
