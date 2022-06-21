package eu.clarin.cmdi.cpa.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "status", indexes = {@Index(columnList = "url_id", unique = true)})
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
   
   @OneToOne
   @JoinColumn(name = "url_id")
   private Url url;

}
