/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 80027
Source Host           : localhost:3306
Source Database       : im

Target Server Type    : MYSQL
Target Server Version : 80027
File Encoding         : 65001

Date: 2022-01-21 10:44:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for contacts
-- ----------------------------
DROP TABLE IF EXISTS `contacts`;
CREATE TABLE `contacts` (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户表id',
  PRIMARY KEY (`id`,`user_id`),
  KEY `fk_1` (`user_id`),
  CONSTRAINT `fk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of contacts
-- ----------------------------
INSERT INTO `contacts` VALUES ('1', '张三', '2');
INSERT INTO `contacts` VALUES ('1', '张三', '5');
INSERT INTO `contacts` VALUES ('1', '张三', '8');
INSERT INTO `contacts` VALUES ('1', '张三', '99');
INSERT INTO `contacts` VALUES ('2', '李四', '1');
INSERT INTO `contacts` VALUES ('2', '李四', '3');
INSERT INTO `contacts` VALUES ('2', '李四', '5');
INSERT INTO `contacts` VALUES ('3', '一生一世一双人', '1');
INSERT INTO `contacts` VALUES ('3', '一生一世一双人', '2');
INSERT INTO `contacts` VALUES ('4', '彷徨~~', '1');
INSERT INTO `contacts` VALUES ('4', '彷徨~~', '2');
INSERT INTO `contacts` VALUES ('5', '李白', '1');
INSERT INTO `contacts` VALUES ('99', '王', '1');
