package eu.clarin.cmdi.cpa.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Embeddable
public class AggregatedStatusId implements Serializable {
   
   private static final long serialVersionUID = 1L;

   private final Long urlId;
   
   private final Long contextId;
}
