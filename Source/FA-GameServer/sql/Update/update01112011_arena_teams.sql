/*
Navicat MySQL Data Transfer

Source Server         : Aion Extreme
Source Server Version : 50512
Source Host           : localhost:3306
Source Database       : au_server_ls

Target Server Type    : MYSQL
Target Server Version : 50512
File Encoding         : 65001

Date: 2011-06-23 01:06:47
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for `arena_teams`
-- ----------------------------
DROP TABLE IF EXISTS `arena_teams`;
CREATE TABLE `arena_teams` (
  `id` int(11) NOT NULL auto_increment,
  `type` int(2) NOT NULL DEFAULT '0',
  `points` int(11) NOT NULL DEFAULT '0',
  `name` varchar(150) NOT NULL,
  `players` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

