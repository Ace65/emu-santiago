ALTER TABLE `players`
ADD COLUMN `partner_id` int NULL,
ADD CONSTRAINT `setPartnerNull` FOREIGN KEY (`partner_id`) REFERENCES `players` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION;
