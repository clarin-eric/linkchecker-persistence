package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.AggregatedStatus;
import eu.clarin.linkchecker.persistence.model.AggregatedStatusId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

public interface AggregatedStatusRepository extends Repository<AggregatedStatus, AggregatedStatusId> {

    @Query(
            """
                    SELECT new AggregatedStatus(p.name, s.category, avg(s.duration), max(s.duration), count(u), count(s.duration))
                            FROM Providergroup p
                                    JOIN p.contexts c
                                            JOIN c.urlContexts uc
                                                    JOIN uc.url u
                                                            JOIN u.status s
                                                                    WHERE uc.active = true
                                                                            GROUP BY p.id, s.category
                    """
    )
    Stream<AggregatedStatus> findAll();
}
