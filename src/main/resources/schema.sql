# This script creates a linkchecker database as it is assumed by this mysql implementation of the rasa-api
# author: Wolfgang Walter SAUER (wowasa) <clarin@wowasa.com>
# date: July 2022

#DROP DATABASE  IF EXISTS `linkcheckerTest`;
CREATE DATABASE  IF NOT EXISTS `linkcheckerTest` CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `linkcheckerTest`;

CREATE TABLE IF NOT EXISTS `providerGroup`(
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey_providerGroup_name` (`name`)
);

CREATE TABLE IF NOT EXISTS client (
   id INT NOT NULL AUTO_INCREMENT,
   email VARCHAR(256) NOT NULL,
   token VARCHAR(256) NOT NULL,
   quota INT DEFAULT NULL, 
   KEY key_customer_email_token (email, token),
   PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS context (
  id INT NOT NULL AUTO_INCREMENT,
  client_id INT NOT NULL,
  origin VARCHAR(256) NOT NULL,
  providerGroup_id INT DEFAULT NULL,
  expectedMimeType VARCHAR(64) DEFAULT NULL,
  PRIMARY KEY (id),  
  UNIQUE KEY ukey_context_origin_providerGroup_id_expectedMimeType_client_id (origin, providerGroup_id, expectedMimeType, client_id),
  KEY key_providerGroup_id (providerGroup_id),
  FOREIGN KEY key_providerGroup_id (providerGroup_id) REFERENCES providerGroup (id),
  KEY key_context_client_id (client_id),
  FOREIGN KEY key_context_client_id (client_id) REFERENCES client (id)
);



CREATE TABLE IF NOT EXISTS `url` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(1024) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `groupKey` VARCHAR(128) DEFAULT NULL,
  `valid` boolean DEFAULT NULL, 
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey_url_url` (`url`),
  KEY `key_url_host` (`groupKey`)
);


CREATE TABLE IF NOT EXISTS `url_context` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url_id` INT NOT NULL,
  `context_id` INT NOT NULL,
  `ingestionDate` DATETIME NOT NULL DEFAULT NOW(),
  `active` boolean NOT NULL DEFAULT false,
  PRIMARY KEY (`id`),
  KEY `key_url_context_url_id_active_context_id` (`url_id`, `active`, `context_id`),
  KEY `key_url_context_context_id_active_url_id` (`context_id`, `active`, `url_id`),
  UNIQUE KEY `ukey_url_context_url_id_context_id` (`url_id`, `context_id`),
  FOREIGN KEY `key_url_context_url_id_active_context_id` (`url_id`) REFERENCES `url` (`id`),
  FOREIGN KEY `key_url_context_context_id_active_url_id` (`context_id`) REFERENCES `context` (`id`)
);


CREATE TABLE IF NOT EXISTS `status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url_id` INT NOT NULL,
  `statusCode` INT DEFAULT NULL,
  `message` VARCHAR(1024) NOT NULL,
  `category` VARCHAR(25) NOT NULL,
  `method` VARCHAR(10) DEFAULT NULL,
  `contentType` VARCHAR(256) DEFAULT NULL,
  `byteSize` bigint DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checkingDate` DATETIME NOT NULL,
  `redirectCount` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey_status_url_id` (`url_id`),
  KEY `key_status_category` (`category`),
  FOREIGN KEY `ukey_status_url_id` (`url_id`) REFERENCES `url` (`id`)
);


CREATE TABLE IF NOT EXISTS `history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status_id` INT NOT NULL,
  `url_id` INT DEFAULT NULL,
  `statusCode` INT DEFAULT NULL,
  `message` VARCHAR(256),
  `category` VARCHAR(25) NOT NULL,
  `method` VARCHAR(10) NOT NULL,
  `contentType` VARCHAR(256) DEFAULT NULL,
  `byteSize` INT DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checkingDate` DATETIME NOT NULL,
  `redirectCount` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey_history_url_id_ceckingDate` (`url_id`,`checkingDate`)
);

CREATE TABLE IF NOT EXISTS `obsolete` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(1024) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `source` VARCHAR(256) DEFAULT NULL,
  `providerGroupName` VARCHAR(256) DEFAULT NULL,
  `record` VARCHAR(256) DEFAULT NULL,
  `expectedMimeType` VARCHAR(256) DEFAULT NULL,
  `ingestionDate` DATETIME DEFAULT NULL,
  `statusCode` INT DEFAULT NULL,
  `message` VARCHAR(1024) DEFAULT NULL,
  `category` VARCHAR(25) NOT NULL,
  `method` VARCHAR(10) DEFAULT NULL,
  `contentType` VARCHAR(256) DEFAULT NULL,
  `byteSize` bigint DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checkingDate` DATETIME NOT NULL,
  `redirectCount` INT DEFAULT NULL,
  `deletionDate` DATETIME NOT NULL DEFAULT NOW(),
   PRIMARY KEY (`id`)
);


CREATE VIEW IF NOT EXISTS aggregatedStatus AS
 SELECT ROW_NUMBER() OVER (ORDER BY p.name, s.category) AS id, p.name, s.category, COUNT(s.id) AS number, AVG(s.duration) AS avg_duration, MAX(s.duration) AS max_duration
 FROM url_context uc
 JOIN (status s, context c)
 ON (uc.url_id=s.url_id AND uc.context_id=c.id)
 JOIN providerGroup p
 ON (p.id=c.providerGroup_id)
 WHERE uc.active=true
 GROUP BY p.name, s.category;
