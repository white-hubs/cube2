CREATE DATABASE IF NOT EXISTS`cube-srv-gen-securitymanagement` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-srv-gen-securitymanagement`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attributive_group
-- ----------------------------

CREATE TABLE IF NOT EXISTS `attributive_group`  (
  `id` bigint(32) NULL DEFAULT NULL,
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户号',
  `deleted` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标识',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组名称'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '属性分组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bed_file
-- ----------------------------

CREATE TABLE IF NOT EXISTS `bed_file`  (
  `id` bigint(20) NOT NULL,
  `bed` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '床号',
  `current_visit_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前就诊ID',
  `date_hospitalization` datetime NULL DEFAULT NULL COMMENT '住院日期',
  `discharge_date` datetime NULL DEFAULT NULL COMMENT '出院日期',
  `current_patient` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前病人',
  `bed_management_doctor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管床医生',
  `bed_nurse` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管床护士',
  `patient_gender` int(2) NULL DEFAULT NULL COMMENT '0女1男',
  `attending_physician` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主治医生',
  `pathological_information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '病理信息',
  `medication_information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '用药信息',
  `age` int(2) NULL DEFAULT NULL COMMENT '年龄',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bed_file
-- ----------------------------

-- ----------------------------
-- Table structure for boiler_data
-- ----------------------------

CREATE TABLE IF NOT EXISTS `boiler_data`  (
  `id` bigint(20) NOT NULL,
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id',
  `water_value` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '水量',
  `water_last_value` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '上次水读数',
  `temperature` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '温度',
  `electricity_value` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '电量',
  `electricity_last_value` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '上次电读数',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `collect_time` datetime NULL DEFAULT NULL COMMENT '采集时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of boiler_data
-- ----------------------------

-- ----------------------------
-- Table structure for call_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `call_record`  (
  `id` bigint(20) NOT NULL,
  `call_time` datetime NULL DEFAULT NULL COMMENT '呼叫时间',
  `device_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `device_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '呼叫设备',
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `call_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '呼叫类型',
  `call_duration` int(255) NULL DEFAULT NULL COMMENT '通话时长',
  `record_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录id',
  `bed` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '床位',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of call_record
-- ----------------------------

-- ----------------------------
-- Table structure for clod_water_attribute_map
-- ----------------------------

CREATE TABLE IF NOT EXISTS `clod_water_attribute_map`  (
  `id` bigint(20) NULL DEFAULT NULL,
  `attribute_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性code',
  `translate_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性翻译',
  `tenant` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `value_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '值类型0开关，1数值'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cold_water_attribute
-- ----------------------------

CREATE TABLE IF NOT EXISTS `cold_water_attribute`  (
  `id` bigint(20) NOT NULL,
  `attribute_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cold_water_electricity
-- ----------------------------

CREATE TABLE IF NOT EXISTS `cold_water_electricity`  (
  `id` bigint(20) NOT NULL,
  `quantity_electricity` decimal(10, 2) NULL DEFAULT NULL COMMENT '电量',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id',
  `last_reade` decimal(10, 2) NULL DEFAULT NULL COMMENT '上次读数',
  `collection_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cold_water_electricity
-- ----------------------------

-- ----------------------------
-- Table structure for cold_wholesaler
-- ----------------------------

CREATE TABLE IF NOT EXISTS `cold_wholesaler`  (
  `id` bigint(20) NOT NULL,
  `wholesaler_id` bigint(20) NULL DEFAULT NULL COMMENT '批发商',
  `wholesaler_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批发商姓名',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家编号',
  `shift_master` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批发商档主',
  `shift_master_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主电话',
  `shift_master_card` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主身份证',
  `other_contacts` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其他联系方人',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cold_wholesaler
-- ----------------------------

-- ----------------------------
-- Table structure for collect_configure
-- ----------------------------

CREATE TABLE IF NOT EXISTS `collect_configure`  (
  `id` bigint(20) NOT NULL,
  `time_cycle` int(2) NULL DEFAULT NULL COMMENT '时间周期',
  `reade_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采集时间',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `distribution_task_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配电任务id',
  `consumption_task_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用电任务id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of collect_configure
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;


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
    'ALTER TABLE `cube-srv-gen-securitymanagement`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.0-1.mysql.sql', '2024-04-23 01:27:41', 1, 'EXECUTED', '8:5769fdae6b0b9904a8b82e481ff59a7c', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806861441');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.2-1.mysql.sql', '2024-04-23 01:27:41', 2, 'EXECUTED', '8:1f0d9a927d07e1331f06cd4cc126e689', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806861515');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.5-0.mysql.sql', '2024-04-23 01:27:41', 3, 'EXECUTED', '8:cf36045aaddb856e5cc65a572640e646', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806861577');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.5-1.mysql.sql', '2024-04-23 01:27:42', 4, 'EXECUTED', '8:5c0862feba5d5549451115bc050cb5ff', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862016');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.8-1.mysql.sql', '2024-04-23 01:27:42', 5, 'EXECUTED', '8:48fbee9afe6bb4cef7337309575d623f', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862129');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.11-0.mysql.sql', '2024-04-23 01:27:42', 6, 'EXECUTED', '8:4dc4332ac1bf4e5a183672f07bef5150', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862186');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.13-0.mysql.sql', '2024-04-23 01:27:42', 7, 'EXECUTED', '8:8e9496b3c06b7ac25abaa8731e7d3fa1', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862304');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.14-0.mysql.sql', '2024-04-23 01:27:42', 8, 'EXECUTED', '8:4f0e6cde7ab16196b295a7c7d126c4bc', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862401');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.15-0.mysql.sql', '2024-04-23 01:27:42', 9, 'EXECUTED', '8:08b25a496b6b8f939023fe0cf9a6aa8f', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862459');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.3.28-0.mysql.sql', '2024-04-23 01:27:42', 10, 'EXECUTED', '8:367defa5d8076ad9a44e7c7da039d420', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862543');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.4.0-1.mysql.sql', '2024-04-23 01:27:42', 11, 'EXECUTED', '8:706739a4b2021161d7296f6a688a6689', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862604');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.4.1-1.mysql.sql', '2024-04-23 01:27:42', 12, 'EXECUTED', '8:aa0a3b556bbb24fbe56a4ef6e70e2646', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862685');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.4.1-2.mysql.sql', '2024-04-23 01:27:42', 13, 'EXECUTED', '8:2d9ae23657ffc0edbc6f1fbf88af7f3e', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862755');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.5.1-1.mysql.sql', '2024-04-23 01:27:43', 14, 'EXECUTED', '8:8dc3ffc8dcccf0055b0c353b9a10972e', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806862856');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.securitymanagement/changelog-1.6.1-1.mysql.sql', '2024-04-23 01:27:43', 15, 'EXECUTED', '8:8c42a6f48087108a22b084fe48f7d02d', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806863146');

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------

CREATE TABLE IF NOT EXISTS `databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of databasechangeloglock
-- ----------------------------
INSERT IGNORE INTO `databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for device_collection
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_collection`  (
  `id` bigint(20) NULL DEFAULT NULL COMMENT 'id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备收藏 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_collection
-- ----------------------------

-- ----------------------------
-- Table structure for device_monitor
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_monitor`  (
  `id` bigint(20) NOT NULL,
  `monitor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控信息',
  `monitor_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组态地址',
  `application_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务',
  `tenant` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_monitor
-- ----------------------------

-- ----------------------------
-- Table structure for device_position
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_position`  (
  `id` bigint(20) NOT NULL,
  `cold_water_device` bigint(20) NULL DEFAULT NULL COMMENT '设备',
  `gear_position_id` bigint(20) NULL DEFAULT NULL COMMENT '档位',
  `unit_price` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单价',
  `emplace_temperature` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设定温度',
  `unit_traffic` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷媒体单位流量',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gear_position_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位比编号',
  `gear_position_area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位区域',
  `gear_position_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `gear_position_area_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_position
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_phone_number
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_phone_number`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `record_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录id',
  `data_time` datetime NULL DEFAULT NULL COMMENT '使用时间',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方设备id',
  `file_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件地址',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of emergency_phone_number
-- ----------------------------

-- ----------------------------
-- Table structure for environmental_monitoring
-- ----------------------------

CREATE TABLE IF NOT EXISTS `environmental_monitoring`  (
  `id` bigint(20) NOT NULL,
  `co` int(5) NULL DEFAULT NULL COMMENT 'co浓度',
  `visibility` int(10) NULL DEFAULT NULL COMMENT '能见度',
  `brightness` int(10) NULL DEFAULT NULL COMMENT '亮度',
  `wind_speed` int(10) NULL DEFAULT NULL COMMENT '风速',
  `data_time` datetime NULL DEFAULT NULL COMMENT '数据时间',
  `paragraph` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '段',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `co_time` int(2) NULL DEFAULT NULL COMMENT '持续时间',
  `visibility_time` int(2) NULL DEFAULT NULL COMMENT '持续时间',
  `brightness_time` int(2) NULL DEFAULT NULL COMMENT '持续时间',
  `wind_speed_time` int(2) NULL DEFAULT NULL COMMENT '持续时间',
  `record_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录id',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方设备id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of environmental_monitoring
-- ----------------------------

-- ----------------------------
-- Table structure for gear_position
-- ----------------------------

CREATE TABLE IF NOT EXISTS `gear_position`  (
  `id` bigint(20) NOT NULL,
  `wholesaler` bigint(20) NULL DEFAULT NULL COMMENT '批发商id',
  `gear_position_id` bigint(20) NULL DEFAULT NULL COMMENT '档位id',
  `gear_position_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位名称',
  `gear_position_area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  `gear_position_area_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域id',
  `department_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `billable_time` datetime NULL DEFAULT NULL COMMENT '计费费时间',
  `gear_position_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位code',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gear_position
-- ----------------------------

-- ----------------------------
-- Table structure for group_attributive_details
-- ----------------------------

CREATE TABLE IF NOT EXISTS `group_attributive_details`  (
  `id` bigint(20) NOT NULL,
  `tenant` int(32) NULL DEFAULT NULL COMMENT '租户号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性code',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '最大id',
  `attributive_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组属性详情' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_base_details
-- ----------------------------

CREATE TABLE IF NOT EXISTS `group_base_details`  (
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `id` bigint(20) NOT NULL COMMENT 'id',
  `device_id` bigint(20) NOT NULL COMMENT '回路id',
  `group_control_id` bigint(20) NOT NULL COMMENT '群控组id',
  `area_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `device_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群控详情 群控详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_base_details
-- ----------------------------

-- ----------------------------
-- Table structure for group_base_info
-- ----------------------------

CREATE TABLE IF NOT EXISTS `group_base_info`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `group_control_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '群控组名称 群控组名称',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码 密码',
  `num` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回路数 回路数',
  `ascription` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群控归属 群控归属',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群控基础信息表 群控基础信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_base_info
-- ----------------------------

-- ----------------------------
-- Table structure for group_control_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `group_control_log`  (
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `group_control_id` bigint(20) NULL DEFAULT NULL COMMENT '群控组主键',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '回路id',
  `device_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回路名称',
  `action` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '动作',
  `result` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结果',
  `opt_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '群控日志 群控日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_control_log
-- ----------------------------

-- ----------------------------
-- Table structure for lift_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `lift_record`  (
  `id` bigint(20) NOT NULL,
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `device_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `transport_way` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运输方式',
  `device_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `area_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `run_time` datetime NULL DEFAULT NULL COMMENT '运行时间',
  `floor` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '楼层',
  `area_id` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域id',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lift_record
-- ----------------------------

-- ----------------------------
-- Table structure for medical_gas_data
-- ----------------------------

CREATE TABLE IF NOT EXISTS `medical_gas_data`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '气体名称',
  `gas_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '气体类型',
  `collection_time` datetime NULL DEFAULT NULL COMMENT '时间',
  `data_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '数据采集数据',
  `third_device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方id',
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户号',
  `deleted` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除标识',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of medical_gas_data
-- ----------------------------

-- ----------------------------
-- Table structure for operate_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `operate_log`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  `log_time` datetime NULL DEFAULT NULL COMMENT '时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id',
  `log_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for passenger_flow_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `passenger_flow_record`  (
  `id` bigint(20) NOT NULL,
  `record_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录主键',
  `area_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `record_device` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录产生设备',
  `record_time` datetime NULL DEFAULT NULL COMMENT '记录时间',
  `in_out` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '进出(1进，0出)',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `record_num` int(5) NULL DEFAULT 1 COMMENT '人数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `an_time_inout_index`(`area_name`, `record_time`, `in_out`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客流记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of passenger_flow_record
-- ----------------------------

-- ----------------------------
-- Table structure for play_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `play_record`  (
  `id` bigint(20) NOT NULL,
  `record_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录id',
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端ID',
  `client_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端设备(设备名称)',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容（节目名称）',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `stop_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `total_time` int(20) NULL DEFAULT NULL COMMENT '总时间',
  `total_circle` int(20) NULL DEFAULT NULL COMMENT '次数',
  `area_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域ID',
  `area_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  `tenant` int(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of play_record
-- ----------------------------

-- ----------------------------
-- Table structure for power_distribution_box
-- ----------------------------

CREATE TABLE IF NOT EXISTS `power_distribution_box`  (
  `id` bigint(20) NOT NULL,
  `device_id` bigint(20) NOT NULL COMMENT '设备id',
  `statistical_method` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '统计方式',
  `meter_a` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '电表读数a',
  `meter_b` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '电表读数b',
  `current_quantity` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '电量',
  `voltage_a` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '高压侧电压a',
  `voltage_b` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '高压侧电压d',
  `voltage_c` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '高压侧电压c',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `collect_time` datetime NOT NULL COMMENT '采集时间',
  `box_electric_total` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '配电箱当前总电流',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of power_distribution_box
-- ----------------------------

-- ----------------------------
-- Table structure for processing_records
-- ----------------------------

CREATE TABLE IF NOT EXISTS `processing_records`  (
  `id` bigint(20) NOT NULL,
  `processed_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人',
  `processed_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `processed_result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `call_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of processing_records
-- ----------------------------

-- ----------------------------
-- Table structure for security_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `security_device`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `device_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备管理设备id',
  `device_prod_category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备品类',
  `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备品类code',
  `application_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块应用',
  `area_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域id',
  `device_prod_id` bigint(20) NULL DEFAULT NULL COMMENT '产品id',
  `device_prod_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品',
  `area_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编码',
  `device_status` int(10) NULL DEFAULT NULL COMMENT '设备状态',
  `communication_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '在线离线状态（0离线,1是在线）',
  `third_party_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方设备id',
  `brand_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌code',
  `brand_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `lnt_and_lat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经纬度',
  `device_prod_category_code_concat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类序列号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `deviceid-application-deleted`(`deleted`, `device_id`, `application_code`) USING BTREE COMMENT '设备id应用code删除标识'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基础设备 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of security_device
-- ----------------------------

-- ----------------------------
-- Table structure for security_panel_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `security_panel_config`  (
  `id` bigint(20) NOT NULL,
  `business_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务主键(设备主键或产品等)',
  `scene_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用场景(如安全设备列表,设备详情等)',
  `panel_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '面板配置',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属应用',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备面板配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of security_panel_config
-- ----------------------------

-- ----------------------------
-- Table structure for surgical_demonstration
-- ----------------------------

CREATE TABLE IF NOT EXISTS `surgical_demonstration`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `operating_room` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手术室',
  `chief_surgeon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主刀医生',
  `date_operation` datetime NULL DEFAULT NULL COMMENT '手术日期',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始日期',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束日期',
  `video_duration` int(2) NULL DEFAULT NULL COMMENT '时长',
  `course_classification` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程分类',
  `subject` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '科目',
  `modality` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '形式',
  `playback_count` int(255) NULL DEFAULT NULL COMMENT '播放次数',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of surgical_demonstration
-- ----------------------------

-- ----------------------------
-- Table structure for time_control_details
-- ----------------------------

CREATE TABLE IF NOT EXISTS `time_control_details`  (
  `id` bigint(20) NOT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户',
  `object_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象类型',
  `device_object` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象',
  `area_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `control_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '控制时间点',
  `open_gate` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开关值 开关值',
  `brightness` int(32) NULL DEFAULT NULL COMMENT '光照度',
  `time_control_id` bigint(20) NOT NULL COMMENT '时控计划主键',
  `device_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象id',
  `sort` int(10) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `控制时间点`(`control_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '时控详情表 时控详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of time_control_details
-- ----------------------------

-- ----------------------------
-- Table structure for time_control_info
-- ----------------------------

CREATE TABLE IF NOT EXISTS `time_control_info`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `plan_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计划名称',
  `time_cycle` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '周期',
  `plan_start_time` datetime NULL DEFAULT NULL COMMENT '计划开始时间',
  `plan_end_time` datetime NULL DEFAULT NULL COMMENT '计划结束时间',
  `enabled` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否启用',
  `num` int(10) NULL DEFAULT NULL COMMENT '回路数',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '时控基础信息 时控基础信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of time_control_info
-- ----------------------------

-- ----------------------------
-- Table structure for time_control_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `time_control_log`  (
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除 删除',
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户 租户',
  `id` bigint(20) NOT NULL COMMENT 'id',
  `object_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象类型',
  `device_object` bigint(32) NULL DEFAULT NULL COMMENT '对象',
  `open_gate` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开关',
  `brightness` int(11) NULL DEFAULT NULL COMMENT '光照',
  `result` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行结果',
  `control_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间点 ',
  `time_control_id` bigint(20) NULL DEFAULT NULL COMMENT '时控计划id',
  `object_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象名称',
  `history_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '时控日志 时控日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of time_control_log
-- ----------------------------

-- ----------------------------
-- Table structure for traffic_light
-- ----------------------------

CREATE TABLE IF NOT EXISTS `traffic_light`  (
  `id` bigint(20) NOT NULL,
  `record_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录id',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方id',
  `red` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '红灯',
  `green` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '绿灯',
  `yellow` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '黄灯',
  `data_time` datetime NULL DEFAULT NULL COMMENT '时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of traffic_light
-- ----------------------------

-- ----------------------------
-- Table structure for transformer_distribution
-- ----------------------------

CREATE TABLE IF NOT EXISTS `transformer_distribution`  (
  `id` bigint(20) NOT NULL,
  `transformer` bigint(20) NULL DEFAULT NULL COMMENT '变压器',
  `high_distribution` bigint(20) NULL DEFAULT NULL COMMENT '高压配电',
  `transformer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变压器',
  `high_distribution_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '高压配电',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of transformer_distribution
-- ----------------------------

-- ----------------------------
-- Table structure for transformer_meter
-- ----------------------------

CREATE TABLE IF NOT EXISTS `transformer_meter`  (
  `id` bigint(20) NOT NULL,
  `device_id` bigint(20) NOT NULL COMMENT '变压器设备',
  `statistical_method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '统计方式',
  `meter_a` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '电表读数a',
  `meter_b` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '电表读数b',
  `current_quantity` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '电流量',
  `power_factor` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '功率因素',
  `electric_a` decimal(10, 2) NULL DEFAULT 0.00 COMMENT 'a相电流',
  `temperature_a` decimal(10, 2) NULL DEFAULT 0.00 COMMENT 'a相温度',
  `electric_b` decimal(10, 2) NULL DEFAULT 0.00 COMMENT 'b相电流',
  `temperature_b` decimal(10, 2) NULL DEFAULT 0.00 COMMENT 'b相温度',
  `electric_c` decimal(10, 2) NULL DEFAULT 0.00 COMMENT 'c相电流',
  `temperature_c` decimal(10, 2) NULL DEFAULT 0.00 COMMENT 'c相温度',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `collect_time` datetime NOT NULL COMMENT '采集时间',
  `distribution_device` bigint(20) NOT NULL COMMENT '配电设备',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of transformer_meter
-- ----------------------------

-- ----------------------------
-- Table structure for treated_water_usage_hour
-- ----------------------------

CREATE TABLE IF NOT EXISTS `treated_water_usage_hour`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `date_hour` datetime NULL DEFAULT NULL COMMENT '2023-10-30 10:00:00 每小时的开始时间；也可以是2023-10-30 00:00:00 表示每天的用量；以此类推',
  `water_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '净化水的类型',
  `water_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `water_usage` decimal(8, 2) NULL DEFAULT NULL COMMENT '使用量',
  `usage_unit` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用量单位',
  `device_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备ID',
  `device_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `area_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域ID',
  `area_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  `dept_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of treated_water_usage_hour
-- ----------------------------

-- ----------------------------
-- Table structure for vehicle_flow_monitoring
-- ----------------------------

CREATE TABLE IF NOT EXISTS `vehicle_flow_monitoring`  (
  `id` bigint(20) NOT NULL,
  `record_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录',
  `data_time` datetime NULL DEFAULT NULL COMMENT '时间',
  `direction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方向',
  `lane` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车道',
  `traffic_volume` int(10) NULL DEFAULT NULL COMMENT '车流量',
  `average_speed` int(10) NULL DEFAULT NULL COMMENT '平均车速',
  `time_occupancy_rate` int(10) NULL DEFAULT NULL COMMENT '时间占有率',
  `average_headway` int(10) NULL DEFAULT NULL COMMENT '平均车头时距',
  `average_time_interval` int(10) NULL DEFAULT NULL COMMENT '平均车间时距',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方设备id',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vehicle_flow_monitoring
-- ----------------------------

-- ----------------------------
-- Table structure for water_data_day
-- ----------------------------

CREATE TABLE IF NOT EXISTS `water_data_day`  (
  `id` bigint(20) NOT NULL,
  `statistical_flag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统计标识',
  `shift_master` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主姓名',
  `gear_position_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位号',
  `department_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  `gear_position_area_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `device_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `unit_traffic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '媒体单位流量',
  `duration` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '时长',
  `flow` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '流量',
  `unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `total` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '合计',
  `data_time` datetime NULL DEFAULT NULL COMMENT '数据时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主编号',
  `gear_position_area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_data_day
-- ----------------------------

-- ----------------------------
-- Table structure for water_data_month
-- ----------------------------

CREATE TABLE IF NOT EXISTS `water_data_month`  (
  `id` bigint(20) NOT NULL,
  `statistical_flag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统计标识',
  `statistical_cycle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统计周期',
  `shift_master` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主姓名',
  `gear_position_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位号',
  `department_id` varchar(50) CHARACTER SET utf16 COLLATE utf16_general_ci NULL DEFAULT NULL COMMENT '部门',
  `gear_position_area_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `device_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `unit_traffic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '媒体单位流量',
  `duration` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '时长',
  `flow` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '流量',
  `unit_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '单价',
  `total` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '合计',
  `data_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主编号',
  `gear_position_area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  `shift_master_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主电话',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_data_month
-- ----------------------------

-- ----------------------------
-- Table structure for water_data_year
-- ----------------------------

CREATE TABLE IF NOT EXISTS `water_data_year`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主编号',
  `shift_master` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主姓名',
  `gear_position_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位号',
  `department_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  `gear_position_area_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `device_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `unit_traffic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '媒体单位流量',
  `one` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '一月',
  `two` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '二月',
  `three` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '三月',
  `four` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '四月',
  `five` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '五月',
  `six` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '六月',
  `seven` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '七月',
  `eight` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '八月',
  `nine` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '九月',
  `ten` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '十月',
  `eleven` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '十一月',
  `twelve` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '十二月',
  `total` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '合计',
  `data_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `gear_position_area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  `flow` decimal(10, 2) NULL DEFAULT NULL COMMENT '流量',
  `duration` decimal(10, 2) NULL DEFAULT NULL COMMENT '时长',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_data_year
-- ----------------------------

-- ----------------------------
-- Table structure for water_map_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `water_map_type`  (
  `id` bigint(20) NOT NULL,
  `map_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '映射code',
  `device_prod_category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `device_prod_category_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类code',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of water_map_type
-- ----------------------------

-- ----------------------------
-- Table structure for key_person
-- ----------------------------

CREATE TABLE IF NOT EXISTS `key_person` (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `person_code` varchar(32) NOT NULL COMMENT '人员编号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `phone` varchar(50) NOT NULL COMMENT '手机号原值',
  `id_card` varchar(64) DEFAULT NULL COMMENT '身份证号原值',
  `area_id` bigint(20) UNSIGNED NOT NULL COMMENT 'mdata区域id',
  `area_name` varchar(100) NOT NULL COMMENT '区域名称快照',
  `area_sequence` varchar(500) NOT NULL COMMENT '区域序列号',
  `risk_level` varchar(16) NOT NULL COMMENT '风险等级 HIGH/MEDIUM/LOW',
  `supervisor_user_id` bigint(20) UNSIGNED NOT NULL COMMENT '监管人员用户id',
  `supervisor_name` varchar(50) NOT NULL COMMENT '监管人员姓名快照',
  `supervision_reason` varchar(500) NOT NULL COMMENT '监管原因',
  `status` varchar(16) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态 ACTIVE/INACTIVE',
  `photo_file_id` bigint(20) UNSIGNED DEFAULT NULL COMMENT '人员照片fileId',
  `person_attachment_file_ids` json DEFAULT NULL COMMENT '人员附件fileId数组',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_key_person_code_tenant_deleted` (`person_code`, `tenant`, `deleted`),
  KEY `idx_key_person_keyword_tenant_deleted` (`name`, `phone`, `tenant`, `deleted`),
  KEY `idx_key_person_risk_status_tenant_deleted` (`risk_level`, `status`, `tenant`, `deleted`),
  KEY `idx_key_person_area_seq_tenant_deleted` (`area_sequence`, `tenant`, `deleted`),
  KEY `idx_key_person_supervisor_tenant_deleted` (`supervisor_user_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重点人员';

-- ----------------------------
-- Table structure for key_person_supervision_report
-- ----------------------------

CREATE TABLE IF NOT EXISTS `key_person_supervision_report` (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `person_id` bigint(20) UNSIGNED NOT NULL COMMENT '重点人员id',
  `content` varchar(1000) NOT NULL COMMENT '上报内容',
  `report_attachment_file_ids` json DEFAULT NULL COMMENT '上报附件fileId数组',
  `report_time` datetime NOT NULL COMMENT '上报时间',
  `reporter_user_id` bigint(20) UNSIGNED DEFAULT NULL COMMENT '上报人用户id',
  `reporter_name` varchar(50) DEFAULT NULL COMMENT '上报人姓名',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标识',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_key_person_report_person_time_tenant_deleted` (`person_id`, `report_time`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='重点人员监管上报';

-- ----------------------------
-- Risk control tables
-- ----------------------------

CREATE TABLE IF NOT EXISTS `sm_risk_warning_sign` (
  `id` bigint(20) NOT NULL,
  `sign_code` varchar(64) NOT NULL COMMENT '警示标志编号',
  `sign_name` varchar(100) NOT NULL COMMENT '警示标志名称',
  `sign_type` varchar(64) DEFAULT NULL COMMENT '标志类型',
  `icon_file_id` varchar(100) DEFAULT NULL COMMENT '图标文件ID',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_warning_sign_code_tenant_deleted` (`sign_code`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险警示标志';

CREATE TABLE IF NOT EXISTS `sm_risk_unit_type` (
  `id` bigint(20) NOT NULL,
  `type_code` varchar(64) NOT NULL COMMENT '单元类型编号',
  `type_name` varchar(100) NOT NULL COMMENT '单元类型名称',
  `default_identify_cycle_days` int(11) DEFAULT NULL COMMENT '默认辨识周期天数',
  `identify_dimension_desc` varchar(500) DEFAULT NULL COMMENT '辨识维度说明',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_unit_type_code_tenant_deleted` (`type_code`, `tenant`, `deleted`),
  UNIQUE KEY `uk_sm_risk_unit_type_name_tenant_deleted` (`type_name`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险单元类型';

CREATE TABLE IF NOT EXISTS `sm_risk_assessment_method` (
  `id` bigint(20) NOT NULL,
  `method_code` varchar(64) NOT NULL,
  `method_name` varchar(100) NOT NULL,
  `method_desc` varchar(500) DEFAULT NULL,
  `formula` varchar(200) DEFAULT NULL,
  `preset_flag` tinyint(1) NOT NULL DEFAULT 0,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_assessment_method_code_tenant_deleted` (`method_code`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估方法';

CREATE TABLE IF NOT EXISTS `sm_risk_assessment_dimension` (
  `id` bigint(20) NOT NULL,
  `method_id` bigint(20) NOT NULL,
  `dimension_code` varchar(64) NOT NULL,
  `dimension_name` varchar(100) NOT NULL,
  `score_range` varchar(50) DEFAULT NULL,
  `sort_no` int(11) DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_dimension_method` (`method_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估维度';

CREATE TABLE IF NOT EXISTS `sm_risk_assessment_option` (
  `id` bigint(20) NOT NULL,
  `dimension_id` bigint(20) NOT NULL,
  `option_name` varchar(500) NOT NULL COMMENT '评估选项判断标准',
  `score` decimal(18,4) NOT NULL DEFAULT 0,
  `sort_no` int(11) DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_option_dimension` (`dimension_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估维度选项';

CREATE TABLE IF NOT EXISTS `sm_risk_assessment_level_rule` (
  `id` bigint(20) NOT NULL,
  `method_id` bigint(20) NOT NULL,
  `risk_level` varchar(32) NOT NULL,
  `min_score` decimal(18,4) DEFAULT NULL,
  `max_score` decimal(18,4) DEFAULT NULL,
  `color` varchar(32) DEFAULT NULL,
  `control_requirement` varchar(200) DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_level_rule_method` (`method_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估等级规则';

CREATE TABLE IF NOT EXISTS `sm_risk_area` (
  `id` bigint(20) NOT NULL,
  `area_code` varchar(64) NOT NULL,
  `area_name` varchar(100) NOT NULL,
  `department_id` varchar(100) DEFAULT NULL,
  `range_desc` varchar(500) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL COMMENT '备注',
  `owner_user_id` varchar(100) DEFAULT NULL,
  `area_type` varchar(64) DEFAULT NULL,
  `gis_geometry` longtext DEFAULT NULL,
  `inherent_risk_level` varchar(32) DEFAULT NULL COMMENT '区域固有风险等级，按风险单元固有风险等级取最高值',
  `control_risk_level` varchar(32) DEFAULT NULL COMMENT '区域现状风险等级，按风险单元控制风险等级取最高值',
  `risk_level` varchar(32) DEFAULT NULL COMMENT '区域当前风险等级，按风险单元当前等级取最高值',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_area_code_tenant_deleted` (`area_code`, `tenant`, `deleted`),
  KEY `idx_sm_risk_area_inherent_risk_tenant_deleted` (`inherent_risk_level`, `tenant`, `deleted`),
  KEY `idx_sm_risk_area_control_risk_tenant_deleted` (`control_risk_level`, `tenant`, `deleted`),
  KEY `idx_sm_risk_area_risk_tenant_deleted` (`risk_level`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险区域';

CREATE TABLE IF NOT EXISTS `sm_risk_unit` (
  `id` bigint(20) NOT NULL,
  `unit_code` varchar(64) NOT NULL,
  `unit_name` varchar(100) NOT NULL,
  `area_id` bigint(20) DEFAULT NULL,
  `unit_type_id` bigint(20) DEFAULT NULL,
  `responsible_user_id` varchar(100) DEFAULT NULL,
  `identify_cycle_days` int(11) DEFAULT NULL,
  `identify_status` varchar(32) NOT NULL,
  `current_identify_round_no` varchar(100) DEFAULT NULL,
  `last_identify_submit_time` datetime DEFAULT NULL,
  `last_identify_review_time` datetime DEFAULT NULL,
  `next_identify_time` datetime DEFAULT NULL,
  `location_desc` varchar(300) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `inherent_risk_level` varchar(32) DEFAULT NULL,
  `control_risk_level` varchar(32) DEFAULT NULL,
  `risk_level` varchar(32) DEFAULT NULL COMMENT '单元当前风险等级，按风险对象当前等级取最高值',
  `control_status` varchar(32) DEFAULT NULL,
  `gis_point` longtext DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_unit_code_tenant_deleted` (`unit_code`, `tenant`, `deleted`),
  KEY `idx_sm_risk_unit_risk_tenant_deleted` (`risk_level`, `tenant`, `deleted`),
  KEY `idx_sm_risk_unit_area_status_tenant_deleted` (`area_id`, `identify_status`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险单元';

CREATE TABLE IF NOT EXISTS `sm_risk_unit_sign_rel` (
  `id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `sign_id` bigint(20) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_unit_sign_rel_unit` (`unit_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险单元警示标志关系';

CREATE TABLE IF NOT EXISTS `sm_risk_unit_identifier_rel` (
  `id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_unit_identifier_rel_unit` (`unit_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险单元辨识人员关系';

CREATE TABLE IF NOT EXISTS `sm_risk_object` (
  `id` bigint(20) NOT NULL,
  `object_code` varchar(64) NOT NULL,
  `object_name` varchar(100) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `asset_id` bigint(20) DEFAULT NULL COMMENT '资产管理服务资产ID',
  `identify_round_no` varchar(100) DEFAULT NULL,
  `responsible_user_id` varchar(100) DEFAULT NULL,
  `status` varchar(32) NOT NULL,
  `inherent_risk_level` varchar(32) DEFAULT NULL,
  `control_risk_level` varchar(32) DEFAULT NULL,
  `hazard_desc` varchar(1000) DEFAULT NULL,
  `accident_types` varchar(500) DEFAULT NULL,
  `accident_cause` varchar(1000) DEFAULT NULL,
  `trigger_factor` varchar(1000) DEFAULT NULL,
  `emergency_measure` varchar(1000) DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sm_risk_object_code_tenant_deleted` (`object_code`, `tenant`, `deleted`),
  KEY `idx_sm_risk_object_unit_round_status_tenant_deleted` (`unit_id`, `identify_round_no`, `status`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险对象';

CREATE TABLE IF NOT EXISTS `sm_risk_identification_record` (
  `id` bigint(20) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `identify_round_no` varchar(100) NOT NULL,
  `operation_type` varchar(32) NOT NULL,
  `identifier_user_id` varchar(100) DEFAULT NULL,
  `submit_time` datetime NOT NULL,
  `snapshot_json` longtext DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_identification_record_unit` (`unit_id`, `identify_round_no`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险辨识记录';

CREATE TABLE IF NOT EXISTS `sm_risk_review_record` (
  `id` bigint(20) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  `identify_round_no` varchar(100) NOT NULL,
  `review_mode` varchar(32) NOT NULL,
  `review_batch_no` varchar(100) DEFAULT NULL,
  `review_result` varchar(32) NOT NULL,
  `reject_reason` varchar(500) DEFAULT NULL,
  `review_comment` varchar(500) DEFAULT NULL,
  `reviewer_user_id` varchar(100) DEFAULT NULL,
  `review_time` datetime NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_review_record_object` (`object_id`, `identify_round_no`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险辨识审核记录';

CREATE TABLE IF NOT EXISTS `sm_risk_assessment_record` (
  `id` bigint(20) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  `method_id` bigint(20) NOT NULL,
  `assessment_type` varchar(32) NOT NULL,
  `total_score` decimal(18,4) DEFAULT NULL,
  `risk_level` varchar(32) DEFAULT NULL,
  `method_snapshot_json` longtext DEFAULT NULL,
  `score_detail_json` longtext DEFAULT NULL,
  `submitted` tinyint(1) NOT NULL DEFAULT 1,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_record_object` (`object_id`, `assessment_type`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估记录';

CREATE TABLE IF NOT EXISTS `sm_risk_change_log` (
  `id` bigint(20) NOT NULL,
  `business_type` varchar(64) NOT NULL,
  `business_id` bigint(20) NOT NULL,
  `change_type` varchar(64) NOT NULL,
  `before_json` longtext DEFAULT NULL,
  `after_json` longtext DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_change_log_business` (`business_type`, `business_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险变更日志';

CREATE TABLE IF NOT EXISTS `sm_risk_control_measure` (
  `id` bigint(20) NOT NULL,
  `object_id` bigint(20) NOT NULL,
  `control_type` varchar(64) DEFAULT NULL,
  `control_level` varchar(64) DEFAULT NULL,
  `executor_user_id` varchar(100) DEFAULT NULL,
  `content` varchar(1000) NOT NULL,
  `execution_status` varchar(32) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_control_measure_object` (`object_id`, `execution_status`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险管控措施';

CREATE TABLE IF NOT EXISTS `sm_risk_measure_execution_record` (
  `id` bigint(20) NOT NULL,
  `measure_id` bigint(20) NOT NULL,
  `execution_result` varchar(100) DEFAULT NULL,
  `execution_desc` varchar(1000) DEFAULT NULL,
  `executor_user_id` varchar(100) DEFAULT NULL,
  `execution_time` datetime NOT NULL,
  `gps_location` longtext DEFAULT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_measure_execution_record_measure` (`measure_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险措施执行记录';

CREATE TABLE IF NOT EXISTS `sm_risk_measure_attachment` (
  `id` bigint(20) NOT NULL,
  `execution_record_id` bigint(20) NOT NULL,
  `file_id` varchar(100) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_measure_attachment_record` (`execution_record_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险措施执行附件';

CREATE TABLE IF NOT EXISTS `sm_risk_assessment_plan` (
  `id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  `plan_name` varchar(100) NOT NULL,
  `cycle_type` varchar(32) DEFAULT NULL,
  `next_execute_time` datetime DEFAULT NULL,
  `last_execute_time` datetime DEFAULT NULL,
  `status` varchar(32) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_assessment_plan_unit_status` (`unit_id`, `status`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估计划';

CREATE TABLE IF NOT EXISTS `sm_risk_plan_notify_user` (
  `id` bigint(20) NOT NULL,
  `plan_id` bigint(20) NOT NULL,
  `user_id` varchar(100) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_plan_notify_user_plan` (`plan_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险评估计划通知人员';

CREATE TABLE IF NOT EXISTS `sm_risk_warning_rule` (
  `id` bigint(20) NOT NULL,
  `warning_type` varchar(64) NOT NULL,
  `condition_json` longtext DEFAULT NULL,
  `severity` varchar(32) DEFAULT NULL,
  `notify_channels` varchar(200) DEFAULT NULL,
  `dispose_deadline_days` int(11) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_warning_rule_enabled` (`enabled`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险预警规则';

CREATE TABLE IF NOT EXISTS `sm_risk_warning_record` (
  `id` bigint(20) NOT NULL,
  `rule_id` bigint(20) DEFAULT NULL,
  `unit_id` bigint(20) DEFAULT NULL,
  `object_id` bigint(20) DEFAULT NULL,
  `severity` varchar(32) DEFAULT NULL,
  `warning_status` varchar(32) NOT NULL,
  `trigger_time` datetime DEFAULT NULL,
  `dispose_time` datetime DEFAULT NULL,
  `dispose_desc` varchar(500) DEFAULT NULL COMMENT '处置说明',
  `dispose_result` varchar(50) DEFAULT NULL COMMENT '处置结果',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) NOT NULL DEFAULT 0,
  `create_by` varchar(100) NOT NULL DEFAULT 'system',
  `creator_id` varchar(100) DEFAULT NULL,
  `creator_type` varchar(50) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(100) NOT NULL DEFAULT 'system',
  `updater_id` varchar(100) DEFAULT NULL,
  `updater_type` varchar(50) DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_sm_risk_warning_record_status` (`warning_status`, `unit_id`, `object_id`, `tenant`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='风险预警记录';

SET FOREIGN_KEY_CHECKS = 1;
