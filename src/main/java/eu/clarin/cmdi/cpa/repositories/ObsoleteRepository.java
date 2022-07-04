package eu.clarin.cmdi.cpa.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import eu.clarin.cmdi.cpa.entities.Obsolete;

public interface ObsoleteRepository extends CrudRepository<Obsolete, Long> {
   
   @Query(
         value = "INSERT INTO obsolete (url, source, providerGroupName, record, expectedMimeType, ingestionDate, statusCode, message, category, method, contentType, byteSize, duration, checkingDate, redirectCount) "
               + "SELECT u.url, c.source, p.name, c.record, c.expectedMimeType, uc.ingestionDate, s.statusCode, s.message, s.category, s.method, s.contentType, s.byteSize, s.duration, s.checkingDate, s.redirectCount "
               + "FROM url_context uc "
               + "INNER JOIN (url u, context c) "
               + "ON (u.id=uc.url_id AND c.id=uc.context_id) "
               + "INNER JOIN providerGroup p "
               + "ON p.id=c.providerGroup_id "
               + "INNER JOIN status s "
               + "ON s.url_id=u.id "
               + "WHERE timestampdiff(day, uc.ingestionDate, now()) > ?1", 
         nativeQuery = true
      )
   @Modifying
   public void saveStatusLinksOlderThan(int periodOfDays);
   
   @Query(
         value = "INSERT INTO obsolete (url, source, providerGroupName, record, expectedMimeType, ingestionDate, statusCode, message, category, method, contentType, byteSize, duration, checkingDate, redirectCount) "
               + "SELECT u.url, c.source, p.name, c.record, c.expectedMimeType, uc.ingestionDate, h.statusCode, h.message, h.category, h.method, h.contentType, h.byteSize, h.duration, h.checkingDate, h.redirectCount "
               + "FROM url_context uc "
               + "INNER JOIN (url u, context c) "
               + "ON (u.id=uc.url_id AND c.id=uc.context_id) "
               + "INNER JOIN providerGroup p "
               + "ON p.id=c.providerGroup_id "
               + "INNER JOIN history h "
               + "ON h.url_id=u.id "
               + "WHERE timestampdiff(day, uc.ingestionDate, now()) > ?1",
         nativeQuery = true
      )
   @Modifying
   public void saveHistoryLinksOlderThan(int persiodOfDays);

}
