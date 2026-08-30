CREATE TABLE IF NOT EXISTS `providergroup` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(256) NOT NULL,
  UNIQUE KEY (`name`)
);

CREATE TABLE IF NOT EXISTS `client` (
   `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `name` VARCHAR(256) NOT NULL,
   `password` VARCHAR(128) NOT NULL,
   `email` VARCHAR(256) DEFAULT NULL,
   `quota` INT DEFAULT NULL, 
   `role` VARCHAR(64) NOT NULL,
   `enabled` BOOLEAN DEFAULT NULL,
   UNIQUE KEY (`name`)
);


CREATE TABLE IF NOT EXISTS `context` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `client_id` INT NOT NULL,
  `origin` VARCHAR(512) NOT NULL,
  `providergroup_id` INT DEFAULT NULL,
  UNIQUE KEY (`origin`, `providergroup_id`, `client_id`),
  INDEX (`providergroup_id`),
  FOREIGN KEY (`providergroup_id`) REFERENCES `providergroup` (`id`),
  INDEX (`client_id`),
  FOREIGN KEY (`client_id`) REFERENCES `client` (`id`)
);



CREATE TABLE IF NOT EXISTS `url` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(512) NOT NULL,
  `group_key` VARCHAR(128) DEFAULT NULL,
  `valid` BOOLEAN DEFAULT NULL, 
  `priority` TINYINT NOT NULL DEFAULT 0,
  `exclude_checking` BOOLEAN DEFAULT NULL,
  UNIQUE KEY (`name`),
  INDEX (`group_key`)
);


CREATE TABLE IF NOT EXISTS `url_context` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `url_id` INT NOT NULL,
  `context_id` INT NOT NULL,
  `expected_mime_type` VARCHAR(128) DEFAULT NULL,
  `ingestion_date` DATETIME NOT NULL,
  `active` BOOLEAN NOT NULL,
  INDEX (`url_id`, `active`, `context_id`),
  INDEX (`context_id`, `active`, `url_id`),
  UNIQUE KEY (`url_id`, `context_id`, `expected_mime_type`),
  FOREIGN KEY (`url_id`) REFERENCES `url` (`id`),
  FOREIGN KEY (`context_id`) REFERENCES `context` (`id`)
);


CREATE TABLE IF NOT EXISTS `status` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `url_id` INT NOT NULL,
  `status_code` INT DEFAULT NULL,
  `message` VARCHAR(1024) NOT NULL,
  `category` VARCHAR(25) NOT NULL,
  `method` VARCHAR(10) DEFAULT NULL,
  `content_type` VARCHAR(256) DEFAULT NULL,
  `content_length` BIGINT DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checking_date` DATETIME NOT NULL,
  `redirect_count` INT DEFAULT NULL,
  `final_url` VARCHAR(512) DEFAULT NULL,
  UNIQUE KEY (`url_id`),
  INDEX (`category`),
  INDEX (`checking_date`),
  FOREIGN KEY (`url_id`) REFERENCES `url` (`id`)
  ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS `history` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `url_id` INT DEFAULT NULL,
  `status_code` INT DEFAULT NULL,
  `message` VARCHAR(1024) DEFAULT NULL,
  `category` VARCHAR(25) NOT NULL,
  `method` VARCHAR(10) DEFAULT NULL,
  `content_type` VARCHAR(256) DEFAULT NULL,
  `content_length` BIGINT DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checking_date` DATETIME NOT NULL,
  `redirect_count` INT DEFAULT NULL,
  `final_url` VARCHAR(512) DEFAULT NULL,
  UNIQUE KEY (`url_id`,`checking_date`),
  FOREIGN KEY (`url_id`) REFERENCES `url` (`id`)
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `obsolete` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `url_name` VARCHAR(512) NOT NULL,
  `client_name` VARCHAR(256) DEFAULT NULL,
  `providergroup_name` VARCHAR(256) DEFAULT NULL,
  `origin` VARCHAR(512) DEFAULT NULL,
  `expected_mime_type` VARCHAR(256) DEFAULT NULL,
  `ingestion_date` DATETIME DEFAULT NULL,
  `status_code` INT DEFAULT NULL,
  `message` VARCHAR(1024) DEFAULT NULL,
  `category` VARCHAR(25) DEFAULT NULL,
  `method` VARCHAR(10) DEFAULT NULL,
  `content_type` VARCHAR(256) DEFAULT NULL,
  `content_length` BIGINT DEFAULT NULL,
  `duration` INT DEFAULT NULL,
  `checking_date` DATETIME DEFAULT NULL,
  `redirect_count` INT DEFAULT NULL,
  `deletion_date` DATETIME NOT NULL
);

CREATE OR REPLACE VIEW `url_to_check` AS
     SELECT name, url_id, status_id, priority FROM
           (SELECT ROW_NUMBER() OVER (PARTITION BY u.group_key ORDER BY u.priority DESC, s.checking_date) AS order_nr, u.id AS url_id, u.name, u.group_key, u.valid, u.priority, s.id AS status_id, s.checking_date
           FROM url u
           LEFT JOIN status s ON s.url_id = u.id
           WHERE u.valid IS TRUE
           AND u.exclude_checking IS NOT TRUE
           AND u.id IN (SELECT uc.url_id FROM url_context uc WHERE uc.active = true)
           AND (s.checking_date IS NULL OR DATEDIFF(NOW(), s.checking_date) > 1)
           ORDER BY u.group_key, u.priority DESC, s.checking_date) tab1
        ORDER by order_nr;
