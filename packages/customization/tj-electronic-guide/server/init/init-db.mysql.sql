CREATE DATABASE IF NOT EXISTS `cube_srv_gen_tj_electronic_guide` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube_srv_gen_tj_electronic_guide`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for databasechangelog
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `databasechangelog`  (
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
    'ALTER TABLE `cube_srv_gen_tj_electronic_guide`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'db/com.guhecloud.cube.srv.tj.electronic.guide/global-1.0.0.mysql.sql', '2025-10-20 17:13:16', 1, 'EXECUTED', '9:2bef5b4b38addf102114cdc3e0b0e0f5', 'sql', '', NULL, '4.32.0', NULL, NULL, '0951595268');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'db/com.guhecloud.cube.srv.tj.electronic.guide/changelog-1.1.0-1.mysql.sql', '2025-10-20 17:13:18', 2, 'EXECUTED', '9:3fef8800feaa45322813ceea326521b1', 'sql', '', NULL, '4.32.0', NULL, NULL, '0951595268');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'db/com.guhecloud.cube.srv.tj.electronic.guide/changelog-1.2.0-1.mysql.sql', '2025-10-20 17:13:18', 3, 'EXECUTED', '9:5b2218d6a1039238b5405d07446d0387', 'sql', '', NULL, '4.32.0', NULL, NULL, '0951595268');

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `databasechangeloglock`  (
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
-- Table structure for tj_guide_area_manage
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tj_guide_area_manage` (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '名称',
  `coordinate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '坐标',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int DEFAULT NULL,
  `zoom` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL,
  `level` int DEFAULT NULL COMMENT '顺序',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '状态：启用（enable）、禁用（disable）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;

-- ----------------------------
-- Records of tj_guide_area_manage
-- ----------------------------

-- ----------------------------
-- Table structure for tj_layer_manage
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tj_layer_manage` (
  `id` bigint NOT NULL,
  `layer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '图层名称',
  `layer_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '图层附件',
  `state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '使用状态',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int DEFAULT NULL,
  `southwest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '西南角',
  `northeast` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '东北角',
  `center` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '中心点',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;

-- ----------------------------
-- Records of tj_layer_manage
-- ----------------------------

-- ----------------------------
-- Table structure for tj_point_manage
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tj_point_manage` (
  `id` bigint NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '点位名称',
  `area_id` bigint DEFAULT NULL COMMENT '区域id',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '点位地址',
  `coordinate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '点位坐标',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '备注',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int DEFAULT NULL,
  `level` int DEFAULT NULL COMMENT '顺序',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci DEFAULT NULL COMMENT '状态：启用（enable）、禁用（disable）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci;

-- ----------------------------
-- Records of tj_point_manage
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
