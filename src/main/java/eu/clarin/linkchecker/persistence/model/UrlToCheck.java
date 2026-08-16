package eu.clarin.linkchecker.persistence.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "url_to_check")
public class UrlToCheck {

    @Id
    private Long id;

    private Long urlId;

    private String urlName;
}
