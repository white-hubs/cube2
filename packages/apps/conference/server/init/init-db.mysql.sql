CREATE DATABASE IF NOT EXISTS`cube-srv-gen-conferencemanage` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube-srv-gen-conferencemanage`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appointment_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `appointment_record`  (
  `tenant` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `conference_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会议室',
  `operate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作',
  `department_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  `time_interval` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时段',
  `conference_device` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会议设备',
  `appointment_date` date NULL DEFAULT NULL COMMENT '预约日期',
  `goods_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会议物品id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预约记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of appointment_record
-- ----------------------------

-- ----------------------------
-- Table structure for book_interval_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `book_interval_record`  (
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户号',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `time_interval` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时段',
  `book_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预约id',
  `appointment_date` date NULL DEFAULT NULL COMMENT '预约日期',
  `conference_id` bigint(20) NULL DEFAULT NULL COMMENT '会议室id',
  `deleted` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预约时段记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book_interval_record
-- ----------------------------

-- ----------------------------
-- Table structure for book_room
-- ----------------------------

CREATE TABLE IF NOT EXISTS `book_room`  (
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户号',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `id` bigint(20) NOT NULL,
  `book_num` int(10) NULL DEFAULT NULL COMMENT '预约人数',
  `content` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `time_interval` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时段',
  `subscribe_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预约人id',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预约状态',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除表示',
  `conference_id` bigint(20) NULL DEFAULT NULL COMMENT '会议id',
  `end_book_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预约结束时间',
  `conference_theme` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会议主题',
  `conference_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会议类型',
  `goods_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会议物品id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预约表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book_room
-- ----------------------------

-- ----------------------------
-- Table structure for book_time_interval
-- ----------------------------

CREATE TABLE IF NOT EXISTS `book_time_interval`  (
  `id` bigint(20) NOT NULL,
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户号',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  `book_id` bigint(20) NULL DEFAULT NULL COMMENT '预约id',
  `time_interval` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '时段',
  `conference_id` bigint(20) NULL DEFAULT NULL COMMENT '会议室id',
  `book_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预约时段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book_time_interval
-- ----------------------------

-- ----------------------------
-- Table structure for conference
-- ----------------------------

CREATE TABLE IF NOT EXISTS `conference`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'name 会议室名称（20个字符以内的中英文）',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT 'area_id 所属区域',
  `area_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'area_name 所属区域',
  `capacity` int(11) NULL DEFAULT NULL COMMENT 'capacity 可容纳人数',
  `period` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'period 可预约时间段',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT 'status 使用状态(0默认开启)',
  `administrators` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT 'deleted',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL COMMENT 'tenant',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'meeting 会议表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of conference
-- ----------------------------

-- ----------------------------
-- Table structure for conference_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `conference_device`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `conference_id` bigint(20) NULL DEFAULT NULL COMMENT 'conference_id 会议室id',
  `device_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编码',
  `device_model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备型号',
  `device_brand` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备品牌',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT 'device_id 设备id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'name 设备名称',
  `area_id` bigint(20) NULL DEFAULT NULL,
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属区域',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'type 产品分类',
  `deleted` int(11) NULL DEFAULT NULL COMMENT 'deleted',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tenant',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'device 设备表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of conference_device
-- ----------------------------

-- ----------------------------
-- Table structure for conference_goods
-- ----------------------------

CREATE TABLE IF NOT EXISTS `conference_goods`  (
  `id` bigint(20) NOT NULL,
  `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物品名称',
  `illustrate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域id',
  `area_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  `tenant` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会议物品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of conference_goods
-- ----------------------------

-- ----------------------------
-- Table structure for conference_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `conference_type`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` int(10) NULL DEFAULT NULL COMMENT '状态',
  `tenant` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of conference_type
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
    'ALTER TABLE `cube-srv-gen-conferencemanage`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.conferencemanage/test-0.0.1.mysql.sql', '2024-04-23 01:27:43', 1, 'EXECUTED', '8:d087f88ccf4fc36546ab06460f8d1942', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806863369');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.conferencemanage/changelog-1.1.0-1.mysql.sql', '2024-04-23 01:27:43', 2, 'EXECUTED', '8:13229c71bdd0e38a164dda394dfad47b', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806863421');
INSERT IGNORE INTO `databasechangelog` VALUES ('ddl-1', 'zengyadong', 'classpath:db/com.guhecloud.cube.srv.gen.conferencemanage/changelog-1.2.0-1.mysql.sql', '2024-05-27 20:25:54', 3, 'EXECUTED', '8:82175dba951d5f718cbef25f28b15379', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812754358');
INSERT IGNORE INTO `databasechangelog` VALUES ('insert-2', 'zengyadong', 'classpath:db/com.guhecloud.cube.srv.gen.conferencemanage/changelog-1.2.0-1.mysql.sql', '2024-05-27 20:25:55', 4, 'EXECUTED', '8:8ac8d7c4b929da73e3d9029552c0e2f1', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812754358');
INSERT IGNORE INTO `databasechangelog` VALUES ('insert-3', 'zengyadong', 'classpath:db/com.guhecloud.cube.srv.gen.conferencemanage/changelog-1.2.0-1.mysql.sql', '2024-05-27 20:25:56', 5, 'EXECUTED', '8:8e1b5a8802be0912c87d68e49a8157d3', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812754358');

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

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS `field_configuration` (
                                       `id` BIGINT ( 20 ) NOT NULL,
                                       `field_code` VARCHAR ( 255 ) DEFAULT NULL COMMENT '字段编码',
                                       `field_name` VARCHAR ( 255 ) DEFAULT NULL COMMENT '字段名称',
                                       `required` BIGINT ( 20 ) DEFAULT NULL COMMENT '是否必输',
                                       `tenant` VARCHAR ( 255 ) DEFAULT NULL,
                                       `create_by` VARCHAR ( 32 ) DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_by` VARCHAR ( 32 ) DEFAULT NULL COMMENT '更新人',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `deleted` VARCHAR ( 255 ) DEFAULT NULL,
                                       PRIMARY KEY ( `id` )
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '字段必输配置';

INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123103815294978, 'conferenceType', '会议类型', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-19 10:22:19', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123320803418114, 'conferenceTheme', '会议主题', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123396611268609, 'goodsNames', '会议物品', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123845573689346, 'bookNum', '参会人数', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1814123917011075073, 'content', '会议内容', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
INSERT IGNORE INTO `cube-srv-gen-conferencemanage`.`field_configuration` (`id`, `field_code`, `field_name`, `required`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, 'cancelReason', '取消原因', 1, '58197260', '17311369782', '2024-07-18 19:59:39', '17311369782', '2024-07-18 19:59:39', '0');
