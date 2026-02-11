package eu.clarin.linkchecker.persistence.model;

public record UrlCount(String providergroupName, Long count, Long distinctCount) {

}
