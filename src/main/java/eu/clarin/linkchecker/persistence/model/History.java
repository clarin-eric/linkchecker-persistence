package eu.clarin.linkchecker.persistence.model;

import eu.clarin.linkchecker.persistence.utils.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Entity
@Table(name = "history", indexes = {@Index(columnList = "url_id, checkingDate", unique = true)})
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;

    private Integer statusCode;

    private String contentType;

    private Long contentLength;

    private Integer duration;


    private String message;

    private Integer redirectCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id")
    private final Url url;

    @NonNull
    @Enumerated(EnumType.STRING)
    private final Category category;

    @NonNull
    private final LocalDateTime checkingDate;
}
