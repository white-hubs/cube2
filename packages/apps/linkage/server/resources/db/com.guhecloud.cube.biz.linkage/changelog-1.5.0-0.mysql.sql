-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'event_action_before') AS column_exists;
CREATE TABLE `event_action_before` (
  `id` bigint unsigned NOT NULL,
  `event_id` bigint unsigned DEFAULT NULL COMMENT '事件主键',
  `linkage_id` bigint unsigned DEFAULT NULL COMMENT '联动预案主键',
  `event_linkage_id` bigint unsigned DEFAULT NULL COMMENT '事件关联预案主键',
  `action_before_type` varchar(50) DEFAULT NULL COMMENT '动作类型',
  `action_before_config` text COMMENT '动作json',
  `tenant` int unsigned DEFAULT NULL,
  `deleted` bigint DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_event_id_tenant_deleted` (`event_id`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='事件动作前置条件配置';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'linkage_action_before') AS column_exists;
CREATE TABLE `linkage_action_before` (
  `id` bigint unsigned NOT NULL,
  `linkage_id` bigint unsigned DEFAULT NULL COMMENT '联动预案主键',
  `action_before_type` varchar(50) CHARACTER SET utf8mb3 DEFAULT NULL COMMENT '动作前置类型',
  `action_before_config` text CHARACTER SET utf8mb3 COMMENT '动作前置json',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '启用状态',
  `tenant` int unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint unsigned DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb3 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb3 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_linkage_id_tenant_deleted` (`linkage_id`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='联动预案关联动作执行前置配置';
