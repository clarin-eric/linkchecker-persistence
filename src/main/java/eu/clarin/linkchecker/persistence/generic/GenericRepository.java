/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.generic;

import jakarta.persistence.Tuple;

import java.util.List;


/**
 *
 */
public interface GenericRepository {

    List<Tuple> findAll(String query, boolean isNative);

}
