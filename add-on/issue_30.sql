ALTER TABLE `status` ADD COLUMN `final_url` VARCHAR(512) DEFAULT NULL;
ALTER TABLE `history` ADD COLUMN `final_url` VARCHAR(512) DEFAULT NULL;
ALTER TABLE `url` ADD COLUMN `exclude_checking` BOOLEAN DEFAULT NULL;
# corrections
# dropping table providerGroup (!= providergroup)
DROP TABLE `providerGroup`;
# dropping table urlToCheck
DROP TABLE `urlToCheck`
# using handle prefix instead of host as group key for handles
UPDATE `url` SET `group_key` = SUBSTRING_INDEX(SUBSTRING_INDEX(name , '/', 4), '/', -1) WHERE `group_key` = 'hdl.handle.net';
