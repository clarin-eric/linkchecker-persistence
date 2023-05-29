/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.repository;

import java.util.stream.Stream;


/**
 *
 */
public interface GenericRepository<T> {
   
   public Stream<T> findAll(String nativeQuery);

}
