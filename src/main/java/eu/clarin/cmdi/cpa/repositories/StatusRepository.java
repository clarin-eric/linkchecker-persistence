package eu.clarin.cmdi.cpa.repositories;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Modifying;
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
   @Query("SELECT s FROM Status s JOIN s.url u ON u.url=?1")
   public Status findByUrlString(String urlString);
   
   @Query("SELECT s FROM Status s JOIN s.url u JOIN u.urlContexts uc JOIN uc.context c JOIN c.providerGroup p ON p.name=?1 AND s.category=?2")
   public Stream<Status> findAllByProviderGroupAndCategory(String providerGroupName, Category category);
   
   @Query("DELETE FROM Status s WHERE s.url NOT IN (SELECT uc.url FROM UrlContext uc)")
   @Modifying
   public void deleteWithoutContext();
   
   

}
