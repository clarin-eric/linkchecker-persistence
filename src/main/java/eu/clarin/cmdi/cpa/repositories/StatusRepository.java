package eu.clarin.cmdi.cpa.repositories;

import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;


import eu.clarin.cmdi.cpa.entities.Status;
import eu.clarin.cmdi.cpa.entities.Url;
import eu.clarin.cmdi.cpa.utils.Category;


public interface StatusRepository extends PagingAndSortingRepository<Status, Long> {
   
   @Nullable
   public Status findByUrl(Url url);
   
   @Nullable
//   @Query("SELECT s FROM Status s JOIN Url u WHERE u.url=?1")
   public Status findByUrlUrl(String url);
   
//   @Query("SELECT s FROM Status s JOIN FETCH Url u WHERE u.url IN (?1)")
   public Stream<Status> findAllByUrlUrlIn(String... urls);
   
   public Page<Status> findAllByCategory(Category category, Pageable pageable);
   
   @Query("SELECT s FROM Status s JOIN s.url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providergroup p ON p.name=?1 AND s.category=?2")
   public Page<Status> findAllByProvidergroupAndCategory(String providerGroupName, Category category, Pageable pageable);

}
