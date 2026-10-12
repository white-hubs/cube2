-- liquibase formatted sql

-- changeset changsha:door-gas-linkage-config-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'door_gas_linkage_config') AS table_exists;
CREATE TABLE IF NOT EXISTS `door_gas_linkage_config` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `door_device_id` bigint(20) NOT NULL COMMENT '门禁设备ID',
  `gas_linkage_enabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启气体联动',
  `data_valid_seconds` int(11) NOT NULL DEFAULT 60 COMMENT '气体数据有效时长，单位秒',
  `device_config_json` json DEFAULT NULL COMMENT '关联设备配置JSON，包含氧气、二氧化碳、磷化氢配置',
  `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant` int(11) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_door_gas_linkage_config_tenant_device` (`tenant`,`door_device_id`,`deleted`),
  KEY `idx_door_gas_linkage_config_enabled` (`tenant`,`gas_linkage_enabled`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='门禁气体联动配置表';
