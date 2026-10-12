CREATE DATABASE IF NOT EXISTS`cube-base-alarm` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube-base-alarm`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alarm_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `alarm_record`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `alarm_level_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '级别主键',
  `alarm_level_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '级别编号',
  `alarm_level_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '级别名称',
  `alarm_level_sort` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '级别序号',
  `alarm_type_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '类型主键',
  `alarm_type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型编号',
  `alarm_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `alarm_type_file_id` bigint(20) NULL DEFAULT NULL COMMENT '类型图标附件',
  `alarm_target_key` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '对象主键',
  `alarm_target_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象编号',
  `alarm_target_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象名称',
  `alarm_target_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象类型主键',
  `alarm_target_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象类型名称',
  `alarm_target_longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象经纬度信息',
  `alarm_target_address` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象位置信息',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `alarm_time` datetime NULL DEFAULT NULL COMMENT '报警发生时间',
  `area` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警区域',
  `area_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警区域名称',
  `alarm_threshold` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警阀值',
  `alarm_event` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警事件主键(报警规则id)',
  `alarm_event_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警事件名称',
  `alarm_event_type_id` bigint(20) NULL DEFAULT NULL COMMENT '报警事件分类主键',
  `alarm_event_type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警事件分类编号',
  `alarm_event_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警事件分类名称',
  `alarm_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警状态',
  `alarm_status_order` int(1) NULL DEFAULT NULL COMMENT '状态序号',
  `alarm_confirmed_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警确认人员',
  `alarm_confirmed_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警确认人员名称',
  `alarm_confirmed_time` datetime NULL DEFAULT NULL COMMENT '报警确认时间',
  `alarm_confirmed_result_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警确认结果描述',
  `suspend_time` datetime NULL DEFAULT NULL COMMENT '挂起时间',
  `estimate_handler_time` datetime NULL DEFAULT NULL COMMENT '预计处理时间',
  `alarm_handler_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警处理人员',
  `alarm_handler_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警处理人员名称',
  `alarm_handler_time` datetime NULL DEFAULT NULL COMMENT '报警处理时间',
  `alarm_handler_result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警处理结果状态',
  `alarm_handler_result_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警处理结果描述',
  `timeout_equipped` tinyint(1) NULL DEFAULT NULL COMMENT '是否配置超时',
  `timeout_process_time` datetime NULL DEFAULT NULL COMMENT '超时处理时间',
  `timeout_process_times` int(11) NULL DEFAULT NULL COMMENT '超时处理时间配置',
  `timeout_process_times_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '超时处理时间配置单位',
  `processing_time` int(11) NULL DEFAULT NULL COMMENT '处理耗时(单位秒)',
  `application_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '应用主键',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `snapshot` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '业务快照',
  `alarm_snapshot` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '报警快照',
  `filter_scope` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '过滤报警作用域',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_business_key_tenant_deleted`(`business_key`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of alarm_record
-- ----------------------------

-- ----------------------------
-- Table structure for alarm_record_handler_user
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `alarm_record_handler_user`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alarm_record_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '报警记录主键',
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户主键',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '部门主键',
  `department_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门所属名称',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_alarm_record_id_user_id_tenant_deleted`(`alarm_record_id`, `user_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警处理人配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of alarm_record_handler_user
-- ----------------------------

-- ----------------------------
-- Table structure for alarm_record_operator_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `alarm_record_operator_log`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alarm_record_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '报警记录主键',
  `operator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `operator_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `operator_by_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人名称',
  `operator_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `operator_content` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作备注',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警记录操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of alarm_record_operator_log
-- ----------------------------

-- ----------------------------
-- Table structure for alarm_record_target
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `alarm_record_target`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alarm_record_id` bigint(20) NULL DEFAULT NULL COMMENT '报警记录主键',
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户主键',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `alarm_record_target_tenant_IDX`(`user_id`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警记录通知用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of alarm_record_target
-- ----------------------------

-- ----------------------------
-- Table structure for alarm_state_event_operate
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `alarm_state_event_operate`  (
  `id` bigint(20) NOT NULL,
  `alarm_record_id` bigint(20) NULL DEFAULT NULL COMMENT '报警记录主键',
  `alarm_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警状态',
  `alarm_status_event` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态事件',
  `operation_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作参数',
  `tenant` int(10) NULL DEFAULT NULL,
  `deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警状态事件操作' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of alarm_state_event_operate
-- ----------------------------

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
    'ALTER TABLE `cube-base-alarm`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.alarm/test001.mysql.sql', '2024-04-23 01:27:29', 1, 'EXECUTED', '8:1dd17f522a75a67f1780da1945e42eb8', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806849589');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.alarm/changelog-1.1.8.mysql.sql', '2024-04-23 01:27:29', 2, 'EXECUTED', '8:9c8b866703199f571d85e6f73aa22274', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806849651');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.alarm/changelog-1.1.14.mysql.sql', '2024-04-23 01:27:29', 3, 'EXECUTED', '8:06147486c168213b2b37cd0602d8b45e', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806849770');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.alarm/changelog-1.1.15.mysql.sql', '2024-05-27 20:20:21', 4, 'EXECUTED', '8:fd9c0039f500b56680f1d945ac66198f', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812420989');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.alarm/changelog-1.1.16.mysql.sql', '2024-06-05 10:18:20', 5, 'EXECUTED', '8:ada6afe8f7f61589bf97b93a6adea7b6', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553900227');

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
-- Table structure for safety_risk_score
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `safety_risk_score`  (
  `id` bigint(11) UNSIGNED NOT NULL COMMENT 'id',
  `index_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指标类型',
  `section_start` double(6, 2) NULL DEFAULT NULL COMMENT '区间开始值',
  `section_end` double(6, 2) NULL DEFAULT NULL COMMENT '区间结束值',
  `slope` double(6, 2) NULL DEFAULT NULL COMMENT '斜率',
  `score_start` int(11) NULL DEFAULT NULL COMMENT '分数开始值',
  `score_end` int(11) NULL DEFAULT NULL COMMENT '分值结束值',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分值区间' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_alarm_business_url
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `sat_alarm_business_url`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型名称',
  `application_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `terminal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问端',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
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
  UNIQUE INDEX `idx_business_type_application_code_terminal_deleted`(`business_type`, `application_code`, `terminal`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_alarm_level
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `sat_alarm_level`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `sort` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '序号越小优先级越高',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_name_tenant_deleted`(`name`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警级别' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sat_alarm_level
-- ----------------------------
INSERT INTO `sat_alarm_level` (`id`, `code`, `name`, `sort`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385498145112066, 'one_level', '一级', 1, 0, '13907080000', '2022-07-11 14:46:48', '15181147301', '2022-12-02 11:42:05') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_level` (`id`, `code`, `name`, `sort`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385498145112067, 'two_level', '二级', 2, 0, '1', '2022-11-04 09:56:10', '15181147301', '2022-12-02 11:42:08') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_level` (`id`, `code`, `name`, `sort`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385498145112068, 'three_level', '三级', 3, 0, '1', '2022-11-04 09:57:13', '1', '2022-11-04 09:57:17') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_level` (`id`, `code`, `name`, `sort`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385498145112069, 'four_level', '四级', 4, 0, '1', '2022-11-04 09:57:15', '1', '2022-11-04 09:57:18') ON DUPLICATE KEY UPDATE `update_time` = NOW();


-- ----------------------------
-- Table structure for sat_alarm_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `sat_alarm_type`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_name_tenant_deleted`(`name`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sat_alarm_type
-- ----------------------------
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367810, 'SECURITY_MONITORING', '安防监控报警', 'SECURITY_MONITORING', '安防监控', 0, '1', '2022-11-04 14:45:24', '1', '2022-11-04 14:45:26') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367811, 'ENERGY_MANAGE', '能耗报警', 'ENERGY_MANAGE', '能耗管理', 0, '1', '2022-11-25 13:40:48', '1', '2022-11-25 13:40:52') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367812, 'EQUIPMENT_SUPERVISION', '健康报警', 'EQUIPMENT_SUPERVISION', '健康报警', 0, '1', '2022-11-25 13:41:25', '1', '2022-11-25 13:41:28') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367813, 'ELECTRICMANAGE', '电气管理', 'ELECTRICMANAGE', '电气管理', 0, '1', '2023-01-04 14:33:21', '1', '2023-01-04 14:33:24') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367814, 'ROTATINGMANAGE', '动环报警', 'ROTATINGMANAGE', '动环报警', 0, '1', '2023-01-04 14:33:55', '1', '2023-01-04 14:33:57') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367815, 'AIRCONDITIONER', '空调管理', 'AIRCONDITIONER', '空调管理', 0, '1', '2023-01-04 14:34:16', '1', '2023-01-04 14:34:18') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367816, 'FIRECONTROL', '消防管理', 'FIRECONTROL', '消防管理', 0, '1', '2023-01-04 14:34:47', '1', '2023-01-04 14:34:49') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367817, 'WATERSUPPLYDRAINAGE', '给排水', 'WATERSUPPLYDRAINAGE', '给排水', 0, '1', '2023-01-04 14:35:08', '1', '2023-01-04 14:35:09') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367818, 'AIRSUPPLYANDEXHAUST', '送排风', 'AIRSUPPLYANDEXHAUST', '送排风', 0, '1', '2023-01-04 14:35:30', '1', '2023-01-04 14:35:33') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367819, 'ELECTRONIC_PATROL', '电子巡更', 'ELECTRONIC_PATROL', '电子巡更', 0, '1', '2023-06-01 15:52:07', '1', '2023-06-01 15:52:12') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367820, 'INFORMATION_PUBLISH', '信息发布', 'INFORMATION_PUBLISH', '信息发布', 0, '1', '2023-06-29 18:09:22', '1', '2023-06-29 18:09:25') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367821, 'MEDICAL_INTERCOM', '医护对讲', 'MEDICAL_INTERCOM', '医护对讲', 0, '1', '2023-06-29 18:09:55', '1', '2023-06-29 18:09:57') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367822, 'LIFT_MANAGEMENT', '电梯管理', 'LIFT_MANAGEMENT', '电梯管理', 0, '1', '2023-06-29 18:10:26', '1', '2023-06-29 18:10:28') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367823, 'INTRUDER_ALARM', '入侵报警', 'INTRUDER_ALARM', '入侵报警', 0, '1', '2023-06-29 18:10:53', '1', '2023-06-29 18:10:56') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367824, 'MEDICAL_GAS', '医用气体', 'MEDICAL_GAS', '医用气体', 0, '1', '2023-06-29 18:11:25', '1', '2023-06-29 18:11:27') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367825, 'TRIAGE_RANK', '分诊排号', 'TRIAGE_RANK', '分诊排号', 0, '1', '2023-06-29 18:11:47', '1', '2023-06-29 18:11:50') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367826, 'SURGICAL_INSTRUCTION', '手术示教', 'SURGICAL_INSTRUCTION', '手术示教', 0, '1', '2023-06-29 18:12:10', '1', '2023-06-29 18:12:12') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367827, 'ICU_MONITORING', 'ICU监测', 'ICU_MONITORING', 'ICU监测', 0, '1', '2023-06-29 18:12:43', '1', '2023-06-29 18:12:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367828, 'MEDICAL_CRYOEXTRACTO', '医用冷冻冷藏', 'MEDICAL_CRYOEXTRACTO', '医用冷冻冷藏', 0, '1', '2023-06-29 18:13:05', '1', '2023-06-29 18:13:07') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367829, 'MEAT_TRADE', '肉类交易', 'MEAT_TRADE', '肉类交易', 0, '1', '2023-08-09 11:28:41', '1', '2023-08-09 11:28:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367830, 'GAS_MANAGEMENT', '用气管理', 'GAS_MANAGEMENT', '用气管理', 0, '1', '2023-09-22 15:34:14', '1', '2023-09-22 15:34:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367831, 'COOLINGWATER', '冷冻水', 'COOLINGWATER', '冷冻水', 0, '1', '2023-09-22 15:34:14', '1', '2023-09-22 15:34:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367832, 'COLDANDHEATSOURCES', '冷热源', 'COLDANDHEATSOURCES', '冷热源', 0, '1', '2023-09-22 15:34:14', '1', '2023-09-22 15:34:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367833, 'PERIMETER_PREVENTION', '周界防范', 'PERIMETER_PREVENTION', '周界防范', 0, '1', '2023-10-30 15:03:01', '1', '2023-10-30 15:03:06') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367834, 'BOLIER', '锅炉系统', 'BOLIER', '锅炉系统', 0, '1', '2023-10-30 15:03:01', '1', '2023-10-30 15:03:06') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367835, 'WATERPURIFICATION', '净水系统', 'WATERPURIFICATION', '净水系统', 0, '1', '2023-11-10 09:13:54', '1', '2023-11-10 09:13:58') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1546385459813367836, 'LOGISTICS', '物流管理', 'LOGISTICS', '物流管理', 0, '1', '2023-11-10 09:13:54', '1', '2023-11-10 09:13:58') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1729040405851611138, 'FRESH_AIR_MANAGEMENT', '新风管理', 'FRESH_AIR_MANAGEMENT', '新风管理', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1729040873336152066, 'SECURITY_CHECKP', '安检通道', 'SECURITY_CHECKP', '安检通道', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1729041291374043138, 'PASSENGER_FLOW_ANAL', '客流分析', 'PASSENGER_FLOW_ANAL', '客流分析', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1734113900646178817, 'CLEANCONDITIONING', '洁净空调', 'CLEANCONDITIONING', '洁净空调', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1734113900646178818, 'SEWAGE_TREATMENT', '污水处理', 'SEWAGE_TREATMENT', '污水处理', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1734113900646178819, 'MEDICAL-REFRIGERATIO', '医用冷藏', 'MEDICAL-REFRIGERATIO', '医用冷藏', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1734113900646178820, 'GARDENS', '园林系统', 'GARDENS', '园林系统', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1734113900646178821, 'EMERGENCY_RESOURCES', '应急资源', 'EMERGENCY_RESOURCES', '应急资源', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1740183143212982274, 'EMERGENCY_CALL', '紧急电话', 'EMERGENCY_CALL', '紧急电话', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1740186048959684610, 'ENVIRONMENT_MONITORI', '环境监控', 'ENVIRONMENT_MONITORI', '环境监控', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1740190953762004994, 'TRAFFIC_LIGHT_SYS', '交通灯系统', 'TRAFFIC_LIGHT_SYS', '交通灯系统', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1740193326190370817, 'TRAFFIC_CONTROL', '交通控制', 'TRAFFIC_CONTROL', '交通控制', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1740195150947491842, 'TRAFFIC_FLOW_MONIRIN', '车流量监测', 'TRAFFIC_FLOW_MONIRIN', '车流量监测', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1740195150947491843, 'OPERATING_ROOM', '洁净手术室', 'OPERATING_ROOM', '洁净手术室', 0, '1', '2024-01-08 11:39:00', '1', '2024-01-08 11:39:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1740195150947491844, 'ENERGY_CONSUMPTION', '能碳管理', 'ENERGY_CONSUMPTION', '能碳管理', 0, '1', '2024-03-29 17:39:31', '1', '2024-03-29 17:39:34') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1740195150947491845, 'ULTRA_FINE_DRY_POWDE', '超细干粉', 'ULTRA_FINE_DRY_POWDE', '超细干粉', 0, '1', '2024-03-29 17:39:31', '1', '2024-03-29 17:39:34') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1807616839044304900, 'VISITOR_MANAGEMENT', '访客管理', 'VISITOR_MANAGEMENT', '访客管理', 0, '1', '2024-08-15 11:46:59', '1', '2024-08-15 11:47:05') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1807616839044304901, 'BOOKS_MANAGEMENT', '图书管理', 'BOOKS_MANAGEMENT', '图书管理', 0, '1', '2024-08-15 11:47:01', '1', '2024-08-15 11:47:07') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1807616839044304902, 'VIDEO_INTERCOM', '可视对讲', 'VIDEO_INTERCOM', '可视对讲', 0, '1', '2024-10-30 10:06:16', '1', '2024-10-30 10:06:19') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1807616839044304903, 'ELECTRlC_VEHICLE', '电动车管理', 'ELECTRlC_VEHICLE', '电动车管理', 0, '1', '2025-03-18 20:51:07', '1', '2025-03-18 20:51:10') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1807616839044304904, 'PARKINGMANAGE', '停车管理', 'PARKINGMANAGE', '停车管理', 0, '1', '2025-05-15 09:42:29', '1', '2025-05-15 09:42:32') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1807616839044304905, 'BROADCAST_CENTER', '广播中心', 'BROADCAST_CENTER', '广播中心', 0, '1', '2025-05-15 09:52:16', '1', '2025-05-15 09:52:19') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1807616839044304906, 'SOLAR_ENERGY', '太阳能热水管理', 'SOLAR_ENERGY', '太阳能热水管理', 0, '1', '2025-05-20 10:12:22', '1', '2025-05-20 10:12:25') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES(1807616839044304907, 'LEAKAGE_DETECTION', '漏水检测', 'LEAKAGE_DETECTION', '漏水检测', 0, '1', '2025-03-18 20:51:07', '1', '2025-05-22 17:13:33') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES(1807616839044304908, 'CUSTOMER_CONTROL', '客控管理', 'CUSTOMER_CONTROL', '客控管理', 0, '1', '2025-03-18 20:51:07', '1', '2025-05-22 17:13:33') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES(1807616839044304909, 'DOORMANAGE', '门禁管理', 'DOORMANAGE', '门禁管理', 0, '1', '2025-05-15 09:52:16', '1', '2025-05-15 09:52:19') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES(1807616839044304910, 'IPTV_MANAGEMENT', 'IPTV管理', 'IPTV_MANAGEMENT', 'IPTV管理', 0, '1', '2025-05-15 09:52:16', '1', '2025-05-15 09:52:19') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES(1807616839044304911, 'TRAFFIC_DETECTION', '人流量检测', 'TRAFFIC_DETECTION', '人流量检测', 0, '1', '2025-12-25 09:52:16', '1', '2025-12-25 09:52:19') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES(1807616839044304912, 'BUILDING_SUBSIDENCE', '建筑沉降', 'BUILDING_SUBSIDENCE', '建筑沉降', 0, '1', '2025-12-25 09:52:16', '1', '2025-12-25 09:52:19') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES(1807616839044304913, 'GARBAGE_BIN_MANAGEME', '垃圾桶管理', 'GARBAGE_BIN_MANAGEME', '垃圾桶管理', 0, '1', '2025-12-25 09:52:16', '1', '2025-12-25 09:52:19') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `sat_alarm_type` (`id`, `code`, `name`, `application_code`, `application_name`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES(1807616839044304914, 'KEY_PERSONNEL', '重点人员管理', 'KEY_PERSONNEL', '重点人员管理', 0, 'admin', '2026-05-27 10:00:00', 'admin', '2026-05-27 10:00:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for sat_alarm_type_status
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `sat_alarm_type_status`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alarm_type_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警类型编号',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态编号',
  `initialized` tinyint(1) NULL DEFAULT NULL COMMENT '是否是初始化的状态',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警类型允许的状态配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sat_alarm_type_status
-- ----------------------------

-- ----------------------------
-- Table structure for sat_alarm_type_status_operator
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `sat_alarm_type_status_operator`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alarm_type_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警类型编号',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态编号',
  `operator` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警操作类型编号',
  `operator_after_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作后的状态编号',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_type_status_operator_operator_after_status`(`alarm_type_code`, `status`, `operator`, `operator_after_status`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警类型允许的状态操作配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_alarm_type_terminal
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `sat_alarm_type_terminal`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alarm_type_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '报警类型主键',
  `terminal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问端',
  `terminal_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问端名称',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_alarm_type_id_terminal_deleted`(`alarm_type_id`, `terminal`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警类型访问端' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for weight_allot
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `weight_allot`  (
  `id` bigint(11) UNSIGNED NOT NULL COMMENT 'id',
  `index_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指标类型',
  `index_item` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指标项',
  `weight` int(11) NULL DEFAULT NULL COMMENT '权重占比',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权重分配' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
