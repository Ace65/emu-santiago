ALTER TABLE `legion_history`
MODIFY COLUMN `history_type`  enum('CREATE','JOIN','KICK','LEVEL_UP','APPOINTED','EMBLEM_REGISTER','EMBLEM_MODIFIED') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `date`;