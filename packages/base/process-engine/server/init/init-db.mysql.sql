CREATE DATABASE IF NOT EXISTS `cube-base-flow` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube-base-flow`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for approval_node
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `approval_node`  (
  `task_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `support_text` bit(1) NULL DEFAULT NULL,
  `support_handwritten_signature` bit(1) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of approval_node
-- ----------------------------

-- ----------------------------
-- Table structure for copy_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `copy_log`  (
  `id` bigint(20) NOT NULL,
  `task_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `process_instance_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `data` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
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
    'ALTER TABLE `cube-base-flow`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('raw', 'includeAll', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.3.0-0.mysql.sql', '2024-04-23 01:27:28', 1, 'EXECUTED', '8:777a1b4b3061f0bbc7587df05939088c', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806848602');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.3.0-1.mysql.sql', '2024-04-23 01:27:28', 2, 'EXECUTED', '8:05e2d7b319d7ab78670ab205e9ad23fd', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806848680');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.3.6-0.mysql.sql', '2024-04-23 01:27:28', 3, 'EXECUTED', '8:f96a3ec53aae8b4bb436b31b70909bdc', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806848772');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-1', 'lijie', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.4.0-1.mysql.sql', '2024-05-27 20:20:17', 4, 'EXECUTED', '8:a9e8d0bca497fff6d49349ef462ccd36', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812416450');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-2', 'lijie', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.4.0-1.mysql.sql', '2024-05-27 20:20:17', 5, 'EXECUTED', '8:9b6c1ca7b8966288571224fbbb8cf3cc', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812416450');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-3', 'lijie', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.4.0-1.mysql.sql', '2024-05-27 20:20:18', 6, 'EXECUTED', '8:fd5fdeed64caacecbd6fa82ad8ccb10e', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812416450');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-4', 'lijie', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.4.0-1.mysql.sql', '2024-05-27 20:20:19', 7, 'EXECUTED', '8:5b9bcd6cde486fe952567bb006dd2605', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812416450');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-5', 'lijie', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.4.0-1.mysql.sql', '2024-05-27 20:20:20', 8, 'EXECUTED', '8:93a95a94889ff22c649b30aeda2f91f4', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812416450');
INSERT IGNORE INTO `databasechangelog` VALUES ('index-1', 'lijie', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.3.33-1.mysql.sql', '2024-06-05 10:18:16', 9, 'EXECUTED', '8:7ad9be9db3d2e8befca36a71204a8b7b', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553896211');
INSERT IGNORE INTO `databasechangelog` VALUES ('index-2', 'lijie', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.3.33-1.mysql.sql', '2024-06-05 10:18:16', 10, 'EXECUTED', '8:0d74704aee1e460070453748f58e9348', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553896211');
INSERT IGNORE INTO `databasechangelog` VALUES ('index-3', 'lijie', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.3.33-1.mysql.sql', '2024-06-05 10:18:16', 11, 'EXECUTED', '8:a0b2409eaafb7e509bf0ae38475cc63c', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553896211');
INSERT IGNORE INTO `databasechangelog` VALUES ('ddl-1', 'zengyadong', 'classpath:db/com.guhecloud.cube.base.flow/changelog-2.3.38-0.mysql.sql', '2024-06-24 09:42:45', 12, 'EXECUTED', '8:a29abd81791e0e012be991b58c5c5842', 'sql', '', NULL, '4.3.1', NULL, NULL, '9193365589');

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `databasechangeloglock`  (
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
-- Table structure for form_business_number_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `form_business_number_config`  (
  `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `form_no_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务编号前缀'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单业务编号配置' ROW_FORMAT = DYNAMIC;


SELECT COUNT(1) IndexIsThere
FROM INFORMATION_SCHEMA.STATISTICS
WHERE table_schema=DATABASE()
AND   table_name='form_business_number_config'
AND   index_name='unique_type';

SET @IndexIsThere =
(SELECT COUNT(1) IndexIsThere
 FROM INFORMATION_SCHEMA.STATISTICS
 WHERE table_schema=DATABASE()
 AND   table_name='form_business_number_config'
 AND   index_name='unique_type');

SET @sqlstmt =
IF (@IndexIsThere = 0,
    'ALTER TABLE `cube-base-flow`.form_business_number_config ADD UNIQUE KEY unique_type (`business_type`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of form_business_number_config
-- ----------------------------
INSERT IGNORE INTO `form_business_number_config` VALUES ('REPAIR_CLOCK', 'BK');
INSERT IGNORE INTO `form_business_number_config` VALUES ('OVERTIME', 'JB');
INSERT IGNORE INTO `form_business_number_config` VALUES ('CHANGE_CLASSES', 'HB');
INSERT IGNORE INTO `form_business_number_config` VALUES ('LEAVE_THE_POST', 'LG');
INSERT IGNORE INTO `form_business_number_config` VALUES ('PERSONNEL_RECRUIT', 'ZP');
INSERT IGNORE INTO `form_business_number_config` VALUES ('PERSONNEL_INDUCTION', 'RZ');
INSERT IGNORE INTO `form_business_number_config` VALUES ('PERSONNEL_PROBATION_ASSESS', 'KH');
INSERT IGNORE INTO `form_business_number_config` VALUES ('PERSONNEL_REGULAR_WORKER', 'ZZ');
INSERT IGNORE INTO `form_business_number_config` VALUES ('PERSONNEL_TRAIN', 'PX');
INSERT IGNORE INTO `form_business_number_config` VALUES ('PERSONNEL_LEAVE_OFFICE', 'LZSP');
INSERT IGNORE INTO `form_business_number_config` VALUES ('PERSONNEL_LEAVE_OFFICE_PROCEDURES', 'LZSX');
INSERT IGNORE INTO `form_business_number_config` VALUES ('PERSONNEL_ROTATION_POST', 'LG');

-- ----------------------------
-- Table structure for form_control_business
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `form_control_business`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `form_definition_id` bigint(20) NULL DEFAULT NULL COMMENT '表单定义ID',
  `control_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '控件ID',
  `control_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '控件类型（基础、人事、考勤）',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_field` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '控件与业务字段的映射关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of form_control_business
-- ----------------------------
INSERT IGNORE INTO `form_control_business` VALUES (1805060677855412226, 1778686034238771202, 'field8560706680414', NULL, NULL, 'handler');
INSERT IGNORE INTO `form_control_business` VALUES (1805060677872189441, 1778686034238771202, 'field5886606620216', NULL, NULL, 'storeId');
INSERT IGNORE INTO `form_control_business` VALUES (1805060677897355265, 1778686034238771202, 'field1372606633665', NULL, NULL, 'addDate');
INSERT IGNORE INTO `form_control_business` VALUES (1805060677914132482, 1778686034238771202, 'field6469306655514', NULL, NULL, 'supplier');
INSERT IGNORE INTO `form_control_business` VALUES (1805060677930909697, 1778686034238771202, 'field2351106693882', NULL, NULL, 'remark');
INSERT IGNORE INTO `form_control_business` VALUES (1805060677956075522, 1778686034238771202, 'field5566606715463', NULL, NULL, 'files');
INSERT IGNORE INTO `form_control_business` VALUES (1805060677972852738, 1778686034238771202, 'field5577206731281', NULL, NULL, 'data');
INSERT IGNORE INTO `form_control_business` VALUES (1805060949201715202, 1778689369868079106, 'field9815206924879', NULL, NULL, 'handler');
INSERT IGNORE INTO `form_control_business` VALUES (1805060949222686722, 1778689369868079106, 'field8889906811462', NULL, NULL, 'storeId');
INSERT IGNORE INTO `form_control_business` VALUES (1805060949247852545, 1778689369868079106, 'field7283106864955', NULL, NULL, 'deptId');
INSERT IGNORE INTO `form_control_business` VALUES (1805060949264629761, 1778689369868079106, 'field2775306882650', NULL, NULL, 'claimant');
INSERT IGNORE INTO `form_control_business` VALUES (1805060949293989889, 1778689369868079106, 'field7914206903000', NULL, NULL, 'reduceDate');
INSERT IGNORE INTO `form_control_business` VALUES (1805060949310767106, 1778689369868079106, 'field9721806957270', NULL, NULL, 'remark');
INSERT IGNORE INTO `form_control_business` VALUES (1805060949344321538, 1778689369868079106, 'field3688806974744', NULL, NULL, 'files');
INSERT IGNORE INTO `form_control_business` VALUES (1805060949411430401, 1778689369868079106, 'field1970206987061', NULL, NULL, 'data');
INSERT IGNORE INTO `form_control_business` VALUES (1805061067942461442, 1778683673445404674, 'field3381704153476', NULL, NULL, 'startTime');
INSERT IGNORE INTO `form_control_business` VALUES (1805061067963432961, 1778683673445404674, 'field8751704235061', NULL, NULL, 'sourceId');
INSERT IGNORE INTO `form_control_business` VALUES (1805061067980210178, 1778683673445404674, 'field2642404246589', NULL, NULL, '');
INSERT IGNORE INTO `form_control_business` VALUES (1805061068005376002, 1778683673445404674, 'field2857504266307', NULL, NULL, 'receiptNumbers');
INSERT IGNORE INTO `form_control_business` VALUES (1805061068026347521, 1778683673445404674, 'field6219204289124', NULL, NULL, 'remarks');
INSERT IGNORE INTO `form_control_business` VALUES (1805061068038930434, 1778683673445404674, 'field8423504313205', NULL, NULL, 'files');
INSERT IGNORE INTO `form_control_business` VALUES (1805061068055707650, 1778683673445404674, 'field9594304338286', NULL, NULL, 'data');
INSERT IGNORE INTO `form_control_business` VALUES (1805061182644092930, 1778683400022921218, 'field4131805235704', NULL, NULL, 'receiveDepId');
INSERT IGNORE INTO `form_control_business` VALUES (1805061182983831553, 1778683400022921218, 'field6918805237703', NULL, NULL, 'receiveUserId');
INSERT IGNORE INTO `form_control_business` VALUES (1805061183008997377, 1778683400022921218, 'field8191205334235', NULL, NULL, 'receiveTime');
INSERT IGNORE INTO `form_control_business` VALUES (1805061183021580289, 1778683400022921218, 'field2083205441898', NULL, NULL, 'areaIds');
INSERT IGNORE INTO `form_control_business` VALUES (1805061183042551809, 1778683400022921218, 'field7096105471815', NULL, NULL, 'place');
INSERT IGNORE INTO `form_control_business` VALUES (1805061183055134721, 1778683400022921218, 'field7261605484411', NULL, NULL, 'remarks');
INSERT IGNORE INTO `form_control_business` VALUES (1805061183071911937, 1778683400022921218, 'field7338705507094', NULL, NULL, 'files');
INSERT IGNORE INTO `form_control_business` VALUES (1805061183088689154, 1778683400022921218, 'field9457505524158', NULL, NULL, 'propertyIds');
INSERT IGNORE INTO `form_control_business` VALUES (1805061376475463682, 1778678528619843586, 'field1786604461854', NULL, NULL, 'overDepId');
INSERT IGNORE INTO `form_control_business` VALUES (1805061376492240897, 1778678528619843586, 'field9899104584900', NULL, NULL, 'receiveDepId');
INSERT IGNORE INTO `form_control_business` VALUES (1805061376509018113, 1778678528619843586, 'field4811904606563', NULL, NULL, 'receiveTime');
INSERT IGNORE INTO `form_control_business` VALUES (1805061376529989634, 1778678528619843586, 'field7671804644483', NULL, NULL, 'areaIds');
INSERT IGNORE INTO `form_control_business` VALUES (1805061376546766850, 1778678528619843586, 'field4896404674882', NULL, NULL, 'place');
INSERT IGNORE INTO `form_control_business` VALUES (1805061376571932673, 1778678528619843586, 'field6609904689094', NULL, NULL, 'remarks');
INSERT IGNORE INTO `form_control_business` VALUES (1805061376592904193, 1778678528619843586, 'field8291204702861', NULL, NULL, 'files');
INSERT IGNORE INTO `form_control_business` VALUES (1805061376609681409, 1778678528619843586, 'field8365304799026', NULL, NULL, 'propertyIds');
INSERT IGNORE INTO `form_control_business` VALUES (1805061456536338433, 1778685192769114114, 'field9285906275762', NULL, NULL, 'weedOutUserId');
INSERT IGNORE INTO `form_control_business` VALUES (1805061456553115650, 1778685192769114114, 'field2629606306997', NULL, NULL, '');
INSERT IGNORE INTO `form_control_business` VALUES (1805061456569892866, 1778685192769114114, 'field1227906324218', NULL, NULL, 'manageTime');
INSERT IGNORE INTO `form_control_business` VALUES (1805061456586670081, 1778685192769114114, 'field6357806361253', NULL, NULL, 'causeId');
INSERT IGNORE INTO `form_control_business` VALUES (1805061456603447297, 1778685192769114114, 'field2604706380776', NULL, NULL, 'overdue');
INSERT IGNORE INTO `form_control_business` VALUES (1805061456616030209, 1778685192769114114, 'field5806306403064', NULL, NULL, 'remarks');
INSERT IGNORE INTO `form_control_business` VALUES (1805061456632807425, 1778685192769114114, 'field1876506423657', NULL, NULL, 'files');
INSERT IGNORE INTO `form_control_business` VALUES (1805061456649584641, 1778685192769114114, 'field2173106434376', NULL, NULL, 'propertyIds');

-- ----------------------------
-- Table structure for form_control_flow
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `form_control_flow`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `flow_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义key',
  `task_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务节点信息',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '配置json',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '控件与流程的映射关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of form_control_flow
-- ----------------------------

-- ----------------------------
-- Table structure for form_definition
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `form_definition`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单key',
  `json_schema` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'json数据',
  `json_schema_ui` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'json视图-ui',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '冗余-表单名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '冗余-表单类型',
  `application` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冗余-表单应用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冗余-表单备注',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `abandon` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '是否废弃',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `key_version_index`(`key`, `version`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单定义' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for form_flow
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `form_flow`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  `application` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '启停用',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `flow_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程key',
  `form_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表单key',
  `administrator_id` bigint(20) NULL DEFAULT NULL COMMENT '管理员ID',
  `administrator` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员姓名',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `auto_remove_duplication` tinyint(1) NULL DEFAULT 0 COMMENT '自动去重',
  `auto_approval_printing` tinyint(1) NULL DEFAULT NULL COMMENT '自动审批打印展示',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单、流程基础信息管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for form_flow_business
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `form_flow_business`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `form_flow_id` bigint(20) NOT NULL COMMENT '表单流程ID',
  `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务',
  `business_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务名称',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户主键',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单、流程与业务的关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for form_instance
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `form_instance`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `form_flow_id` bigint(20) NOT NULL COMMENT '表单流程ID',
  `form_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单定义key',
  `version` int(11) NULL DEFAULT NULL COMMENT '表单定义版本号',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联业务类型',
  `business_key` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联业务key',
  `business_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联业务状态',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单实例（暂缺少关于业务的字段）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of form_instance
-- ----------------------------

-- ----------------------------
-- Table structure for form_instance_data
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `form_instance_data`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `form_instance_id` bigint(20) NOT NULL COMMENT '表单实例ID',
  `form_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单数据',
  `business_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单映射后的业务数据',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单实例数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of form_instance_data
-- ----------------------------

-- ----------------------------
-- Table structure for process_definition
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `process_definition`  (
  `id` bigint(20) NOT NULL,
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `form_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_definition
-- ----------------------------

-- ----------------------------
-- Table structure for process_instance_running
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `process_instance_running`  (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `application_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `business_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `process_definition_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程结果',
  `data` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `application_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_user_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `instance_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_user_id` bigint(20) NULL DEFAULT NULL COMMENT '发起人id',
  `start_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起人',
  `organization_id` bigint(20) NULL DEFAULT NULL COMMENT '发起人部门id',
  `organization_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起人部门',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX ```start_user_id```(`start_user_id`) USING BTREE,
  INDEX ```start_user_name```(`start_user_name`) USING BTREE,
  INDEX ```organization_id```(`organization_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_instance_running
-- ----------------------------

-- ----------------------------
-- Table structure for task_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `task_log`  (
  `task_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `process_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operation_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `id` bigint(20) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_log
-- ----------------------------

-- ----------------------------
-- Table structure for task_node
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `task_node`  (
  `task_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `node_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `process_definition_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `task_operations` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_node
-- ----------------------------

-- ----------------------------
-- Table structure for task_operate_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `task_operate_log`  (
  `id` bigint(20) NOT NULL,
  `business_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operate_node_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `task_definition_key` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `task_infos` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `process_instance_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `task_operation_type` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for task_ru_node
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `task_ru_node`  (
  `task_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `process_instance_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `task_config` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `process_definition_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `data` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_ru_node
-- ----------------------------

-- ----------------------------
-- Table structure for task_running_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `task_running_log`  (
  `task_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `process_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operation_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `id` bigint(20) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `currented` bit(2) NULL DEFAULT b'0' COMMENT '是否是当前节点'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of task_running_log
-- ----------------------------

-- ----------------------------
-- Table structure for webhook_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `webhook_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `from_flow_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程表单id',
  `trigger_node` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回调节点',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回调地址',
  `token` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密钥',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '业务回调配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of webhook_config
-- ----------------------------

-- ----------------------------
-- View structure for all_process_instance
-- ----------------------------
DROP VIEW IF EXISTS `all_process_instance`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `all_process_instance` AS select floor((rand() * 100)) AS `id`,json_unquote(json_extract(`r`.`start_user_info`,'$.startUserId')) AS `start_user_id`,json_unquote(json_extract(`r`.`start_user_info`,'$.startUserName')) AS `start_user_name`,json_unquote(json_extract(`r`.`start_user_info`,'$.organizationId')) AS `organization_id`,json_unquote(json_extract(`r`.`start_user_info`,'$.organizationName')) AS `organization_name`,'ACTIVE' AS `state`,`r`.`tenant` AS `tenant`,`t`.`PROC_INST_ID_` AS `proc_inst_id`,`t`.`ASSIGNEE_` AS `assignee`,`t`.`CREATE_TIME_` AS `task_create_time`,NULL AS `task_end_time`,`r`.`instance_name` AS `apply_name`,`r`.`start_user_info` AS `start_user_info`,`r`.`application_code` AS `business_model`,`r`.`business_type` AS `business_type`,`e`.`START_TIME_` AS `start_time`,NULL AS `end_time`,'APPROVAL' AS `type`,'' AS `copy_user` from ((`act_ru_task` `t` left join `process_instance_running` `r` on((`r`.`id` = `t`.`PROC_INST_ID_`))) left join `act_hi_procinst` `e` on((`e`.`PROC_INST_ID_` = `t`.`PROC_INST_ID_`))) union select floor((rand() * 100)) AS `id`,json_unquote(json_extract(`r`.`start_user_info`,'$.startUserId')) AS `start_user_id`,json_unquote(json_extract(`r`.`start_user_info`,'$.startUserName')) AS `start_user_name`,json_unquote(json_extract(`r`.`start_user_info`,'$.organizationId')) AS `organization_id`,json_unquote(json_extract(`r`.`start_user_info`,'$.organizationName')) AS `organization_name`,ifnull(`r`.`result`,'ACTIVE') AS `state`,`r`.`tenant` AS `tenant`,`p`.`PROC_INST_ID_` AS `proc_inst_id`,`t`.`ASSIGNEE_` AS `assignee`,NULL AS `task_create_time`,`t`.`END_TIME_` AS `task_end_time`,`r`.`instance_name` AS `apply_name`,`r`.`start_user_info` AS `start_user_info`,`r`.`application_code` AS `business_model`,`r`.`business_type` AS `business_type`,`p`.`START_TIME_` AS `start_time`,`p`.`END_TIME_` AS `end_time`,'APPROVAL' AS `type`,'' AS `copy_user` from ((`act_hi_procinst` `p` left join `process_instance_running` `r` on((`r`.`id` = `p`.`PROC_INST_ID_`))) left join `act_hi_taskinst` `t` on(((`t`.`PROC_INST_ID_` = `p`.`PROC_INST_ID_`) and (`t`.`TASK_DEF_KEY_` <> 'root') and (`t`.`END_TIME_` is not null) and ((`t`.`DELETE_REASON_` <> 'Multi-instance complete condition expression passed') or isnull(`t`.`DELETE_REASON_`))))) union select floor((rand() * 100)) AS `id`,json_unquote(json_extract(`r`.`start_user_info`,'$.startUserId')) AS `start_user_id`,json_unquote(json_extract(`r`.`start_user_info`,'$.startUserName')) AS `start_user_name`,json_unquote(json_extract(`r`.`start_user_info`,'$.organizationId')) AS `organization_id`,json_unquote(json_extract(`r`.`start_user_info`,'$.organizationName')) AS `organization_name`,ifnull(`r`.`result`,'ACTIVE') AS `state`,`r`.`tenant` AS `tenant`,`t`.`process_instance_id` AS `proc_inst_id`,`t`.`user_ids` AS `assignee`,NULL AS `task_create_time`,NULL AS `task_end_time`,`r`.`instance_name` AS `apply_name`,`r`.`start_user_info` AS `start_user_info`,`r`.`application_code` AS `business_model`,`r`.`business_type` AS `business_type`,`p`.`START_TIME_` AS `start_time`,`p`.`END_TIME_` AS `end_time`,'COPY' AS `type`,`t`.`user_ids` AS `copy_user` from ((`copy_log` `t` left join `process_instance_running` `r` on((`r`.`id` = `t`.`process_instance_id`))) left join `act_hi_procinst` `p` on((`p`.`PROC_INST_ID_` = `t`.`process_instance_id`)));

SET FOREIGN_KEY_CHECKS = 1;
