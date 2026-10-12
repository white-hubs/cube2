-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'device_notify_config') AS table_exists;
CREATE TABLE `device_notify_config` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `rule_id` bigint NOT NULL COMMENT '规则id',
  `device_id` bigint NOT NULL COMMENT '设备id',
  `notify_config` text COMMENT '通知配置',
  `tenant` int NOT NULL COMMENT '租户编号',
  `deleted` bigint NOT NULL DEFAULT '0' COMMENT '是否已删除(0:未删除 1:已删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_device_notify_config_rule_id` (`rule_id`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备通知配置表';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'device_notify_rule') AS table_exists;
CREATE TABLE `device_notify_rule` (
  `id` bigint NOT NULL COMMENT '设备通知规则id',
  `device_id` bigint NOT NULL COMMENT '设备id',
  `iot_rule_id` bigint NOT NULL COMMENT '物联规则id',
  `enabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用(0:禁用 1:启用)',
  `tenant` int NOT NULL COMMENT '租户编号',
  `deleted` bigint NOT NULL DEFAULT '0' COMMENT '是否已删除(0:未删除 1:已删除)',
  `create_by` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_device_notify_rule_device_id` (`device_id`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备通知规则表';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'device_notify_rule_condition') AS table_exists;
CREATE TABLE `device_notify_rule_condition` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `condition_type` varchar(50) DEFAULT NULL COMMENT '条件类型(事件或者自定条件)',
  `device_event_id` varchar(50) DEFAULT NULL COMMENT '设备事件主键',
  `device_event_code` varchar(100) DEFAULT NULL COMMENT '设备事件编号',
  `device_event_name` varchar(100) DEFAULT NULL COMMENT '设备事件名称',
  `device_event_type` varchar(50) DEFAULT NULL COMMENT '设备事件类型',
  `logical_op` varchar(10) DEFAULT NULL COMMENT '逻辑运算符（or and）',
  `rule_id` bigint NOT NULL COMMENT '设备通知规则主键',
  `tenant` int NOT NULL COMMENT '租户编号',
  `deleted` bigint NOT NULL DEFAULT '0' COMMENT '是否已删除(0:未删除 1:已删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备通知规则条件表';

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'device_notify_rule_condition_exp') AS table_exists;
CREATE TABLE `device_notify_rule_condition_exp` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `rule_id` bigint NOT NULL COMMENT '规则主键',
  `condition_id` bigint NOT NULL COMMENT '条件主键',
  `prop_code` varchar(50) DEFAULT NULL COMMENT '属性编号',
  `prop_name` varchar(100) DEFAULT NULL COMMENT '属性名称',
  `prop_unit` varchar(20) DEFAULT NULL COMMENT '属性单位',
  `op` varchar(10) DEFAULT NULL COMMENT '比较符（= >=等）',
  `oprand` varchar(200) DEFAULT NULL COMMENT '属性值',
  `delay_time` int DEFAULT NULL COMMENT '延时时间',
  `start_delay_time` int DEFAULT NULL COMMENT '延迟开始时间',
  `end_delay_time` int DEFAULT NULL COMMENT '延迟结束时间',
  `tenant` int NOT NULL COMMENT '租户编号',
  `deleted` bigint NOT NULL DEFAULT '0' COMMENT '是否已删除(0:未删除 1:已删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备通知规则条件表达式表';

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'device_notify_rule_trigger_record') AS table_exists;
CREATE TABLE `device_notify_rule_trigger_record` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `rule_id` bigint NOT NULL COMMENT '规则id',
  `device_id` bigint NOT NULL COMMENT '设备id',
  `device_code` varchar(100) DEFAULT NULL COMMENT '设备编号',
  `device_name` varchar(100) DEFAULT NULL COMMENT '设备名称',
  `product_category_name` varchar(200) DEFAULT NULL COMMENT '产品分类名称',
  `prod_name` varchar(200) DEFAULT NULL COMMENT '产品名称',
  `trigger_content` varchar(2000) DEFAULT NULL COMMENT '触发内容',
  `trigger_time` datetime DEFAULT NULL COMMENT '触发时间',
  `read_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '读取状态(0:未读 1:已读)',
  `tenant` int NOT NULL COMMENT '租户编号',
  `deleted` bigint NOT NULL DEFAULT '0' COMMENT '是否已删除(0:未删除 1:已删除)',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备通知规则触发记录表';