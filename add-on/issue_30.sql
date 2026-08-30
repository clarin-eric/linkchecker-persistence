ALTER TABLE `status` ADD COLUMN `final_url` VARCHAR(512) DEFAULT NULL;
ALTER TABLE `history` ADD COLUMN `final_url` VARCHAR(512) DEFAULT NULL;
ALTER TABLE `url` ADD COLUMN `exclude_checking` BOOLEAN DEFAULT NULL;
# corrections
# dropping table providerGroup (!= providergroup)
DROP TABLE IF EXISTS `providerGroup`;
# dropping table urlToCheck
DROP TABLE IF EXISTS `urlToCheck`;
# using handle prefix instead of host as group key for handles
UPDATE `url` SET `group_key` = SUBSTRING_INDEX(SUBSTRING_INDEX(name , '/', 4), '/', -1) WHERE `group_key` = 'hdl.handle.net';
# exclude 'archive.mpi.nl' temporarely from checking to process more handles
UPDATE `url` SET `exclude_checking`=TRUE WHERE `group_key` = 'archive.mpi.nl';
# ordered list of URLs to check
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
