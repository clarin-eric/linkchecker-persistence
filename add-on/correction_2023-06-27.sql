UPDATE history SET category='Ok' WHERE category='0';
UPDATE history SET category='Undetermined' WHERE category='1';
UPDATE history SET category='Restricted_Access' WHERE category='2';
UPDATE history SET category='Blocked_By_Robots_txt' WHERE category='3';
UPDATE history SET category='Broken' WHERE category='4';
UPDATE history SET category='Invalid_URL' WHERE category='5';
UPDATE obsolete SET category='Ok' WHERE category='0';
UPDATE obsolete SET category='Undetermined' WHERE category='1';
UPDATE obsolete SET category='Restricted_Access' WHERE category='2';
UPDATE obsolete SET category='Blocked_By_Robots_txt' WHERE category='3';
UPDATE obsolete SET category='Broken' WHERE category='4';
UPDATE obsolete SET category='Invalid_URL' WHERE category='5';
