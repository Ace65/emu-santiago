-- New update: legion_members ranks 16.11.2011

ALTER TABLE `legion_members` 
MODIFY COLUMN `rank` enum('BRIGADE_GENERAL','SUB_GENERAL','CENTURION','LEGIONARY','NEW_LEGIONARY','DEPUTY','VOLUNTEER') NOT NULL DEFAULT 'NEW_LEGIONARY';

UPDATE `legion_members` 
SET `rank` = 'DEPUTY' WHERE `rank` = 'SUB_GENERAL';
UPDATE `legion_members` 
SET `rank` = 'VOLUNTEER' WHERE `rank` = 'NEW_LEGIONARY';

ALTER TABLE `legion_members` 
MODIFY COLUMN `rank` enum('BRIGADE_GENERAL','DEPUTY','CENTURION','LEGIONARY','VOLUNTEER') NOT NULL DEFAULT 'VOLUNTEER' AFTER `nickname`;