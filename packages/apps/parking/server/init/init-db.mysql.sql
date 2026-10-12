CREATE DATABASE IF NOT EXISTS`cube-srv-gen-subsystem-parking` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-srv-gen-subsystem-parking`;
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
    'ALTER TABLE `cube-srv-gen-subsystem-parking`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.subsystem.parking/test-0.0.1.mysql.sql', '2024-05-09 18:24:03', 1, 'EXECUTED', '8:712b01aa463d05eb0eb812d05cad4eff', 'sql', '', NULL, '4.3.1', NULL, NULL, '5250243723');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.subsystem.parking/changelog-1.0.6.mysql.sql', '2024-05-27 20:25:48', 2, 'EXECUTED', '8:3b8ddfa908e9e9564da109232573df3d', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812747628');

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
-- Table structure for vehicle_info
-- ----------------------------

CREATE TABLE IF NOT EXISTS `vehicle_info`  (
  `id` bigint(20) NOT NULL,
  `vehicle_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `vehicle_type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车辆类型编号',
  `vehicle_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车辆类型名称',
  `customer_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车主姓名',
  `customer_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车主电话',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车辆信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vehicle_info
-- ----------------------------


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for parking_access_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `parking_access_record`  (
  `id` bigint(20) NOT NULL,
  `identify_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通行唯一标识',
  `vehicle_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `access_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出入标识',
  `channel_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道号',
  `channel_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道名称',
  `access_time` datetime NULL DEFAULT NULL COMMENT '通行时间',
  `image_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片路径',
  `image_base` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片base64',
  `parking_info_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车场编号',
  `parking_info_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车场名称',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车辆通行记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for parking_info
-- ----------------------------
CREATE TABLE IF NOT EXISTS `parking_info`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车场编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车场名称',
  `total_num` int(11) NULL DEFAULT NULL COMMENT '车位总数',
  `total_in_num` int(11) NULL DEFAULT NULL COMMENT '车位使用总数',
  `total_space_num` int(11) NULL DEFAULT NULL COMMENT '剩余总数',
  `parent_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车场信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for parking_info_area
-- ----------------------------

CREATE TABLE IF NOT EXISTS `parking_info_area`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `parking_info_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车场编号',
  `area` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存多级,以逗号隔开,方便后续查询',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车场所属区域' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for parking_pay_convert
-- ----------------------------

CREATE TABLE IF NOT EXISTS `parking_pay_convert`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式code',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式名称',
  `pay_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其他渠道支付编码',
  `pay_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其他渠道支付名称',
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
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for parking_pay_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `parking_pay_record`  (
  `id` bigint(20) NOT NULL,
  `identify_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通行唯一标识',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '缴费时间',
  `vehicle_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `parking_times` int(11) NULL DEFAULT NULL COMMENT '停车时长',
  `pay_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收费方式',
  `receivable_amount` decimal(18, 2) NULL DEFAULT NULL COMMENT '应收金额',
  `free_amount` decimal(18, 2) NULL DEFAULT NULL COMMENT '实收金额',
  `discount_amount` decimal(18, 2) NULL DEFAULT NULL COMMENT '优惠金额',
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_identify_id_tenant_deleted`(`identify_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
