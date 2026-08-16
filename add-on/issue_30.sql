ALTER TABLE `status` ADD COLUMN `final_url` VARCHAR(512) DEFAULT NULL;
ALTER TABLE `history` ADD COLUMN `final_url` VARCHAR(512) DEFAULT NULL;
ALTER TABLE `url` ADD COLUMN `exclude_checking` BOOLEAN DEFAULT NULL;
# reindexing of the id field since the numbers became extremly high
ALTER TABLE `url_context` DROP COLUMN `id`;
ALTER TABLE `url_context` ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;