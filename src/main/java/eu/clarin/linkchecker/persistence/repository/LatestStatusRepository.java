/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.LatestStatus;
import eu.clarin.linkchecker.persistence.model.LatestStatusId;

/**
 *
 */
public interface LatestStatusRepository extends CrudRepository<LatestStatus, LatestStatusId>{

}
