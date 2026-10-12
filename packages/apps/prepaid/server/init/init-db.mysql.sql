CREATE DATABASE IF NOT EXISTS`cube-srv-prepaid` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-srv-prepaid`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ammeter
-- ----------------------------

CREATE TABLE IF NOT EXISTS `ammeter`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `meter_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电表编号',
  `room_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商铺编号',
  `power_remain` decimal(10, 2) NULL DEFAULT NULL COMMENT '剩余电费',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电表型号',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '合并计量余额',
  `owe_money` tinyint(1) NULL DEFAULT NULL COMMENT '是否欠费（true表示欠费）',
  `un_connect` tinyint(1) NULL DEFAULT NULL COMMENT '是否失连（true为失连）',
  `open_or_close` tinyint(1) NULL DEFAULT NULL COMMENT '分合闸状态（true为拉闸，false为合闸）',
  `power_total` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '总用电量',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT 'deleted',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ammeter_meter_id_IDX`(`meter_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ammeter
-- ----------------------------

-- ----------------------------
-- Table structure for collect_records
-- ----------------------------

CREATE TABLE IF NOT EXISTS `collect_records`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `meter_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电表编号',
  `collect_time` datetime NULL DEFAULT NULL COMMENT '采集时间',
  `power_remain` decimal(10, 2) NULL DEFAULT NULL COMMENT '剩余金额(元)',
  `power_tatol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '总用电量',
  `power_higher` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '尖电能',
  `power_High` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '峰电能',
  `power_Flat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平电能',
  `power_Low` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '谷电能',
  `price_higher` decimal(10, 2) NULL DEFAULT NULL COMMENT '尖电价',
  `price_High` decimal(10, 2) NULL DEFAULT NULL COMMENT '峰电价',
  `price_Flat` decimal(10, 2) NULL DEFAULT NULL COMMENT '平电价',
  `price_Low` decimal(10, 2) NULL DEFAULT NULL COMMENT '谷电价',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '删除时间(0未删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `collect_records_meter_id_IDX`(`meter_id`) USING BTREE,
  INDEX `collect_records_collect_time_IDX`(`collect_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '抄表记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect_records
-- ----------------------------

-- ----------------------------
-- Table structure for databasechangelog
-- ----------------------------

CREATE TABLE IF NOT EXISTS `databasechangelog`  (
  `ID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


SELECT COUNT(1) IndexIsThere
FROM INFORMATION_SCHEMA.STATISTICS
WHERE table_schema=DATABASE()
AND   table_name='databasechangelog'
AND   index_name='unique_id_file_name';

SET @IndexIsThere =
(SELECT COUNT(1) IndexIsThere
 FROM INFORMATION_SCHEMA.STATISTICS
 WHERE table_schema=DATABASE()
 AND   table_name='databasechangelog'
 AND   index_name='unique_id_file_name');

SET @sqlstmt =
IF (@IndexIsThere = 0,
    'ALTER TABLE `cube-srv-prepaid`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.prepaid/changelog-v1.0.0.mysql.sql', '2024-06-05 10:18:19', 1, 'EXECUTED', '8:569ea4ada042a0c4e264468ef55b0ade', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553899500');

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------

CREATE TABLE IF NOT EXISTS `databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of databasechangeloglock
-- ----------------------------
INSERT IGNORE INTO `databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for power_recharge
-- ----------------------------

CREATE TABLE IF NOT EXISTS `power_recharge`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `meter_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电表编号',
  `recharge_time` datetime NULL DEFAULT NULL COMMENT '充值时间',
  `sale_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '售电金额',
  `buy_type` int(2) NULL DEFAULT NULL COMMENT '购电类型(现金缴费= 1/刷卡机缴费= 2/微信扫码支付= 3/  支付宝扫码支付= 4/公对公转账= 5/  冲正或退电= 6/微信支付= 7/支付宝支付= 8/ 容量费= 9/充值批量导入= 10/网银转账= 11/  公摊电费= 12/基础电费= 13/在线卡充值= 20/  电费退补= 14)',
  `sale_type` int(2) NULL DEFAULT NULL COMMENT '售电类型(正常售电= 1/冲正= 2/退电= 3 / 初始化= 4/容量费= 5)',
  `last_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '上次累计金额',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '删除时间(0未删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `power_recharge_meter_id_IDX`(`meter_id`) USING BTREE,
  INDEX `power_recharge_recharge_time_IDX`(`recharge_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电表充值记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of power_recharge
-- ----------------------------

-- ----------------------------
-- Table structure for power_use_day
-- ----------------------------

CREATE TABLE IF NOT EXISTS `power_use_day`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `meter_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电表编号',
  `use_month` datetime NULL DEFAULT NULL COMMENT '用能月份',
  `use_date` datetime NULL DEFAULT NULL COMMENT '采集时间',
  `power_remain` decimal(10, 2) NULL DEFAULT NULL COMMENT '剩余金额(元)',
  `pv` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本段时间内的用电量',
  `pvj` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本段时间内的尖电能用电量',
  `pvf` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本段时间内的峰电能用电量',
  `pvp` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本段时间内的平电能用电量',
  `pvg` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本段时间内的谷电能用电量',
  `current_Pv` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前抄表用电量',
  `current_Pvj` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前抄表尖电量',
  `current_Pvf` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前抄表峰电量',
  `current_Pvp` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前抄表平电量',
  `current_Pvg` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前抄表谷电量',
  `last_Pv` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上次抄表用电量',
  `last_Pvj` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上次抄表尖电量',
  `last_Pvf` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上次抄表峰电量',
  `last_Pvp` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上次抄表平电量',
  `last_Pvg` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上次抄表谷电量',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '删除时间(0未删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `power_use_day_meter_id_IDX`(`meter_id`) USING BTREE,
  INDEX `power_use_day_use_date_IDX`(`use_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日用电表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of power_use_day
-- ----------------------------

-- ----------------------------
-- Table structure for pull_data_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `pull_data_record`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `pull_business` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拉取的业务',
  `pull_time` datetime NULL DEFAULT NULL COMMENT '本次拉取时间',
  `pull_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拉取类型(全量,增量.目前只用到增量拉取)',
  `pull_total` bigint(20) NULL DEFAULT NULL COMMENT '本次拉取数据总量',
  `pull_result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本次拉取结果',
  `pull_data_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '拉取数据的主键',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '删除时间(0未删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pull_data_record_pull_time_IDX`(`pull_time`) USING BTREE,
  INDEX `pull_data_record_pull_business_IDX`(`pull_business`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '拉取记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pull_data_record
-- ----------------------------

-- ----------------------------
-- Table structure for water_meter
-- ----------------------------

CREATE TABLE IF NOT EXISTS `water_meter`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `meter_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电表编号',
  `room_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商铺编号',
  `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '剩余水量',
  `total_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '总购水金额',
  `total_power` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '累计购水量',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '水价',
  `owe_status` tinyint(1) NULL DEFAULT NULL COMMENT '是否欠费（true表示欠费）',
  `un_connect` tinyint(1) NULL DEFAULT NULL COMMENT '是否失连（true为失连）',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '删除时间戳,0未删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `water_meter_meter_id_IDX`(`meter_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_meter
-- ----------------------------

-- ----------------------------
-- Table structure for water_recharge
-- ----------------------------

CREATE TABLE IF NOT EXISTS `water_recharge`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `meter_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '水表编号',
  `recharge_time` datetime NULL DEFAULT NULL COMMENT '充值时间',
  `sale_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '售水金额',
  `buy_type` int(2) NULL DEFAULT NULL COMMENT '购水类型(现金缴费= 1/刷卡机缴费= 2/微信扫码支付= 3/  支付宝扫码支付= 4/公对公转账= 5/  冲正或退水= 6/微信支付= 7/支付宝支付= 8/ 容量费= 9/充值批量导入= 10/网银转账= 11/  公摊水费= 12/基础水费= 13/在线卡充值= 20/  水费退补= 14)',
  `sale_type` int(2) NULL DEFAULT NULL COMMENT '售水类型(正常售水= 1/冲正= 2/退水= 3 / 初始化= 4/容量费= 5)',
  `last_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '上次累计金额',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '删除时间(0未删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `water_recharge_meter_id_IDX`(`meter_id`) USING BTREE,
  INDEX `water_recharge_recharge_time_IDX`(`recharge_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '水表充值记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_recharge
-- ----------------------------

-- ----------------------------
-- Table structure for water_use_day
-- ----------------------------

CREATE TABLE IF NOT EXISTS `water_use_day`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `meter_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '水表编号',
  `use_month` datetime NULL DEFAULT NULL COMMENT '用能月份',
  `use_date` datetime NULL DEFAULT NULL COMMENT '采集时间',
  `power_remain` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '剩余水量',
  `pv` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本段时间内的用水量',
  `current_Pv` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前抄表用水量',
  `last_Pv` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上次抄表用水量',
  `deleted` bigint(20) NULL DEFAULT 0 COMMENT '删除时间(0未删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `water_use_day_meter_id_IDX`(`meter_id`) USING BTREE,
  INDEX `water_use_day_use_date_IDX`(`use_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日用水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_use_day
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
