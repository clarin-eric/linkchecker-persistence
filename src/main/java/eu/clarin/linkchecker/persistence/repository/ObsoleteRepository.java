package eu.clarin.linkchecker.persistence.repository;

import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import eu.clarin.linkchecker.persistence.model.Obsolete;

public interface ObsoleteRepository extends CrudRepository<Obsolete, Long> {
   
   public void deleteByCheckingDateBefore(@Param("checkingDate") LocalDateTime checkingDate);   

}
