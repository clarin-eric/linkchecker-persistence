/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.model;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


/**
 *
 */
@Data
@RequiredArgsConstructor
public class StatusDetail {

   private final String providergroupname;

   private final String origin;
   
   private final String urlname;

   private final String method;

   private final Integer statusCode;

   private final Category category;

   private final String message;

   private final LocalDateTime checkingDate;

   private final String contentType;
   
   private final String expectedMimeType;

   private final Long contentLength;

   private final Integer duration;

   private final Integer redirectCount;
}
