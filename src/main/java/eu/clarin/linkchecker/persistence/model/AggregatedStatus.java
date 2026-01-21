package eu.clarin.linkchecker.persistence.model;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AggregatedStatus {
    @NonNull
    private final String providergroupName;
    @NonNull
    private final Category category;

    private final Double avgDuration;

    private final Integer maxDuration;

    private final Long numberId;

    private final Long numberDuration;

}
