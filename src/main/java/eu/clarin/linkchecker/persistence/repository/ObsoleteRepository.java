package eu.clarin.linkchecker.persistence.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.clarin.linkchecker.persistence.model.Obsolete;

public interface ObsoleteRepository extends CrudRepository<Obsolete, Long> {
   
   @Modifying(clearAutomatically = true, flushAutomatically = true)
   @Query("DELETE FROM Obsolete o WHERE o.checkingDate < :checkingDate")
   public void deleteByCheckingDateBefore(@Param("checkingDate") LocalDateTime checkingDate);   

}
