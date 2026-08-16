package eu.clarin.linkchecker.persistence.repository;

import eu.clarin.linkchecker.persistence.model.UrlToCheck;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface UrlToCheckRepository extends CrudRepository<UrlToCheck, Long> {

    Stream<UrlToCheck> getUrlToCheckOrderById();

    void deleteById(Long id);
}
