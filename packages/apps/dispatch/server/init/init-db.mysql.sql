CREATE DATABASE IF NOT EXISTS`cube-biz-dispatch` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-biz-dispatch`;

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
    'ALTER TABLE `cube-biz-dispatch`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.dispatch/changelog-1.0.3-0.mysql.sql', '2024-04-23 01:27:37', 1, 'EXECUTED', '8:d23418617b9fc7791206688645528b47', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806857759');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.dispatch/changelog-1.0.4-0.mysql.sql', '2024-06-24 09:42:47', 2, 'EXECUTED', '8:72afdc92585cf63eeea25fdae0e7e427', 'sql', '', NULL, '4.3.1', NULL, NULL, '9193367236');

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
-- Table structure for dispatch_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dispatch_config`  (
  `id` bigint(20) NOT NULL,
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `platform_addr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '平台地址',
  `extend_config` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展配置',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '配置调度台的默认账号，平台地址，和一些其他扩展配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dispatch_config
-- ----------------------------
INSERT INTO `dispatch_config` VALUES (1, '800002', '800002', 'http://182.150.63.62:8098', '{\"rootNode\":\"100\",\"center\":{\"lng\":\"104.0561883055594\",\"lat\":\"30.586050055614265\"}}', 68547128, 'admin', '2022-07-29 17:29:19', 'admin', '2022-07-29 17:29:26', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for dispatch_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dispatch_device`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称，可以是人员名称，也可以是设备名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码；通常与code相同',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型：参见对接文档',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '预留字段',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code_index`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用于指挥调度的设备' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dispatch_device
-- ----------------------------

-- ----------------------------
-- Table structure for dispatch_device_extend
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dispatch_device_extend`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `device_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备ID',
  `is_record` tinyint(1) NULL DEFAULT NULL COMMENT '是否录音',
  `is_video` tinyint(1) NULL DEFAULT NULL COMMENT '是否录像',
  `device_level` int(11) NULL DEFAULT NULL COMMENT '设备级别',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `device_tenant_index`(`device_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dispatch_device_extend
-- ----------------------------

-- ----------------------------
-- Table structure for dispatch_device_gps_history
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dispatch_device_gps_history`  (
  `id` bigint(20) NOT NULL,
  `device_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称，可以是人员名称，也可以是设备名称',
  `device_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `upload_time` datetime NULL DEFAULT NULL COMMENT '上传时间',
  `direction` int(10) NULL DEFAULT NULL COMMENT '方向',
  `lng` double NULL DEFAULT NULL COMMENT '经度',
  `lat` double NULL DEFAULT NULL COMMENT '纬度',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置信息',
  `height` double NULL DEFAULT NULL COMMENT '高度',
  `speed` double NULL DEFAULT NULL COMMENT '速度',
  `tenant` int(10) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_device_id_device_code_upload_time_tenant`(`device_id`, `device_code`, `upload_time`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备坐标信息历史记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dispatch_device_gps_history
-- ----------------------------

-- ----------------------------
-- Table structure for dispatch_device_group_refer
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dispatch_device_group_refer`  (
  `id` bigint(20) NOT NULL,
  `group_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组ID',
  `device_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备ID',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `group_device_id`(`group_id`, `device_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dispatch_device_group_refer
-- ----------------------------

-- ----------------------------
-- Table structure for dispatch_device_status
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dispatch_device_status`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '与调度设备表保持一致',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `lng` double NULL DEFAULT NULL COMMENT '经度',
  `lat` double NULL DEFAULT NULL COMMENT '纬度',
  `direction` int(11) NULL DEFAULT NULL COMMENT '方向',
  `speed` float NULL DEFAULT NULL COMMENT '速度',
  `upload_time` datetime NULL DEFAULT NULL COMMENT '上报时间',
  `online_status` tinyint(4) NULL DEFAULT NULL COMMENT '在线状态( 1 在线 0 离线)',
  `video_status` tinyint(4) NULL DEFAULT NULL COMMENT '视频状态( 1 打开 0 关闭)',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code_index`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '该表为调度设备的当前状态表，记录最新一次的状态' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dispatch_device_status
-- ----------------------------

-- ----------------------------
-- Table structure for dispatch_group
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dispatch_group`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级节点',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组编号',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组名称',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组类型:0固定组 1临时组',
  `valid_time` int(11) NULL DEFAULT NULL COMMENT '有效期',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code_index`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dispatch_group
-- ----------------------------

-- ----------------------------
-- Table structure for dispatch_group_extend
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dispatch_group_extend`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组ID',
  `is_record` tinyint(1) NULL DEFAULT NULL COMMENT '是否录音',
  `is_video` tinyint(1) NULL DEFAULT NULL COMMENT '是否录像',
  `group_level` int(11) NULL DEFAULT NULL COMMENT '组级别',
  `intercom_idle_time_out` int(11) NULL DEFAULT NULL COMMENT '对讲空闲超时',
  `intercom_power_time_count` int(11) NULL DEFAULT NULL COMMENT '话权方总超时',
  `queue_max_num` int(11) NULL DEFAULT NULL COMMENT '排队人数限制',
  `intercom_state_space` int(11) NULL DEFAULT NULL COMMENT '对讲状态通知时间间隔',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `group_id_tenant_index`(`group_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dispatch_group_extend
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
