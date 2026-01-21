package eu.clarin.linkchecker.persistence.model;

import lombok.*;

import eu.clarin.linkchecker.persistence.utils.Category;

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
