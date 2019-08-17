/*
Navicat MySQL Data Transfer

Source Server         : 120.79.62.89_3306
Source Server Version : 50724
Source Host           : 120.79.62.89:3306
Source Database       : yetdwell

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-06-26 19:58:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for advice
-- ----------------------------
DROP TABLE IF EXISTS `advice`;
CREATE TABLE `advice` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` int(20) DEFAULT '0' COMMENT '标题',
  `content` bigint(20) DEFAULT NULL COMMENT '内容',
  `img` varchar(70) DEFAULT NULL COMMENT '头像',
  `status` tinyint(3) DEFAULT '0' COMMENT '来',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- ----------------------------
-- Table structure for agent
-- ----------------------------
DROP TABLE IF EXISTS `agent`;
CREATE TABLE `agent` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` int(20) DEFAULT NULL COMMENT '用户ID',
  `agentId` int(20) DEFAULT NULL COMMENT '上级ID',
  `createTime` datetime DEFAULT NULL COMMENT '邀请时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '0正常;1冻结',
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `userId_Id` (`userId`),
  KEY `agentId` (`agentId`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `userId` (`userId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='邀请表';

-- ----------------------------
-- Table structure for agentlog
-- ----------------------------
DROP TABLE IF EXISTS `agentlog`;
CREATE TABLE `agentlog` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `agentId` int(20) DEFAULT '0' COMMENT '运营商Id',
  `userId` int(20) DEFAULT '0' COMMENT '升级的用户Id',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代理升级表';

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `userId` int(20) DEFAULT '0' COMMENT '用户id',
  `goodId` bigint(20) DEFAULT NULL COMMENT '商品id',
  `title` varchar(70) DEFAULT NULL COMMENT '商品标题',
  `src` tinyint(3) DEFAULT '0' COMMENT '来源平台 0 天猫 1拼多多 2 京东',
  `price` decimal(12,2) DEFAULT '0.00' COMMENT '原价',
  `coupon_price` decimal(12,2) DEFAULT '0.00' COMMENT '卷后价钱',
  `image` varchar(150) DEFAULT NULL COMMENT '商品图片URL',
  `promotion_rate` bigint(11) DEFAULT '0' COMMENT '佣金比率',
  `coupon` bigint(10) DEFAULT '0' COMMENT '优惠卷',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `volume` int(15) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId_2` (`userId`,`goodId`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- ----------------------------
-- Table structure for collectlog
-- ----------------------------
DROP TABLE IF EXISTS `collectlog`;
CREATE TABLE `collectlog` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `sum` int(10) DEFAULT '0' COMMENT '采集数',
  `createTime` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8877 DEFAULT CHARSET=utf8mb4 COMMENT='收集';

-- ----------------------------
-- Table structure for invcode
-- ----------------------------
DROP TABLE IF EXISTS `invcode`;
CREATE TABLE `invcode` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` int(20) DEFAULT NULL COMMENT '用户id',
  `createTime` datetime DEFAULT NULL COMMENT '邀请时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '0正常;1冻结',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uI` (`userId`) USING BTREE,
  KEY `userId` (`userId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='邀请表';

-- ----------------------------
-- Table structure for jdoder
-- ----------------------------
DROP TABLE IF EXISTS `jdoder`;
CREATE TABLE `jdoder` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `positionId` varchar(35) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '京东pid',
  `actualCosPrice` decimal(20,2) DEFAULT '0.00' COMMENT '1',
  `actualFee` decimal(20,2) DEFAULT '0.00' COMMENT '推客获得的实际佣金',
  `commissionRate` decimal(10,0) DEFAULT '0' COMMENT '佣金比例',
  `estimateCosPrice` decimal(10,2) DEFAULT '0.00' COMMENT '预估计佣金额',
  `estimateFee` decimal(10,2) DEFAULT '0.00' COMMENT '推客的预估佣金额',
  `finalRate` decimal(10,0) DEFAULT '0' COMMENT '最终比例',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `skuId` bigint(20) DEFAULT '0' COMMENT '商品Id',
  `skuName` varchar(70) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '商品名称',
  `orderId` bigint(20) DEFAULT '0' COMMENT '订单ID',
  `payMonth` bigint(20) DEFAULT '0' COMMENT '结算时间',
  `finishTime` bigint(20) DEFAULT '0' COMMENT '订单完成时间',
  `orderTime` bigint(20) DEFAULT '0' COMMENT '下单时间',
  `validCode` tinyint(5) DEFAULT '0' COMMENT '15.待付款,16.已付款,17.已完成,18.已结算',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `settle` tinyint(3) DEFAULT '0' COMMENT '结算状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `orderId` (`orderId`) USING BTREE,
  KEY `positionId` (`positionId`),
  KEY `orderTime` (`orderTime`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='京东订单表';

-- ----------------------------
-- Table structure for jh_advice_dev
-- ----------------------------
DROP TABLE IF EXISTS `jh_advice_dev`;
CREATE TABLE `jh_advice_dev` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `titile` varchar(130) DEFAULT NULL COMMENT '标题',
  `content` varchar(220) DEFAULT '' COMMENT '内容',
  `image` varchar(130) DEFAULT NULL COMMENT '头像',
  `content_image` varchar(200) DEFAULT NULL COMMENT '图片URL',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='官方订单通知表';

-- ----------------------------
-- Table structure for jh_advice_oder
-- ----------------------------
DROP TABLE IF EXISTS `jh_advice_oder`;
CREATE TABLE `jh_advice_oder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `oderSn` varchar(80) NOT NULL COMMENT '订单编号',
  `src` tinyint(3) NOT NULL DEFAULT '0' COMMENT '平台类型',
  `src_name` varchar(5) DEFAULT NULL COMMENT '平台名称',
  `name` varchar(85) DEFAULT NULL COMMENT '订单标题',
  `userName` varchar(45) DEFAULT NULL COMMENT '用户名',
  `pid` varchar(50) NOT NULL COMMENT '推广位',
  `order_status` int(5) DEFAULT '0' COMMENT '订单状态',
  `order_status_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单状态描述',
  `oder_createTime` datetime DEFAULT NULL COMMENT '订单的创建时间',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COMMENT='订单通知表';

-- ----------------------------
-- Table structure for jh_banner_good
-- ----------------------------
DROP TABLE IF EXISTS `jh_banner_good`;
CREATE TABLE `jh_banner_good` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goodId` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `src` tinyint(5) DEFAULT '0' COMMENT '平台类型',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(2) DEFAULT '0',
  `imgUrl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='轮播图';

-- ----------------------------
-- Table structure for jh_banner_img
-- ----------------------------
DROP TABLE IF EXISTS `jh_banner_img`;
CREATE TABLE `jh_banner_img` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(280) DEFAULT NULL COMMENT 'url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COMMENT='轮播图';

-- ----------------------------
-- Table structure for jh_cash_apply
-- ----------------------------
DROP TABLE IF EXISTS `jh_cash_apply`;
CREATE TABLE `jh_cash_apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `account` varchar(130) DEFAULT NULL COMMENT '支付宝账号',
  `money` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '提现金额',
  `audit` tinyint(3) NOT NULL DEFAULT '0' COMMENT '处理状态 0处理中 1提现完成 2提现失败',
  `roleid` tinyint(3) DEFAULT NULL,
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COMMENT='提现申请表';

-- ----------------------------
-- Table structure for jh_cash_log
-- ----------------------------
DROP TABLE IF EXISTS `jh_cash_log`;
CREATE TABLE `jh_cash_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` int(20) DEFAULT NULL COMMENT '用户id',
  `src` varchar(10) DEFAULT NULL COMMENT '结算平台',
  `amount` decimal(20,0) DEFAULT '0' COMMENT '结算金额',
  `userName` varchar(50) DEFAULT '0' COMMENT '用户名',
  `role` varchar(10) DEFAULT '0' COMMENT '用户身份',
  `score` tinyint(3) DEFAULT '0' COMMENT '分成比例',
  `settleTime` bigint(20) DEFAULT '0' COMMENT '结算时间',
  `oderSn` varchar(80) DEFAULT '0' COMMENT '订单号',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `status` tinyint(2) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=178 DEFAULT CHARSET=utf8mb4 COMMENT='结算记录表';

-- ----------------------------
-- Table structure for jh_config
-- ----------------------------
DROP TABLE IF EXISTS `jh_config`;
CREATE TABLE `jh_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ConfigNo` varchar(30) NOT NULL,
  `ConfigName` varchar(60) NOT NULL,
  `ConfigValue` varchar(90) NOT NULL,
  `Remark` varchar(255) DEFAULT NULL,
  `EditBy` varchar(30) DEFAULT NULL,
  `EditTime` varchar(19) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `config_idx1` (`ConfigNo`)
) ENGINE=MyISAM AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for jh_day_goods
-- ----------------------------
DROP TABLE IF EXISTS `jh_day_goods`;
CREATE TABLE `jh_day_goods` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `titile` varchar(80) DEFAULT NULL COMMENT '标题',
  `content` varchar(360) DEFAULT NULL COMMENT '内容',
  `image` varchar(130) DEFAULT NULL COMMENT '头像',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='每日爆款';

-- ----------------------------
-- Table structure for jh_day_image
-- ----------------------------
DROP TABLE IF EXISTS `jh_day_image`;
CREATE TABLE `jh_day_image` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `day` int(20) NOT NULL COMMENT '爆款表id',
  `image` varchar(170) DEFAULT NULL COMMENT '图片地址',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='每日爆款图片';

-- ----------------------------
-- Table structure for jh_friend_dto
-- ----------------------------
DROP TABLE IF EXISTS `jh_friend_dto`;
CREATE TABLE `jh_friend_dto` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `titile` varchar(50) NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(50) NOT NULL DEFAULT '' COMMENT '内容',
  `goodIds` varchar(500) DEFAULT NULL COMMENT 'id列表',
  `image` varchar(50) NOT NULL DEFAULT '' COMMENT '头像',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `createtime` datetime NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COMMENT='sys_friend_dto';

-- ----------------------------
-- Table structure for jh_friend_image
-- ----------------------------
DROP TABLE IF EXISTS `jh_friend_image`;
CREATE TABLE `jh_friend_image` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `day` int(20) NOT NULL COMMENT '朋友圈id',
  `image` varchar(170) DEFAULT NULL COMMENT '图片地址',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈图片';

-- ----------------------------
-- Table structure for jh_gains
-- ----------------------------
DROP TABLE IF EXISTS `jh_gains`;
CREATE TABLE `jh_gains` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL DEFAULT '0' COMMENT '提成用户uid',
  `order_id` varchar(80) NOT NULL COMMENT '订单id',
  `totalmoney` decimal(11,2) NOT NULL COMMENT '佣金金额',
  `withdraw` decimal(11,2) NOT NULL COMMENT '提成',
  `gainleft` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '收益余额',
  `createTime` datetime DEFAULT NULL COMMENT '日期',
  `status` int(5) DEFAULT NULL COMMENT '状态',
  `dev` tinyint(3) DEFAULT '0' COMMENT '平台编号',
  `endmoney` decimal(11,2) DEFAULT '0.00',
  `orderuid` int(11) DEFAULT NULL,
  `endcash` decimal(11,2) DEFAULT '0.00',
  `settle` tinyint(3) DEFAULT '0',
  `state` tinyint(6) DEFAULT '0' COMMENT '分成是否有效',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni` (`uid`,`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='财务分润流水表';

-- ----------------------------
-- Table structure for jh_jd_good
-- ----------------------------
DROP TABLE IF EXISTS `jh_jd_good`;
CREATE TABLE `jh_jd_good` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `pictUrl` varchar(130) DEFAULT '0' COMMENT '商品图片',
  `shopTitle` varchar(155) DEFAULT '0' COMMENT '店铺名',
  `jdurl` varchar(130) DEFAULT '0' COMMENT 'url',
  `title` varchar(170) DEFAULT '0' COMMENT '标题',
  `commissionRate` decimal(10,3) DEFAULT '0.000' COMMENT '佣金比例',
  `coupon` int(8) DEFAULT '0' COMMENT '优惠卷金额',
  `zkFinalPrice` decimal(10,2) DEFAULT '0.00' COMMENT '推客的预估佣金额',
  `volume` int(10) DEFAULT '0' COMMENT '销量',
  `numIid` bigint(20) DEFAULT '0' COMMENT '商品id',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `opt` tinyint(3) DEFAULT '0' COMMENT '类目属性',
  `order_coupon` int(10) DEFAULT '0' COMMENT '优惠卷权重',
  `order_commiss` int(10) DEFAULT '0' COMMENT '佣金权重',
  `order_volume` int(10) DEFAULT '0' COMMENT '销量权重',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `istamll` tinyint(5) DEFAULT NULL,
  `commission` decimal(20,2) DEFAULT NULL,
  `cid` int(11) NOT NULL COMMENT 'cid',
  `link` varchar(255) DEFAULT NULL COMMENT '优惠券地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cc` (`shopTitle`,`commissionRate`,`coupon`) USING BTREE,
  KEY `volume` (`volume`),
  KEY `commissionRate` (`commissionRate`),
  KEY `coupon` (`coupon`)
) ENGINE=InnoDB AUTO_INCREMENT=1778283 DEFAULT CHARSET=utf8mb4 COMMENT='淘宝采集表2';

-- ----------------------------
-- Table structure for jh_oder_scan
-- ----------------------------
DROP TABLE IF EXISTS `jh_oder_scan`;
CREATE TABLE `jh_oder_scan` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `src` tinyint(3) NOT NULL COMMENT '平台编号',
  `devName` varchar(10) DEFAULT '0' COMMENT '平台名称',
  `lastTime` datetime DEFAULT NULL COMMENT '扫描时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=390232 DEFAULT CHARSET=utf8mb4 COMMENT='扫描记录表';

-- ----------------------------
-- Table structure for jh_pay_log
-- ----------------------------
DROP TABLE IF EXISTS `jh_pay_log`;
CREATE TABLE `jh_pay_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `userId` int(20) DEFAULT '0' COMMENT '用户ID',
  `orderSn` varchar(50) DEFAULT '0' COMMENT '平台订单号',
  `accept` tinyint(3) DEFAULT '0' COMMENT '审核状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='付款记录';

-- ----------------------------
-- Table structure for jh_pdd_all
-- ----------------------------
DROP TABLE IF EXISTS `jh_pdd_all`;
CREATE TABLE `jh_pdd_all` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `pictUrl` varchar(130) DEFAULT '0' COMMENT '商品图片',
  `shopTitle` varchar(80) DEFAULT '0' COMMENT '店铺名',
  `title` varchar(150) DEFAULT '0' COMMENT '标题',
  `commissionRate` decimal(10,2) DEFAULT '0.00' COMMENT '佣金比例',
  `commission` decimal(10,2) DEFAULT '0.00' COMMENT '佣金',
  `coupon` decimal(10,2) DEFAULT '0.00' COMMENT '优惠卷金额',
  `zkFinalPrice` decimal(10,2) DEFAULT '0.00' COMMENT '折后价',
  `couponPrice` decimal(10,2) DEFAULT '0.00' COMMENT '券后价',
  `volume` int(10) DEFAULT '0' COMMENT '销量',
  `numIid` bigint(20) DEFAULT '0' COMMENT '商品id',
  `opt` tinyint(3) DEFAULT '0' COMMENT '模块',
  `cat` int(10) DEFAULT '0' COMMENT '类目',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `numIid` (`numIid`) USING BTREE,
  KEY `volume` (`volume`),
  KEY `couponPrice` (`couponPrice`),
  KEY `coupon` (`coupon`)
) ENGINE=InnoDB AUTO_INCREMENT=12977 DEFAULT CHARSET=utf8mb4 COMMENT='拼多多采集表';

-- ----------------------------
-- Table structure for jh_pid_jd
-- ----------------------------
DROP TABLE IF EXISTS `jh_pid_jd`;
CREATE TABLE `jh_pid_jd` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'pid',
  `status` tinyint(2) DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `pill` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=2497 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='京东pid';

-- ----------------------------
-- Table structure for jh_pid_pdd
-- ----------------------------
DROP TABLE IF EXISTS `jh_pid_pdd`;
CREATE TABLE `jh_pid_pdd` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'pid',
  `status` tinyint(2) DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `pii` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=6117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='拼多多pid';

-- ----------------------------
-- Table structure for jh_pid_tb
-- ----------------------------
DROP TABLE IF EXISTS `jh_pid_tb`;
CREATE TABLE `jh_pid_tb` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` bigint(20) DEFAULT NULL COMMENT 'pid',
  `status` tinyint(2) DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `pii` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=10961 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='淘宝pid';

-- ----------------------------
-- Table structure for jh_problem
-- ----------------------------
DROP TABLE IF EXISTS `jh_problem`;
CREATE TABLE `jh_problem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `detail` text COMMENT '详情文章',
  `title` varchar(99) NOT NULL DEFAULT '0' COMMENT '标题',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='常见问题';

-- ----------------------------
-- Table structure for jh_taobao_all
-- ----------------------------
DROP TABLE IF EXISTS `jh_taobao_all`;
CREATE TABLE `jh_taobao_all` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `pictUrl` varchar(130) DEFAULT '0' COMMENT '商品图片',
  `shopTitle` varchar(80) DEFAULT '0' COMMENT '店铺名',
  `title` varchar(150) DEFAULT '0' COMMENT '标题',
  `commissionRate` decimal(10,2) DEFAULT '0.00' COMMENT '佣金比例',
  `commission` decimal(10,2) DEFAULT '0.00' COMMENT '佣金',
  `coupon` decimal(10,2) DEFAULT '0.00' COMMENT '优惠卷金额',
  `zkFinalPrice` decimal(10,2) DEFAULT '0.00' COMMENT '折后价',
  `couponPrice` decimal(10,2) DEFAULT '0.00' COMMENT '券后价',
  `volume` int(10) DEFAULT '0' COMMENT '销量',
  `numIid` bigint(20) DEFAULT '0' COMMENT '商品id',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `opt` tinyint(3) DEFAULT '0' COMMENT '1 上百券 2 聚划算 3 9.9包邮 4生活家居',
  `cat` int(10) DEFAULT '0' COMMENT '淘宝类目',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `istamll` tinyint(5) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `numIid` (`numIid`) USING BTREE,
  KEY `volume` (`volume`),
  KEY `couponPrice` (`couponPrice`),
  KEY `coupon` (`coupon`)
) ENGINE=InnoDB AUTO_INCREMENT=121010 DEFAULT CHARSET=utf8mb4 COMMENT='淘宝采集表';

-- ----------------------------
-- Table structure for jh_taobao_alla
-- ----------------------------
DROP TABLE IF EXISTS `jh_taobao_alla`;
CREATE TABLE `jh_taobao_alla` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `pictUrl` varchar(130) DEFAULT '0' COMMENT '商品图片',
  `shopTitle` varchar(80) DEFAULT '0' COMMENT '店铺名',
  `title` varchar(150) DEFAULT '0' COMMENT '标题',
  `commissionRate` decimal(10,3) DEFAULT '0.000' COMMENT '佣金比例',
  `commission` decimal(10,3) DEFAULT '0.000' COMMENT '佣金',
  `coupon` int(8) DEFAULT '0' COMMENT '优惠卷金额',
  `zkFinalPrice` decimal(10,3) DEFAULT '0.000' COMMENT '折后价',
  `couponPrice` decimal(10,3) DEFAULT '0.000' COMMENT '券后价',
  `volume` int(10) DEFAULT '0' COMMENT '销量',
  `numIid` bigint(20) DEFAULT '0' COMMENT '商品id',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `opt` tinyint(3) DEFAULT '0' COMMENT '1 上百券 2 聚划算 3 9.9包邮 4生活家居',
  `cat` int(10) DEFAULT '0' COMMENT '淘宝类目',
  `order_coupon` int(10) DEFAULT '0' COMMENT '优惠卷权重',
  `order_commiss` int(10) DEFAULT '0' COMMENT '佣金权重',
  `order_volume` int(10) DEFAULT '0' COMMENT '销量权重',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `istamll` tinyint(5) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `numIid` (`numIid`) USING BTREE,
  KEY `volume` (`volume`),
  KEY `couponPrice` (`couponPrice`),
  KEY `coupon` (`coupon`)
) ENGINE=InnoDB AUTO_INCREMENT=58398 DEFAULT CHARSET=utf8mb4 COMMENT='淘宝采集表';

-- ----------------------------
-- Table structure for jh_taobao_good
-- ----------------------------
DROP TABLE IF EXISTS `jh_taobao_good`;
CREATE TABLE `jh_taobao_good` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `pictUrl` varchar(130) DEFAULT '0' COMMENT '商品图片',
  `shopTitle` varchar(80) DEFAULT '0' COMMENT '店铺名',
  `title` varchar(150) DEFAULT '0' COMMENT '标题',
  `commissionRate` decimal(10,3) DEFAULT '0.000' COMMENT '佣金比例',
  `coupon` int(8) DEFAULT '0' COMMENT '优惠卷金额',
  `zkFinalPrice` decimal(10,3) DEFAULT '0.000' COMMENT '推客的预估佣金额',
  `volume` int(10) DEFAULT '0' COMMENT '销量',
  `numIid` bigint(20) DEFAULT '0' COMMENT '商品id',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `opt` tinyint(3) DEFAULT '0' COMMENT '类目属性',
  `order_coupon` int(10) DEFAULT '0' COMMENT '优惠卷权重',
  `order_commiss` int(10) DEFAULT '0' COMMENT '佣金权重',
  `order_volume` int(10) DEFAULT '0' COMMENT '销量权重',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `istamll` tinyint(5) DEFAULT NULL,
  `commission` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `numIid` (`numIid`) USING BTREE,
  KEY `volume` (`volume`),
  KEY `commissionRate` (`commissionRate`),
  KEY `coupon` (`coupon`)
) ENGINE=InnoDB AUTO_INCREMENT=6958 DEFAULT CHARSET=utf8mb4 COMMENT='淘宝采集表2';

-- ----------------------------
-- Table structure for jh_video_tutorial
-- ----------------------------
DROP TABLE IF EXISTS `jh_video_tutorial`;
CREATE TABLE `jh_video_tutorial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `video` varchar(255) NOT NULL DEFAULT '' COMMENT '视频URL地址',
  `title` varchar(255) NOT NULL DEFAULT '0' COMMENT '视频标题',
  `status` tinyint(3) DEFAULT '0' COMMENT '删除状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='视频教程';

-- ----------------------------
-- Table structure for oder
-- ----------------------------
DROP TABLE IF EXISTS `oder`;
CREATE TABLE `oder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `order_sn` varchar(70) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单号',
  `goods_id` bigint(30) DEFAULT NULL COMMENT '商品id',
  `goods_name` varchar(79) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名',
  `goods_thumbnail_url` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品图片url',
  `goods_quantity` int(10) DEFAULT '0' COMMENT '购买商品的数量',
  `goods_price` int(20) DEFAULT '0' COMMENT '订单中sku的单件价格，单位为分',
  `order_amount` int(20) DEFAULT '0' COMMENT '实际支付金额，单位为分',
  `p_id` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '推广位ID',
  `promotion_rate` bigint(20) DEFAULT '0' COMMENT '佣金比例，千分比',
  `promotion_amount` bigint(20) DEFAULT '0' COMMENT '佣金金额，单位为分',
  `order_status` int(5) DEFAULT '-1' COMMENT '订单状态： -1 未支付;0-已支付；1-已成团；2-确认收货；3-审核成功；4-审核失败（不可提现）；5-已经结算；8-非多多进宝商品（无佣金订单）',
  `order_status_desc` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单状态描述',
  `order_create_time` bigint(20) DEFAULT '0' COMMENT '订单生成时间，UNIX时间戳',
  `order_pay_time` bigint(20) DEFAULT '0' COMMENT '支付时间',
  `order_group_success_time` bigint(20) DEFAULT '0' COMMENT '成团时间',
  `order_verify_time` bigint(20) DEFAULT '0' COMMENT '审核时间',
  `order_modify_at` bigint(20) DEFAULT '0' COMMENT '最后更新时间',
  `updateTime` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '0正常;1冻结',
  `settle` tinyint(4) NOT NULL DEFAULT '0' COMMENT '结算状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_sn` (`order_sn`) USING BTREE,
  KEY `p_id` (`p_id`)
) ENGINE=InnoDB AUTO_INCREMENT=214071 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='拼多多订单表';

-- ----------------------------
-- Table structure for score_user
-- ----------------------------
DROP TABLE IF EXISTS `score_user`;
CREATE TABLE `score_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` int(20) NOT NULL COMMENT '用户id',
  `score` int(20) DEFAULT NULL COMMENT '积分数',
  `dataSrc` tinyint(9) DEFAULT '1' COMMENT ' 	来源     1：订单 2:评价 3：订单取消返还 4：拒收返还',
  `dataId` int(20) DEFAULT NULL COMMENT '	来源记录ID	',
  `dataRemarks` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述	',
  `scoreType` tinyint(4) DEFAULT '1' COMMENT '	积分标识  1:收入 2：支出',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '0正常;1冻结',
  `day` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uii` (`userId`,`dataSrc`,`day`),
  KEY `status` (`status`) USING BTREE,
  KEY `userId` (`userId`) USING BTREE,
  KEY `scoreType` (`scoreType`) USING BTREE,
  KEY `dataSrc` (`dataSrc`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT COMMENT='用户积分流水表';

-- ----------------------------
-- Table structure for tboder
-- ----------------------------
DROP TABLE IF EXISTS `tboder`;
CREATE TABLE `tboder` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `adzone_id` bigint(20) DEFAULT '0' COMMENT '推广位Id',
  `adzone_name` varchar(50) DEFAULT '0' COMMENT '推广位名称',
  `alipay_total_price` varchar(50) DEFAULT '0' COMMENT '付款金额',
  `commission_rate` varchar(20) DEFAULT '0' COMMENT '推广者获得的分成比率',
  `income_rate` varchar(20) DEFAULT '0' COMMENT '收入比率',
  `order_type` varchar(10) DEFAULT '0' COMMENT '平台类型',
  `item_title` varchar(50) DEFAULT '0' COMMENT '商品标题',
  `item_num` bigint(20) DEFAULT '0' COMMENT '商品数量',
  `num_iid` bigint(20) DEFAULT '0' COMMENT '商品id',
  `pay_price` varchar(30) DEFAULT '0' COMMENT '实际支付金额',
  `pub_share_pre_fee` decimal(11,2) DEFAULT '0.00' COMMENT '预估佣金',
  `price` varchar(50) DEFAULT '0' COMMENT '原价',
  `tk_status` tinyint(3) DEFAULT '0' COMMENT '淘客订单状态，3：订单结算，12：订单付款， 13：订单失效，14：订单成功',
  `site_id` varchar(30) DEFAULT NULL COMMENT '来源媒体ID',
  `site_name` varchar(30) DEFAULT NULL COMMENT '来源媒体名称',
  `total_commission_rate` varchar(30) DEFAULT NULL COMMENT '佣金比率',
  `trade_id` bigint(20) DEFAULT '0' COMMENT '  淘宝订单号',
  `trade_parent_id` bigint(20) DEFAULT '0' COMMENT '淘宝父订单号',
  `odercreate_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `commission` decimal(10,3) NOT NULL COMMENT '推广者获得的收入金，',
  `settle` tinyint(5) NOT NULL COMMENT '结算状态',
  `relation_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `special_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `click_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `trade_id` (`trade_id`) USING BTREE,
  KEY `order_type` (`order_type`),
  KEY `adzone_id` (`adzone_id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COMMENT='淘宝订单表';

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `userName` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `loginName` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
  `loginPwd` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `loginSecret` bigint(30) DEFAULT '0' COMMENT '安全码',
  `userSex` tinyint(2) DEFAULT '0' COMMENT '性别',
  `userScore` int(11) DEFAULT '0' COMMENT '用户积分',
  `userPhoto` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userStatus` tinyint(2) DEFAULT '0' COMMENT '账号状态',
  `userTotalScore` bigint(20) DEFAULT NULL COMMENT '用户历史消费积分',
  `jdPid` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '京东pid',
  `pddPid` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '拼多多pid',
  `tbPid` bigint(20) DEFAULT '0' COMMENT '淘宝pid',
  `wphPid` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '唯品会pid',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '修改时间',
  `userPhone` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
  `roleId` tinyint(3) DEFAULT '3' COMMENT '身份',
  `score` smallint(5) DEFAULT '0' COMMENT '佣金比率',
  `wxOpenId` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信openid',
  `status` tinyint(2) DEFAULT '0' COMMENT '状态',
  `cash` decimal(15,2) DEFAULT '0.00' COMMENT '用户可提现余额',
  `spid` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '会员id',
  `rid` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '渠道ID',
  `pid` int(11) DEFAULT NULL COMMENT '上级id',
  `tree` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '关系树',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userPhone` (`userPhone`),
  UNIQUE KEY `jdPid` (`jdPid`),
  UNIQUE KEY `pddPid` (`pddPid`),
  UNIQUE KEY `tbPid` (`tbPid`),
  UNIQUE KEY `wxOpenId` (`wxOpenId`)
) ENGINE=InnoDB AUTO_INCREMENT=413 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商城用户表';

-- ----------------------------
-- Table structure for userlog
-- ----------------------------
DROP TABLE IF EXISTS `userlog`;
CREATE TABLE `userlog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` int(20) DEFAULT NULL COMMENT '用户id',
  `operation` tinyint(8) DEFAULT '0' COMMENT '用户操作类型',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `userId` (`userId`)
) ENGINE=MyISAM AUTO_INCREMENT=1786 DEFAULT CHARSET=utf8mb4 COMMENT='用户操作日志';
