/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80027
Source Host           : localhost:3306
Source Database       : im

Target Server Type    : MYSQL
Target Server Version : 80027
File Encoding         : 65001

Date: 2022-01-21 10:44:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(20) NOT NULL,
  `name` varchar(30) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '张三', '123456789');
INSERT INTO `users` VALUES ('2', '李四', '123456');
INSERT INTO `users` VALUES ('3', '一生一世一双人', '12345678');
INSERT INTO `users` VALUES ('4', '彷徨~~', '666666');
INSERT INTO `users` VALUES ('5', '李白', '12345678');
INSERT INTO `users` VALUES ('56', '刘传', '123456789');
INSERT INTO `users` VALUES ('8', '朱', '12345678');
INSERT INTO `users` VALUES ('99', '王', '12345678');
