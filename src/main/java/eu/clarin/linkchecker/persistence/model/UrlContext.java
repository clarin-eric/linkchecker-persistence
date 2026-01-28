package eu.clarin.linkchecker.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "url_context", indexes = @Index(columnList = "url_id, context_id, expectedMimeType", unique = true))
public class UrlContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    @NonNull
    private final Url url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "context_id")
    @NonNull
    private final Context context;

    private String expectedMimeType;
    @NonNull
    private LocalDateTime ingestionDate;
    @NonNull
    private Boolean active;

}
