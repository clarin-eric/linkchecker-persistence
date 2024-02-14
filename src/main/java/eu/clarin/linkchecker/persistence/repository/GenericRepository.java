/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import java.util.List;

import jakarta.persistence.Tuple;


/**
 *
 */
public interface GenericRepository {
   
   public List<Tuple> findAll(String query, boolean isNative);

}
