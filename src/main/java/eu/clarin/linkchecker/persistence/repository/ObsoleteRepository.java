package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.Obsolete;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ObsoleteRepository extends CrudRepository<Obsolete, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    //@Query necessary to create just one singe statement! (see https://www.baeldung.com/spring-data-jpa-delete)
    @Query("DELETE FROM Obsolete o WHERE o.checkingDate < :checkingDate")
    void deleteByCheckingDateBefore(@Param("checkingDate") LocalDateTime checkingDate);
}
