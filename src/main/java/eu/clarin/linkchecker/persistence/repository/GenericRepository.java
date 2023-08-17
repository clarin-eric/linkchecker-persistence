/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import java.util.stream.Stream;

import javax.persistence.Tuple;


/**
 *
 */
public interface GenericRepository {
   
   public Stream<Tuple> findAll(String query, boolean isNative);

}
