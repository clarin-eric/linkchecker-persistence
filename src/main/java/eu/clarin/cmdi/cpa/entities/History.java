package eu.clarin.cmdi.cpa.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import eu.clarin.cmdi.cpa.utils.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
@Entity
@Table(name="history", indexes = {@Index(columnList = "url_id, checkingDate", unique = true)})
public class History {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private String method;
   
   private Integer status;
   
   private String contentType;
   
   private Long byteSize;
   
   private Integer duration;
   
   @Column(nullable = false)   
   private Timestamp checkingDate;
   
   private String message;
   
   private Integer redirectCount;
   
   private final Category category;
   
   @OneToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "url_id", referencedColumnName = "id")
   private final Url url;
}
