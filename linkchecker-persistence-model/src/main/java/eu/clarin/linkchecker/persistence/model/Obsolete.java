package eu.clarin.linkchecker.persistence.model;

import eu.clarin.linkchecker.persistence.utils.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
@Entity
public class Obsolete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String urlName;

    private String clientName;

    private String origin;

    private String providergroupName;

    private String expectedMimeType;

    private String method;
    private Integer statusCode;
    private String contentType;
    private Long contentLength;
    private Integer duration;
    private Integer redirectCount;
    private LocalDateTime checkingDate;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Category category;
    @NonNull
    private String message;
    @NonNull
    private LocalDateTime deletionDate;

}
