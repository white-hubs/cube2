CREATE DATABASE IF NOT EXISTS`deliver-project-merchant` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `deliver-project-merchant`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appeal_description
-- ----------------------------

CREATE TABLE IF NOT EXISTS `appeal_description`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '诉求类型',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '诉求描述表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appeal_description
-- ----------------------------

-- ----------------------------
-- Table structure for area
-- ----------------------------

CREATE TABLE IF NOT EXISTS `area`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父级区域id',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目ID',
  `bim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'BIM模型ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域编码',
  `acreage` double(10, 2) NULL DEFAULT NULL COMMENT '区域面积',
  `level` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域级别',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域类型（楼栋、室外）',
  `sort` int(4) NOT NULL COMMENT '排序',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `building_area` float(10, 0) NULL DEFAULT 0 COMMENT '建筑面积',
  `cover_area` float(10, 0) NULL DEFAULT 0 COMMENT '占地面积',
  `lease_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁面积',
  `lease_real_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁实际面积',
  `lease_public_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁公摊面积',
  `fire_area` float(10, 0) NULL DEFAULT 0 COMMENT '消防面积',
  `clean_area` float(10, 0) NULL DEFAULT 0 COMMENT '清洁面积',
  `parking_area` float(10, 0) NULL DEFAULT 0 COMMENT '车位面积',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
  `tenant` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '区域表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of area
-- ----------------------------

-- ----------------------------
-- Table structure for assembly_notice
-- ----------------------------

CREATE TABLE IF NOT EXISTS `assembly_notice`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `msg_id` bigint(20) NULL DEFAULT NULL COMMENT '消息id',
  `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息配置json',
  `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知消息配置' ROW_FORMAT = Dynamic;

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
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

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
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of assembly_notice_user
-- ----------------------------

-- ----------------------------
-- Table structure for company
-- ----------------------------

CREATE TABLE IF NOT EXISTS `company`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商铺名称',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商铺编号',
  `purpose` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商铺用途',
  `owner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属人姓名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属人电话',
  `start_time` date NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` date NULL DEFAULT NULL COMMENT '结束时间',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `introduction` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户经营信息简介',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
  `security_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安全协议',
  `training_photo_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安全培训照片',
  `company_photo_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户灭火器/消防栓',
  `long_term` tinyint(1) NULL DEFAULT 0 COMMENT '营业期限是否长期(true长期',
  `business_license_file` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照图片',
  `expiration_notice` tinyint(1) UNSIGNED ZEROFILL NOT NULL DEFAULT 0 COMMENT '到期通知(true表示已经通知过',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pid`(`project_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商铺表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company
-- ----------------------------

-- ----------------------------
-- Table structure for company_user
-- ----------------------------

CREATE TABLE IF NOT EXISTS `company_user`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '项目ID',
  `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户ID',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `owner` int(1) NOT NULL DEFAULT 0 COMMENT '是否档主',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商铺-用户关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_user
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
    'ALTER TABLE `deliver-project-merchant`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.deliver.project.merchant/global-1.0.0.mysql.sql', '2023-10-07 03:16:22', 1, 'EXECUTED', '8:1c2e1ff3686bdc54f8322bdd99ecca6f', 'sql', '', NULL, '4.3.1', NULL, NULL, '6662981578');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-1', 'chenhongxin', 'classpath:db/com.guhecloud.cube.deliver.project.merchant/changlog-v1.0.3.mysql.sql', '2024-06-24 09:42:53', 2, 'EXECUTED', '8:ccbfacb7f687197c7725e99fdcd2bad2', 'sql', '', NULL, '4.3.1', NULL, NULL, '9193373366');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-2', 'chenhongxin', 'classpath:db/com.guhecloud.cube.deliver.project.merchant/changlog-v1.0.3.mysql.sql', '2024-06-24 09:42:53', 3, 'EXECUTED', '8:b16142112a4327f6ac6037a7080086f5', 'sql', '', NULL, '4.3.1', NULL, NULL, '9193373366');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-3', 'chenhongxin', 'classpath:db/com.guhecloud.cube.deliver.project.merchant/changlog-v1.0.3.mysql.sql', '2024-06-24 09:42:53', 4, 'EXECUTED', '8:d0f8e6a4b1098db8077db570c3fb9927', 'sql', '', NULL, '4.3.1', NULL, NULL, '9193373366');

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
-- Table structure for device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `area_id3` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '三级区域ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `area_id1` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '一级区域ID',
  `area_id2` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '二级区域ID',
  `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型ID',
  `model` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备型号',
  `iot_device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联网设备ID',
  `iot_device_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联网设备类型',
  `bim_model_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'BIM模型ID',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称 ',
  `pinyin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '首字母',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态（正常、故障、报废 ...）',
  `online` int(1) NOT NULL DEFAULT 0 COMMENT '在离线状态',
  `manual_maint` date NULL DEFAULT NULL COMMENT '手动维护数据日期',
  `price` double(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `install_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装地址',
  `install_date` date NULL DEFAULT NULL COMMENT '安装时间',
  `start_date` date NULL DEFAULT NULL COMMENT '启用时间',
  `expire_date` date NULL DEFAULT NULL COMMENT '到期时间',
  `production_date` date NULL DEFAULT NULL COMMENT '生产时间',
  `time_limit` int(10) NULL DEFAULT NULL COMMENT '有效期',
  `maint_cycle` int(11) NULL DEFAULT 0 COMMENT '维保周期（天）',
  `maint_date` datetime NULL DEFAULT NULL COMMENT '上次维保时间',
  `manufacturer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产厂家',
  `specification` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `dept_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理部门ID',
  `contact_person` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人员',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `contact_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系地址',
  `qr_code` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二维码图',
  `gis_longitude` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'gis经度',
  `gis_latitude` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'gis纬度',
  `account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备登录账号',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备登录密码',
  `ip_addr` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `port` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '端口',
  `videobox_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联视频分析盒子ID',
  `token` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'token信息',
  `finance_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财政编码',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `nearby_cams` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附近摄像头',
  `visibility` int(1) NULL DEFAULT 0 COMMENT '可见性（摄像头设备针对外部市场人员）',
  `expire_change` tinyint(1) NULL DEFAULT NULL COMMENT 'true设备到期更换过',
  `monitoring_object` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '监控对象',
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0代表存在，1代表已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device
-- ----------------------------

-- ----------------------------
-- Table structure for device_elevator_property
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_elevator_property`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ID',
  `floor_num` int(11) NOT NULL DEFAULT 0 COMMENT '层数',
  `station_num` int(11) NOT NULL DEFAULT 0 COMMENT '站数',
  `door_num` int(11) NOT NULL DEFAULT 0 COMMENT '门数',
  `load` int(11) NOT NULL DEFAULT 0 COMMENT '载重',
  `speed` float NOT NULL DEFAULT 0 COMMENT '速度',
  `year_inspect_date` datetime NULL DEFAULT NULL COMMENT '年检日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电梯设备特有属性' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_elevator_property
-- ----------------------------

-- ----------------------------
-- Table structure for device_logs
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_logs`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目id',
  `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `device_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '昵称',
  `dept_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门id',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名',
  `time` datetime NOT NULL COMMENT '时间',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备修改日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_logs
-- ----------------------------

-- ----------------------------
-- Table structure for device_notice
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_notice`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知ID',
  `business_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型',
  `days` int(10) NOT NULL COMMENT '还有多少天到期通知，负数就表示过期多少天',
  `site` int(1) NOT NULL DEFAULT 0 COMMENT '站内信',
  `sms` int(1) NOT NULL DEFAULT 0 COMMENT '短信',
  `wx` int(1) NOT NULL DEFAULT 0 COMMENT '微信',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备相关通知（目前只有设备到期）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_notice
-- ----------------------------

-- ----------------------------
-- Table structure for device_notice_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_notice_config`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `notice_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知ID',
  `notice_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知对象类型（用户、部门、岗位、部门岗位）',
  `user_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的用户IDS',
  `dept_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的部门IDS',
  `post_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的岗位IDS',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备相关通知人员配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_notice_config
-- ----------------------------

-- ----------------------------
-- Table structure for device_online_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_online_record`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ID',
  `online` tinyint(1) NOT NULL COMMENT '上线/离线',
  `date` datetime NOT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备上下线记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_online_record
-- ----------------------------

-- ----------------------------
-- Table structure for device_threshold_value
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_threshold_value`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '阈值模板名称',
  `device_id_str` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '设备IDS',
  `dygxsz` int(10) NULL DEFAULT NULL COMMENT '电压高限',
  `dydxsz` int(10) NULL DEFAULT NULL COMMENT '电压低限',
  `dlgxsz` int(10) NULL DEFAULT NULL COMMENT '电流高限',
  `glgxsz` int(10) NULL DEFAULT NULL COMMENT '功率高限',
  `ldlgxsz` int(10) NULL DEFAULT NULL COMMENT '漏电流高限',
  `wdgxsz` int(10) NULL DEFAULT NULL COMMENT '温度高限',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备阈值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_threshold_value
-- ----------------------------

-- ----------------------------
-- Table structure for device_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_type`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '父ID',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代码',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `validity_months` int(10) NULL DEFAULT NULL COMMENT '有效期（月）',
  `sort` int(4) NOT NULL COMMENT '排序',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_type
-- ----------------------------

-- ----------------------------
-- Table structure for device_videobox
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_videobox`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型外键(数据字典)',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称 ',
  `production_date` date NULL DEFAULT NULL COMMENT '生产时间',
  `manufacturer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产厂家',
  `specification` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `contact_person` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人员',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `contact_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系地址',
  `live_action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实时场景',
  `purchase_contract` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购合同',
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0代表存在，1代表已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备（智能视频分析盒）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device_videobox
-- ----------------------------

-- ----------------------------
-- Table structure for envmodel_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `envmodel_config`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `x` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `y` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `z` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `angle` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '旋转角度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '环境模型配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of envmodel_config
-- ----------------------------

-- ----------------------------
-- Table structure for faq
-- ----------------------------

CREATE TABLE IF NOT EXISTS `faq`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `question` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '常见问题',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  `answer` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '答案',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_day` date NULL DEFAULT NULL COMMENT '创建天',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '常见问题解答' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of faq
-- ----------------------------

-- ----------------------------
-- Table structure for faq_copy1
-- ----------------------------

CREATE TABLE IF NOT EXISTS `faq_copy1`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `question` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '常见问题',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  `answer` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '答案',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_day` date NULL DEFAULT NULL COMMENT '创建天',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '常见问题解答' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of faq_copy1
-- ----------------------------

-- ----------------------------
-- Table structure for fireinsp_item_evaluation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `fireinsp_item_evaluation`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `standard_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标准ID',
  `standard_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标准名',
  `item_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项ID',
  `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项名称',
  `item_result_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项结果编号',
  `item_result_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项结果名',
  `item_result_mark` int(3) NOT NULL DEFAULT 0 COMMENT '项结果扣分',
  `enabled` int(1) NOT NULL DEFAULT 0 COMMENT '启停状态',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检项评价配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fireinsp_item_evaluation
-- ----------------------------

-- ----------------------------
-- Table structure for firm_manage
-- ----------------------------

CREATE TABLE IF NOT EXISTS `firm_manage`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的项目ID',
  `project_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '关联的项目名',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公司名称',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '填报url地址',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公司管理-用于安全月报' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of firm_manage
-- ----------------------------

-- ----------------------------
-- Table structure for history_area
-- ----------------------------

CREATE TABLE IF NOT EXISTS `history_area`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `object_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对象类型(区域、空间)',
  `object_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对象ID',
  `building_area` float(10, 0) NULL DEFAULT 0 COMMENT '建筑面积',
  `cover_area` float(10, 0) NULL DEFAULT 0 COMMENT '占地面积',
  `lease_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁面积',
  `lease_real_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁实际面积',
  `lease_public_area` float(10, 0) NULL DEFAULT 0 COMMENT '租赁公摊面积',
  `fire_area` float(10, 0) NULL DEFAULT 0 COMMENT '消防面积',
  `clean_area` float(10, 0) NULL DEFAULT 0 COMMENT '清洁面积',
  `parking_area` float(10, 0) NULL DEFAULT 0 COMMENT '车位面积',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '空间、区域历史面积' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of history_area
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_config`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节假日名称',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目ID',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节假日备注',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL COMMENT '删除标志（0代表存在，1代表删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '节假日或非工作日配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of holiday_config
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_date_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_date_config`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `holiday_config_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节假日配置id',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目id',
  `holiday_time` date NULL DEFAULT NULL COMMENT '假日时间',
  `holiday_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假日类型,节假日或非工作日(数据字典)',
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL COMMENT '删除标志（0代表存在，1代表删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '节假日关联日期' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of holiday_date_config
-- ----------------------------

-- ----------------------------
-- Table structure for imported_goods
-- ----------------------------

CREATE TABLE IF NOT EXISTS `imported_goods`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目ID',
  `imported_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '进口ID',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '档位ID',
  `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商户ID',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商户名',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品类型',
  `supplier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商',
  `quantity` int(11) NOT NULL DEFAULT 0 COMMENT '数量',
  `unit` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '单位',
  `producing_area` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产地',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '检查状态',
  `result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '结果',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '进口商品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of imported_goods
-- ----------------------------

-- ----------------------------
-- Table structure for iot_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `iot_config`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `iot_project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'iot平台项目ID',
  `platform_site` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '平台地址Api访问',
  `platform_site_web` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '平台前端跳转地址',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录密码',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物联网配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_config
-- ----------------------------

-- ----------------------------
-- Table structure for knowledge
-- ----------------------------

CREATE TABLE IF NOT EXISTS `knowledge`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '项目ID',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '编号',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型',
  `device_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备类型ID',
  `device_ids` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '设备最多10个',
  `description` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '描述',
  `remark` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0代表存在，1代表已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识库表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of knowledge
-- ----------------------------

-- ----------------------------
-- Table structure for project
-- ----------------------------

CREATE TABLE IF NOT EXISTS `project`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `abbr` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '简称',
  `pinyin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '首字母',
  `longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '经度',
  `latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '纬度',
  `enabled` int(1) NOT NULL COMMENT '是否启用',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `province_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '省级区域',
  `city_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '市级区域',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '市场地址',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目URL地址',
  `api` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目后端api地址',
  `launch_date` date NULL DEFAULT NULL COMMENT '上线日期',
  `launched` int(1) NOT NULL DEFAULT 0 COMMENT '是否已上线',
  `introduction` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '简介',
  `sort` int(4) NOT NULL DEFAULT 0 COMMENT '排序',
  `figures` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目图片路径',
  `acreage` int(10) NULL DEFAULT NULL COMMENT '面积',
  `copyright` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版权信息',
  `model` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模式（集贸、农批）',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0存在，1已删除)',
  `tenant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project
-- ----------------------------

-- ----------------------------
-- Table structure for project_user
-- ----------------------------

CREATE TABLE IF NOT EXISTS `project_user`  (
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `manager` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`project_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目和用户中间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_user
-- ----------------------------

-- ----------------------------
-- Table structure for purchaser
-- ----------------------------

CREATE TABLE IF NOT EXISTS `purchaser`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名称',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `pinyin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '拼音首字母',
  `gender` int(1) NOT NULL DEFAULT 1 COMMENT '性别（男：1，女：2，未知：9）',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像路径',
  `enabled` int(1) NOT NULL DEFAULT 1 COMMENT '状态（启用，停用）',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'userType_staff' COMMENT '用户类型',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在，1代表删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户端-采购商' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of purchaser
-- ----------------------------

-- ----------------------------
-- Table structure for resouce
-- ----------------------------

CREATE TABLE IF NOT EXISTS `resouce`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父ID',
  `group` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源类型：MENU、FUNCTION等',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源代码',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'MENU的图标',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'MENU的path',
  `apis` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'MENU需要使用的api',
  `sort` int(4) NOT NULL COMMENT '排序',
  `enabled` int(1) NOT NULL COMMENT '是否启用',
  `identity_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `pid`(`pid`) USING BTREE,
  INDEX `sort`(`sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resouce
-- ----------------------------

-- ----------------------------
-- Table structure for sensitive_word
-- ----------------------------

CREATE TABLE IF NOT EXISTS `sensitive_word`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '敏感词库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sensitive_word
-- ----------------------------

-- ----------------------------
-- Table structure for space
-- ----------------------------

CREATE TABLE IF NOT EXISTS `space`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `area_id1` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '所属一级区域ID',
  `area_id2` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '所属二级区域ID',
  `area_id3` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '所属三级区域ID',
  `bim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'BIM模型ID',
  `dept_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理部门ID',
  `dept_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理部门名称',
  `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '商户ID',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位类型',
  `attribute` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '属性',
  `trade_model` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '经营类型',
  `acreage` double(10, 2) NOT NULL DEFAULT 0.00 COMMENT '空间面积',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `protocol_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协议ID（档位需要签订的协议，2022/1/10王量）',
  `building_area` float(10, 0) NULL DEFAULT NULL COMMENT '建筑面积',
  `cover_area` float(10, 0) NULL DEFAULT NULL COMMENT '占地面积',
  `lease_area` float(10, 0) NULL DEFAULT NULL COMMENT '租赁面积',
  `lease_real_area` float(10, 0) NULL DEFAULT NULL COMMENT '租赁实际面积',
  `lease_public_area` float(10, 0) NULL DEFAULT NULL COMMENT '租赁公摊面积',
  `fire_area` float(10, 0) NULL DEFAULT NULL COMMENT '消防面积',
  `clean_area` float(10, 0) NULL DEFAULT NULL COMMENT '清洁面积',
  `parking_area` float(10, 0) NULL DEFAULT NULL COMMENT '车位面积',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
  `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `company`(`company_id`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '空间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of space
-- ----------------------------

-- ----------------------------
-- Table structure for spare
-- ----------------------------

CREATE TABLE IF NOT EXISTS `spare`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型ID',
  `type_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型Code',
  `factory` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '厂家',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '型号',
  `num` int(11) NOT NULL DEFAULT 0 COMMENT '库存 数量',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '计量单位',
  `price` decimal(32, 2) NOT NULL DEFAULT 0.00 COMMENT '价格',
  `estimate_low` int(11) NOT NULL DEFAULT 0 COMMENT '数量过低预警值 库存最小',
  `estimate_high` int(11) NOT NULL DEFAULT 0 COMMENT '数量过高预警值 库存最大',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '地址',
  `time` datetime NULL DEFAULT NULL COMMENT '添加时间',
  `has_details` int(1) NOT NULL DEFAULT 0 COMMENT '是否有详情',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备件信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spare
-- ----------------------------

-- ----------------------------
-- Table structure for spare_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `spare_device`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `spare_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备件ID',
  `device_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备件关联设备表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spare_device
-- ----------------------------

-- ----------------------------
-- Table structure for spare_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `spare_type`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '父ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `sort` int(1) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备品备件类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spare_type
-- ----------------------------

-- ----------------------------
-- Table structure for spare_use
-- ----------------------------

CREATE TABLE IF NOT EXISTS `spare_use`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `miant_firm` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '维保公司',
  `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ID',
  `device_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `device_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型ID',
  `device_type_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型code',
  `device_type_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型名称',
  `area_id1` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '一级区域',
  `area_id2` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '二级区域',
  `area_id3` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '三级区域',
  `install_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '安装位置',
  `purpose` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用途',
  `contact_person` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系人',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '联系电话',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务类型',
  `business_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务ID',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备品备件使用记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spare_use
-- ----------------------------

-- ----------------------------
-- Table structure for spare_use_spare
-- ----------------------------

CREATE TABLE IF NOT EXISTS `spare_use_spare`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID ',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `spare_use_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件使用记录ID',
  `spare_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件ID',
  `spare_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件名称',
  `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件类型ID',
  `type_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件类型Code',
  `type_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件类型名称',
  `factory` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备件厂家',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备件型号',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计量单位',
  `price` decimal(32, 2) NOT NULL DEFAULT 0.00 COMMENT '价格',
  `use_num` int(11) NOT NULL DEFAULT 0 COMMENT '使用数量',
  `time` datetime NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '备品备件使用记录中使用的备件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of spare_use_spare
-- ----------------------------

-- ----------------------------
-- Table structure for stall_evaluation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_evaluation`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
  `total` int(11) NOT NULL DEFAULT 0 COMMENT '评价次数',
  `z_score` int(11) NOT NULL DEFAULT 0 COMMENT '商品质量总评分',
  `z_score_avg` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '商品质量平均分',
  `s_score` int(11) NOT NULL DEFAULT 0 COMMENT '诚实守信总评分',
  `s_score_avg` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '诚实守信平均分',
  `t_score` int(11) NOT NULL DEFAULT 0 COMMENT '服务态度总评分',
  `t_score_avg` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '服务态度平均分',
  `w_score` int(11) NOT NULL DEFAULT 0 COMMENT '环境卫生总评分',
  `w_score_avg` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '环境卫生平均分',
  `total_score` int(11) NOT NULL COMMENT '总得分',
  `honesty_score` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '诚信经营值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位评价总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stall_evaluation
-- ----------------------------

-- ----------------------------
-- Table structure for stall_evaluation_ratio
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_evaluation_ratio`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `z_ratio` float(3, 2) NOT NULL DEFAULT 0.00 COMMENT '商品质量权重占比',
  `s_ratio` float(3, 2) NOT NULL DEFAULT 0.00 COMMENT '诚实守信权重占比',
  `t_ratio` float(3, 2) NOT NULL DEFAULT 0.00 COMMENT '服务态度权重占比',
  `w_ratio` float(3, 2) NOT NULL DEFAULT 0.00 COMMENT '环境卫生权重占比',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位评价维度权重占比' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stall_evaluation_ratio
-- ----------------------------

-- ----------------------------
-- Table structure for stall_evaluation_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_evaluation_record`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录编号',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价人ID',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价人名',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价人昵称',
  `z_score` int(1) NOT NULL DEFAULT 0 COMMENT '商品质量评分',
  `s_score` int(1) NOT NULL DEFAULT 0 COMMENT '诚实守信评分',
  `t_score` int(1) NOT NULL DEFAULT 0 COMMENT '服务态度评分',
  `w_score` int(1) NOT NULL DEFAULT 0 COMMENT '环境卫生评分',
  `score` float(2, 1) NOT NULL COMMENT '综合分',
  `honesty_score` float(2, 1) NOT NULL DEFAULT 0.0 COMMENT '诚信经营值',
  `descr` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `has_file` int(1) NULL DEFAULT 0 COMMENT '是否有图',
  `tags` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价标签',
  `examine_status` int(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0:未审核，1:已通过，2:未通过）',
  `examine_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `examine_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位评价记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stall_evaluation_record
-- ----------------------------

-- ----------------------------
-- Table structure for stall_evaluation_record_tag
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_evaluation_record_tag`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `tag_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签ID',
  `record_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录ID',
  `tag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签',
  `evaluation` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评价维度',
  `time` datetime NULL DEFAULT NULL COMMENT '时间',
  `examine_status` int(1) NOT NULL DEFAULT 0 COMMENT '审核状态（0:未审核，1:已通过，2:未通过）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评价记录关联标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stall_evaluation_record_tag
-- ----------------------------

-- ----------------------------
-- Table structure for stall_evaluation_tag
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_evaluation_tag`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评价标签',
  `evaluation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评价维度',
  `category` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评价分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位评价标签' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stall_evaluation_tag
-- ----------------------------

-- ----------------------------
-- Table structure for stall_safety_insp
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_safety_insp`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
  `task_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名',
  `record_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录ID',
  `record_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录编号',
  `record_exist` int(1) NOT NULL DEFAULT 1 COMMENT '巡检记录是否存在',
  `standard_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标准名',
  `item_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项ID',
  `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项名称',
  `item_result_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项结果名',
  `item_result_mark` int(3) NOT NULL DEFAULT 0 COMMENT '项结果扣分',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `time` datetime NULL DEFAULT NULL COMMENT '巡检时间',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检人ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检人名',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检人昵称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位安全生产巡检扣分记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stall_safety_insp
-- ----------------------------

-- ----------------------------
-- Table structure for stall_safety_mark
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_safety_mark`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
  `year` int(11) NOT NULL DEFAULT 0 COMMENT '年份',
  `score` int(6) NOT NULL DEFAULT 0 COMMENT '分数',
  `insp_total` int(11) NOT NULL DEFAULT 0 COMMENT '扣分巡检项数',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位安全生产值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stall_safety_mark
-- ----------------------------

-- ----------------------------
-- Table structure for stall_safety_mark_logs
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_safety_mark_logs`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
  `point` int(11) NOT NULL DEFAULT 0 COMMENT '调整分数（加|减）',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在，1已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位安全生产分值修改记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stall_safety_mark_logs
-- ----------------------------

-- ----------------------------
-- Table structure for stream_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stream_config`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `host` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'IP',
  `port` int(5) NULL DEFAULT 0 COMMENT '端口',
  `rtmp_port` int(5) NULL DEFAULT 0 COMMENT '公网rtmp端口',
  `lan_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '局域网内网IP',
  `lan_port` int(5) NULL DEFAULT 0 COMMENT '局域网内网端口',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流媒体服务配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of stream_config
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `is_pwd_random` int(1) NOT NULL DEFAULT 0 COMMENT '是否是随机密码',
  `nickname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `pinyin` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '拼音首字母',
  `gender` int(1) NOT NULL DEFAULT 1 COMMENT '性别（男：1，女：2，未知：9）',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号码',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `identity_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像路径',
  `enabled` int(1) NOT NULL DEFAULT 1 COMMENT '状态（启用，停用）',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'userType_staff' COMMENT '用户类型',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `theme` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题肤色',
  `is_external` int(1) NULL DEFAULT 0 COMMENT '是否来自外部系统',
  `source` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '来自哪里',
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0代表存在，1代表删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for user_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user_device`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '项目ID',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '业务类型',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户ID',
  `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '设备ID',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户设备表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_device
-- ----------------------------

-- ----------------------------
-- Table structure for user_link
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user_link`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `wxopenid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信openid',
  `wxunionid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信unionid',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户绑定微信openid' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_link
-- ----------------------------

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------

CREATE TABLE IF NOT EXISTS `warehouse`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仓库' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of warehouse
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_auth
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_auth`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `project_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商铺ID',
  `company_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户名称',
  `enterprise_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企业名称',
  `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `identity_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电话',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
  `post_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮政',
  `registration_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册工商号',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '审核状态 0未审核，1审核通过，2驳回',
  `login_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号，user.username',
  `reviewer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `reject_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '驳回原因',
  `stall_ids` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联的档位IDS',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申请人',
  `create_time` datetime NOT NULL COMMENT '申请时间',
  `personnel_pic_id` bigint(20) NULL DEFAULT NULL COMMENT '身份证人像面照片ID',
  `id_card_photo_id` bigint(20) NULL DEFAULT NULL COMMENT '身份证人像面照片DTO',
  `id_card_nation_id` bigint(20) NULL DEFAULT NULL COMMENT '身份证国徽面照片ID',
  `license_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '营业执照ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `userId`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '实名认证信息表，' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_auth
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_contract
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_contract`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合同编号',
  `signing_way` int(1) NOT NULL COMMENT '签约方式（0新签约，1续签）',
  `contract_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合同类型ID',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '合同状态（0草稿 1待签订 2 乙方签约 3签章（生效））',
  `begin_time` date NOT NULL COMMENT '租赁日期始',
  `end_time` date NOT NULL COMMENT '租赁日期终',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `signatory_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签约人ID',
  `signatory_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '甲方签约人名字',
  `signatory_time` datetime NULL DEFAULT NULL COMMENT '甲方签约时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `second_part_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '乙方ID',
  `paper_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纸质合同编号',
  `old_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原合同ID',
  `old_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原合同编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户合同表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_contract
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_contract_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_contract_type`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `enabled` int(1) NOT NULL COMMENT '是否启用（1启用，0没有启用）',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合同类型名称',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户合同类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_contract_type
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_device`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT 'id',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型ID',
  `count` int(11) NOT NULL COMMENT '设备数量',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所在档位ID',
  `stall_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位名称',
  `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属商户ID',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备描述',
  `danger` int(1) NOT NULL COMMENT '是否危险物品',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` int(1) NOT NULL COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户档案-所有设备（批发商的设备）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_device
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_device_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_device_type`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `pid` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父级ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `is_danger` int(1) NOT NULL DEFAULT 0 COMMENT '是否是危险源',
  `sort` int(2) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标志(0代表存在，1代表已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户设备类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_device_type
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_first_part
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_first_part`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '甲方名称',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
  `post_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `seal_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子章',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '甲方信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_first_part
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_notice
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_notice`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `business_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型',
  `notice_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知对象类型（用户、部门、岗位、部门岗位）',
  `user_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的用户IDS',
  `dept_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的部门IDS',
  `post_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知的岗位IDS',
  `site` int(1) NOT NULL DEFAULT 0 COMMENT '站内信',
  `sms` int(1) NOT NULL DEFAULT 0 COMMENT '短信',
  `wx` int(1) NOT NULL DEFAULT 0 COMMENT '微信',
  `pop` int(1) NOT NULL DEFAULT 0 COMMENT '弹窗',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户相关功能通知市场方（目前合同、认证）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_notice
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_protocol
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_protocol`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `template_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `template_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `template_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `begin_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `processing_status` int(1) NOT NULL DEFAULT 0 COMMENT '处理是否完成',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '有效状态：未生效、有效、失效',
  `xxl_task_ids` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'xxl的任务IDS',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `deleted` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户协议' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_protocol
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_protocol_template
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_protocol_template`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目id',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板类型',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户协议模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_protocol_template
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_question
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_question`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主键ID',
  `questionnaire_temp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷模板ID',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题类型',
  `number` int(2) NOT NULL COMMENT '题号',
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题干',
  `options` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选项',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户问卷-问卷上的题目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_question
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_questionnaire
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_questionnaire`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '主键ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷名称',
  `temp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板id',
  `temp_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板类型',
  `temp_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板类型',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态（是否有效）',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '描述',
  `begin_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `xxl_task_ids` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'xxl的任务IDS',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '问卷调查' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_questionnaire
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_questionnaire_temp
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_questionnaire_temp`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷模板名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷模板类型',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户问卷模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_questionnaire_temp
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_relationship
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_relationship`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型，可能是问卷类型，可能是协议类型',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位ID',
  `business_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务ID，可能是问卷ID可能是协议ID',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态（是否签约，做题）',
  `signed_who` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签约（做题）人',
  `sigend_time` datetime NULL DEFAULT NULL COMMENT '签约（做题）时间',
  `owner` int(1) NULL DEFAULT NULL COMMENT '1档主0非档主',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户中间关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_relationship
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_response
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_response`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位id',
  `questionnaire_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷ID',
  `temp_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板ID',
  `question_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题iD',
  `answer` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '回答',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '问卷调查结果' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_response
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_second_part
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_second_part`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `contract_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对应的合同ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '乙方名称',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
  `post_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮编',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电话',
  `company_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企业机构代码',
  `legal_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '法人',
  `identity_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '法人身份证',
  `signatory` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '签约人',
  `signatory_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '签约人号码',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '0是没有通过验证，1是通过了',
  `create_time` datetime NULL DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '乙方信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_second_part
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_signatory
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_signatory`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `first_part_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '甲方ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '签约人ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签约人姓名',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `seal_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子章',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '甲方签约人信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_signatory
-- ----------------------------

-- ----------------------------
-- Table structure for wholesaler_vehicle
-- ----------------------------

CREATE TABLE IF NOT EXISTS `wholesaler_vehicle`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `project_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目ID',
  `vehicle_plate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号（三轮车无车牌）',
  `vehicle_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '颜色',
  `owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属人姓名',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属人电话（巡逻的人要根据电话找人）',
  `stall_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位',
  `stall_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位名称',
  `company_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户',
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户档案-所有车辆（批发商交通工具表）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wholesaler_vehicle
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
