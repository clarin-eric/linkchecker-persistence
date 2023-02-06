package eu.clarin.linkchecker.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import eu.clarin.linkchecker.persistence.model.Obsolete;

public interface ObsoleteRepository extends CrudRepository<Obsolete, Long> {

}
