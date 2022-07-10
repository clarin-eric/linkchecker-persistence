

CREATE TABLE IF NOT EXISTS `providergroup` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
);

CREATE TABLE IF NOT EXISTS `client` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `email` VARCHAR(256) NOT NULL,
   `token` CHAR(32) NOT NULL,
   `quota` INT DEFAULT NULL, 
   PRIMARY KEY (`id`),
   INDEX (`email`, `token`)  
);


CREATE TABLE IF NOT EXISTS `context` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `client_id` INT NOT NULL,
  `origin` VARCHAR(256) NOT NULL,
  `providergroup_id` INT DEFAULT NULL,
  `expected_mime_type` VARCHAR(64) DEFAULT NULL,
  PRIMARY KEY (`id`),  
  UNIQUE KEY (`origin`, `providergroup_id`, `expected_mime_type`, `client_id`),
  INDEX (`providergroup_id`),
  FOREIGN KEY (`providergroup_id`) REFERENCES `providergroup` (`id`),
  INDEX (`client_id`),
  FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
);



CREATE TABLE IF NOT EXISTS `url` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(1024) NOT NULL,
  `group_key` VARCHAR(128) DEFAULT NULL,
  `valid` boolean DEFAULT NULL, 
  PRIMARY KEY (`id`),
  UNIQUE KEY (`url`),
  INDEX (`group_key`)
);


CREATE TABLE IF NOT EXISTS `url_context` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url_id` INT NOT NULL,
  `context_id` INT NOT NULL,
  `ingestion_date` DATETIME NOT NULL DEFAULT NOW(),
  `active` boolean NOT NULL DEFAULT false,
  PRIMARY KEY (`id`),
  INDEX (`url_id`, `active`, `context_id`),
  INDEX (`context_id`, `active`, `url_id`),
  UNIQUE KEY (`url_id`, `context_id`),
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
  `byte_size` bigint DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checking_date` DATETIME NOT NULL,
  `redirect_count` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`url_id`),
  INDEX (`category`),
  FOREIGN KEY (`url_id`) REFERENCES `url` (`id`)
);


CREATE TABLE IF NOT EXISTS `history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status_id` INT NOT NULL,
  `url_id` INT DEFAULT NULL,
  `status_code` INT DEFAULT NULL,
  `message` VARCHAR(256),
  `category` VARCHAR(25) NOT NULL,
  `method` VARCHAR(10) NOT NULL,
  `content_type` VARCHAR(256) DEFAULT NULL,
  `byte_size` INT DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checking_date` DATETIME NOT NULL,
  `redirect_count` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`url_id`,`checking_date`)
);

CREATE TABLE IF NOT EXISTS `obsolete` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `url` VARCHAR(1024) NOT NULL,
  `client` int DEFAULT NULL,
  `providergroup_name` VARCHAR(256) DEFAULT NULL,
  `origin` VARCHAR(256) DEFAULT NULL,
  `expected_mime_type` VARCHAR(256) DEFAULT NULL,
  `ingestion_date` DATETIME DEFAULT NULL,
  `status_code` INT DEFAULT NULL,
  `message` VARCHAR(1024) DEFAULT NULL,
  `category` VARCHAR(25) NOT NULL,
  `method` VARCHAR(10) DEFAULT NULL,
  `content_type` VARCHAR(256) DEFAULT NULL,
  `byte_size` bigint DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checking_date` DATETIME NOT NULL,
  `redirect_count` INT DEFAULT NULL,
  `deletion_date` DATETIME NOT NULL DEFAULT NOW(),
   PRIMARY KEY (`id`)
);


CREATE VIEW IF NOT EXISTS `aggregatedStatus` AS
 SELECT uc.url_id AS url_id, c.id AS context_id, s.category, COUNT(s.id) AS number, AVG(s.duration) AS avg_duration, MAX(s.duration) AS max_duration
 FROM url_context uc
 JOIN (status s)
 ON (uc.url_id=s.url_id)
 JOIN (context c)
 ON (uc.context_id=c.id)
 JOIN providergroup p
 ON (p.id=c.providergroup_id)
 WHERE uc.active=true
 GROUP BY p.name, s.category;
