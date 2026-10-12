CREATE DATABASE IF NOT EXISTS`cube-biz-emergency` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-biz-emergency`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
    'ALTER TABLE `cube-biz-emergency`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.emergency/test001.mysql.sql', '2024-04-23 01:27:40', 1, 'EXECUTED', '8:a22c41eff120bacd02d7933b4f54d351', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860790');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.emergency/changelog-1.2.2.mysql.sql', '2024-04-23 01:27:40', 2, 'EXECUTED', '8:5d1e6f3d8291b66d241b74a3a22cd4cc', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860900');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.emergency/changelog-1.2.3.mysql.sql', '2024-04-23 01:27:41', 3, 'EXECUTED', '8:0f569473438cbc37b79242e0b11c63fb', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860963');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.emergency/changelog-1.2.4.mysql.sql', '2024-04-23 01:27:41', 4, 'EXECUTED', '8:1e0f644d1b1770af214a2edaeb310119', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806861074');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.emergency/changelog-1.2.7-1.mysql.sql', '2024-04-23 01:27:41', 5, 'EXECUTED', '8:06d75d5d2f41a15426756f6a8624bdd7', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806861282');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.emergency/changelog-1.2.8-1.mysql.sql', '2024-05-27 20:25:53', 6, 'EXECUTED', '8:41965a5a9db6781c308ea16221f73f22', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812752286');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.emergency/changelog-1.2.9-1.mysql.sql', '2024-06-05 10:18:19', 7, 'EXECUTED', '8:20f4e38fb1bbdddd40292ce9d0fc0b2b', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553899741');

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
-- Table structure for emergency_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_device`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `file_names` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备资料名称（冗余：存放第三方的文件名称）',
  `file_paths` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备资料路径（冗余：存放第三方的文件路径）',
  `file_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备资料(文件IDS)',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `device_num` int(11) NULL DEFAULT NULL COMMENT '设备数量',
  `device_used_num` int(11) NULL DEFAULT NULL COMMENT '使用数量',
  `device_loss_num` int(11) NULL DEFAULT NULL COMMENT '损失数量',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急设备' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_device
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_device_genre
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_device_genre`  (
  `id` bigint(20) NOT NULL,
  `emergency_type_id` bigint(20) NULL DEFAULT NULL COMMENT '预案类型主键',
  `device_genre_id` bigint(20) NULL DEFAULT NULL COMMENT '设备类型主键',
  `device_genre_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型名称',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案设备类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_device_genre
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_device_used_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_device_used_record`  (
  `id` bigint(20) NOT NULL,
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '使用人ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用人姓名',
  `used_time` datetime NULL DEFAULT NULL COMMENT '使用时间',
  `used_num` int(11) NULL DEFAULT NULL COMMENT '使用数量',
  `used_type_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用类型编号',
  `used_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用类型名称',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急设备使用记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_device_used_record
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_event_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_event_type`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件分类编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件分类名称',
  `initialized` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '初始化的',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案事件分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for emergency_facility
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_facility`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设施名称',
  `facility_type_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设施类型编号',
  `facility_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设施类型名称',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设施状态 USED 已使用 NOT_USED 未使用',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急设施' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_facility
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_file
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_file`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `level_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '级别编号',
  `expiration_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到期状态',
  `expiration_date` datetime NULL DEFAULT NULL COMMENT '到期日期',
  `emergency_file_type_id` bigint(20) NULL DEFAULT NULL COMMENT '文件类型主键',
  `emergency_event_type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预案事件分类编号',
  `remark` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `document_file_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文档附件',
  `process_file_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理流程附件',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案文件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_file
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_file_notice
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_file_notice`  (
  `id` bigint(20) NOT NULL,
  `scene_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景编号',
  `notice_way` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知通道',
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_scene_code_tenant_deleted`(`scene_code`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案文件通知配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_file_notice
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_file_notice_obj
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_file_notice_obj`  (
  `id` bigint(20) NOT NULL,
  `scene_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息场景编号',
  `emergency_file_type_id` bigint(20) NULL DEFAULT NULL COMMENT '文件类型主键',
  `obj_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送对象类型',
  `obj_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发送对象主键',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案文件消息推送人员配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_file_notice_obj
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_file_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_file_type`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案文件类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_file_type
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_fund_inout_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_fund_inout_record`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记录编号',
  `inout_time` datetime NULL DEFAULT NULL COMMENT '记录时间',
  `inout_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `inout_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '金额',
  `inout_tag` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类标签',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急资金调拨记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_fund_inout_record
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_knowledge
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_knowledge`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '知识名称',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '知识备注',
  `file_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件地址(冗余：存放三方的文件地址)',
  `file_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急知识库：应急技术' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_knowledge
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_organization
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_organization`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级ID',
  `level` int(11) NULL DEFAULT NULL COMMENT '等级',
  `sequence` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急组织' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_organization
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `alarm_level_id` bigint(20) NULL DEFAULT NULL COMMENT '报警级别',
  `emergency_event_type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预案事件分类编号',
  `enabled` tinyint(4) NULL DEFAULT NULL COMMENT '是否启用',
  `repeat_time` int(11) NULL DEFAULT NULL COMMENT '去重周期',
  `repeat_time_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '去重周期单位',
  `estimated_handler_time` int(11) NULL DEFAULT NULL COMMENT '预计处理时间',
  `estimated_handler_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预计处理时间单位',
  `emergency_type_id` bigint(20) NULL DEFAULT NULL COMMENT '预案类型主键',
  `application_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `application_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `logical_op` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '逻辑运算符',
  `rule_period_timed` tinyint(1) NULL DEFAULT NULL COMMENT '规则生效时段是否开启',
  `appoint_device` tinyint(1) NULL DEFAULT NULL COMMENT '是否指定设备',
  `notify_command_dispatch` tinyint(1) NULL DEFAULT NULL COMMENT '是否推送到调度中心',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule_condition
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule_condition`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '预案主键',
  `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号',
  `device_prod_category_code_concat` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号序列号',
  `device_prod_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称',
  `device_prod_id` bigint(20) NULL DEFAULT NULL COMMENT '设备产品主键',
  `device_prod_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称',
  `condition_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条件类型(属性，事件)',
  `device_event_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件主键',
  `device_event_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件编号',
  `device_event_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件名称',
  `device_event_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件类型',
  `logical_op` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '逻辑运算符',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '条件组主键',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案规则条件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule_condition
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule_condition_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule_condition_device`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `emergency_rule_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '预案规则主键',
  `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号',
  `device_prod_category_code_concat` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号序列号',
  `device_prod_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称',
  `device_prod_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备产品主键',
  `device_prod_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称',
  `device_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备主键',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_area_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备所属区域',
  `device_area_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备所属区域名称',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案规则条件设备' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule_condition_device
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule_condition_exp
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule_condition_exp`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '预案主键',
  `emergency_rule_condition_id` bigint(20) NULL DEFAULT NULL COMMENT '预案规则条件主键',
  `prop_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性编号',
  `prop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性名称',
  `prop_unit` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性单位',
  `op` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性操作符',
  `oprand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性值',
  `start_delay_time` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '延迟开始时间',
  `delay_time` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '延迟时间',
  `end_delay_time` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '延迟结束时间',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '条件组主键',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案规则条件表达式' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule_condition_exp
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule_condition_group
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule_condition_group`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规则条件组' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule_condition_group
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule_time
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule_time`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `emergency_rule_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '预案规则主键',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '条件组主键',
  `time_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时段类型',
  `time_conditions` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间条件',
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案规则时段配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule_time
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule_time_job
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule_time_job`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规则id',
  `emergency_rule_condition_group_id` bigint(20) NULL DEFAULT NULL COMMENT '条件组id',
  `emergency_rule_time_id` bigint(20) NULL DEFAULT NULL COMMENT '时间配置主键',
  `time_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间周期类型',
  `time_conditions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '单次时间条件',
  `rule_time_conditions` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '时间条件',
  `operate_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `trigger_last_time` bigint(13) NULL DEFAULT NULL COMMENT '上次执行时间',
  `trigger_next_time` bigint(13) NULL DEFAULT NULL COMMENT '下次执行时间',
  `tenant` int(10) NULL DEFAULT NULL,
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule_time_job
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule_time_job_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule_time_job_log`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `emergency_rule_time_job_id` bigint(20) NULL DEFAULT NULL,
  `open_or_close` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开启规则or关闭规则',
  `execute_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行状态',
  `trigger_time` datetime NULL DEFAULT NULL COMMENT '触发时间',
  `handler_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `error_msg` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `tenant` int(10) NULL DEFAULT NULL,
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案时间任务执行记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule_time_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule_writer_event
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule_writer_event`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规则主键',
  `emergency_rule_time_id` bigint(20) NULL DEFAULT NULL COMMENT '规则时段主键',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备主键',
  `device_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `event_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件编号',
  `threshold_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件参数名',
  `threshold_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件参数值',
  `write_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '写值状态',
  `start_time` datetime NULL DEFAULT NULL COMMENT '写值有效开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '写值有效结束时间',
  `retry_count` int(2) NULL DEFAULT NULL COMMENT '重试次数',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '事件写值日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule_writer_event
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_rule_writer_event_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_rule_writer_event_log`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规则主键',
  `emergency_rule_time_id` bigint(20) NULL DEFAULT NULL COMMENT '规则时段主键',
  `emergency_rule_writer_event_id` bigint(20) NULL DEFAULT NULL COMMENT '事件写值记录主键',
  `iot_write_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联写入状态',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '事件写值日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_rule_writer_event_log
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_supplies
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_supplies`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物资名称',
  `type` bigint(20) NULL DEFAULT NULL COMMENT '物资类型',
  `store_num` decimal(8, 2) NULL DEFAULT NULL COMMENT '库存数量',
  `loss_num` decimal(8, 2) NULL DEFAULT NULL COMMENT '报损数量',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急物资' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_supplies
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_supplies_inout_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_supplies_inout_record`  (
  `id` bigint(20) NOT NULL,
  `supplies_id` bigint(20) NULL DEFAULT NULL COMMENT '应急物资ID',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出入库记录号',
  `inout_time` datetime NULL DEFAULT NULL COMMENT '出入库时间',
  `inout_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出入库类型',
  `inout_tag` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出入库标签',
  `inout_num` decimal(8, 2) NULL DEFAULT NULL COMMENT '数量',
  `inout_unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急物资出入库记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_supplies_inout_record
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_supplies_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_supplies_type`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急物资类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_supplies_type
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_type`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预案类型名称',
  `application_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_application_code_tenant_deleted`(`application_code`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_type
-- ----------------------------
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1, 'SECURITY_MONITORING', '安防监控', 'SECURITY_MONITORING', '安防监控', 1, 0, '1', '2022-11-04 09:29:09', '1', '2022-11-04 09:29:12') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (2, 'ELECTRICMANAGE', '电气管理', 'ELECTRICMANAGE', '电气管理', 1, 0, '1', '2023-01-04 14:26:03', '1', '2023-01-04 14:26:05') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (3, 'ROTATINGMANAGE', '动环报警', 'ROTATINGMANAGE', '动环报警', 1, 0, '1', '2023-01-04 14:26:36', '1', '2023-01-04 14:26:38') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (4, 'AIRCONDITIONER', '空调管理', 'AIRCONDITIONER', '空调管理', 1, 0, '1', '2023-01-04 14:28:07', '1', '2023-01-04 14:28:09') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (5, 'FIRECONTROL', '消防管理', 'FIRECONTROL', '消防管理', 1, 0, '1', '2023-01-04 14:29:05', '1', '2023-01-04 14:29:07') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (6, 'WATERSUPPLYDRAINAGE', '给排水', 'WATERSUPPLYDRAINAGE', '给排水', 1, 0, '1', '2023-01-04 14:30:49', '1', '2023-01-04 14:30:51') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (7, 'AIRSUPPLYANDEXHAUST', '送排风', 'AIRSUPPLYANDEXHAUST', '送排风', 1, 0, '1', '2023-01-04 14:31:11', '1', '2023-01-04 14:31:13') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367820, 'INFORMATION_PUBLISH', '信息发布', 'INFORMATION_PUBLISH', '信息发布', NULL, 0, '1', '2023-06-29 18:09:22', '1', '2023-06-29 18:09:25') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367821, 'MEDICAL_INTERCOM', '医护对讲', 'MEDICAL_INTERCOM', '医护对讲', NULL, 0, '1', '2023-06-29 18:09:55', '1', '2023-06-29 18:09:57') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367822, 'LIFT_MANAGEMENT', '电梯管理', 'LIFT_MANAGEMENT', '电梯管理', NULL, 0, '1', '2023-06-29 18:10:26', '1', '2023-06-29 18:10:28') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367823, 'INTRUDER_ALARM', '入侵报警', 'INTRUDER_ALARM', '入侵报警', NULL, 0, '1', '2023-06-29 18:10:53', '1', '2023-06-29 18:10:56') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367824, 'MEDICAL_GAS', '医用气体', 'MEDICAL_GAS', '医用气体', NULL, 0, '1', '2023-06-29 18:11:25', '1', '2023-06-29 18:11:27') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367825, 'TRIAGE_RANK', '分诊排号', 'TRIAGE_RANK', '分诊排号', NULL, 0, '1', '2023-06-29 18:11:47', '1', '2023-06-29 18:11:50') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367826, 'SURGICAL_INSTRUCTION', '手术示教', 'SURGICAL_INSTRUCTION', '手术示教', NULL, 0, '1', '2023-06-29 18:12:10', '1', '2023-06-29 18:12:12') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367827, 'ICU_MONITORING', 'ICU监测', 'ICU_MONITORING', 'ICU监测', NULL, 0, '1', '2023-06-29 18:12:43', '1', '2023-06-29 18:12:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367828, 'MEDICAL_CRYOEXTRACTO', '医用冷冻冷藏', 'MEDICAL_CRYOEXTRACTO', '医用冷冻冷藏', NULL, 0, '1', '2023-06-29 18:13:05', '1', '2023-06-29 18:13:07') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367829, 'MEAT_TRADE', '肉类交易', 'MEAT_TRADE', '肉类交易', NULL, 0, '1', '2023-08-09 11:27:42', '1', '2023-08-09 11:27:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367830, 'GAS_MANAGEMENT', '用气管理', 'GAS_MANAGEMENT', '用气管理', NULL, 0, '1', '2023-09-22 15:33:37', '1', '2023-09-22 15:33:40') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367831, 'COOLINGWATER', '冷冻水', 'COOLINGWATER', '冷冻水', NULL, 0, '1', '2023-09-22 15:33:37', '1', '2023-09-22 15:33:40') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367832, 'COLDANDHEATSOURCES', '冷热源', 'COLDANDHEATSOURCES', '冷热源', NULL, 0, '1', '2023-09-22 15:33:37', '1', '2023-09-22 15:33:40') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367833, 'PERIMETER_PREVENTION', '周界防范', 'PERIMETER_PREVENTION', '周界防范', NULL, 0, '1', '2023-09-22 15:33:37', '1', '2023-09-22 15:33:40') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367834, 'BOLIER', '锅炉系统', 'BOLIER', '锅炉系统', NULL, 0, '1', '2023-09-22 15:33:37', '1', '2023-09-22 15:33:40') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367835, 'WATERPURIFICATION', '净水系统', 'WATERPURIFICATION', '净水系统', NULL, 0, '1', '2023-09-22 15:33:37', '1', '2023-09-22 15:33:40') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1546385459813367836, 'LOGISTICS', '物流管理', 'LOGISTICS', '物流管理', NULL, 0, '1', '2023-09-22 15:33:37', '1', '2023-09-22 15:33:40') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1729040405851611138, 'FRESH_AIR_MANAGEMENT', '新风管理', 'FRESH_AIR_MANAGEMENT', '新风管理', NULL, 0, '1', '2023-12-11 18:43:00', 'admin', '2023-11-27 15:32:13') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1729040873336152066, 'SECURITY_CHECKP', '安检通道', 'SECURITY_CHECKP', '安检通道', NULL, 0, '1', '2023-12-11 18:42:57', 'admin', '2023-11-27 15:34:04') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1729041291374043138, 'PASSENGER_FLOW_ANAL', '客流分析', 'PASSENGER_FLOW_ANAL', '客流分析', NULL, 0, '1', '2023-12-06 15:12:53', '1', '2023-11-27 15:35:43') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1734113900646178817, 'CLEANCONDITIONING', '洁净空调', 'CLEANCONDITIONING', '洁净空调', NULL, 0, '1', '2023-12-11 15:42:05', '1', '2023-12-11 15:32:27') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1734113900646178818, 'SEWAGE_TREATMENT', '污水处理', 'SEWAGE_TREATMENT', '污水处理', NULL, 0, '1', '2023-12-11 15:42:05', '1', '2023-12-11 15:32:27') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1734113900646178819, 'MEDICAL-REFRIGERATIO', '医用冷藏', 'MEDICAL-REFRIGERATIO', '医用冷藏', NULL, 0, '1', '2023-12-11 15:42:05', '1', '2023-12-11 15:32:27') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1734113900646178820, 'GARDENS', '园林系统', 'GARDENS', '园林系统', NULL, 0, '1', '2023-12-11 15:42:05', '1', '2023-12-11 15:32:27') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1734113900646178821, 'EMERGENCY_RESOURCES', '应急资源', 'EMERGENCY_RESOURCES', '应急资源', NULL, 0, '1', '2023-12-11 15:42:05', '1', '2023-12-11 15:32:27') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1740183143212982274, 'EMERGENCY_CALL', '紧急电话', 'EMERGENCY_CALL', '紧急电话', NULL, 0, '1', '2023-12-28 18:52:47', '1', '2023-12-28 09:29:27') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1740186048959684610, 'ENVIRONMENT_MONITORI', '环境监控', 'ENVIRONMENT_MONITORI', '环境监控', NULL, 0, '1', '2023-12-28 18:53:09', 'admin', '2023-12-28 09:41:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1740190953762004994, 'TRAFFIC_LIGHT_SYS', '交通灯系统', 'TRAFFIC_LIGHT_SYS', '交通灯系统', NULL, 0, '1', '2023-12-28 18:53:25', 'admin', '2023-12-28 10:00:29') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1740193326190370817, 'TRAFFIC_CONTROL', '交通控制', 'TRAFFIC_CONTROL', '交通控制', NULL, 0, '1', '2023-12-28 18:53:44', 'admin', '2023-12-28 10:09:55') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1740195150947491842, 'TRAFFIC_FLOW_MONIRIN', '车流量监测', 'TRAFFIC_FLOW_MONIRIN', '车流量监测', NULL, 0, '1', '2023-12-28 18:53:59', 'admin', '2023-12-28 10:17:10') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1740195150947491843, 'OPERATING_ROOM', '洁净手术室', 'OPERATING_ROOM', '洁净手术室', NULL, 0, '1', '2023-12-28 18:53:59', 'admin', '2023-12-28 10:17:10') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1740195150947491844, 'ENERGY_CONSUMPTION', '能碳管理', 'ENERGY_CONSUMPTION', '能碳管理', NULL, 0, '1', '2023-12-28 18:53:59', 'admin', '2023-12-28 10:17:10') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304899, 'ULTRA_FINE_DRY_POWDE', '超细干粉', 'ULTRA_FINE_DRY_POWDE', '超细干粉', NULL, 0, 'client_2', '2024-07-01 11:26:50', 'client_2', '2024-07-01 11:26:50') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304900, 'VISITOR_MANAGEMENT', '访客管理', 'VISITOR_MANAGEMENT', '访客管理', NULL, 0, 'client_2', '2024-07-01 11:26:50', 'client_2', '2024-07-30 22:55:24') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304901, 'BOOKS_MANAGEMENT', '图书管理', 'BOOKS_MANAGEMENT', '图书管理', NULL, 0, 'client_2', '2024-07-01 11:26:50', 'client_2', '2024-07-30 22:55:24') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304902, 'VIDEO_INTERCOM', '可视对讲', 'VIDEO_INTERCOM', '可视对讲', NULL, 0, 'client_2', '2024-07-01 11:26:50', 'client_2', '2024-07-30 22:55:24') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304903, 'ELECTRlC_VEHICLE', '电动车管理', 'ELECTRlC_VEHICLE', '电动车管理', NULL, 0, 'client_2', '2025-03-18 20:29:12', 'client_2', '2025-03-18 20:29:18') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304904, 'PARKINGMANAGE', '停车管理', 'PARKINGMANAGE', '停车管理', NULL, 0, 'client_2', '2025-03-18 20:29:12', 'client_2', '2025-03-18 20:29:18') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304905, 'BROADCAST_CENTER', '广播中心', 'BROADCAST_CENTER', '广播中心', NULL, 0, 'client_2', '2025-03-18 20:29:12', 'client_2', '2025-03-18 20:29:18') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304906, 'SOLAR_ENERGY', '太阳能热水管理', 'SOLAR_ENERGY', '太阳能热水管理', NULL, 0, 'client_2', '2025-03-18 20:29:12', 'client_2', '2025-03-18 20:29:18') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304907, 'LEAKAGE_DETECTION', '漏水检测', 'LEAKAGE_DETECTION', '漏水检测', NULL, 0, 'client_2', '2025-03-18 20:29:12', 'client_2', '2025-05-22 17:13:48') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304908, 'DOORMANAGE', '门禁管理', 'DOORMANAGE', '门禁管理', NULL, 0, 'client_2', '2025-03-18 20:29:12', 'client_2', '2025-05-22 17:13:48') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304909, 'CUSTOMER_CONTROL', '客控管理', 'CUSTOMER_CONTROL', '客控管理', NULL, 0, 'client_2', '2025-03-18 20:29:12', 'client_2', '2025-10-15 15:31:35')  ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304910, 'IPTV_MANAGEMENT', 'IPTV管理', 'IPTV_MANAGEMENT', 'IPTV管理', null, 0, 'client_2', '2025-03-18 20:29:12', 'client_2', '2025-05-22 17:13:48') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304911, 'TRAFFIC_DETECTION', '人流量检测', 'TRAFFIC_DETECTION', '人流量检测', NULL, 0, 'client_2', '2025-12-25 20:29:12', 'client_2', '2025-12-25 17:13:48') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304912, 'BUILDING_SUBSIDENCE', '建筑沉降', 'BUILDING_SUBSIDENCE', '建筑沉降', NULL, 0, 'client_2', '2025-12-25 20:29:12', 'client_2', '2025-12-25 17:13:48') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304913, 'GARBAGE_BIN_MANAGEME', '垃圾桶管理', 'GARBAGE_BIN_MANAGEME', '垃圾桶管理', NULL, 0, 'client_2', '2025-12-25 20:29:12', 'client_2', '2025-12-25 17:13:48') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `emergency_type`
(id, code, name, application_code, application_name, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1807616839044304914, 'KEY_PERSONNEL', '重点人员管理', 'KEY_PERSONNEL', '重点人员管理', NULL, 0, 'admin', '2026-05-27 10:00:00', 'admin', '2026-05-27 10:00:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for emergency_user
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_user`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `sex` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `category_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员类型编号',
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员类型名称',
  `from_org` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源组织(手动填写的组织)',
  `skill_desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业特长描述',
  `emg_org_id` bigint(20) NULL DEFAULT NULL COMMENT '所属组织',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急人员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_user
-- ----------------------------

-- ----------------------------
-- Table structure for emergency_user_category
-- ----------------------------

CREATE TABLE IF NOT EXISTS `emergency_user_category`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级ID',
  `level` int(11) NULL DEFAULT NULL COMMENT '等级',
  `sequence` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应急人员类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emergency_user_category
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
