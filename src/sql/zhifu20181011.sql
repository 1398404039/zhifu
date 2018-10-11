/*
Navicat MySQL Data Transfer

Source Server         : 39.106.145.162
Source Server Version : 50641
Source Host           : 39.106.145.162:3306
Source Database       : zhifu

Target Server Type    : MYSQL
Target Server Version : 50641
File Encoding         : 65001

Date: 2018-10-11 13:42:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for lwz_order_info
-- ----------------------------
DROP TABLE IF EXISTS `lwz_order_info`;
CREATE TABLE `lwz_order_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(32) NOT NULL COMMENT '订单id',
  `item_id` varchar(255) DEFAULT NULL COMMENT '业务方  商品Id',
  `item_name` varchar(255) DEFAULT NULL COMMENT '业务方 商品名称',
  `item_amount` double DEFAULT NULL COMMENT '业务方 商品价格',
  `order_type` int(11) DEFAULT NULL COMMENT '业务方 源（来自于哪个系统）',
  `create_time` datetime DEFAULT NULL,
  `pay_state` int(11) DEFAULT NULL COMMENT '支付状态',
  `pay_type` int(11) DEFAULT NULL COMMENT '支付类型',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`,`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lwz_order_info
-- ----------------------------
