CREATE DATABASE IF NOT EXISTS `cube_srv_gen_medical_cleanliness` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube_srv_gen_medical_cleanliness`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- cube_srv_gen_medical_cleanliness.databasechangelog definition

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
    'ALTER TABLE `cube_srv_gen_medical_cleanliness`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

INSERT IGNORE INTO cube_srv_gen_medical_cleanliness.databasechangelog
(ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, EXECTYPE, MD5SUM, DESCRIPTION, COMMENTS, TAG, LIQUIBASE, CONTEXTS, LABELS, DEPLOYMENT_ID)
VALUES('ddl-1', 'lijie', 'classpath*:db/com.guhecloud.cube.srv.gen.medical.cleanliness/mysql/changelog-1.1.0-1.mysql.sql', '2024-03-29 11:25:21', 1, 'EXECUTED', '8:d1c7839b7f871273d0014cb388708300', 'sql', '', NULL, '4.3.1', NULL, NULL, '1682720850');
INSERT IGNORE INTO cube_srv_gen_medical_cleanliness.databasechangelog
(ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, EXECTYPE, MD5SUM, DESCRIPTION, COMMENTS, TAG, LIQUIBASE, CONTEXTS, LABELS, DEPLOYMENT_ID)
VALUES('ddl-5', 'lijie', 'classpath*:db/com.guhecloud.cube.srv.gen.medical.cleanliness/mysql/changelog-1.1.0-1.mysql.sql', '2024-03-29 11:25:21', 2, 'EXECUTED', '8:34fee4cf8862946b67baa54e52c23408', 'sql', '', NULL, '4.3.1', NULL, NULL, '1682720850');

-- cube_srv_gen_medical_cleanliness.databasechangeloglock definition

CREATE TABLE IF NOT EXISTS `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT IGNORE INTO cube_srv_gen_medical_cleanliness.databasechangeloglock
(ID, LOCKED, LOCKGRANTED, LOCKEDBY)
VALUES(1, 0, NULL, NULL);
-- cube_srv_gen_medical_cleanliness.medical_alarm_statistics definition

CREATE TABLE IF NOT EXISTS `medical_alarm_statistics` (
  `id` bigint(20) NOT NULL,
  `tenant_name` varchar(255) DEFAULT NULL COMMENT '租户名称',
  `tenant` int(10) DEFAULT NULL COMMENT '租户id',
  `device_prod_category_name` varchar(32) DEFAULT NULL COMMENT '设备品类名称',
  `device_prod_category_code` varchar(50) DEFAULT NULL COMMENT '设备品类编码',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `total_num` bigint(20) DEFAULT NULL COMMENT '报警总数',
  `un_confirmed_num` bigint(20) DEFAULT NULL COMMENT '待接收报警数',
  `progress_confirmed_num` bigint(20) DEFAULT NULL COMMENT '处理中报警数',
  `suspend_num` bigint(20) DEFAULT NULL COMMENT '已挂起报警数',
  `handler_num` bigint(20) DEFAULT NULL COMMENT '已处理报警数',
  `device_code` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '对象编号',
  `device_name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '对象名称',
  `acquisition_date` varchar(255) NOT NULL DEFAULT '' COMMENT '采集日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- cube_srv_gen_medical_cleanliness.medical_device_statistics definition

CREATE TABLE IF NOT EXISTS `medical_device_statistics` (
  `id` bigint(20) NOT NULL,
  `tenant_name` varchar(255) NOT NULL COMMENT '租户名称',
  `tenant` int(10) NOT NULL COMMENT '租户id',
  `device_prod_category_name` varchar(32) DEFAULT NULL COMMENT '设备品类名称',
  `device_prod_category_code` varchar(50) DEFAULT NULL COMMENT '设备品类编码',
  `total_number` int(11) DEFAULT NULL,
  `normal_number` int(11) DEFAULT NULL,
  `offline_number` int(11) DEFAULT NULL,
  `alarm_number` bigint(11) DEFAULT NULL,
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `acquisition_date` date DEFAULT NULL COMMENT '采集日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- cube_srv_gen_medical_cleanliness.medical_rule definition

CREATE TABLE IF NOT EXISTS `medical_rule` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL COMMENT '规则名称',
  `device_ids` text,
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- cube_srv_gen_medical_cleanliness.rule_data_orrection definition

CREATE TABLE IF NOT EXISTS `rule_data_orrection` (
  `id` bigint(20) NOT NULL,
  `device_ids` varchar(255) DEFAULT NULL COMMENT '设备id',
  `rule_norm_code` varchar(20) NOT NULL COMMENT '指标编码',
  `value` varchar(255) NOT NULL,
  `status` varchar(50) NOT NULL COMMENT '状态',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- cube_srv_gen_medical_cleanliness.rule_norm definition

CREATE TABLE IF NOT EXISTS `rule_norm` (
  `id` bigint(20) NOT NULL,
  `rule_id` bigint(20) NOT NULL,
  `norm_name` varchar(255) DEFAULT NULL COMMENT '指标名称',
  `unit` varchar(255) DEFAULT NULL COMMENT '单位',
  `norm_code` varchar(255) DEFAULT NULL COMMENT '指标编码',
  `display` int(11) DEFAULT NULL COMMENT '是否展示',
  `upper_limit` decimal(50,10) DEFAULT NULL COMMENT '上限',
  `lower_limit` decimal(50,10) DEFAULT NULL COMMENT '下限',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `sort` int(10) DEFAULT NULL COMMENT '排序',
  `show_area` varchar(255) DEFAULT NULL COMMENT '区域',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
