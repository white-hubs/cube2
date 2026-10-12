CREATE DATABASE IF NOT EXISTS `cube_customized_dingtalkuserworkingstatussync` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube_customized_dingtalkuserworkingstatussync`;

-- cube_customized_dingtalkuserworkingstatussync.databasechangelog definition

CREATE TABLE IF NOT EXISTS `databasechangelog` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
    'ALTER TABLE `cube_customized_dingtalkuserworkingstatussync`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- cube_customized_dingtalkuserworkingstatussync.databasechangeloglock definition

CREATE TABLE IF NOT EXISTS `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
INSERT IGNORE INTO cube_customized_dingtalkuserworkingstatussync.databasechangeloglock (ID, LOCKED, LOCKGRANTED, LOCKEDBY) VALUES(1, 0, NULL, NULL);

CREATE TABLE `ding_talk_schedule` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `ding_talk_schedule_id` bigint DEFAULT NULL COMMENT '排班ID',
  `check_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '考勤类型：Onduty：上班打卡, OffDuty：下班打卡',
  `plan_check_time` datetime DEFAULT NULL COMMENT '计划打卡时间',
  `group_id` bigint DEFAULT NULL COMMENT '考勤组ID',
  `userid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户userId',
  `approve_id` bigint DEFAULT NULL COMMENT '排班绑定的审批单ID',
  `work_date` datetime DEFAULT NULL COMMENT '工作日，代表具体哪一天的排班',
  `shift_version` bigint DEFAULT NULL COMMENT '班次版本',
  `shift_id` bigint DEFAULT NULL COMMENT '班次ID，该值为0，表明当天休息',
  `is_rest` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否休息：Y：当天排休, N：当天不休息',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ding_talk_schedule_ding_talk_schedule_id_IDX` (`ding_talk_schedule_id`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='钉钉排班表';

CREATE TABLE `ding_talk_user_ref` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `ding_talk_user_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '钉钉用户userId',
  `system_user_id` bigint DEFAULT NULL COMMENT '系统用户id',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ding_talk_user_ref_ding_talk_user_id_IDX` (`ding_talk_user_id`,`system_user_id`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='钉钉用户关联表';