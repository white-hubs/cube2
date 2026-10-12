-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'event_ai_result') AS column_exists;
CREATE TABLE `event_ai_result` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `event_record_id` bigint unsigned NOT NULL COMMENT '事件记录主键',
  `origin_result` varchar(500) NOT NULL COMMENT '原始分析结果',
  `result` varchar(50) NOT NULL COMMENT '分析结果(实报、误报)',
  `tenant` int unsigned NOT NULL COMMENT '租户ID',
  `deleted` bigint unsigned DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='事件ai分析结果';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'linkage_event_record') AS column_exists;
CREATE TABLE `linkage_event_record` (
  `id` bigint unsigned NOT NULL COMMENT '主键ID',
  `code` varchar(64) DEFAULT NULL COMMENT '事件编号',
  `name` varchar(128) DEFAULT NULL COMMENT '事件名称',
  `event_id` bigint unsigned NOT NULL COMMENT '事件id',
  `event_time` datetime DEFAULT NULL COMMENT '触发时间',
  `level_id` bigint unsigned NOT NULL COMMENT '触发级别ID',
  `level_code` varchar(64) DEFAULT NULL COMMENT '级别编号',
  `level_name` varchar(64) DEFAULT NULL COMMENT '级别名称',
  `device_id` bigint unsigned NOT NULL COMMENT '设备id',
  `device_code` varchar(64) DEFAULT NULL COMMENT '设备编号',
  `device_name` varchar(128) DEFAULT NULL COMMENT '设备名称',
  `device_area_name` varchar(128) DEFAULT NULL COMMENT '设备区域',
  `device_lnt_and_lat` varchar(64) DEFAULT NULL COMMENT '设备经纬度',
  `device_address` varchar(256) DEFAULT NULL COMMENT '设备位置信息',
  `event_type_id` bigint unsigned DEFAULT NULL COMMENT '事件分类主键',
  `event_type_code` varchar(64) DEFAULT NULL COMMENT '事件分类编号',
  `event_type_name` varchar(64) DEFAULT NULL COMMENT '事件分类名称',
  `application_code` varchar(64) DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(64) DEFAULT NULL COMMENT '应用名称',
  `snapshot` text COMMENT 'iot快照',
  `tenant` int unsigned NOT NULL COMMENT '租户编号',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  PRIMARY KEY (`id`),
  KEY `event_record_event_id_IDX` (`event_id`,`deleted`,`tenant`) USING BTREE,
  KEY `event_record_level_code_IDX` (`level_code`,`tenant`,`deleted`) USING BTREE,
  KEY `event_record_application_code_IDX` (`application_code`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='事件记录表';