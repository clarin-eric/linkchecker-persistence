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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "status", indexes = {@Index(columnList = "url_id", unique = true), @Index(columnList = "category", unique = false)})
public class Status {
   
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
   private String category;
   
   @OneToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "url_id")
   @NonNull
   private Url url;

}
