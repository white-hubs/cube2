CREATE DATABASE IF NOT EXISTS`cube-srv-gen-todo` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-srv-gen-todo`;
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
    'ALTER TABLE `cube-srv-gen-todo`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.todo/test001.mysql.sql', '2024-06-05 10:18:24', 1, 'EXECUTED', '8:549a0fd21dd378a2e1815ec440b3b808', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553904802');

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
-- Table structure for todo_business_url
-- ----------------------------

CREATE TABLE IF NOT EXISTS `todo_business_url`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型名称',
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
  UNIQUE INDEX `idx_business_type_terminal_deleted`(`business_type`, `terminal`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of todo_business_url
-- ----------------------------
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (1, 'HIDDENDANGER', '隐患排查', 'bimops-manage-app', '/hiddenDangerDetails', 0, '1', '2022-08-02 14:04:31', '1', '2022-08-02 14:04:33');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (2, 'FAULT', '故障报修', 'bimops-manage-app', '/faultReportDetails', 0, '1', '2022-08-02 14:05:20', '1', '2022-08-02 14:05:23');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (3, 'MAINTENACE', '维保作业', 'bimops-manage-app', '/maintenanceTaskDetails', 0, '1', '2022-08-02 14:05:53', '1', '2022-08-02 14:05:56');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (4, 'OUTSOURCE_REPAIR', '委外维修', 'bimops-manage-app', '/outsourceRepairDetails', 0, '1', '2022-08-02 14:06:55', '1', '2022-08-02 14:06:58');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (5, 'INSPECTION', '日常巡检', 'bimops-manage-app', '/patrolInspectTaskDetails', 0, '1', '2022-08-02 14:07:25', '1', '2022-08-02 14:07:27');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (6, 'retrofit_ticket', '更新改造', 'bimops-manage-app', '/renovationDetails', 0, '1', '2022-08-02 14:09:08', '1', '2022-08-02 14:09:10');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (7, 'HIDDENDANGER', '隐患排查', 'bimops-manage-web', '/portal/problems/problemsDetails', 0, '1', '2022-08-02 15:07:42', '1', '2022-08-02 15:07:44');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (8, 'FAULT', '故障报修', 'bimops-manage-web', '/portal/fault/faultDetails', 0, '1', '2022-08-02 15:08:05', '1', '2022-08-02 15:08:08');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (9, 'MAINTENACE', '维保作业', 'bimops-manage-web', '/portal/maintenance/maintenanceTaskDetails', 0, '1', '2022-08-02 15:08:26', '1', '2022-08-02 15:08:29');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (10, 'OUTSOURCE_REPAIR', '委外维修', 'bimops-manage-web', '/portal/outsourcing/repairListDetails', 0, '1', '2022-08-02 15:08:44', '1', '2022-08-02 15:08:46');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (11, 'INSPECTION', '日常巡检', 'bimops-manage-web', '/portal/inspection/inspectionTaskDetails', 0, '1', '2022-08-02 15:09:21', '1', '2022-08-02 15:09:24');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (12, 'retrofit_ticket', '更新改造', 'bimops-manage-web', '/portal/renovation/renovationDetails', 0, '1', '2022-08-02 15:09:46', '1', '2022-08-02 15:09:48');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (14, 'SECURITY_MONITORING', '安防监控报警', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-03-08 18:28:31', '1', '2023-03-08 18:28:34');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (15, 'ENERGY_MANAGE', '能耗报警', 'bimops-manage-web', '/portal/energyManagement/energyAlarmDetail', 0, '1', '2023-03-08 18:29:35', '1', '2023-03-08 18:29:38');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (16, 'EQUIPMENT_SUPERVISION', '健康报警', 'bimops-manage-web', '/portal/equipmentSupervision/healthyAlarmDetails', 0, '1', '2023-03-08 18:33:01', '1', '2023-03-08 18:33:03');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (17, 'ELECTRICMANAGE', '电气报警', 'bimops-manage-web', '/portal/genericEquipmentMonitor/alarmDetail', 0, '1', '2023-03-08 18:33:45', '1', '2023-03-08 18:33:48');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (18, 'ROTATINGMANAGE', '动环报警', 'bimops-manage-web', '/portal/genericEquipmentMonitor/alarmDetail', 0, '1', '2023-03-08 18:34:19', '1', '2023-03-08 18:34:22');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (19, 'AIRCONDITIONER', '空调报警', 'bimops-manage-web', '/portal/genericEquipmentMonitor/alarmDetail', 0, '1', '2023-03-08 18:34:56', '1', '2023-03-08 18:34:58');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (20, 'FIRECONTROL', '消防报警', 'bimops-manage-web', '/portal/genericEquipmentMonitor/alarmDetail', 0, '1', '2023-03-08 18:35:41', '1', '2023-03-08 18:35:44');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (21, 'WATERSUPPLYDRAINAGE', '给排水报警', 'bimops-manage-web', '/portal/genericEquipmentMonitor/alarmDetail', 0, '1', '2023-03-08 18:36:15', '1', '2023-03-08 18:36:18');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (22, 'AIRSUPPLYANDEXHAUST', '送排风报警', 'bimops-manage-web', '/portal/genericEquipmentMonitor/alarmDetail', 0, '1', '2023-03-08 18:36:49', '1', '2023-03-08 18:36:52');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (23, 'SECURITY_MONITORING', '安防监控报警', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-03-08 18:28:31', '1', '2023-03-08 18:28:34');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (24, 'ENERGY_MANAGE', '能耗报警', 'bimops-manage-app', '/energyAlarmDetails', 0, '1', '2023-03-08 18:29:35', '1', '2023-03-08 18:29:38');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (25, 'EQUIPMENT_SUPERVISION', '健康报警', 'bimops-manage-app', '/deviceAlarmDetails', 0, '1', '2023-03-08 18:33:01', '1', '2023-03-08 18:33:03');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (26, 'ELECTRICMANAGE', '电气报警', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-03-08 18:33:45', '1', '2023-03-08 18:33:48');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (27, 'ROTATINGMANAGE', '动环报警', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-03-08 18:34:19', '1', '2023-03-08 18:34:22');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (28, 'AIRCONDITIONER', '空调报警', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-03-08 18:34:56', '1', '2023-03-08 18:34:58');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (29, 'FIRECONTROL', '消防报警', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-03-08 18:35:41', '1', '2023-03-08 18:35:44');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (30, 'WATERSUPPLYDRAINAGE', '给排水报警', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-03-08 18:36:15', '1', '2023-03-08 18:36:18');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (31, 'AIRSUPPLYANDEXHAUST', '送排风报警', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-03-08 18:36:49', '1', '2023-03-08 18:36:52');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (33, 'RETROFIT', '更新改造', 'bimops-manage-web', '/portal/renovation/renovationDetails', 0, '1', '2022-08-02 15:09:46', '1', '2022-08-02 15:09:48');
INSERT IGNORE INTO `todo_business_url`
(id, business_type, business_type_name, terminal, url, deleted, create_by, create_time, update_by, update_time)
VALUES (34, 'ELECTRONIC_PATROL', '电子巡更', 'bimops-manage-web', '/portal/electronic-patrol/task/detiles', 0, '1', '2023-06-28 16:28:32', NULL, NULL);

-- ----------------------------
-- Table structure for todo_person
-- ----------------------------

CREATE TABLE IF NOT EXISTS `todo_person`  (
  `id` bigint(20) NOT NULL,
  `todo_task_id` bigint(20) NULL DEFAULT NULL COMMENT '待办任务主键',
  `business_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户主键',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '待办状态',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代办人' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of todo_person
-- ----------------------------

-- ----------------------------
-- Table structure for todo_tag
-- ----------------------------

CREATE TABLE IF NOT EXISTS `todo_tag`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '待办自定义标签' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of todo_tag
-- ----------------------------
INSERT INTO `todo_tag`
(id, code, name, deleted, tenant, create_by, create_time, update_by, update_time)
VALUES (1, 'ALARM_TASK', '报警任务', 0, 1, '1', '2023-02-27 17:49:15', '1', '2023-02-27 17:49:17') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag`
(id, code, name, deleted, tenant, create_by, create_time, update_by, update_time)
VALUES (2, 'APPROVE_TASK', '审批任务', 0, 1, '1', '2023-02-27 17:50:16', '1', '2023-02-27 17:50:18') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag`
(id, code, name, deleted, tenant, create_by, create_time, update_by, update_time)
VALUES (3, 'INSPECT_TASK', '巡检任务', 0, 1, '1', '2023-02-27 17:53:07', '1', '2023-02-27 17:53:23') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag`
(id, code, name, deleted, tenant, create_by, create_time, update_by, update_time)
VALUES (4, 'MAINTENACE_TASK', '维保作业', 0, 1, '1', '2023-02-27 17:53:00', '1', '2023-02-27 17:53:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag`
(id, code, name, deleted, tenant, create_by, create_time, update_by, update_time)
VALUES (5, 'CULTIVATE_TASK', '培训任务', 0, 1, '1', '2023-05-11 17:46:57', '1', '2023-05-11 17:46:59') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag`
(id, code, name, deleted, tenant, create_by, create_time, update_by, update_time)
VALUES (6, 'EXAM_TASK', '考试任务', 0, 1, '1', '2023-05-11 17:47:09', '1', '2023-05-11 17:47:13') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag`
(id, code, name, deleted, tenant, create_by, create_time, update_by, update_time)
VALUES (7, 'ELECTRONIC_PATROL', '电子巡更', 0, 1, '1', '2023-05-11 00:00:00', '1', '2023-05-11 00:00:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag`
(id, code, name, deleted, tenant, create_by, create_time, update_by, update_time)
VALUES (8, 'PROPERTY_MANAGEMENT', '资产管理', 0, 1, '1', '2023-05-11 00:00:00', '1', '2023-05-11 00:00:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag`
(id, code, name, deleted, tenant, create_by, create_time, update_by, update_time)
VALUES (9, 'FOCUS_WORK_REPLY', '督办反馈', 0, 1, '1', '2023-12-19 17:08:53', '1', '2023-12-19 17:08:56') ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for todo_tag_refer_business_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `todo_tag_refer_business_type`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `todo_tag_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签编号',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型',
  `tenant` int(10) UNSIGNED NOT NULL,
  `deleted` bigint(20) UNSIGNED NOT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '待办tag关联的待办任务类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of todo_tag_refer_business_type
-- ----------------------------
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1, 'APPROVE_TASK', 'LEAVE', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (2, 'APPROVE_TASK', 'CHANGE_CLASSES', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (3, 'APPROVE_TASK', 'OVERTIME', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (4, 'APPROVE_TASK', 'LEAVE_THE_POST', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (5, 'APPROVE_TASK', 'REPAIR_CLOCK', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (6, 'INSPECT_TASK', 'INSPECTION', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (7, 'APPROVE_TASK', 'HIDDENDANGER', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (8, 'APPROVE_TASK', 'FAULT', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (9, 'MAINTENACE_TASK', 'MAINTENACE', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (10, 'APPROVE_TASK', 'OUTSOURCE_REPAIR', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (11, 'APPROVE_TASK', 'retrofit_ticket', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (13, 'ALARM_TASK', 'SECURITY_MONITORING', 1, 0, '1', '2023-02-27 18:07:16', '1', '2023-02-27 18:07:16') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (14, 'ALARM_TASK', 'ENERGY_MANAGE', 1, 0, '1', '2023-03-02 15:31:44', '1', '2023-03-02 15:34:11') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (15, 'ALARM_TASK', 'EQUIPMENT_SUPERVISION', 1, 0, '1', '2023-03-02 15:33:58', '1', '2023-03-02 15:34:11') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (16, 'ALARM_TASK', 'ELECTRICMANAGE', 1, 0, '1', '2023-03-02 15:34:00', '1', '2023-03-02 15:34:11') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (17, 'ALARM_TASK', 'ROTATINGMANAGE', 1, 0, '1', '2023-03-02 15:34:02', '1', '2023-03-02 15:34:11') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (18, 'ALARM_TASK', 'AIRCONDITIONER', 1, 0, '1', '2023-03-02 15:34:05', '1', '2023-03-02 15:34:11') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (19, 'ALARM_TASK', 'FIRECONTROL', 1, 0, '1', '2023-03-02 15:34:07', '1', '2023-03-02 15:34:11') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (20, 'ALARM_TASK', 'WATERSUPPLYDRAINAGE', 1, 0, '1', '2023-03-02 15:34:09', '1', '2023-03-02 15:34:11') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (21, 'ALARM_TASK', 'AIRSUPPLYANDEXHAUST', 1, 0, '1', '2023-03-02 15:34:11', '1', '2023-03-02 15:34:11') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (22, 'APPROVE_TASK', 'RETROFIT', 1, 0, '1', '2023-03-10 10:40:00', '1', '2023-03-10 10:40:02') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (23, 'APPROVE_TASK', 'PERSONNEL_RECRUIT', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (24, 'APPROVE_TASK', 'PERSONNEL_INDUCTION', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (25, 'APPROVE_TASK', 'PERSONNEL_PROBATION_ASSESS', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (26, 'APPROVE_TASK', 'PERSONNEL_REGULAR_WORKER', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (27, 'APPROVE_TASK', 'PERSONNEL_TRAIN', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (28, 'APPROVE_TASK', 'PERSONNEL_LEAVE_OFFICE', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (29, 'APPROVE_TASK', 'PERSONNEL_LEAVE_OFFICE_PROCEDURES', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (30, 'APPROVE_TASK', 'PERSONNEL_ROTATION_POST', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (31, 'APPROVE_TASK', 'OFFICIAL_REGULATION', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (32, 'APPROVE_TASK', 'OFFICIAL_SEAL', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (33, 'APPROVE_TASK', 'OFFICIAL_TOPIC', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (34, 'APPROVE_TASK', 'OFFICIAL_DOCUMENT_CIRCULATION', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (35, 'APPROVE_TASK', 'OFFICIAL_DOCUMENT_RECEIVED', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (36, 'APPROVE_TASK', 'OFFICIAL_KEY_WORK', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (37, 'APPROVE_TASK', 'OFFICIAL_URGENT_WORK', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (38, 'APPROVE_TASK', 'OFFICIAL_FOCUS_WORK', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (39, 'APPROVE_TASK', 'OFFICIAL_FOCUS_WORK_REPLY', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (40, 'APPROVE_TASK', 'OFFICIAL_FOCUS_WORK_UPDATE', 1, 0, '1', '2023-03-17 18:18:31', '1', '2023-03-17 18:18:36') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (41, 'APPROVE_TASK', 'WORK_REPORT', 1, 0, '1', '2023-04-07 18:55:25', '1', '2023-04-07 18:55:28') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (42, 'CULTIVATE_TASK', 'CULTIVATE_TASK', 1, 0, '1', '2023-05-11 17:50:02', '1', '2023-05-11 17:50:05') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (43, 'EXAM_TASK', 'EXAM_TASK', 1, 0, '1', '2023-05-11 17:50:18', '1', '2023-05-11 17:50:20') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (46, 'APPROVE_TASK', 'CONSUMABLE_ADD', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (47, 'APPROVE_TASK', 'CONSUMABLE_REDUCE', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (48, 'APPROVE_TASK', 'PROPERTY_ALLOT_PERSONAL', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (49, 'APPROVE_TASK', 'PROPERTY_ALLOT_UNIT', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (50, 'APPROVE_TASK', 'PROPERTY_DEPOSITORY', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (51, 'APPROVE_TASK', 'PROPERTY_WEED_OUT', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (52, 'ELECTRONIC_PATROL', 'ELECTRONIC_PATROL', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (53, 'PROPERTY_MANAGEMENT', 'PROPERTY_TASK', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (54, 'PROPERTY_MANAGEMENT', 'CONSUMABLE_TASK', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (55, 'XRXPROGECT', 'approve_node', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (56, 'XRXPROGECT', 'approve_report', 1, 0, NULL, NULL, NULL, NULL) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `todo_tag_refer_business_type`
(id, todo_tag_code, business_type, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (57, 'FOCUS_WORK_REPLY', 'OFFICIAL_FOCUS_WORK_REPLY', 1, 0, '1', '2023-12-19 18:12:23', '1', '2023-12-19 18:12:27') ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for todo_task
-- ----------------------------

CREATE TABLE IF NOT EXISTS `todo_task`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `task_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `task_time` datetime NULL DEFAULT NULL COMMENT '任务时间',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `business_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务名称',
  `business_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务状态',
  `business_status_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务状态名称',
  `next_business_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一节点状态',
  `next_business_status_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一节点状态名称',
  `handler_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `business_principal` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '业务实体信息',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '待办状态',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `task_del_state` tinyint(1) UNSIGNED NULL DEFAULT 0 COMMENT '任务删除状态',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '待办任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of todo_task
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
