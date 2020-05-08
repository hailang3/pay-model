/*
Navicat MySQL Data Transfer

Source Server         : hw开发
Source Server Version : 50636
Source Host           : 47.75.183.12:3306
Source Database       : pay_model

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2020-05-08 16:11:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for config_bank_info
-- ----------------------------
DROP TABLE IF EXISTS `config_bank_info`;
CREATE TABLE `config_bank_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `bank_name` varchar(50) DEFAULT NULL COMMENT '银行名称',
  `bank_code` varchar(50) DEFAULT NULL COMMENT '银行编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config_pay_channel
-- ----------------------------
DROP TABLE IF EXISTS `config_pay_channel`;
CREATE TABLE `config_pay_channel` (
  `id` int(11) NOT NULL COMMENT '渠道id',
  `name` varchar(100) NOT NULL COMMENT '支付渠道名称',
  `extend_properties` text COMMENT '扩展属性配置',
  `status` tinyint(4) DEFAULT '0' COMMENT '渠道状态 {0:关闭，1：开启}',
  `channel_desc` varchar(255) DEFAULT NULL COMMENT '渠道描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config_pay_channel_method
-- ----------------------------
DROP TABLE IF EXISTS `config_pay_channel_method`;
CREATE TABLE `config_pay_channel_method` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `submit_way` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单提交方式{0:浏览器，1:webview}',
  `extend` text COMMENT '额外配置',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态 {0:关闭，1：开启}',
  `pay_channel_id` int(11) NOT NULL COMMENT '支付渠道id',
  `pay_method_id` int(11) NOT NULL COMMENT '支付方式id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pay_cha_met#cid_mid` (`pay_channel_id`,`pay_method_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config_pay_channel_using
-- ----------------------------
DROP TABLE IF EXISTS `config_pay_channel_using`;
CREATE TABLE `config_pay_channel_using` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `which_one` int(11) DEFAULT NULL,
  `pay_channel_id` int(11) DEFAULT NULL COMMENT '支付渠道ID',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pk_tea_pay_cha#uq` (`which_one`,`pay_channel_id`),
  KEY `pk_tea_pay_cha#cid` (`pay_channel_id`),
  CONSTRAINT `pk_tea_pay_cha#cid` FOREIGN KEY (`pay_channel_id`) REFERENCES `config_pay_channel` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config_pay_merchant
-- ----------------------------
DROP TABLE IF EXISTS `config_pay_merchant`;
CREATE TABLE `config_pay_merchant` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_desc` varchar(255) DEFAULT NULL COMMENT '商户描述',
  `merchant_code` varchar(100) NOT NULL COMMENT '商户代号（唯一标识商户）',
  `account` varchar(255) DEFAULT NULL COMMENT '商户号',
  `sign_key` varchar(2000) DEFAULT NULL COMMENT '签名key',
  `api_url` varchar(255) DEFAULT NULL COMMENT '支付渠道接口地址',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '通知（回调）地址',
  `white_ips` varchar(255) DEFAULT NULL COMMENT '通知（回调）ip白名单',
  `extend` varchar(500) DEFAULT NULL COMMENT '额外配置',
  `status` tinyint(255) DEFAULT '0' COMMENT '商户状态 {0：关闭，1：开启}',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `which_one` int(11) NOT NULL COMMENT '属于的团队',
  `pay_channel_id` int(11) NOT NULL COMMENT '支付渠道id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pay_merchant#code` (`merchant_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config_pay_method
-- ----------------------------
DROP TABLE IF EXISTS `config_pay_method`;
CREATE TABLE `config_pay_method` (
  `id` int(11) NOT NULL COMMENT '支付方式id',
  `name` varchar(255) DEFAULT NULL COMMENT '支付方式名称',
  `bol_fixed` tinyint(4) DEFAULT '0' COMMENT '是否是固额， {0: no，1：yes}',
  `status` tinyint(4) DEFAULT '1' COMMENT '支付方式状态，{0：关闭，1：开启}',
  `pay_type_id` int(4) NOT NULL COMMENT '支付类型id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config_pay_method_instance
-- ----------------------------
DROP TABLE IF EXISTS `config_pay_method_instance`;
CREATE TABLE `config_pay_method_instance` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `amounts` varchar(500) DEFAULT NULL COMMENT '推荐金额，用逗号分隔',
  `min_amount` int(11) DEFAULT NULL COMMENT '最小金额',
  `max_amount` bigint(11) DEFAULT NULL COMMENT '最大金额',
  `pay_method_id` int(11) NOT NULL COMMENT '支付方式id',
  `pay_type_id` int(11) NOT NULL COMMENT '支付类型id',
  `status` tinyint(4) DEFAULT NULL COMMENT '支付方法状态 {0:关闭，1:开启}',
  `which_one` int(11) DEFAULT NULL,
  `pay_merchant_id` int(11) NOT NULL COMMENT '支付商户id',
  `pay_channel_id` int(11) NOT NULL COMMENT '支付渠道id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pay_met_ins#mid_merid` (`pay_method_id`,`pay_merchant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config_pay_type
-- ----------------------------
DROP TABLE IF EXISTS `config_pay_type`;
CREATE TABLE `config_pay_type` (
  `id` int(11) NOT NULL COMMENT '支付类型id',
  `name` varchar(100) NOT NULL COMMENT '支付类型名称',
  `sort_num` int(11) DEFAULT NULL COMMENT '排序号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config_pay_view
-- ----------------------------
DROP TABLE IF EXISTS `config_pay_view`;
CREATE TABLE `config_pay_view` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '支付方式名称',
  `pay_type_id` int(4) NOT NULL COMMENT '支付类型id',
  `pay_method_id` int(11) DEFAULT NULL COMMENT '支付方式id',
  `which_one` int(11) DEFAULT NULL,
  `goods_expand` varchar(2000) DEFAULT NULL COMMENT '扩展商品信息',
  `amounts` varchar(500) NOT NULL COMMENT '推荐金额，用逗号分隔',
  `min_amount` int(11) DEFAULT NULL COMMENT '最小金额',
  `max_amount` bigint(11) DEFAULT NULL COMMENT '最大金额',
  `recom_flag` tinyint(4) DEFAULT '0' COMMENT '推荐标志 {0：不推荐，1：推荐}',
  `rebate_rate` int(11) DEFAULT '0' COMMENT '返利比率，单位：千分之几',
  `open_min_limit` int(11) DEFAULT '0' COMMENT '充值方式开放 最低充值金额限制',
  `sort_num` int(11) DEFAULT '9999' COMMENT '排序',
  `status` tinyint(4) DEFAULT '0' COMMENT '支付方式状态，{0：关闭，1：开启}',
  `is_show_bank` int(11) DEFAULT '0' COMMENT '是否显示银行选项0：不显示，1显示',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for config_pay_view_bind
-- ----------------------------
DROP TABLE IF EXISTS `config_pay_view_bind`;
CREATE TABLE `config_pay_view_bind` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `pay_view_id` int(11) NOT NULL COMMENT '充值视窗id',
  `pay_instance_id` int(11) NOT NULL COMMENT '支付方式实例id',
  `submit_way` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单提交方式{0:浏览器，1:webview，2:Sdk}',
  `weight` int(11) NOT NULL DEFAULT '1' COMMENT '>0, 用于计算 在当前支付类型下 存在多个支付方法时被选中的概率',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近的更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for discount_info
-- ----------------------------
DROP TABLE IF EXISTS `discount_info`;
CREATE TABLE `discount_info` (
  `id` int(22) NOT NULL AUTO_INCREMENT,
  `user_id` int(22) NOT NULL,
  `domino_gold_t_001` int(2) DEFAULT '0' COMMENT '打折的状态： 0可以打折，1：不可以打折',
  `domino_gold_t_002` int(2) DEFAULT NULL,
  `domino_gold_t_003` int(2) DEFAULT NULL,
  `domino_gold_t_004` int(2) DEFAULT NULL,
  `domino_gold_t_005` int(2) DEFAULT NULL,
  `domino_gold_t_006` int(2) DEFAULT NULL,
  `domino_gold_t_first` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_google_info
-- ----------------------------
DROP TABLE IF EXISTS `pay_google_info`;
CREATE TABLE `pay_google_info` (
  `channel_id` varchar(20) NOT NULL COMMENT '渠道ID',
  `file_path` varchar(100) NOT NULL COMMENT 'GOOGLE支付文件系统路径',
  `email_address` varchar(100) DEFAULT NULL COMMENT '邮箱地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator` varchar(20) DEFAULT NULL COMMENT '后台操作人',
  `app_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_order_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_order_record`;
CREATE TABLE `pay_order_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id 主键',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单号（唯一）',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(200) DEFAULT NULL COMMENT '用户名',
  `plan_pay` int(18) DEFAULT NULL COMMENT '计划充值金额(单位，分)',
  `actual_pay` int(18) DEFAULT NULL COMMENT 'actual_pay',
  `rebate_rate` int(11) DEFAULT NULL COMMENT '返利比例，(单位：千分之一）',
  `rebate_coin` int(18) DEFAULT NULL COMMENT '返利金额(为0，则根据rebateRate*actualPay计算)',
  `give_coin` int(18) DEFAULT NULL COMMENT '赠送金币',
  `add_coin` int(18) DEFAULT NULL COMMENT '实际添加货币数量（actual_pay+rebate_coin+give_coin）',
  `transaction_no` varchar(100) DEFAULT NULL COMMENT '交易订单号（充值渠道订单号）',
  `status` tinyint(4) DEFAULT NULL COMMENT '订单状态 {0:待付款, -1:付款失败, 1: 付款成功, 2:加金币成功}',
  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `transaction_time` datetime DEFAULT NULL COMMENT '玩家付款时间',
  `notify_time` datetime DEFAULT NULL COMMENT '订单状态通知时间',
  `handle_time` datetime DEFAULT NULL COMMENT '订单处理（加金币）时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `currency` varchar(500) DEFAULT 'IDR' COMMENT '添加价值道具类型人民币RMB,美刀USD,印尼盾IDR，Ip ',
  `order_machine` varchar(255) DEFAULT NULL COMMENT '订单机器码',
  `order_ip` varchar(50) DEFAULT NULL COMMENT '订单ip',
  `channel_id` varchar(100) DEFAULT NULL COMMENT '渠道id',
  `which_one` int(11) DEFAULT NULL COMMENT '团队id',
  `pay_view_id` int(11) DEFAULT NULL COMMENT '支付方式视图id',
  `pay_merchant_id` int(11) DEFAULT NULL COMMENT '支付商户id',
  `pay_channel_id` int(11) DEFAULT NULL COMMENT '支付渠道id',
  `pay_method_id` int(11) DEFAULT NULL COMMENT '支付方式id',
  `pay_type_id` int(11) DEFAULT NULL COMMENT '支付类别id',
  `crossday_flag` tinyint(4) DEFAULT NULL COMMENT '订单是否跨天',
  `order_game_id` int(11) DEFAULT '0' COMMENT '哪个游戏界面下的订单， {0:表示大厅，>0,表示对应游戏id}',
  `goods_id` int(11) DEFAULT '0' COMMENT '商品ID（若不为零位官方支付，谷歌或苹果）',
  `virtual_account` varchar(32) DEFAULT NULL COMMENT 'VA-flashpay专用',
  `repay_address` varchar(512) DEFAULT NULL COMMENT '还款人地址',
  `repay_phone_number` varchar(32) DEFAULT NULL COMMENT '还款人手机号',
  `repay_id_number` varchar(32) DEFAULT NULL COMMENT '还款人身份证号',
  `repay_name` varchar(64) DEFAULT NULL COMMENT '还款人姓名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pay_order#order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=584 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_update_log
-- ----------------------------
DROP TABLE IF EXISTS `pay_update_log`;
CREATE TABLE `pay_update_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '操作人ID',
  `login_name` varchar(255) NOT NULL COMMENT '操作人姓名',
  `opt_type` int(2) NOT NULL COMMENT '操作类型{0:新增,1:修改,2:删除}',
  `table_name` varchar(255) DEFAULT NULL COMMENT '操作的表名',
  `before_val` text COMMENT '操作前数据',
  `after_val` text COMMENT '操作后数据',
  PRIMARY KEY (`id`),
  KEY `pay_update_log_q1` (`login_name`),
  KEY `pay_update_log_q2` (`table_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8;

-- ----------------------------
-- View structure for v_team_pay_method_id_list
-- ----------------------------
DROP VIEW IF EXISTS `v_team_pay_method_id_list`;
CREATE ALGORITHM=UNDEFINED DEFINER=`hw_user`@`%` SQL SECURITY DEFINER VIEW `v_team_pay_method_id_list` AS select `a`.`which_one` AS `which_one`,`a`.`pay_method_id` AS `pay_method_id` from (((`config_pay_method_instance` `a` left join `config_pay_merchant` `b` on((`a`.`pay_merchant_id` = `b`.`id`))) left join `config_pay_channel` `c` on((`a`.`pay_channel_id` = `c`.`id`))) left join `config_pay_channel_method` `d` on(((`a`.`pay_channel_id` = `d`.`pay_channel_id`) and (`a`.`pay_method_id` = `d`.`pay_method_id`)))) where ((`a`.`status` = 1) and (`b`.`status` = 1) and (`c`.`status` = 1) and (`d`.`status` = 1)) group by `a`.`which_one`,`a`.`pay_method_id` ;
