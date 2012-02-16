ALTER TABLE `aionshop_transactions`
ADD `gift_receiver` varchar(20) NOT NULL DEFAULT '0' AFTER `player_id`; 
