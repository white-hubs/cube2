CREATE DATABASE IF NOT EXISTS `cube-base-dictionary` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube-base-dictionary`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cascade_dictionary
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `cascade_dictionary`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NOT NULL COMMENT '租户id',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
  `variable` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变量',
  `item_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编码',
  `level` int(11) NOT NULL DEFAULT 1 COMMENT '级别',
  `superiors_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '上一级id',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：启用：enable；禁用：disable',
  `permission_state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限（可编辑、可删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '级联字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cascade_tree
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `cascade_tree`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dictionary_id` bigint(20) NOT NULL COMMENT '字典id',
  `superiors_ids` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所有父级id',
  `next_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所有子集id',
  `level` int(11) NOT NULL COMMENT '层级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1789857557758685187 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `data_dictionary`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NOT NULL COMMENT '租户id',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
  `variable` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变量',
  `item_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编号',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：启用：enable；禁用：disable',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通用字典' ROW_FORMAT = DYNAMIC;

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
    'ALTER TABLE `cube-base-dictionary`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.dictionary/global-1.0.0.mysql.sql', '2023-07-10 10:09:37', 1, 'EXECUTED', NULL, 'sql', '', NULL, '4.3.1', NULL, NULL, '8954977210');
INSERT IGNORE INTO `databasechangelog` VALUES ('raw', 'includeAll', 'classpath:db/com.guhecloud.cube.base.dictionary/test.mysql.sql', '2023-11-08 20:22:37', 2, 'EXECUTED', '8:b42a93f0aa32c4ea0372405a5e8daa89', 'sql', '', NULL, '4.3.1', NULL, NULL, '9492957711');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.dictionary/global-1.2.0.mysql.sql', '2024-02-21 17:27:34', 3, 'EXECUTED', '8:8add33f7cc6e11ee261fd90f7f3acd31', 'sql', '', NULL, '4.3.1', NULL, NULL, '8507654113');

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
-- Table structure for dictionary_item
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `dictionary_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编码',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备注',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `change_notice` varchar(255) DEFAULT NULL COMMENT '变更通知,默认不通知',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `唯一索引`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1797816811400540162 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `sys_dictionary`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
  `variable` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变量',
  `item_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编号',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：启用（ENABLE）、禁用（DISABLE）、删除（DELETE）',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dictionary
-- ----------------------------
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1680784600368635906, 'dictionary', '01', '字典服务编号', 'v1.0.0', NULL, 'all-application-error', '1', '2023-07-13 17:40:31', NULL, '2023-07-13 17:40:31', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1682235800836997121, 'property.management', '02', '资产管理编号', 'v1.0.0', NULL, 'all-application-error', '17311369782', '2023-07-21 11:47:24', '17311369782', '2023-07-21 11:47:24', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1696094549569798146, 'UE5', 'unreal-engine-five', '模型类型-UE5', 'v1.0.0', NULL, 'visualization-modelType', '17311369782', '2023-08-28 17:37:07', '17311369782', '2023-08-28 17:37:07', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1696096274284380161, '二维组态', 'two-dimensional-config', '模型类型-二维组态', 'v1.0.0', NULL, 'visualization-modelType', '17311369782', '2023-08-28 17:43:58', '17311369782', '2023-08-28 17:43:58', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1696427627169284097, 'visualconflg', 'VISUALCONFIG', '可视化中心bucket', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2023-08-29 15:40:39', '17311369782', '2023-08-29 15:40:39', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1698532558298775554, '站内通知', 'CHANNEL_SITE', '站内信消息', 'v1.0.0', NULL, 'message-notice-type', '17311369782', '2023-09-04 11:04:54', '17311369782', '2023-09-04 11:04:54', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1698533983099650049, '短信通知', 'CHANNEL_SMS', '短信消息', 'v1.0.0', NULL, 'message-notice-type', '17311369782', '2023-09-04 11:10:33', '17311369782', '2023-09-04 11:10:33', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1698534106709983234, 'app通知', 'CHANNEL_APP', 'app消息', 'v1.0.0', NULL, 'message-notice-type', '17311369782', '2023-09-04 11:11:03', '17311369782', '2023-09-04 11:11:03', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1698534951103070209, '语音通知', 'CHANNEL_VOICE', '语音通知', 'v1.0.0', NULL, 'message-notice-type', '17311369782', '2023-09-04 11:14:24', '17311369782', '2023-09-04 11:14:24', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798190806354665473, 'alarm', 'alarm', '告警服务报警业务数据', 'v1.0.0', '', 'file-minio-bucket', '17311369782', '2024-06-05 11:11:12', '17311369782', '2024-06-05 11:11:12', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798190877980794882, 'apk', 'apk', 'android app 发版存档', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:11:29', '17311369782', '2024-06-05 11:11:29', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798192119058571265, 'clock-in', 'clock-in', 'app 打卡时存储人脸照片', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:16:25', '17311369782', '2024-06-05 11:16:25', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798192300730654721, 'cnp-ouaa', 'cnp-ouaa', '里面一堆人脸数据', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:17:08', '17311369782', '2024-06-05 11:17:08', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798192346578591745, 'emergency', 'emergency', '预案管理业务数据', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:17:19', '17311369782', '2024-06-05 11:17:19', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798192425817382914, 'maintain', 'maintain', '运维相关服务(基本都是图片)', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:17:38', '17311369782', '2024-06-05 11:17:38', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798192495774179330, 'mdata', 'mdata', '主数据（但是里面创建了很多其它业务数据）', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:17:54', '17311369782', '2024-06-05 11:17:54', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798192571267457025, 'ouaa', 'ouaa', 'ouaa', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:18:12', '17311369782', '2024-06-05 11:18:12', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798192620722495490, 'video', 'video', '安防监控', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:18:24', '17311369782', '2024-06-05 11:18:24', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798196558565777409, 'access-os', 'access-os', '接入层抓拍数据等', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:34:03', '17311369782', '2024-06-05 11:34:03', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798196840842436609, 'alarm-word', 'alarm-word', '给深农需求定制的 word 存储所需的 bucket', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 11:35:10', '17311369782', '2024-06-05 11:35:10', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798232991267594241, 'safety-education', 'safety-education', '安全教育业务桶', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 13:58:49', '17311369782', '2024-06-05 13:58:49', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sys_dictionary`
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES (1798233047609679873, 'safetymanage', 'safetymanage', '安全总部业务桶', 'v1.0.0', NULL, 'file-minio-bucket', '17311369782', '2024-06-05 13:59:03', '17311369782', '2024-06-05 13:59:03', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();


INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763020943662587906, '销假', 'CANCEL_LEAVE', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763021049224830978, '补卡', 'REPAIR_CLOCK', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763021115545165825, '加班', 'OVERTIME', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763021194775568385, '打卡', 'CLOCK_IN', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763021249003724801, '出差', 'AWAY', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763021536447766529, '离岗', 'POST', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763021592760492033, '请假', 'LEAVE', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763021947007213570, '值班', 'DUTY', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763021997934452737, '排班', 'ARRANGE', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO sys_dictionary
(id, name, code, remarks, version, variable, item_code, create_by, create_time, update_by, update_time, status, deleted)
VALUES(1763022043904024577, '换班', 'CHANGE_CLASSES', '考勤应用能力配置', 'v1.0.0', NULL, 'ATTENDANCE_MANAGEMENT', 'admin', '2024-02-29 09:58:43', 'admin', '2024-02-29 09:58:43', 'ENABLE', 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
SET FOREIGN_KEY_CHECKS = 1;

INSERT IGNORE INTO `dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1821083964009410562, 'FILE_CONFIG', '文件服务-租户默认配置', 0, '17311369782', '2024-08-07 15:20:26', '17311369782', '2024-08-07 15:20:26');
INSERT IGNORE INTO `dictionary_item`(`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `change_notice`) VALUES (1848276221907501058, 'SUBSYSTEM_ENDPOINT_CONFIG', '子系统端点配置key', 0, '17311369782', '2024-10-21 16:12:45', '17311369782', '2024-10-21 16:12:45', 'ROCKET_MQ,REDIS');
INSERT IGNORE INTO `dictionary_item`(`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `change_notice`) VALUES (1848276329671266306, 'SUBSYSTEM_ADAPTER_CONFIG', '子系统适配配置key', 0, '17311369782', '2024-10-21 16:13:11', '17311369782', '2024-10-21 16:13:11', 'ROCKET_MQ,REDIS');
INSERT IGNORE INTO `dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `change_notice`) VALUES (1873904881950003201, 'FRONTEND_CONFIG', '前端配置项', 0, 'admin', '2024-12-31 09:31:54', 'admin', '2024-12-31 09:31:54', 'REDIS');
INSERT IGNORE INTO `dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `change_notice`) VALUES (1912858643615715329, 'PUBLIC_URL', '不受鉴权接口', 0, 'admin', '2025-04-17 21:20:14', 'admin', '2025-04-17 21:20:14', 'ROCKET_MQ,REDIS');
INSERT IGNORE INTO `dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `change_notice`) VALUES (1869719340079693826, 'FILE_SCENE_CODE', '文件场景编码', 0, 'admin', '2024-12-19 20:20:03', 'admin', '2024-12-19 20:20:03', 'ROCKET_MQ,REDIS');
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1821085938490601473, 'minio配置', 'minio', '新租户默认文件存储配置', 'v1.0.0', '{\"endpoint\":\"http://192.168.0.103:9091\",\"bucket\":\"guhecloud\",\"accessKeyId\":\"Wfz7n0JEeQJ08clj\",\"accessKeySecret\":\"xG8pr7rwuTtQTvg0XJb6FUPPatoguw09\",\"accessPrefix\":\"/minio\",\"tag\":\"minio\",\"storageObjectType\":\"MINIO\"}', 'FILE_CONFIG', '17311369782', '2024-08-07 15:28:17', '17311369782', '2024-08-07 15:28:17', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1912858750146842625, '根据字典编码查询字典详情', 'SYS_DICTIONARY_BY_CODE_AND_ITEM_CODE', '', 'v1.0.0', '/dictionary/getSysDictionaryByCodeAndItemCode', 'PUBLIC_URL', 'admin', '2025-04-17 21:20:40', 'admin', '2025-04-17 21:20:40', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1869705537998336001, '资产管理_耗材档案', 'PROPERTY_MANAGEMENT::CONSUMABLE::TEST', '', 'v1.0.0', 'property-management', 'FILE_SCENE_CODE', 'admin', '2024-12-19 19:25:12', 'admin', '2024-12-19 20:05:10', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1869709850405548034, '资产管理_资产清单', 'PROPERTY_MANAGEMENT::PROPERTY', '', 'v1.0.0', 'property-management', 'FILE_SCENE_CODE', 'admin', '2024-12-19 19:42:21', 'admin', '2024-12-19 19:42:21', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1879721094460014594, '企业管理_租户信息', 'OUAA::TENANT', '', 'v1.0.0', 'ouaa', 'FILE_SCENE_CODE', 'admin', '2025-01-16 10:43:27', 'admin', '2025-03-20 15:29:57', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1879721195605655554, '企业管理_用户信息', 'OUAA::USER', '', 'v1.0.0', 'ouaa', 'FILE_SCENE_CODE', 'admin', '2025-01-16 10:43:51', 'admin', '2025-03-20 15:29:59', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1879735433837477890, 'APP安装包', 'OUAA::APP_VERSION', '', 'v1.0.0', 'ouaa', 'FILE_SCENE_CODE', 'admin', '2025-01-16 11:40:26', 'admin', '2025-03-20 15:30:01', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1879785704873590786, '小散工程', 'SMALLENFINEERING', '', 'v1.0.0', 'smallenfineering', 'FILE_SCENE_CODE', 'admin', '2025-01-16 15:00:11', 'admin', '2025-03-20 15:30:05', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1879785881567035394, '小散工程流程发起', 'SMALLENFINEERING::FLOW', '', 'v1.0.0', 'smallenfineering', 'FILE_SCENE_CODE', 'admin', '2025-01-16 15:00:54', 'admin', '2025-03-20 15:30:07', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880064373428912130, '通知公告', 'CONTENT::ANNOUNCEMENT', '', 'v1.0.0', 'content', 'FILE_SCENE_CODE', 'admin', '2025-01-17 09:27:31', 'admin', '2025-03-20 15:31:20', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880064453342986241, '建筑资料', 'CONTENT::BUILDING_DATA', '', 'v1.0.0', 'content', 'FILE_SCENE_CODE', 'admin', '2025-01-17 09:27:50', 'admin', '2025-03-20 15:31:22', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880064514584018946, '设备文档', 'CONTENT::DEVICE_DOCUMENT', '', 'v1.0.0', 'content', 'FILE_SCENE_CODE', 'admin', '2025-01-17 09:28:05', 'admin', '2025-03-20 15:31:24', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880155728566091777, '工作汇报', 'WORK_REPORT::FLOW', '', 'v1.0.0', 'report', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:30:32', 'admin', '2025-03-20 15:31:26', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880155821054689282, '工作计划', 'WORK_PLAN', '', 'v1.0.0', 'report', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:30:54', 'admin', '2025-03-20 15:31:28', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880156667519762434, '制度征求流程审批', 'OFFICIAL_REGULATION::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:34:16', 'admin', '2025-03-20 15:31:30', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880156770095661058, '用印申请流程审批', 'OFFICIAL_SEAL::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:34:40', 'admin', '2025-03-20 15:31:32', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880156881072750593, '议题预报流程审批', 'OFFICIAL_TOPIC::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:35:07', 'admin', '2025-01-17 15:35:07', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880157010844516354, '文件传阅流程审批', 'OFFICIAL_DOCUMENT_CIRCULATION::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:35:38', 'admin', '2025-03-20 15:31:34', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880157215492997121, '来文处理流程审批', 'OFFICIAL_DOCUMENT_RECEIVED::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:36:26', 'admin', '2025-01-17 15:36:26', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880158223250030593, '重点工作流程审批', 'OFFICIAL_KEY_WORK::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:40:27', 'admin', '2025-03-20 15:31:36', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880158343366508545, '催办工作流程审批', 'OFFICIAL_URGENT_WORK::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:40:55', 'admin', '2025-03-20 15:31:37', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880158577308008450, '督办事项流程审批', 'OFFICIAL_FOCUS_WORK::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:41:51', 'admin', '2025-03-20 15:31:39', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880158668471205889, '督办事项处理流程审批', 'OFFICIAL_FOCUS_WORK_REPLY::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:42:13', 'admin', '2025-01-17 15:42:51', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880158776810078210, '督办事项更新流程审批', 'OFFICIAL_FOCUS_WORK_UPDATE::FLOW', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:42:39', 'admin', '2025-01-17 15:42:39', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880159757190889473, '督办事项失败原因', 'FOCUS_WORK_FAIL', '', 'v1.0.0', 'office', 'FILE_SCENE_CODE', 'admin', '2025-01-17 15:46:32', 'admin', '2025-01-17 15:46:32', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880490630213861378, '安全教育_课题库', 'SAFETY_EDUCATION::COURSEWARE', '', 'v1.0.0', 'safety-education', 'FILE_SCENE_CODE', 'admin', '2025-01-18 13:41:19', 'admin', '2025-04-02 13:56:51', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880490817825079298, '安全教育_培训记录照片', 'SAFETY_EDUCATION::CULTIVATE_PHOTO', '', 'v1.0.0', 'safety-education', 'FILE_SCENE_CODE', 'admin', '2025-01-18 13:42:03', 'admin', '2025-04-02 13:56:54', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880490986402545666, '安全教育_集团下发', 'SAFETY_EDUCATION::GROUP_PUBLISH', '', 'v1.0.0', 'safety-education', 'FILE_SCENE_CODE', 'admin', '2025-01-18 13:42:44', 'admin', '2025-04-02 13:56:57', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880496659404226561, '洁净医疗', 'MEDICAL_CLEANLINESS', '', 'v1.0.0', 'medical-cleanliness', 'FILE_SCENE_CODE', 'admin', '2025-01-18 14:05:16', 'admin', '2025-07-07 14:31:31', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880501101323157506, '访客管理', 'VISITOR', '', 'v1.0.0', 'visitor', 'FILE_SCENE_CODE', 'admin', '2025-01-18 14:22:55', 'admin', '2025-01-18 14:22:55', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880504766889000961, '安全报告', 'alarm-word', '', 'v1.0.0', 'alarm-word', 'FILE_SCENE_CODE', 'admin', '2025-01-18 14:37:29', 'admin', '2025-01-18 14:37:29', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880541970105434114, '应急设备', 'EMERGENCY::DEVICE', '', 'v1.0.0', 'emergency', 'FILE_SCENE_CODE', 'admin', '2025-01-18 17:05:19', 'admin', '2025-01-18 17:05:19', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880542041194692610, '预案文件', 'EMERGENCY::FILE', '', 'v1.0.0', 'emergency', 'FILE_SCENE_CODE', 'admin', '2025-01-18 17:05:36', 'admin', '2025-01-18 17:05:36', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1880542123361107970, '应急知识', 'EMERGENCY::KNOWLEDGE', '', 'v1.0.0', 'emergency', 'FILE_SCENE_CODE', 'admin', '2025-01-18 17:05:56', 'admin', '2025-01-18 17:05:56', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882326155811946497, '电子巡更提交', 'ELECT_PATROL:SUBMIT_RESULT', '', 'v1.0.0', 'maintain', 'FILE_SCENE_CODE', 'admin', '2025-01-23 15:15:02', 'admin', '2025-03-20 15:31:54', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882326233427542017, '巡检记录提交', 'INSPECTION_RECORD', '', 'v1.0.0', 'maintain', 'FILE_SCENE_CODE', 'admin', '2025-01-23 15:15:21', 'admin', '2025-03-20 15:31:51', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882326304026066946, '故障案例', 'FAULT_ORDER:CASE', '', 'v1.0.0', 'maintain', 'FILE_SCENE_CODE', 'admin', '2025-01-23 15:15:37', 'admin', '2025-03-20 15:31:48', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882326770994708482, '故障单提交', 'FAULT::FLOW', '', 'v1.0.0', 'maintain', 'FILE_SCENE_CODE', 'admin', '2025-01-23 15:17:29', 'admin', '2025-03-20 15:29:32', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882326980412112898, '隐患单提交', 'HIDDENDANGER::FLOW', '', 'v1.0.0', 'maintain', 'FILE_SCENE_CODE', 'admin', '2025-01-23 15:18:19', 'admin', '2025-03-20 15:31:58', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882327134057857026, '投诉建议提交', 'COMPLAINT::FLOW', '', 'v1.0.0', 'maintain', 'FILE_SCENE_CODE', 'admin', '2025-01-23 15:18:55', 'admin', '2025-03-20 15:31:59', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882327240798699522, '更新改造提交', 'RETROFIT::FLOW', '', 'v1.0.0', 'maintain', 'FILE_SCENE_CODE', 'admin', '2025-01-23 15:19:21', 'admin', '2025-03-20 15:32:01', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882327340287590402, '委外维修提交', 'OUTSOURCE_REPAIR::FLOW', '', 'v1.0.0', 'maintain', 'FILE_SCENE_CODE', 'admin', '2025-01-23 15:19:45', 'admin', '2025-03-20 15:32:03', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882625506845196290, '档位信息', 'DWGL::STALL::INFO', 'STALL', 'v1.0.0', '', 'FILE_SCENE_CODE', 'admin', '2025-01-24 11:04:33', 'admin', '2025-03-20 15:32:07', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882715916481396737, '商家信息', 'SJL::SUPPLIER::LOGO', '商家logo', 'v1.0.0', '', 'FILE_SCENE_CODE', 'admin', '2025-01-24 17:03:48', 'admin', '2025-03-20 15:32:09', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1882979589544280066, '人事管理', 'PERSONNELMANAGEMENT', '', 'v1.0.0', 'personnel', 'FILE_SCENE_CODE', 'admin', '2025-01-25 10:31:33', 'admin', '2025-03-20 15:32:11', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1888410769378836481, '电子导览设备logo', 'ELECREONIC_GUIDE::DEVICE::LOGO', '', 'v1.0.0', '', 'FILE_SCENE_CODE', 'admin', '2025-02-09 10:13:07', 'admin', '2025-02-09 10:13:07', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1888411014535905281, '电子导览点位图片', 'ELECREONIC_GUIDE::POINT', '', 'v1.0.0', '', 'FILE_SCENE_CODE', 'admin', '2025-02-09 10:14:06', 'admin', '2025-02-09 10:14:06', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1888411253414100994, '电子导览图层', 'ELECREONIC_GUIDE::LAYER', '', 'v1.0.0', '', 'FILE_SCENE_CODE', 'admin', '2025-02-09 10:15:02', 'admin', '2025-02-09 10:15:02', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1888411453004251137, '电子导览点位分类', 'ELECREONIC_GUIDE::POINT_TYPE', '', 'v1.0.0', '', 'FILE_SCENE_CODE', 'admin', '2025-02-09 10:15:50', 'admin', '2025-02-09 10:15:50', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1888414769377509377, '肉类交易理货图片', 'MEAT_TRADE::PORK_ARRANGE_IMAGE', '', 'v1.0.0', '', 'FILE_SCENE_CODE', 'admin', '2025-02-09 10:29:01', 'admin', '2025-02-09 10:29:01', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1907258777348915201, '报警记录跟进', 'ALARM:RECORD_FOLLOW_UP', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-04-02 10:28:22', 'admin', '2025-04-02 10:28:22', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1907258877550837761, '报警级别播放配置', 'ALARM:LEVEL_PLAY_CONFIG', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-04-02 10:28:46', 'admin', '2025-04-02 10:28:46', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1907269773119504385, '报警处理', 'ALARM:RECORD_HANDLER', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-04-02 11:12:04', 'admin', '2025-04-02 11:12:04', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1907271584131891201, '报警记录挂起', 'ALARM:RECORD_SUSPEND', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-04-02 11:19:16', 'admin', '2025-04-02 11:19:16', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1907280675101261826, '二维码样式', 'QRCODE:STYLE', '', 'v1.0.0', 'qrcode', 'FILE_SCENE_CODE', 'admin', '2025-04-02 11:55:23', 'admin', '2025-04-02 11:55:23', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1907316849324900354, '资产入库', 'PROPERTY_MANAGEMENT::PROPERTY::FLOW', '', 'v1.0.0', 'property-management', 'FILE_SCENE_CODE', 'admin', '2025-04-02 14:19:08', 'admin', '2025-04-02 14:19:08', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1907359327763611649, '业务菜单配置', 'MDATA::BUSINESS_MENU', '', 'v1.0.0', 'mdata', 'FILE_SCENE_CODE', 'admin', '2025-04-02 17:07:55', 'admin', '2025-04-02 17:07:55', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1913105088821391362, '风险源', 'RISK_SOURCE', '', 'v1.0.0', 'property-management', 'FILE_SCENE_CODE', 'admin', '2025-04-18 13:39:32', 'admin', '2025-04-18 13:39:32', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1913105143540281346, '物联中心', 'DEVICECENTER', '', 'v1.0.0', 'maintain', 'FILE_SCENE_CODE', 'admin', '2025-04-18 13:39:45', 'admin', '2025-04-18 13:39:45', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1914122520876871682, '空间管理', 'MDATA::SPACE_LIST', '', 'v1.0.0', 'mdata', 'FILE_SCENE_CODE', 'admin', '2025-04-21 09:02:26', 'admin', '2025-04-21 09:02:26', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1914238645967056897, '空间管理-空间标签', 'MDATA::SPACE_TAG', '', 'v1.0.0', 'mdata', 'FILE_SCENE_CODE', 'admin', '2025-04-21 16:43:53', 'admin', '2025-04-21 16:43:53', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1916753355539017729, '物联中心-设备安装图片', 'DEVICECENTER::DEVICE_INSTALL_PICTURE', '物联中心下设备安装图片', 'v1.0.0', '', 'FILE_SCENE_CODE', 'admin', '2025-04-28 15:16:26', 'admin', '2025-04-28 15:17:31', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1932263680688910337, '知识文档', 'KNOWLEDGE_BASE_FILE', '', 'v1.0.0', 'content', 'FILE_SCENE_CODE', 'admin', '2025-06-10 10:28:56', 'admin', '2025-06-10 10:28:56', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1936997031770439681, '考勤打卡', 'ATTENDANCE_MANAGEMENT::CLOCK_IN', '', 'v1.0.0', 'attendance', 'FILE_SCENE_CODE', 'admin', '2025-06-23 11:57:35', 'admin', '2025-06-23 11:57:35', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1945724059017396225, '消杀资料', 'DISINFECTION::MATERIAL', '', 'v1.0.0', 'disinfection', 'FILE_SCENE_CODE', 'admin', '2025-07-17 13:55:40', 'admin', '2025-07-17 13:57:05', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1949725433912111105, '消杀业务分类图表', 'DISINFECTION::DATA_TYPE', '', 'v1.0.0', 'disinfection', 'FILE_SCENE_CODE', 'admin', '2025-07-28 14:55:42', 'admin', '2025-07-28 14:55:55', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1949725607644377089, '消杀项目文件', 'DISINFECTION::PROJECT', '', 'v1.0.0', 'disinfection', 'FILE_SCENE_CODE', 'admin', '2025-07-28 14:56:24', 'admin', '2025-07-28 14:57:55', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1949725688246317058, '消杀关键场所文件', 'DISINFECTION::KEY_PLACE', '', 'v1.0.0', 'disinfection', 'FILE_SCENE_CODE', 'admin', '2025-07-28 14:56:43', 'admin', '2025-07-28 14:57:50', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1949725850402304002, '消杀任务', 'DISINFECTION::TASK', '', 'v1.0.0', 'disinfection', 'FILE_SCENE_CODE', 'admin', '2025-07-28 14:57:22', 'admin', '2025-07-28 14:57:22', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1949725935244685313, '消杀任务上传完成记录', 'DISINFECTION::UPLOAD_COMPLETION_RECORD', '', 'v1.0.0', 'disinfection', 'FILE_SCENE_CODE', 'admin', '2025-07-28 14:57:42', 'admin', '2025-07-28 14:57:42', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1957333511548526594, '安防监控报警截图', 'SECURITY_MONITORING::ALARM::SNAP_CAPTURE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-18 14:47:29', 'admin', '2025-08-18 14:48:02', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1957333625763618818, '安防监控报警录像', 'SECURITY_MONITORING::ALARM::VIDEOTAPE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-18 14:47:57', 'admin', '2025-08-18 14:48:05', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961352812219686914, '安防监控报警截图', 'SECURITY_MONITORING::ALARM::SNAP_CAPTURE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-29 16:58:45', 'admin', '2025-08-29 16:58:45', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961352921644883969, '安防监控报警录像', 'SECURITY_MONITORING::ALARM::VIDEOTAPE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-29 16:59:11', 'admin', '2025-08-29 16:59:11', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961353906333245441, '消防管理图片抓拍', 'FIRECONTROL::ALARM::SNAP_CAPTURE', '', 'v1.0.0', 'alram', 'FILE_SCENE_CODE', 'admin', '2025-08-29 17:03:06', 'admin', '2025-08-29 17:03:06', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961354131236020225, '消防管理视频录制', 'FIRECONTROL::ALARM::VIDEOTAPE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-29 17:04:00', 'admin', '2025-08-29 17:04:00', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961354255014125570, '动环监控图片截图', 'ROTATINGMANAGE::ALARM::SNAP_CAPTURE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-29 17:04:29', 'admin', '2025-08-29 17:04:29', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961355588806991873, '动换监控视频录制', 'ROTATINGMANAGE::ALARM::VIDEOTAPE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-29 17:09:47', 'admin', '2025-08-29 17:09:47', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961355762543452162, '电气管理图片抓拍', 'ELECTRICMANAGE::ALARM::SNAP_CAPTURE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-29 17:10:29', 'admin', '2025-08-29 17:10:29', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961355852960063490, '电气管理视频录制', 'ELECTRICMANAGE::ALARM::VIDEOTAPE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-29 17:10:50', 'admin', '2025-08-29 17:10:50', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961356034846056449, '用气管理图片抓拍', 'GAS_MANAGEMENT::ALARM::SNAP_CAPTURE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-29 17:11:34', 'admin', '2025-08-29 17:11:34', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1961356204946055169, '用气管理视频录制', 'GAS_MANAGEMENT::ALARM::VIDEOTAPE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-08-29 17:12:14', 'admin', '2025-08-29 17:12:14', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1963495795846660097, '物联网-接入层', 'IOT::ACCESS::EVENT_ATTACHMENTS', '', 'v1.0.0', 'access-os', 'FILE_SCENE_CODE', 'admin', '2025-09-04 14:54:12', 'admin', '2025-09-04 14:54:12', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1967840409683619841, '视频巡逻抓拍', 'CAMERA_PATROL::SNAP_CAPTURE', '', 'v1.0.0', 'alarm', 'FILE_SCENE_CODE', 'admin', '2025-09-16 14:38:09', 'admin', '2025-10-15 21:01:38', 'ENABLE', 0);INSERT IGNORE INTO `dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `change_notice`) VALUES (1873617961050746881, 'APP_BUSINESS_CONFIG', '应用业务配置', 0, 'admin', '2024-12-30 14:31:47', 'admin', '2024-12-30 14:31:47', 'REDIS,ROCKET_MQ');
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1980000000000000001, '重点人员照片', 'KEY::PERSON_SECURITY_KEY_PERSON_PHOTO', '', 'v1.0.0', 'key-person', 'FILE_SCENE_CODE', 'admin', '2026-05-21 00:00:00', 'admin', '2026-05-21 00:00:00', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1980000000000000002, '重点人员附件', 'KEY::PERSON_SECURITY_KEY_PERSON_ATTACHMENT', '', 'v1.0.0', 'key-person', 'FILE_SCENE_CODE', 'admin', '2026-05-21 00:00:00', 'admin', '2026-05-21 00:00:00', 'ENABLE', 0);
INSERT IGNORE INTO `sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`, `item_code`, `create_by`, `create_time`, `update_by`, `update_time`, `status`, `deleted`) VALUES (1980000000000000003, '重点人员监管上报附件', 'KEY::PERSON_SECURITY_KEY_PERSON_REPORT_ATTACHMENT', '', 'v1.0.0', 'key-person', 'FILE_SCENE_CODE', 'admin', '2026-05-21 00:00:00', 'admin', '2026-05-21 00:00:00', 'ENABLE', 0);
INSERT IGNORE INTO `data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1906595396375855106, '考勤能力配置', 'ATTENDANCE_MANAGEMENT_ABILITY', '', 58197260, 'v1.0.0', '{\n  \"CANCEL_LEAVE\": {\n    \"name\": \"销假\",\n    \"value\": true\n  },\n  \"REPAIR_CLOCK\": {\n    \"name\": \"补卡\",\n    \"value\": true\n  },\n  \"OVERTIME\": {\n    \"name\": \"加班\",\n    \"value\": true\n  },\n  \"CLOCK_IN\": {\n    \"name\": \"打卡\",\n    \"value\": true\n  },\n  \"AWAY\": {\n    \"name\": \"出差\",\n    \"value\": true\n  },\n  \"POST\": {\n    \"name\": \"离岗\",\n    \"value\": true\n  },\n  \"LEAVE\": {\n    \"name\": \"请假\",\n    \"value\": true\n  },\n  \"DUTY\": {\n    \"name\": \"值班\",\n    \"value\": true\n  },\n  \"ARRANGE\": {\n    \"name\": \"排班\",\n    \"value\": true\n  },\n  \"CHANGE_CLASSES\": {\n    \"name\": \"换班\",\n    \"value\": true\n  }\n}', 'APP_BUSINESS_CONFIG', 0, 'admin', '2025-03-31 14:32:20', 'admin', '2025-03-31 14:32:20', 'ENABLE');
