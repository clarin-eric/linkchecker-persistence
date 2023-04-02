#!/bin/bash

# author: Wolfgang Walter SAUER (clarin@wowasa.com)
# date: 2022-12-08
# description:
# The purpose of the script is to transfer and existing linkchecker database accessibe by the RASA project to the new database structure accessible with linkchecker-peristence. 
# Since mariadb doesn't allow to simply rename an existing database, we have to dump the database and restore it to a temporary database. Then we drop the existing linkchecker database, 
# create a new one with the new structure and transfer the data from the temporary database to it. 

DUMP_FILE=/tmp/$(date +%s).sql
echo "dumping database ${MYSQL_DATABASE} as ${DUMP_FILE}"
mysqldump -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} > ${DUMP_FILE}
echo "done dumping"
echo "droping tempory databse tmpDB if exists"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} -e "DROP DATABASE IF EXISTS tmpDB;"
echo "done droping"
echo "creating temporary database tmpDB"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} -e "CREATE DATABASE tmpDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;"
echo "done creating"
echo "restoring dump ${DUMP_FILE} into temporary database tmpDB"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} tmpDB < ${DUMP_FILE}
echo "done restoring"
echo "removing dump file ${DUMP_FILE}"
rm ${DUMP_FILE}
echo "done removing"
echo "dropping database ${MYSQL_DATABASE}"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} -e "DROP DATABASE IF EXISTS ${MYSQL_DATABASE};"
echo "done dropping"
echo "creating new database ${MYSQL_DATABASE}"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} -e "CREATE DATABASE ${MYSQL_DATABASE} CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;"
echo "done creating"
echo "creating database design in database ${MYSQL_DATABASE}"
curl https://raw.githubusercontent.com/clarin-eric/linkchecker-persistence/dev/src/main/resources/schema.sql | mysql -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE}
echo "done creating database design"
echo "transfering url table"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} -e "INSERT INTO ${MYSQL_DATABASE}.url (SELECT * FROM tmpDB.url);" 
echo "done transfering url table"
echo "transfering history table" 
mysql -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} -e "INSERT INTO ${MYSQL_DATABASE}.history (SELECT id, url_id, statusCode, message, category, method, contentType, byteSize, duration, checkingDate, redirectCount FROM tmpDB.history);"
echo "done transfering history table"
echo "transfering obsolete table"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} -e "INSERT INTO ${MYSQL_DATABASE}.obsolete(url_name, client_name, providergroup_name, origin, expected_mime_type, ingestion_date, status_code, message, category, method, content_type, content_length, duration, checking_date, redirect_count, deletion_date) (SELECT url, 'curation', providerGroupName, record, expectedMimeType, ingestionDate, statusCode, message, category, method, contentType, byteSize, duration, checkingDate, redirectCount, deletionDate FROM tmpDB.obsolete);"
echo "done transfering obsolete table"
echo "transfering status table"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} -e "INSERT INTO ${MYSQL_DATABASE}.status (SELECT id, url_id, statusCode, IFNULL(message, 'n/a'), category, method, contentType, byteSize, duration, checkingDate, redirectCount, NULL FROM tmpDB.status);"
echo "done transfering status table"
echo "transfering providergroup table" 
mysql -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} -e "INSERT INTO ${MYSQL_DATABASE}.providergroup (SELECT * FROM tmpDB.providerGroup);"
echo "done transfering providergroup table"
echo "adding curation user to client table"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} -e "INSERT INTO ${MYSQL_DATABASE}.client VALUES(1, 'curation', '*', NULL, NULL, 'USER', true);"
echo "done adding curation user"
echo "transfering context table"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} -e "INSERT IGNORE INTO ${MYSQL_DATABASE}.context (SELECT id, 1, record, providerGroup_id FROM tmpDB.context)"
echo "done transfering context table"
echo "transfering url_context table"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} -e "INSERT IGNORE INTO ${MYSQL_DATABASE}.url_context (SELECT uc.id, uc.url_id, uc.context_id, c.expectedMimeType, uc.ingestionDate, uc.active FROM tmpDB.url_context uc INNER JOIN tmpDB.context c ON uc.context_id=c.id);"
echo "done transfering url_context table"
echo "dropping temeporary database tmpDB"
mysql -u root --password=${MYSQL_ROOT_PASSWORD} -e "DROP DATABASE IF EXISTS tmpDB;"
echo "done dropping database"
echo "database transfer finished successfully!"
