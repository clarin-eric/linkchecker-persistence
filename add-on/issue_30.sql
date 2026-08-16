ALTER TABLE `status` ADD COLUMN `final_url` VARCHAR(512) DEFAULT NULL;
ALTER TABLE `history` ADD COLUMN `final_url` VARCHAR(512) DEFAULT NULL;
ALTER TABLE `url` ADD COLUMN `exclude_checking` BOOLEAN DEFAULT NULL;
# corrections
# reindexing of the id field since the numbers exceed the INT limit
ALTER TABLE `url_context` DROP COLUMN `id`;
ALTER TABLE `url_context` ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST;
# dropping table providerGroup (!= providergroup)
DROP TABLE `providerGroup`;
# dropping table urlToCheck
DROP TABLE `urlToCheck`
# adding new table url_to_check
CREATE TABLE IF NOT EXISTS `url_to_check` (
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `url_id` INT NOT NULL,
    `status_id` INT DEFAULT NULL,
    `url_name` VARCHAR(512)
)
# using handle prefix instead of host as group key for handles
UPDATE `url` SET `group_key` = SUBSTRING_INDEX(SUBSTRING_INDEX(name , '/', 4), '/', -1) WHERE `group_key` = 'hdl.handle.net';
