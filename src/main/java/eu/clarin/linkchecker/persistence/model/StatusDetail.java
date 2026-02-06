/**
 * @author Wolfgang Walter SAUER (wowasa) &lt;clarin@wowasa.com&gt;
 *
 */
package eu.clarin.linkchecker.persistence.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import eu.clarin.linkchecker.persistence.utils.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


/**
 *
 */
@Data
@Entity
@IdClass(StatusDetailId.class)
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class StatusDetail {

    @Id
    private final String providergroupName;
    @Id
    private final String origin;
    @Id
    private final String expectedMimeType;

    private final String urlName;

    private final String method;

    private final Integer statusCode;

    @Enumerated(EnumType.STRING)
    private final Category category;

    private final String message;

    private final LocalDateTime checkingDate;

    private final String contentType;

    private final Long contentLength;

    private final Integer duration;

    private final Integer redirectCount;

}
