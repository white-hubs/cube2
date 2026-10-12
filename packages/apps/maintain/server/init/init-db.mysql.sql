CREATE DATABASE IF NOT EXISTS`cube-srv-gen-maintain` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-srv-gen-maintain`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for assembly_notice
-- ----------------------------

CREATE TABLE IF NOT EXISTS `assembly_notice`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `msg_id` bigint(20) NULL DEFAULT NULL COMMENT '消息id',
  `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息配置json',
  `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知消息配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of assembly_notice
-- ----------------------------

-- ----------------------------
-- Table structure for assembly_notice_condition
-- ----------------------------

CREATE TABLE IF NOT EXISTS `assembly_notice_condition`  (
  `id` bigint(20) NOT NULL,
  `notice_id` bigint(20) NULL DEFAULT NULL COMMENT '通知id(assembly_notice表id)',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知条件类型',
  `value` int(11) NULL DEFAULT NULL COMMENT '通知时间',
  `msg_id` bigint(20) NULL DEFAULT NULL COMMENT '消息id',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `module_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块类型',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知条件配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of assembly_notice_condition
-- ----------------------------

-- ----------------------------
-- Table structure for assembly_notice_user
-- ----------------------------

CREATE TABLE IF NOT EXISTS `assembly_notice_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `notice_id` bigint(20) NOT NULL COMMENT '消息配置id',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `type_ids` bigint(20) NOT NULL COMMENT '类型id',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知人员类型',
  `condition_id` bigint(20) NULL DEFAULT NULL COMMENT '条件id',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知人员配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of assembly_notice_user
-- ----------------------------

-- ----------------------------
-- Table structure for basic_object_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `basic_object_type`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_frozen_product_area` bit(1) NULL DEFAULT NULL COMMENT '是否是冻品区（1是0不是）',
  `object_type_category` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型分类：asset资产 gear挡位 point现场点',
  `object_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `object_type_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型编码',
  `manage_dept_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理部门',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基础对象类型包括：资产类型、挡位类型、现场点类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of basic_object_type
-- ----------------------------

-- ----------------------------
-- Table structure for basic_scene_point
-- ----------------------------

CREATE TABLE IF NOT EXISTS `basic_scene_point`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `scene_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现场点名称',
  `scene_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现场点编码',
  `scene_type_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现场点类型',
  `manage_dept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现场点管理部门',
  `area_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现场点区域',
  `addr_detail` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `scene_x` float NULL DEFAULT NULL COMMENT '现场点坐标X',
  `scene_y` float NULL DEFAULT NULL COMMENT '现场点坐标Y',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `scene_type_id_index`(`scene_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '现场点：目前用于巡检' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of basic_scene_point
-- ----------------------------

-- ----------------------------
-- Table structure for cnp_inspect_item
-- ----------------------------

CREATE TABLE IF NOT EXISTS `cnp_inspect_item`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `inspect_standard_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检标准ID',
  `cnp_inspect_item_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检项名称',
  `parent_item_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级ID',
  `cnp_inspect_item_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果',
  `inspect_result_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果',
  `image_flag` bit(1) NULL DEFAULT NULL COMMENT '是否强制拍照',
  `fun_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能连接编码',
  `text_flag` bit(1) NULL DEFAULT NULL COMMENT '是否强制文字描述',
  INDEX `id_index`(`id`) USING BTREE,
  INDEX `inspect_standard_id_index`(`inspect_standard_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cnp_inspect_item
-- ----------------------------

-- ----------------------------
-- Table structure for cnp_inspect_result
-- ----------------------------

CREATE TABLE IF NOT EXISTS `cnp_inspect_result`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `result_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果名称',
  `item_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项名称',
  `item_value` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项值',
  `error_flag` bit(1) NULL DEFAULT NULL COMMENT '是否异常点',
  `danger_flag` bit(1) NULL DEFAULT NULL COMMENT '是否转隐患',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NULL DEFAULT NULL,
  INDEX `id_index`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cnp_inspect_result
-- ----------------------------

-- ----------------------------
-- Table structure for cnp_inspect_standard
-- ----------------------------

CREATE TABLE IF NOT EXISTS `cnp_inspect_standard`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `standard_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准名称',
  `standard_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准编码',
  `standard_type_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准类型',
  `standard_category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准分类： 1 实体 2 商铺',
  `inspect_category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检类型ID',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `standard_type_id_index`(`standard_type_id`) USING BTREE,
  INDEX `standard_category_index`(`standard_category`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cnp_inspect_standard
-- ----------------------------

-- ----------------------------
-- Table structure for cnp_inspect_standard_type_refer
-- ----------------------------

CREATE TABLE IF NOT EXISTS `cnp_inspect_standard_type_refer`  (
  `inspect_standard_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检标准ID',
  `refer_type_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联资产类型',
  `refer_type_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联资产类型ID',
  `default_standard` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认巡检标准（1.是；0.否）',
  INDEX `inspect_standard_id_index`(`inspect_standard_id`) USING BTREE,
  INDEX `refer_type_id_index`(`refer_type_id`) USING BTREE,
  INDEX `refer_type_code_index`(`refer_type_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cnp_inspect_standard_type_refer
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
    'ALTER TABLE `cube-srv-gen-maintain`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-2.0.8-0.mysql.sql', '2024-04-23 01:27:30', 1, 'EXECUTED', '8:d231462684af36ef191cb4a2535fc0b3', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806850569');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-electron-2.0.9.0.mysql.sql', '2024-04-23 01:27:30', 2, 'EXECUTED', '8:d0b5e6e7d82c98fd047c6c9528e027ba', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806850636');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-2.0.9-1.mysql.sql', '2024-04-23 01:27:30', 3, 'EXECUTED', '8:36e5895d6750acf1e2584464f01d67c4', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806850818');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-fault-2.0.11-0.mysql.sql', '2024-04-23 01:27:31', 4, 'EXECUTED', '8:c7cf3a3bf61743560a1ca854c15af5ab', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806850905');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-inspection-2.0.14.mysql.sql', '2024-04-23 01:27:31', 5, 'EXECUTED', '8:976379d9badd961180821739d5dc44e5', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806851183');
INSERT IGNORE INTO `databasechangelog` VALUES ('raw', 'includeAll', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-2.0.14-2.mysql.sql', '2024-04-23 01:27:31', 6, 'EXECUTED', '8:2ec57f5e6f2c7a2694be6739a4f163a9', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806851361');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-inspection-2.0.15.mysql.sql', '2024-04-23 01:27:31', 7, 'EXECUTED', '8:06af38acd0688146f7591ce38fbed30e', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806851521');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-outsource-2.0.15.mysql.sql', '2024-04-23 01:27:31', 8, 'EXECUTED', '8:1b7e7eecceaff16868ca7036935880ec', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806851593');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-maintain-2.0.16.mysql.sql', '2024-04-23 01:27:31', 9, 'EXECUTED', '8:90cbd53637522bd4ec241464e4ad46ef', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806851657');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-deviceReplace-2.0.27.mysql.sql', '2024-04-23 01:27:32', 10, 'EXECUTED', '8:b8cc6c4dc136c6fbfd3188d210d5684c', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806851949');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-electron-2.0.28.1.mysql.sql', '2024-04-23 01:27:32', 11, 'EXECUTED', '8:f05c7a12beefd95c4a7b83888818c1b0', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806852264');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-electron-2.1.0.mysql.sql', '2024-04-23 01:27:32', 12, 'EXECUTED', '8:5bfff92cc52547b0d5d3c4ed83e89ab9', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806852350');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-electron-2.1.0.1.mysql.sql', '2024-04-23 01:27:32', 13, 'EXECUTED', '8:9a41dcd64cbc5becf59517f08d7596cc', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806852511');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-electron2.1.4.mysql.sql', '2024-04-23 01:27:32', 14, 'EXECUTED', '8:89d06b29a0c9894ed491c9b538a378db', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806852611');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-electron-2.1.9-1.mysql.sql', '2024-04-23 01:27:32', 14, 'EXECUTED', '8:89d06b29a0c9894ed491c9b538a378db', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806852611');
INSERT IGNORE INTO `databasechangelog` VALUES ('insert-1', 'zengyadong', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-maintenance-2.1.10.mysql.sql', '2024-05-27 20:25:36', 15, 'EXECUTED', '8:042b7bedc5d89c39d293e37caffe7ae1', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812736142');
INSERT IGNORE INTO `databasechangelog` VALUES ('ddl-1', 'lijie', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-maintenance-2.1.13.mysql.sql', '2024-05-27 20:25:37', 16, 'EXECUTED', '8:ae0001b6e047c3d9f62e7e9a1f9bd0ae', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812737280');
INSERT IGNORE INTO `databasechangelog` VALUES ('ddl-2', 'lijie', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-maintenance-2.1.13.mysql.sql', '2024-05-27 20:25:38', 17, 'EXECUTED', '8:2f7e1dbf87fedc32069225175aa91812', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812737280');
INSERT IGNORE INTO `databasechangelog` VALUES ('ALTER-1', 'lijie', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-electron-2.1.15-1.mysql.sql', '2024-05-27 20:25:40', 18, 'EXECUTED', '8:dd60b97df00e0b876eba673c3056fbf8', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812738433');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.maintain/changelog-outsource-2.0.16.mysql.sql', '2024-06-05 10:18:16', 19, 'EXECUTED', '8:6f27718ea87fa9fde996ab02ad4f76c0', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553896780');

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
-- Table structure for de_duplication_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `de_duplication_config`  (
  `id` bigint(20) NOT NULL,
  `de_duplication_period` int(3) NULL DEFAULT NULL COMMENT '去重周期',
  `duration` int(2) NULL DEFAULT NULL COMMENT '持续时间',
  `switch_setting` bit(1) NULL DEFAULT NULL COMMENT '开关设置',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of de_duplication_config
-- ----------------------------

-- ----------------------------
-- Table structure for fault
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fault`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障单编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障单名称',
  `obj_id` bigint(20) NULL DEFAULT NULL COMMENT '故障设备对象主键',
  `obj_type` bigint(20) NULL DEFAULT NULL COMMENT '设备类型',
  `obj_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `obj_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障设备对象名称',
  `resource` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障来源',
  `fault_time` datetime NULL DEFAULT NULL COMMENT '故障时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障现象',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '提交人用户主键',
  `submitter` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `confirm_result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认结果',
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
  `device_manager` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域',
  `fault_obj_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障对象类型',
  `fault_type_id` bigint(20) NULL DEFAULT NULL COMMENT '故障类型id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fault
-- ----------------------------

-- ----------------------------
-- Table structure for fault_case
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fault_case`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障案例名称',
  `fault_type_id` bigint(20) NULL DEFAULT NULL COMMENT '故障类型主键',
  `fault_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障类型名称',
  `device_type_id` bigint(20) NULL DEFAULT NULL COMMENT '设备类型主键',
  `device_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型名称',
  `fault_phenomenon` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '故障现象',
  `fault_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '故障原因',
  `treatment_measures` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '处理措施',
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
  `product_category_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备品类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fault_case
-- ----------------------------

-- ----------------------------
-- Table structure for fault_closed
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fault_closed`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `fault_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '故障单主键',
  `fault_type_id` bigint(20) NULL DEFAULT NULL COMMENT '故障类型主键',
  `fault_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障类型名称',
  `result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '解决结果',
  `closed_user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '关闭人主键',
  `closed_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关闭人名称',
  `closed_time` datetime NULL DEFAULT NULL COMMENT '关闭时间',
  `fault_case_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '故障案例主键',
  `fault_case_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障案例名称',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述信息',
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
  `cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '维修城本',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_fault_id_tenant_deleted`(`fault_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fault_closed
-- ----------------------------

-- ----------------------------
-- Table structure for fault_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fault_config`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `notice_mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知方式',
  `mgmt_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障管理人员类别',
  `mgmt_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障管理人员数值',
  `auto_gen_fault` tinyint(1) NULL DEFAULT NULL COMMENT '系统自动生成故障',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fault_config
-- ----------------------------

-- ----------------------------
-- Table structure for fault_confirm
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fault_confirm`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `fault_id` bigint(20) NULL DEFAULT NULL COMMENT '故障单主键',
  `confirm_user_id` bigint(20) NULL DEFAULT NULL COMMENT '故障单提交用户主键',
  `confirm_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障单提交用户名称',
  `confirm_result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认结果',
  `confirm_time` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认描述',
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
  `process_instance_id` varchar(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_fault_id_tenant_deleted`(`fault_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fault_confirm
-- ----------------------------

-- ----------------------------
-- Table structure for fault_order
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fault_order`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `process_ins_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障单编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障单名称',
  `obj_id` bigint(20) NULL DEFAULT NULL COMMENT '故障对象id',
  `obj_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障对象名称',
  `obj_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障对象类型',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '网格id',
  `fault_type_id` bigint(20) NULL DEFAULT NULL COMMENT '故障类型id',
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '故障来源',
  `urgency_level` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '紧急程度',
  `result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `submit_staff` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '维护成本',
  `state` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `process_ins_id`(`process_ins_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '故障单2.0' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for fault_order_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fault_order_config`  (
  `id` bigint(20) NOT NULL,
  `de_duplication_period` int(10) NULL DEFAULT NULL COMMENT '去重周期',
  `duration` int(10) NULL DEFAULT NULL COMMENT '持续时间',
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
  `auto_fault` bigint(1) NULL DEFAULT NULL COMMENT '是否生自动成故障单',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fault_order_config
-- ----------------------------

-- ----------------------------
-- Table structure for fault_report
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fault_report`  (
  `id` bigint(20) NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户',
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
-- Records of fault_report
-- ----------------------------

-- ----------------------------
-- Table structure for fault_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fault_type`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for hidden_danger
-- ----------------------------

CREATE TABLE IF NOT EXISTS `hidden_danger`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患单号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患单名称',
  `hidden_obj_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '隐患对象主键',
  `hidden_obj_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患对象名称',
  `hidden_obj_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患对象类别',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '提交人用户主键',
  `submitter` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `confirm_result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认结果',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患描述',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `manager` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hidden_danger
-- ----------------------------

-- ----------------------------
-- Table structure for hidden_danger_category
-- ----------------------------

CREATE TABLE IF NOT EXISTS `hidden_danger_category`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '隐患分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for hidden_danger_closed
-- ----------------------------

CREATE TABLE IF NOT EXISTS `hidden_danger_closed`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `hidden_danger_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '隐患单主键',
  `result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '解决结果',
  `closed_person` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关闭人',
  `closed_time` datetime NULL DEFAULT NULL COMMENT '关闭时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述信息',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_hidden_danger_id_tenant_deleted`(`hidden_danger_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hidden_danger_closed
-- ----------------------------

-- ----------------------------
-- Table structure for hidden_danger_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `hidden_danger_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `notice_mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知方式',
  `category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类',
  `mgmt_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理类别',
  `mgmt_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理数值',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hidden_danger_config
-- ----------------------------

-- ----------------------------
-- Table structure for hidden_danger_confirm
-- ----------------------------

CREATE TABLE IF NOT EXISTS `hidden_danger_confirm`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `hidden_danger_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '隐患单主键',
  `hidden_danger_category_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '隐患分类主键',
  `hidden_danger_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患分类名称',
  `hidden_danger_type_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '隐患类型主键',
  `hidden_danger_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患类型名称',
  `hidden_danger_level_id` bigint(20) NULL DEFAULT NULL COMMENT '隐患级别主键',
  `hidden_danger_level_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患级别名称',
  `confirm_user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '确认用户主键',
  `confirm_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认用户名称',
  `confirm_result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认结果',
  `confirm_time` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `limit_time` int(10) NULL DEFAULT NULL COMMENT '排查类别',
  `limit_time_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '排查时间类别',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认信息',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_hidden_danger_id_tenant_deleted`(`hidden_danger_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hidden_danger_confirm
-- ----------------------------

-- ----------------------------
-- Table structure for hidden_danger_level
-- ----------------------------

CREATE TABLE IF NOT EXISTS `hidden_danger_level`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '隐患程度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for hidden_danger_new
-- ----------------------------

CREATE TABLE IF NOT EXISTS `hidden_danger_new`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `process_ins_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患单号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患单名称',
  `hidden_obj_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '隐患对象id',
  `hidden_obj_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患对象名称',
  `hidden_obj_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患对象类型',
  `hidden_type_id` bigint(20) NULL DEFAULT NULL COMMENT '隐患类型id',
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患来源',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患描述',
  `urgency_level` bigint(20) NULL DEFAULT NULL COMMENT '隐患程度',
  `result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `submit_staff` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `state` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `response` tinyint(4) NULL DEFAULT 0 COMMENT '是否响应',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `process_ins_id`(`process_ins_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '隐患单2.0' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for hidden_danger_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `hidden_danger_type`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `hidden_danger_category_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '隐患分类主键',
  `hidden_danger_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患分类名称',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '隐患类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_cron
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_cron`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
  `tenant` int(11) NOT NULL COMMENT '租户ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `cron` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表达式',
  `description` varchar(450) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '时间表达式表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_item_template
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_item_template`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alias` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '别名',
  `refer_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用对象类型',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检项名称',
  `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `result_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检结果Id',
  `result_item_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果默认值',
  `image_flag` bit(1) NULL DEFAULT NULL COMMENT '拍照 0 否 1是',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `result_id`(`result_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检标准中的巡检项模板(2.0)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of inspect_item_template
-- ----------------------------

-- ----------------------------
-- Table structure for inspect_plan
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_plan`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `type_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '类型',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名',
  `level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'GENERAL' COMMENT '执行级别，普通/紧急',
  `begin_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `valid_begin_time` datetime NULL DEFAULT NULL COMMENT '生效开始时间',
  `valid_end_time` datetime NULL DEFAULT NULL COMMENT '生效结束时间',
  `enable` bit(1) NULL DEFAULT NULL COMMENT '1开启 0关闭',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行状态：执行中 已结束 已停用',
  `limit_time_json` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'json字符串',
  `limit_total_minute` int(8) NULL DEFAULT NULL COMMENT '精确到分钟',
  `cycle` bit(1) NULL DEFAULT NULL COMMENT '循环类型 0=单次 1=循环',
  `cron_id` bigint(20) NULL DEFAULT NULL COMMENT '表达式ID',
  `cron_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表达式名',
  `cron_expression` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `is_split` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否需要拆解2.0',
  `is_check` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否需要稽查2.0',
  `check_user_type` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '稽查接收类型1个人2部门2.0',
  `check_dept_ids` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '稽查部门id逗号分割2.0',
  `check_post_ids` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '稽查岗位id逗号分隔2.0',
  `receive_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收类型，1＝个人，2＝部门2.0',
  `receive_dept_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收部门',
  `receive_post_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收岗位',
  `receive_ids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收对象ID',
  `target_num` int(11) NULL DEFAULT NULL COMMENT '配置巡检目标对象数',
  `exec_times` int(11) NULL DEFAULT NULL COMMENT '执行次数',
  `xxl_task_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'xxljob的任务ID',
  `copy_target_tree` json NULL COMMENT '计划的巡检对象/标准树结构,用于复制计划',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '创建者ID',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  `check_user_ids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '稽查人/部门id逗号分隔2.0',
  `method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检计划' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_plan_relation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_plan_relation`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NOT NULL COMMENT '租户ID',
  `plan_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检计划ID',
  `target_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象类型：device设备 stall挡位 等',
  `target_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象具体类型',
  `target_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检对象id',
  `target_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象名称',
  `standard_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检标准ID',
  `item_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检项ID',
  `result_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果code',
  `is_raw_item` tinyint(1) NULL DEFAULT NULL COMMENT '是否原始数据(自己新增添加就不是',
  `inspect_time` datetime NULL DEFAULT NULL COMMENT '巡检时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  INDEX `id`(`id`) USING BTREE COMMENT 'ID普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检计划关联关系,计划-对象-标准-结果' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_plan_target_refer
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_plan_target_refer`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `plan_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '计划ID',
  `target_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象类型：device设备 stall挡位 等',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象具体类型',
  `target_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检对象ID',
  `target_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡检对象名称',
  `result_ids` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果ids',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检目标象包括：设备、挡位、等' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of inspect_plan_target_refer
-- ----------------------------

-- ----------------------------
-- Table structure for inspect_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_record`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `task_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '任务ID（临时巡检没有任务ID）',
  `task_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `plan_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '计划ID',
  `plan_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划名',
  `target_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标对象类型',
  `target_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '目标对象ID',
  `target_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标对象名',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `time` datetime NULL DEFAULT NULL COMMENT '巡检时间',
  `organization_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '组织机构ID',
  `organization_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织机构名',
  `position_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '岗位ID',
  `position_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位名',
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  `area_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备区域id',
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备区域名称',
  `revision` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '离线同步版本号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `taskTargetObj`(`tenant`, `task_id`, `target_category`, `target_id`) USING BTREE,
  INDEX `task_id`(`task_id`) USING BTREE,
  INDEX `target_id`(`target_id`) USING BTREE,
  INDEX `idx_inspect_record_offline_task`(`tenant`, `task_id`, `status`) USING BTREE,
  INDEX `idx_inspect_record_offline_revision`(`tenant`, `task_id`, `revision`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_record_item
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_record_item`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '巡检结果记录ID',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `record_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检记录ID',
  `temp_add_flag` bit(1) NULL DEFAULT NULL COMMENT '是否临时添加标准',
  `standard_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检标准ID',
  `standard_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检标准名称',
  `item_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检项ID',
  `item_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检项名称',
  `item_image_flag` bit(1) NULL DEFAULT NULL COMMENT '是否强制拍照 0 否 1是',
  `item_result_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检结果Id',
  `item_result_value` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检项结果值',
  `item_result_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检项结果名称',
  `error_value_flag` bit(1) NULL DEFAULT NULL COMMENT '0 不是  1 是',
  `error_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常说明',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `record_id`(`record_id`) USING BTREE,
  INDEX `item_id`(`item_id`) USING BTREE,
  INDEX `item_result_id`(`item_result_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检记录-结果项\r\n' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of inspect_record_item
-- ----------------------------

-- ----------------------------
-- Table structure for inspect_record_relation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_record_relation`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NOT NULL COMMENT '租户ID',
  `plan_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检计划ID',
  `task_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检任务ID',
  `target_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象类型：device设备 stall挡位 等',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象具体类型',
  `target_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检对象id',
  `target_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡检对象名称',
  `standard_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检标准ID',
  `item_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检项ID',
  `result_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果code',
  `inspect_time` datetime NULL DEFAULT NULL COMMENT '巡检时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  INDEX `id`(`id`) USING BTREE COMMENT 'ID普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检计划关联关系,计划-对象-标准-结果' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of inspect_record_relation
-- ----------------------------

-- ----------------------------
-- Table structure for inspect_record_to_business
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_record_to_business`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `record_id` bigint(20) NULL DEFAULT NULL COMMENT '巡检记录ID',
  `target_category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标对象类别',
  `target_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '目标对象ID',
  `target_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标对象名称',
  `business_category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务分类 工单（ticket）',
  `business_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务ID',
  `business_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务对象名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `business_id`(`business_id`) USING BTREE,
  INDEX `record_id`(`record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检记录转其他业务的关联表(工单、隐患、其他等)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of inspect_record_to_business
-- ----------------------------

-- ----------------------------
-- Table structure for inspect_result
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_result`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果名称',
  `item_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项名称',
  `item_value` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '选项值',
  `error_flag` bit(1) NULL DEFAULT NULL COMMENT '是否异常点',
  `enable` bit(1) NULL DEFAULT b'1' COMMENT '1 启用 0禁用',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  INDEX `id`(`id`) USING BTREE COMMENT 'ID普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检结果  实际为巡检结果项  冗余了名称' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_scene_point
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_scene_point`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
  `code_num` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '现场点类型id',
  `type_name` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现场点类型名称',
  `dep_id` bigint(20) NULL DEFAULT NULL COMMENT '管理部门id',
  `dep_name` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理部门名称',
  `area_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属区域id',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '定位',
  `tenant` int(11) NOT NULL COMMENT '租户id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '现场点' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_scene_point_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_scene_point_device`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `scene_point_id` bigint(20) UNSIGNED NOT NULL COMMENT '现场点ID',
  `device_id` bigint(20) UNSIGNED NOT NULL COMMENT '设备ID',
  `tenant` int(11) NOT NULL COMMENT '租户id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_scene_point_id`(`scene_point_id`) USING BTREE,
  KEY `idx_device_id`(`device_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '现场点关联设备' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_scene_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_scene_type`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `tenant` int(11) NOT NULL COMMENT '租户id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '现场点类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_standard
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_standard`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准名称',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准编码',
  `enable` bit(1) NULL DEFAULT b'1' COMMENT '1启用 0禁用',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  `describe` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检标准' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_standard_item
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_standard_item`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alias` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检项别名',
  `standard_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检标准ID',
  `refer_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检项名称',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检项内容',
  `result_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检结果Id',
  `result_item_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果默认值',
  `image_flag` bit(1) NULL DEFAULT NULL COMMENT '拍照 0 否 1是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `standard_id`(`standard_id`) USING BTREE,
  INDEX `result_id`(`result_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检标准中的巡检项' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_standard_type_refer
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_standard_type_refer`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `standard_id` bigint(20) NULL DEFAULT NULL COMMENT '标准ID',
  `target_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联类别：asset资产 gear挡位 device设备',
  `target_type` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可能为资产、挡位、现场点的具体类型',
  `default_standard` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认标准标识',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检标准与巡检对象关联，类型包括：设备、挡位类型、等' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_task
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_task`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `plan_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '计划ID',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务编号',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '父任务id',
  `plan_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划名',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `is_split` tinyint(1) NULL DEFAULT 0 COMMENT '是否需要拆解',
  `check_task_id` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '稽查任务关联的任务id',
  `type_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检类型ID',
  `type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检类型名',
  `level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'GENERAL' COMMENT '执行级别普通/紧急',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行状态：执行中 已结束 已停用',
  `publisher_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布人ID',
  `publisher_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布人名',
  `limit_time_json` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'json字符串',
  `limit_total_minute` int(8) NULL DEFAULT NULL COMMENT '精确到分钟',
  `receive_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收类型，1＝个人，2＝部门',
  `receive_dept_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收部门',
  `receive_post_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收岗位',
  `receive_ids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收对象ID',
  `assist_Ids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协同人ids',
  `valid_begin_time` datetime NULL DEFAULT NULL COMMENT '生效开始时间',
  `valid_end_time` datetime NULL DEFAULT NULL COMMENT '生效结束时间',
  `begin_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `target_num` int(11) NULL DEFAULT NULL COMMENT '配置巡检目标对象数',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  `result` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检结果',
  `target_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象ids',
  `method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检方式',
  `revision` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '离线同步版本号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `plan_id`(`plan_id`) USING BTREE,
  INDEX `idx_inspect_task_offline_status`(`tenant`, `status`, `update_time`) USING BTREE,
  INDEX `idx_inspect_task_offline_revision`(`tenant`, `status`, `revision`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检任务' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `inspect_offline_sync_session` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `session_id` varchar(64) NOT NULL COMMENT '同步会话ID',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `client_id` varchar(128) NULL DEFAULT NULL COMMENT '设备稳定标识',
  `app_version` varchar(32) NULL DEFAULT NULL COMMENT 'App版本',
  `offline_schema_version` int(11) NULL DEFAULT NULL COMMENT '离线协议版本',
  `base_cursor` varchar(255) NULL DEFAULT NULL COMMENT '客户端上次游标',
  `snapshot_id` varchar(64) NOT NULL COMMENT '快照ID',
  `status` varchar(32) NOT NULL COMMENT '会话状态',
  `progress` int(11) NOT NULL DEFAULT 0 COMMENT '构建进度',
  `cursor` varchar(255) NULL DEFAULT NULL COMMENT '新游标',
  `manifest_json` longtext NULL COMMENT 'manifest JSON',
  `inline_delta_json` longtext NULL COMMENT '小增量 JSON',
  `error_code` varchar(64) NULL DEFAULT NULL COMMENT '错误码',
  `error_message` varchar(500) NULL DEFAULT NULL COMMENT '错误信息',
  `expires_at` datetime NOT NULL COMMENT '过期时间',
  `create_by` varchar(100) NULL DEFAULT NULL COMMENT '创建用户',
  `creator_id` varchar(100) NULL DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) NULL DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NULL DEFAULT NULL COMMENT '更新用户',
  `updater_id` varchar(100) NULL DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) NULL DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_inspect_offline_session`(`session_id`) USING BTREE,
  INDEX `idx_inspect_offline_session_user`(`tenant`, `user_id`, `status`) USING BTREE,
  INDEX `idx_inspect_offline_session_expire`(`expires_at`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检离线同步会话' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `inspect_offline_sync_chunk` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `chunk_id` varchar(64) NOT NULL COMMENT '分片ID',
  `session_id` varchar(64) NOT NULL COMMENT '同步会话ID',
  `snapshot_id` varchar(64) NOT NULL COMMENT '快照ID',
  `chunk_type` varchar(32) NOT NULL COMMENT '分片类型',
  `task_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '任务ID',
  `revision` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '任务版本',
  `part_no` int(11) NOT NULL COMMENT '分片序号',
  `part_count` int(11) NOT NULL COMMENT '分片总数',
  `compressed_size` bigint(20) UNSIGNED NOT NULL COMMENT '压缩后大小',
  `sha256` varchar(64) NOT NULL COMMENT '分片SHA256',
  `storage_key` varchar(500) NOT NULL COMMENT '兼容存储定位符',
  `content_gzip` longblob NULL COMMENT 'gzip JSON分片内容',
  `expires_at` datetime NOT NULL COMMENT '过期时间',
  `create_by` varchar(100) NULL DEFAULT NULL COMMENT '创建用户',
  `creator_id` varchar(100) NULL DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) NULL DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NULL DEFAULT NULL COMMENT '更新用户',
  `updater_id` varchar(100) NULL DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) NULL DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_inspect_offline_chunk`(`session_id`, `chunk_id`) USING BTREE,
  INDEX `idx_inspect_offline_chunk_session`(`session_id`, `part_no`) USING BTREE,
  INDEX `idx_inspect_offline_chunk_expire`(`expires_at`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检离线同步分片' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `inspect_offline_submit_operation` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `client_id` varchar(128) NULL DEFAULT NULL COMMENT '设备稳定标识',
  `client_batch_id` varchar(64) NULL DEFAULT NULL COMMENT '客户端批次ID',
  `client_operation_id` varchar(64) NOT NULL COMMENT '客户端操作ID',
  `record_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检记录ID',
  `task_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检任务ID',
  `record_revision` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '客户端记录版本',
  `payload_hash` varchar(64) NOT NULL COMMENT '请求摘要',
  `request_json` longtext NULL COMMENT '请求JSON',
  `status` varchar(32) NOT NULL COMMENT '操作状态',
  `server_record_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '服务端记录ID',
  `server_record_revision` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '服务端记录版本',
  `error_code` varchar(64) NULL DEFAULT NULL COMMENT '错误码',
  `error_message` varchar(500) NULL DEFAULT NULL COMMENT '错误信息',
  `create_by` varchar(100) NULL DEFAULT NULL COMMENT '创建用户',
  `creator_id` varchar(100) NULL DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) NULL DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NULL DEFAULT NULL COMMENT '更新用户',
  `updater_id` varchar(100) NULL DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) NULL DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_inspect_offline_submit_operation`(`tenant`, `user_id`, `client_operation_id`) USING BTREE,
  INDEX `idx_inspect_offline_submit_record`(`tenant`, `record_id`) USING BTREE,
  INDEX `idx_inspect_offline_submit_batch`(`tenant`, `user_id`, `client_batch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检离线提交幂等操作' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspect_task_target_relation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_task_target_relation`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NOT NULL COMMENT '租户ID',
  `plan_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '计划ID',
  `task_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检任务ID',
  `target_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象类型：device设备 stall挡位 等',
  `target_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检对象具体类型',
  `target_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检对象ID',
  `target_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡检对象名称',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  INDEX `id`(`id`) USING BTREE COMMENT 'ID普通索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检任务和对象关联关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of inspect_task_target_relation
-- ----------------------------

-- ----------------------------
-- Table structure for inspect_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspect_type`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
  `tenant` int(11) NOT NULL COMMENT '租户id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for inspection_area
-- ----------------------------

CREATE TABLE IF NOT EXISTS `inspection_area`  (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID，自增',
  `Code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域编码',
  `Name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域名称',
  `Pid` int(11) NOT NULL DEFAULT 0 COMMENT '区域父级ID',
  `inspection_areacode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `Gearcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`Id`) USING BTREE,
  UNIQUE INDEX `Id`(`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备区域表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of inspection_area
-- ----------------------------

-- ----------------------------
-- Table structure for maintenance_object
-- ----------------------------

CREATE TABLE IF NOT EXISTS `maintenance_object`  (
  `id` bigint(20) NOT NULL,
  `tenant` int(11) NOT NULL COMMENT '租户',
  `object_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对象名称',
  `plan_id` bigint(20) NOT NULL COMMENT '计划id',
  `area` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `key_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id及前端复选定位',
  `installation_address` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装地址',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `area_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  `category_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '维保对象 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of maintenance_object
-- ----------------------------

-- ----------------------------
-- Table structure for maintenance_object_projects
-- ----------------------------

CREATE TABLE IF NOT EXISTS `maintenance_object_projects`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `project_id` bigint(20) NULL DEFAULT NULL COMMENT '结果项项id',
  `object_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '维保对象',
  `remark` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维保说明',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '维保对象维保项 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of maintenance_object_projects
-- ----------------------------

-- ----------------------------
-- Table structure for maintenance_plan
-- ----------------------------

CREATE TABLE IF NOT EXISTS `maintenance_plan`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `maintenance_plan_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '维保计划名称',
  `responsibility_person_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '责任id',
  `responsibility_person_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '责任名称',
  `responsibility_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '责任类型',
  `maintenance_cycle` int(11) NULL DEFAULT NULL COMMENT '维保周期',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `plan_start_time` date NOT NULL COMMENT '计划生效时间',
  `plan_end_time` date NOT NULL COMMENT '计划失效时间',
  `next_task_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下次任务生成时间',
  `maintenance_time_point` int(2) NULL DEFAULT NULL COMMENT '发布时间点',
  `time_unit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '周期单位',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `receive_dept_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门ids',
  `receive_post_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位ids',
  `way` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成任务方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '维保计划 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of maintenance_plan
-- ----------------------------

-- ----------------------------
-- Table structure for maintenance_proget_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `maintenance_proget_record`  (
  `id` bigint(20) NULL DEFAULT NULL COMMENT 'id',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务id',
  `maintrenace_project` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维保项',
  `record` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维保记录',
  `maintenance_describe` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维保描述',
  `maintenance_person` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维保人',
  `maintenance_person_id` bigint(20) NULL DEFAULT NULL COMMENT '维保人id',
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '维保结果记录 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of maintenance_proget_record
-- ----------------------------

-- ----------------------------
-- Table structure for maintenance_project
-- ----------------------------

CREATE TABLE IF NOT EXISTS `maintenance_project`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户',
  `maintenance_content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '维保项内容',
  `code` varchar(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `category_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '维保配置项 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of maintenance_project
-- ----------------------------

-- ----------------------------
-- Table structure for maintenance_rule_condition
-- ----------------------------

CREATE TABLE IF NOT EXISTS `maintenance_rule_condition`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '计划主键',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '维保规则条件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of maintenance_rule_condition
-- ----------------------------

-- ----------------------------
-- Table structure for maintenance_rule_condition_exp
-- ----------------------------

CREATE TABLE IF NOT EXISTS `maintenance_rule_condition_exp`  (
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '维保规则条件表达式' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of maintenance_rule_condition_exp
-- ----------------------------

-- ----------------------------
-- Table structure for maintenance_tasks
-- ----------------------------

CREATE TABLE IF NOT EXISTS `maintenance_tasks`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tenant` int(11) NOT NULL COMMENT '租户',
  `plan_id` bigint(20) NOT NULL COMMENT '计划id',
  `code` varchar(70) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `object_id` bigint(20) NULL DEFAULT NULL COMMENT '对象id',
  `object_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对象名称',
  `maintenance_task_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务状态',
  `maintenance_time` datetime NULL DEFAULT NULL COMMENT '维保发布时间',
  `maintenance_finish_time` datetime NULL DEFAULT NULL COMMENT '维保完成时间',
  `picture` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `task_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `maintenance_task_person` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维保人员',
  `maintenance_task_person_id` bigint(20) NULL DEFAULT NULL COMMENT '维保人员id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `category_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类编码',
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '维保任务 ' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of maintenance_tasks
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_company
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_company`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '委外公司名称',
  `outsource_model` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '委外模式 id数组',
  `outsource_scope` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '委外范围，id数组',
  `responsible_person` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '责任人',
  `responsible_person_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '责任人手机号',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_company
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_company_model
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_company_model`  (
  `id` bigint(20) NOT NULL,
  `outsource_company_id` bigint(20) NULL DEFAULT NULL,
  `outsource_model_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_company_model
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_company_scope
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_company_scope`  (
  `id` bigint(20) NOT NULL,
  `device_genre_id` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '委外范围(设备品类code)',
  `outsource_company_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_company_scope
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_device_instance
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_device_instance`  (
  `id` bigint(20) NOT NULL,
  `device_instance_id` bigint(20) NULL DEFAULT NULL COMMENT '委外设备',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_device_instance
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_model
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_model`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_model
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_repair_application_confirm_receipt
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_repair_application_confirm_receipt`  (
  `id` bigint(20) NOT NULL,
  `confirm_result` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核结果状态',
  `confirm_user_id` bigint(20) NOT NULL COMMENT '确认用户',
  `outsource_company_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '委外单位',
  `outsource_model_id` bigint(10) NULL DEFAULT NULL COMMENT '委外模式',
  `application_check_process_instance_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请审核流程实例',
  `acceptance_process_key` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验收流程',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `application_receipt_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_repair_application_confirm_receipt
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_repair_application_receipt
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_repair_application_receipt`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '委外申请单编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '委外申请单名称',
  `device_object_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '维修对象名称',
  `fault_time` datetime NULL DEFAULT NULL COMMENT '故障时间',
  `submit_user_id` bigint(20) NOT NULL COMMENT '提交用户',
  `repair_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修原因',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `device_instance_id` bigint(20) NULL DEFAULT NULL,
  `handle_result_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_repair_application_receipt
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_repair_notice_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_repair_notice_config`  (
  `id` bigint(20) NOT NULL,
  `notify_channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `specified_departments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `specified_jobs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `specified_users` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指定的用户',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `allow_manager` bit(1) NULL DEFAULT NULL COMMENT 'true:允许设备管理人员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_repair_notice_config
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_repair_result
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_repair_result`  (
  `id` bigint(20) NOT NULL,
  `outsource_repair_ticket_id` bigint(20) NULL DEFAULT NULL,
  `handle_result_status` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_repair_result
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_repair_ticket
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_repair_ticket`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工单编号',
  `application_receipt_id` bigint(20) NULL DEFAULT NULL COMMENT '维修申请单',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工单状态',
  `handle_result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_repair_ticket
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_repair_ticket_acceptance
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_repair_ticket_acceptance`  (
  `id` bigint(20) NOT NULL,
  `repair_ticket_id` bigint(20) NULL DEFAULT NULL,
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_repair_ticket_acceptance
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_repair_ticket_assessment
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_repair_ticket_assessment`  (
  `id` bigint(20) NOT NULL,
  `outsource_repair_ticket_id` bigint(20) NULL DEFAULT NULL,
  `score` int(2) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `assessment_user_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_repair_ticket_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_repair_ticket_distribute
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_repair_ticket_distribute`  (
  `id` bigint(20) NOT NULL,
  `handle_action` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理操作',
  `handle_user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `handle_user_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `expect_solve_date` date NULL DEFAULT NULL COMMENT '期望解决日期',
  `additional_information` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outsource_repair_ticket_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_repair_ticket_distribute
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_repair_ticket_event
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_repair_ticket_event`  (
  `id` bigint(20) NOT NULL,
  `action` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `handle_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `handle_user_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `handle_result` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outsource_repair_ticket_id` bigint(20) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of outsource_repair_ticket_event
-- ----------------------------

-- ----------------------------
-- Table structure for outsource_work_order
-- ----------------------------

CREATE TABLE IF NOT EXISTS `outsource_work_order`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `process_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
  `obj_id` bigint(20) NULL DEFAULT NULL COMMENT '维修对象id',
  `obj_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修对象名称',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修理由',
  `attachment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件图片',
  `identify_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认结果',
  `identify_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认描述',
  `company_id` bigint(20) NULL DEFAULT NULL COMMENT '委外公司',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '委外模式',
  `handle_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `handle_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理描述',
  `appraise` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '效果评估',
  `appraise_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评估描述',
  `appraise_attachment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评估附件',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '委外2.0' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of outsource_work_order
-- ----------------------------

-- ----------------------------
-- Table structure for patrol_alarm
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_alarm`  (
  `id` bigint(20) NOT NULL,
  `alarm_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `alarm_point` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地点',
  `alarm_content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `alarm_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间',
  `alarm_result` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `patrol_record_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检记录ID',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of patrol_alarm
-- ----------------------------

-- ----------------------------
-- Table structure for patrol_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_device`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `dept_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门id',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态编码',
  `status_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态名称',
  `user` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of patrol_device
-- ----------------------------

-- ----------------------------
-- Table structure for patrol_location
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_location`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `patrol_location_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更点名称',
  `nfc_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'nfc芯片编号',
  `bim_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡更点模型编码',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `patrol_longitude` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
  `patrol_dimension` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `area_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域id',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除状态',
  `bluetooth_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '蓝牙编号',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '巡更设备id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡更点信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for patrol_nfc_details
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_nfc_details`  (
  `id` bigint(11) NOT NULL,
  `nfc_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'NFC芯片编号',
  `nfc_longitude` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
  `nfc_dimension` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '绑定状态',
  `device_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '蓝牙编号',
  `bluetooth_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '蓝牙编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'nfc信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for patrol_plan
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_plan`  (
  `id` bigint(11) NOT NULL,
  `plan_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计划名称',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计划状态',
  `start_data` datetime NOT NULL COMMENT '计划生效时间',
  `end_data` datetime NULL DEFAULT NULL COMMENT '计划结束时间',
  `patrol_cycle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更周期',
  `patrol_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更日期',
  `execute_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行时间段',
  `route_id` bigint(11) NOT NULL COMMENT '巡更路线id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `on_duty` tinyint(1) NOT NULL COMMENT '是否指派当班',
  `in_order` tinyint(1) NOT NULL COMMENT '是否按照顺序执行',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更人员',
  `patrol_data_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更日期类型',
  `last_date` date NULL DEFAULT NULL COMMENT '下次生成时间',
  `patrol_way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡更方式',
  `visible_user_id` bigint(20) NULL DEFAULT NULL COMMENT '权限人员',
  `visible_org_id` bigint(20) NULL DEFAULT NULL COMMENT '权限部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡更计划表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for patrol_plan_location
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_plan_location`  (
  `id` bigint(20) NOT NULL,
  `location_id` bigint(20) NOT NULL COMMENT '路线id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `order_num` int(10) NOT NULL COMMENT '巡更点顺序',
  `plan_id` bigint(20) NOT NULL COMMENT '计划id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡更任务-路线' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for patrol_plan_task
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_plan_task`  (
  `id` bigint(20) NOT NULL,
  `plan_id` bigint(20) NOT NULL COMMENT '计划id',
  `time_range` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '时间范围',
  `interval_number` int(11) NOT NULL COMMENT '间隔时间的值',
  `interval_unit` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '间隔时间的单位',
  `execute_period` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实际执行的时间段',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡更计划详情-任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for patrol_route
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_route`  (
  `id` bigint(11) NOT NULL,
  `patrol_route_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更路线名称',
  `patrol_location_id` bigint(11) NOT NULL COMMENT '巡更点id',
  `rule_order` int(10) NOT NULL COMMENT '顺序',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除状态',
  `uuid` bigint(20) NULL DEFAULT NULL COMMENT '唯一标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡更路线' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for patrol_task
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_task`  (
  `id` bigint(11) UNSIGNED NOT NULL,
  `task_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更任务',
  `execute_data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行时间段',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务状态',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `plan_id` bigint(11) NULL DEFAULT NULL COMMENT '计划id',
  `on_duty` int(1) NOT NULL COMMENT '是否指派当班',
  `in_order` int(1) NOT NULL COMMENT '是否按照顺序执行',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '执行人id',
  `start_date` datetime NOT NULL COMMENT '任务开始时间',
  `result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务结果',
  `belonger` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务归属人ids',
  `route_id` bigint(11) NULL DEFAULT NULL COMMENT '巡更路线id',
  `location_num` int(10) NULL DEFAULT NULL COMMENT '巡更点数',
  `complete_date` datetime NULL DEFAULT NULL COMMENT '任务完成时间',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行人姓名',
  `user_dep_id` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行人部门',
  `end_date` datetime NOT NULL COMMENT '任务结束时间',
  `patrol_way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡更方式',
  `affirm_state` tinyint(1) NOT NULL DEFAULT 0 COMMENT '确认状态',
  `visible_user_id` bigint(20) NULL DEFAULT NULL COMMENT '权限人员',
  `visible_org_id` bigint(20) NULL DEFAULT NULL COMMENT '权限部门',
  `interval_number` int(11) NOT NULL COMMENT '间隔时间的值',
  `interval_unit` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '间隔时间的单位',
  `execute_period` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实际执行的实际段',
  `execute_period_time` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '带日期的执行时间段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡更任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for patrol_task_location
-- ----------------------------

CREATE TABLE IF NOT EXISTS `patrol_task_location`  (
  `id` bigint(11) UNSIGNED NOT NULL DEFAULT 0,
  `location_id` bigint(11) NOT NULL COMMENT '巡更点id',
  `order_num` int(10) NOT NULL COMMENT '巡更点顺序',
  `plan_id` bigint(11) NULL DEFAULT NULL COMMENT '计划id',
  `task_id` bigint(11) NULL DEFAULT NULL COMMENT '任务id',
  `execute_period` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属任务时段',
  `add_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（计划、任务）',
  `result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡更结果',
  `patrol_order` int(10) NULL DEFAULT NULL COMMENT '实际巡更顺序',
  `hidden_num` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否上报隐患',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '巡更人',
  `hidden_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '隐患单id',
  `deleted` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `patrol_date` datetime NULL DEFAULT NULL COMMENT '巡更时间',
  `start_date` datetime NOT NULL COMMENT '任务开始时间',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `exception_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常类型',
  `end_date` datetime NOT NULL COMMENT '任务结束时间',
  `hidden_is_done` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '隐患是否处理完成',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡更任务-巡更点' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for retrofit_assessment
-- ----------------------------

CREATE TABLE IF NOT EXISTS `retrofit_assessment`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `score` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '评价分数',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价描述',
  `pre_attribute` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性json数组',
  `after_attribute` varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `result_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手动设置状态',
  `reported_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备上报的状态',
  `retrofit_ticket_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '改造工单id',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `assessment_user_id` bigint(20) NULL DEFAULT NULL COMMENT '评价用户',
  `device_instance_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备实例状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of retrofit_assessment
-- ----------------------------

-- ----------------------------
-- Table structure for retrofit_notice_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `retrofit_notice_config`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `notify_channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'json数组',
  `specified_departments` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `specified_jobs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `specified_users` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指定的用户',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `allow_manager` bit(1) NULL DEFAULT NULL COMMENT 'true:允许设备管理人员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of retrofit_notice_config
-- ----------------------------

-- ----------------------------
-- Table structure for retrofit_order
-- ----------------------------

CREATE TABLE IF NOT EXISTS `retrofit_order`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `process_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
  `obj_id` bigint(20) NOT NULL COMMENT '改造对象id',
  `obj_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '改造对象名称',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申报理由',
  `expected_resolution_date` datetime NULL DEFAULT NULL COMMENT '期望解决日期',
  `attachment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件图片',
  `retrofit_before_status` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '改造前状态',
  `identify_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认结果',
  `identify_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认描述',
  `handle_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `handle_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理描述',
  `appraise` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '效果评估',
  `appraise_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评估描述',
  `appraise_attachment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评估附件',
  `retrofit_after_status` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '改造后状态',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '更新改造2.0' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of retrofit_order
-- ----------------------------

-- ----------------------------
-- Table structure for retrofit_receipt
-- ----------------------------

CREATE TABLE IF NOT EXISTS `retrofit_receipt`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新改造单编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新改造单名称',
  `device_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '改造的设备',
  `retrofit_object_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '改造对象名称',
  `expect_solve_date` date NULL DEFAULT NULL COMMENT '期望解决日期',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '单据确认状态',
  `confirm_result` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认结果',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申报理由',
  `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据来源，系统创建或者人工创建，目前只有人工创建，如果有系统创建应单独分离表',
  `attributes` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'json格式,属性状态信息',
  `submit_user_id` bigint(20) NULL DEFAULT NULL COMMENT '提交用户',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `device_instance_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备实例状态',
  `handle_result_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of retrofit_receipt
-- ----------------------------

-- ----------------------------
-- Table structure for retrofit_receipt_confirm
-- ----------------------------

CREATE TABLE IF NOT EXISTS `retrofit_receipt_confirm`  (
  `id` bigint(20) NOT NULL,
  `result` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认结果',
  `retrofit_receipt_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新改造单据id',
  `confirm_user_id` bigint(20) NULL DEFAULT NULL COMMENT '确认人员 只要有1个确认成功就成功',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of retrofit_receipt_confirm
-- ----------------------------

-- ----------------------------
-- Table structure for retrofit_receipt_confirm_user_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `retrofit_receipt_confirm_user_config`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `retrofit_receipt_id` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of retrofit_receipt_confirm_user_config
-- ----------------------------

-- ----------------------------
-- Table structure for retrofit_ticket
-- ----------------------------

CREATE TABLE IF NOT EXISTS `retrofit_ticket`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `retrofit_receipt_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '更新改造单id',
  `process_instance_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工单状态',
  `handle_result` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除标志',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `handle_user_id` bigint(20) NULL DEFAULT NULL COMMENT '处理用户id',
  `handle_result_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of retrofit_ticket
-- ----------------------------

-- ----------------------------
-- Table structure for scada_resources
-- ----------------------------

CREATE TABLE IF NOT EXISTS `scada_resources`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT 'ID',
  `tenant` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类别编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组态地址',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_code`(`category_code`) USING BTREE COMMENT '类别编号'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '二维组态资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scada_resources
-- ----------------------------

-- ----------------------------
-- Table structure for ticket
-- ----------------------------

CREATE TABLE IF NOT EXISTS `ticket`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `process_instance_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '提交人用户主键',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门主键',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `source_id` bigint(20) NULL DEFAULT NULL COMMENT '工单来源主键',
  `source_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工单来源名称',
  `source_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工单来源类型',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '工单提交时间',
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工单状态',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ticket
-- ----------------------------

-- ----------------------------
-- Table structure for ticket_evaluate
-- ----------------------------

CREATE TABLE IF NOT EXISTS `ticket_evaluate`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `ticket_id` bigint(20) NULL DEFAULT NULL COMMENT '工单主键',
  `level` int(10) NULL DEFAULT NULL COMMENT '工单级别评价',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工单评价描述',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ticket_evaluate
-- ----------------------------

-- ----------------------------
-- Table structure for ticket_user_finished
-- ----------------------------

CREATE TABLE IF NOT EXISTS `ticket_user_finished`  (
  `id` bigint(20) NOT NULL,
  `business_key` bigint(50) NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `complete_time` datetime NULL DEFAULT NULL COMMENT '任务完成时间',
  `task_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ticket_user_finished
-- ----------------------------

-- ----------------------------
-- Table structure for total_obj_info
-- ----------------------------

CREATE TABLE IF NOT EXISTS `total_obj_info`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `target_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标对象类型',
  `target_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '目标对象ID',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `taskTargetObj`(`tenant`, `target_category`, `target_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '所有对象' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of total_obj_info
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
