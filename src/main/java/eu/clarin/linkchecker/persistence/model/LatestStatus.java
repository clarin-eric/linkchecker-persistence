/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.springframework.lang.Nullable;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Data
@Entity
public class LatestStatus {
   
   @EmbeddedId
   private LatestStatusId id;

}
