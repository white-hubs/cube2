CREATE DATABASE IF NOT EXISTS`cube-biz-door` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-biz-door`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for access_event
-- ----------------------------

CREATE TABLE IF NOT EXISTS `access_event`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用于存放第三方的ID',
  `event_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件类型',
  `event_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件类型名称',
  `event_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件名称',
  `event_time` datetime NULL DEFAULT NULL COMMENT '事件时间',
  `person_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员ID',
  `person_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员姓名',
  `card_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `device_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `org_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织名称',
  `alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '别名',
  `access_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问记录类型 (梯控TK 可视对讲KSDJ)',
  `tenant` int(20) NULL DEFAULT NULL,
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '目前用于 海康的梯控和可视对讲事件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of access_event
-- ----------------------------

-- ----------------------------
-- Table structure for card
-- ----------------------------

CREATE TABLE IF NOT EXISTS `card`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `card_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '卡号',
  `person_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '持卡人ID',
  `person_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '持卡人姓名',
  `use_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用状态 NORMAL 正常 LOSS 挂失',
  `start_date` datetime NULL DEFAULT NULL COMMENT '生效日期',
  `end_date` datetime NULL DEFAULT NULL COMMENT '失效日期',
  `loss_date` datetime NULL DEFAULT NULL COMMENT '挂失开始日期',
  `unloss_date` datetime NULL DEFAULT NULL COMMENT '解除挂失日期',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡描述',
  `card_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡类型',
  `card_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡类型名称',
  `org_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `is_bind_finger` tinyint(1) NULL DEFAULT NULL COMMENT '是否绑定指纹',
  `is_bind_face` tinyint(1) NULL DEFAULT NULL COMMENT '是否绑定人脸',
  `tenant` int(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cardno_index`(`card_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of card
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
    'ALTER TABLE `cube-biz-door`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.door/changelog-1.1.2-1.mysql.sql', '2024-04-23 01:27:38', 1, 'EXECUTED', '8:afd65bf8c0013ce132c141c106c6764a', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806858184');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.door/changelog-1.1.3-0.mysql.sql', '2024-05-27 20:25:48', 2, 'EXECUTED', '8:9202bda7effcf6f121e6eb4cb56123f1', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812748419');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.door/changelog-1.1.3-1.mysql.sql', '2024-05-27 20:25:49', 3, 'EXECUTED', '8:c551f0a878208ecb2b4954e353ceea2d', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812749069');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.door/changelog-1.1.5-1.mysql.sql', '2024-05-27 20:25:51', 4, 'EXECUTED', '8:766d4f7326a5b820dc16742aba85a1fc', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812749680');

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
-- Table structure for door_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `door_device`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `door_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `type` bigint(20) NOT NULL COMMENT '门禁类型',
  `door_model` bigint(20) NOT NULL COMMENT '型号',
  `door_area` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门禁区域',
  `door_area_ids` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域ids',
  `business_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务系统冗余id',
  `out_business_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方系统key',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of door_device
-- ----------------------------

-- ----------------------------
-- Table structure for door_monitor
-- ----------------------------

CREATE TABLE IF NOT EXISTS `door_monitor`  (
  `id` bigint(20) NOT NULL,
  `monitor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控信息',
  `monitor_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组态链接',
  `tenant` int(10) NULL DEFAULT NULL,
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
-- Records of door_monitor
-- ----------------------------

-- ----------------------------
-- Table structure for door_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `door_type`  (
  `id` bigint(20) NOT NULL,
  `door_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门禁类型名称',
  `tenant` int(10) NULL DEFAULT NULL,
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
-- Records of door_type
-- ----------------------------
INSERT INTO `door_type`
(id, door_type_name, tenant, create_by, create_time, update_by, update_time, deleted)
VALUES (1, '人脸识别', 58197260, 'admin', '2022-11-14 15:32:46', NULL, NULL, 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `door_type`
(id, door_type_name, tenant, create_by, create_time, update_by, update_time, deleted)
VALUES (2, '刷卡', 58197260, 'admin', '2022-11-14 15:33:04', NULL, NULL, 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `door_type`
(id, door_type_name, tenant, create_by, create_time, update_by, update_time, deleted)
VALUES (3, '出入口道闸', 58197260, 'admin', '2022-11-14 15:33:04', NULL, NULL, 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `door_type`
(id, door_type_name, tenant, create_by, create_time, update_by, update_time, deleted)
VALUES (4, '指纹识别', 58197260, 'admin', '2022-11-21 15:32:46', NULL, NULL, 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `door_type`
(id, door_type_name, tenant, create_by, create_time, update_by, update_time, deleted)
VALUES (5, '刷卡', 58197260, 'admin', '2022-11-14 15:33:04', NULL, NULL, 0) ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for event_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `event_record`  (
  `id` bigint(20) NOT NULL,
  `event_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件名称',
  `event_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件id',
  `event_time` datetime NULL DEFAULT NULL COMMENT '事件时间',
  `person_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员id',
  `person_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员名称',
  `door_index_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门禁点编码',
  `door_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门禁点名称',
  `event_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件类型',
  `in_and_out_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '进出类型',
  `event_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件结果',
  `event_way` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方式',
  `card_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `picture_file_id` bigint(20) NULL DEFAULT NULL COMMENT '抓拍图片文件ID',
  `tenant` int(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `eventId`(`event_id`) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of event_record
-- ----------------------------

-- ----------------------------
-- Table structure for org
-- ----------------------------

CREATE TABLE IF NOT EXISTS `org`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `org_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织机构代码',
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织机构名称',
  `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级组织机构的ID',
  `org_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织机构路径',
  `tenant` int(20) NULL DEFAULT NULL,
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
-- Records of org
-- ----------------------------

-- ----------------------------
-- Table structure for record_time
-- ----------------------------

CREATE TABLE IF NOT EXISTS `record_time`  (
  `id` bigint(20) NOT NULL,
  `end_time` datetime NULL DEFAULT NULL COMMENT '采集最后时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `tenant` int(10) NULL DEFAULT NULL,
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
-- Records of record_time
-- ----------------------------

-- ----------------------------
-- Table structure for type_manage
-- ----------------------------

CREATE TABLE IF NOT EXISTS `type_manage`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type_code` int(10) NULL DEFAULT NULL,
  `type_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of type_manage
-- ----------------------------
INSERT IGNORE INTO `type_manage` VALUES (1, '认证成功等待远程开门', 197390, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (2, '卡不属于多重认证群组', 197391, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (3, '常关时段开始', 198148, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (4, '常关时段结束', 198150, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (5, '开门超时', 198400, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (6, '常开时段开始', 198401, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (7, '常开时段结束', 198402, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (8, '进入首卡常开状态', 198404, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (9, '结束首卡常开状态', 198405, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (10, '门被外力开启', 198657, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (11, '正常开门', 198913, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (12, '按钮开门', 198916, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (13, '远程软件开门', 198919, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (14, '远程软件常开', 198922, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (15, '远程软件常闭', 199172, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (16, '正常关门', 199169, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (17, '远程软件关门', 199171, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (18, '门锁打开', 199941, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (19, '门锁关闭', 199942, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (20, '读卡器防拆报警', 200453, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (21, '读卡器防拆报警恢复', 200454, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (22, '读卡器掉线报警', 200455, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (23, '读卡器掉线报警恢复', 200456, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (24, '设备防拆报警', 199708, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (25, '设备防拆恢复', 199709, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (26, '装置离线', 199710, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (27, '装置连线', 199711, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (28, '重新启动', 199712, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (29, '系统初始化', 199713, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (30, '数据被损坏', 199714, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (31, '呼叫中心（1T500专有）', 199715, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (32, '刷卡+指纹认证通过', 196885, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (33, '指纹+密码认证通过', 196887, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (34, '合法卡比对通过', 198914, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (35, '刷卡+密码认证通过', 198915, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (36, '密码错误', 197121, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (37, '输入卡号错误', 197122, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (38, '输入密码而非卡号', 197123, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (39, '卡号长度错误', 197124, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (40, '卡号数字检查错误', 197125, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (41, '无此卡号', 197634, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (42, '卡号过期', 197633, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (43, '刷卡+密码不开门', 198145, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (44, '卡未分配权限', 197635, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (45, '刷卡+指纹+密码通过', 196886, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (46, '工号+密码认证通过', 196897, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (47, '指纹比对通过', 197127, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (48, '指纹比对失败', 197128, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (49, '刷卡+指纹+密码失败', 197136, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (50, '刷卡+指纹+密码超时', 197137, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (51, '指纹不存在', 197140, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (52, '工号+密码认证失败', 197158, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (53, '人脸+指纹认证通过', 196888, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (54, '人脸+指纹认证失败', 197141, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (55, '人脸+指纹认证超时', 197142, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (56, '人脸+密码认证通过', 196889, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (57, '人脸+密码认证失败', 197143, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (58, '人脸+密码认证超时', 197144, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (59, '人脸+刷卡认证通过', 196890, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (60, '人脸+刷卡认证失败', 197145, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (61, '人脸+刷卡认证超时', 197146, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (62, '人脸+密码+指纹认证通过', 196891, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (63, '人脸+密码+指纹认证失败', 197147, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (64, '人脸+密码+指纹认证超时', 197148, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (65, '人脸+刷卡+指纹认证通过', 196892, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (66, '人脸+刷卡+指纹认证失败', 197149, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (67, '人脸+刷卡+指纹认证超时', 197150, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (68, '人脸认证通过', 196893, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (69, '人脸认证失败', 197151, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (70, '人脸识别失败', 197160, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (71, '真人检测失败', 197161, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (72, '人证比对通过', 197162, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (73, '人证比对失败', 197163, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (74, '首卡比对通过', 196874, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (75, '残疾人卡比对通过', 196875, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (76, '多重认证成功', 196883, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (77, '多重认证超级密码成功', 196884, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (78, '巡查卡比对通过', 198918, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (79, '超级卡比对通过', 198921, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (80, '胁迫卡比对通过', 199425, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (81, '反潜回认证失败', 197383, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (82, '卡不在多重认证时段内', 197392, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (83, '多重认证超级密码错误', 197393, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (84, '多重认证远程认证失败', 197394, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (85, '残疾人卡未分配权限', 197395, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (86, '胁迫卡未分配权限', 197396, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (87, '超级卡未分配权限', 197397, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (88, '多重认证重复认证', 197400, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (89, '多重认证超时', 197401, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (90, '互锁中无法开门', 198146, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (91, '反潜回读卡器刷卡无效', 198149, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (92, '巡查卡无效时段', 197399, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (93, '互锁门未关闭', 198658, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (94, '黑名单事件', 197889, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (95, '时段组错误', 197384, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (96, '假期权限不合', 197378, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (97, '权限不合', 197377, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (98, '指纹+密码认证失败', 197138, 'DO_REFUSE');
INSERT IGNORE INTO `type_manage` VALUES (99, '刷卡+指纹认证失败', 197134, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (100, '卡号认证超次报警', 199429, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (101, '解除警报', 199681, 'DO_PASS');
INSERT IGNORE INTO `type_manage` VALUES (102, '胁迫报警', 199428, 'DO_PASS');

-- ----------------------------
-- Table structure for visiting_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `visiting_record`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `receptionist_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被访人',
  `visitor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来访人',
  `phone_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来访人电话',
  `plate_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来访车牌号',
  `visit_start_time` datetime NULL DEFAULT NULL COMMENT '来访时间',
  `visit_end_time` datetime NULL DEFAULT NULL COMMENT '离开时间',
  `visit_purpose` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来访事由',
  `card_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `visitor_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来访状态 (OVERTIME_AUTO_LEAVE：超期自动签离 LEAVE：已签离 OVERTIME_NOT_LEAVE：超期未签离 ARRIVED：已到达)',
  `visitor_status_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来访状态名称',
  `tenant` int(20) NULL DEFAULT NULL,
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
-- Records of visiting_record
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
