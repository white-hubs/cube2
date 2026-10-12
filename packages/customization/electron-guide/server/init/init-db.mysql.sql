CREATE DATABASE IF NOT EXISTS`cube_srv_gen_electron_guide` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube_srv_gen_electron_guide`;

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
    'ALTER TABLE `cube_srv_gen_electron_guide`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.electron.guide/global-1.0.0.mysql.sql', '2024-04-23 01:49:45', 1, 'EXECUTED', '8:63a08c4353915d53e394595aaf12d4ef', 'sql', '', NULL, '4.3.1', NULL, NULL, '3808185876');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.electron.guide/changelog-1.1.0-1.mysql.sql', '2024-04-23 01:49:45', 2, 'EXECUTED', '8:ca16bc889e0bfc8b3a0696c2e03e4e80', 'sql', '', NULL, '4.3.1', NULL, NULL, '3808185956');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.electron.guide/changelog-1.2.0-1.mysql.sql', '2024-04-23 01:49:46', 3, 'EXECUTED', '8:b545b5c3ba69963aab24f9fc64df21d9', 'sql', '', NULL, '4.3.1', NULL, NULL, '3808186022');

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
-- Table structure for guide_area_manage
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `guide_area_manage`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '名称',
  `coordinate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '坐标',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  `zoom` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL,
  `level` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '状态：启用（enable）、禁用（disable）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of guide_area_manage
-- ----------------------------

-- ----------------------------
-- Table structure for layer_manage
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `layer_manage`  (
  `id` bigint(20) NOT NULL,
  `layer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '图层名称',
  `layer_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '图层附件',
  `state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL COMMENT '使用状态',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  `southwest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '西南角',
  `northeast` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '东北角',
  `center` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '中心点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of layer_manage
-- ----------------------------

-- ----------------------------
-- Table structure for point_manage
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `point_manage`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '点位名称',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域id',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '点位地址',
  `coordinate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '点位坐标',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  `level` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '状态：启用（enable）、禁用（disable）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_german2_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of point_manage
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
