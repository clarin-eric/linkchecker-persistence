# author: Wolfgang Walter SAUER (clarin@wowasa.com)
# creation-date: 2022-09-26
# 
# the purpose of this script is to transfer the old database design used by the RASA API to the new cruration-persistence API design 

RENAME TABLE `url` TO `url_old`;
RENAME TABLE `status` TO `status_old`;
RENAME TABLE `history` TO `history_old`;
RENAME TABLE `obsolete` TO `obsolete_old`;
#RENAME TABLE `providerGroup` TO `providergroup_tmp`;
#RENAME TABLE `providergroup_tmp` TO `providergroup`;
RENAME TABLE `context` TO `context_old`;
RENAME TABLE `url_context` TO `url_context_old`;

ALTER DATABASE `linkchecker`
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `client` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `name` VARCHAR(256) NOT NULL,
   `password` VARCHAR(128) NOT NULL,
   `email` VARCHAR(256) DEFAULT NULL,
   `quota` INT DEFAULT NULL, 
   `role` VARCHAR(64) NOT NULL,
   `enabled` BOOLEAN DEFAULT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY (`name`)
);


CREATE TABLE IF NOT EXISTS `context` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `client_id` INT NOT NULL,
  `origin` VARCHAR(256) NOT NULL,
  `providergroup_id` INT DEFAULT NULL,
  PRIMARY KEY (`id`),  
  UNIQUE KEY (`origin`, `providergroup_id`, `client_id`),
  INDEX (`providergroup_id`),
  FOREIGN KEY (`providergroup_id`) REFERENCES `providergroup` (`id`),
  INDEX (`client_id`),
  FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
);



CREATE TABLE IF NOT EXISTS `url` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(512) NOT NULL,
  `group_key` VARCHAR(128) DEFAULT NULL,
  `valid` boolean DEFAULT NULL, 
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`),
  INDEX (`group_key`)
);


CREATE TABLE IF NOT EXISTS `url_context` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url_id` INT NOT NULL,
  `context_id` INT NOT NULL,
  `expected_mime_type` VARCHAR(64) DEFAULT NULL,
  `ingestion_date` DATETIME NOT NULL,
  `active` BOOLEAN NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`url_id`, `active`, `context_id`),
  INDEX (`context_id`, `active`, `url_id`),
  UNIQUE KEY (`url_id`, `context_id`, `expected_mime_type`),
  FOREIGN KEY (`url_id`) REFERENCES `url` (`id`),
  FOREIGN KEY (`context_id`) REFERENCES `context` (`id`)
);


CREATE TABLE IF NOT EXISTS `status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url_id` INT NOT NULL,
  `status_code` INT DEFAULT NULL,
  `message` VARCHAR(1024) NOT NULL,
  `category` VARCHAR(25) NOT NULL,
  `method` VARCHAR(10) DEFAULT NULL,
  `content_type` VARCHAR(256) DEFAULT NULL,
  `content_length` bigint DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checking_date` DATETIME NOT NULL,
  `redirect_count` INT DEFAULT NULL,
  `recheck` BOOLEAN DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`url_id`),
  INDEX (`category`),
  INDEX (`checking_date`),
  FOREIGN KEY (`url_id`) REFERENCES `url` (`id`)
  ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS `history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url_id` INT DEFAULT NULL,
  `status_code` INT DEFAULT NULL,
  `message` VARCHAR(256) DEFAULT NULL,
  `category` VARCHAR(25) NOT NULL,
  `method` VARCHAR(10) DEFAULT NULL,
  `content_type` VARCHAR(256) DEFAULT NULL,
  `content_length` INT DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checking_date` DATETIME NOT NULL,
  `redirect_count` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`url_id`,`checking_date`),
  FOREIGN KEY (`url_id`) REFERENCES `url` (`id`)
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `obsolete` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url_name` VARCHAR(512) NOT NULL,
  `client_name` INT DEFAULT NULL,
  `providergroup_name` VARCHAR(256) DEFAULT NULL,
  `origin` VARCHAR(256) DEFAULT NULL,
  `expected_mime_type` VARCHAR(256) DEFAULT NULL,
  `ingestion_date` DATETIME DEFAULT NULL,
  `status_code` INT DEFAULT NULL,
  `message` VARCHAR(1024) DEFAULT NULL,
  `category` VARCHAR(25) DEFAULT NULL,
  `method` VARCHAR(10) DEFAULT NULL,
  `content_type` VARCHAR(256) DEFAULT NULL,
  `content_length` bigint DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checking_date` DATETIME DEFAULT NULL,
  `redirect_count` INT DEFAULT NULL,
  `deletion_date` DATETIME NOT NULL,
   PRIMARY KEY (`id`)
);


CREATE VIEW IF NOT EXISTS `aggregated_status` AS
 SELECT p.name, s.category, COUNT(s.id) AS number, AVG(s.duration) AS avg_duration, MAX(s.duration) AS max_duration
 FROM url_context uc
 JOIN (status s)
 ON (uc.url_id=s.url_id)
 JOIN (context c)
 ON (uc.context_id=c.id)
 JOIN providergroup p
 ON (p.id=c.providergroup_id)
 WHERE uc.active=true
 GROUP BY p.name, s.category;


INSERT INTO `client`(`name`, `password`, `role`, `enabled`) VALUES('curation', 'change_password', 'USER', true); 

INSERT INTO `obsolete`(
								`url_name`, 
								`client_name`, 
								`providergroup_name`, 
								`origin`, 
								`expected_mime_type`, 
								`ingestion_date`, 
								`status_code`, 
								`message`, 
								`category`, 
								`method`, 
								`content_type`,
								`content_length`,
								`duration`,
								`checking_date`,
								`redirect_count`,
								`deletion_date`
								)
SELECT `url`, 
	'curation', 
	`providerGroupName`, 
	`record`, 
	`expectedMimeType`, 
	`ingestionDate`, 
	`statusCode`, 
	`message`, 
	`category`, 
	`method`, 
	`contentType`, 
	`byteSize`, 
	`duration`, 
	`checkingDate`, 
	`redirectCount`, 
	`deletionDate`
FROM `obsolete_old`;

DROP TABLE `obsolete_old`;

INSERT IGNORE INTO `url` (`id`, `name`, `group_key`, `valid`)
SELECT `id`, `url`, `groupKey`, `valid`
FROM `url_old`;

INSERT IGNORE INTO `history`(`url_id`, `status_code`, `message`, `category`, `method`, `content_type`, `content_length`, `duration`, `checking_date`, `redirect_count`)
SELECT `url_id`, `statusCode`, `message`, `category`, `method`, `contentType`, `byteSize`, `duration`, `checkingDate`, `redirectCount`
FROM `history_old`; 

DROP TABLE `history_old`;

INSERT IGNORE INTO `status`(`url_id`, `status_code`, `message`, `category`, `method`, `content_type`, `content_length`, `duration`, `checking_date`, `redirect_count`)
SELECT `url_id`, `statusCode`, `message`, `category`, `method`, `contentType`, `byteSize`, `duration`, `checkingDate`, `redirectCount`
FROM `status_old`; 

DROP TABLE `status_old`;

INSERT IGNORE INTO `context`(`id`, `client_id`, `origin`, `providergroup_id`)
SELECT id, 1, record, providerGroup_id
FROM `context_old`;

INSERT IGNORE INTO `url_context`(`url_id`, `context_id`, `expected_mime_type`, `ingestion_date`, `active`)
SELECT uc.url_id, uc.context_id, c.expectedMimeType, uc.ingestionDate, uc.active
FROM url_context_old uc
INNER JOIN context_old c
ON c.id=uc.context_id;

DROP TABLE `url_context_old`;
DROP TABLE `context_old`;
DROP TABLE `url_old`;



