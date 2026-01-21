package eu.clarin.linkchecker.persistence.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UrlCount {

    private final String providergroupName;

    private final Long count;

    private final Long distinctCount;
}
