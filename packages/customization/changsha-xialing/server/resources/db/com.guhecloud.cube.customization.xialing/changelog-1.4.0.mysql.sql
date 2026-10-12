-- liquibase formatted sql

-- changeset changsha:warehouse-temperature-trend-config-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_config') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_temperature_trend_config` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `warehouse_agg_type` varchar(20) NOT NULL COMMENT '全仓温度汇总取值规则：MAX-最高温，MIN-最低温',
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
  UNIQUE KEY `uk_wh_temp_trend_cfg_tenant_wh` (`tenant`,`warehouse_id`,`deleted`),
  KEY `idx_wh_temp_trend_cfg_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='仓库粮温趋势配置表';

-- changeset changsha:warehouse-temperature-trend-layer-config-ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_layer_config') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_temperature_trend_layer_config` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `config_id` bigint(20) unsigned NOT NULL COMMENT '粮温趋势主配置ID',
  `layer_code` varchar(20) NOT NULL COMMENT '层级编码：UPPER-上层，MIDDLE-中层，LOWER-下层',
  `layer_name` varchar(50) NOT NULL COMMENT '层级名称',
  `position_name` varchar(50) NOT NULL COMMENT '页面位置标签',
  `high_alarm_threshold` decimal(10,2) NOT NULL COMMENT '超温预警线',
  `low_alarm_threshold` decimal(10,2) NOT NULL COMMENT '低温预警线',
  `layer_agg_type` varchar(20) NOT NULL COMMENT '分层温度汇总取值规则：MAX-最高温，MIN-最低温',
  `sort_no` int(11) NOT NULL COMMENT '排序号',
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
  UNIQUE KEY `uk_wh_temp_trend_layer_tenant_cfg_layer` (`tenant`,`config_id`,`layer_code`,`deleted`),
  KEY `idx_wh_temp_trend_layer_warehouse_id` (`warehouse_id`),
  KEY `idx_wh_temp_trend_layer_config_id` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='仓库粮温趋势分层配置表';

-- changeset changsha:warehouse-temperature-trend-layer-cable-ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_trend_layer_cable') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_temperature_trend_layer_cable` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `config_id` bigint(20) unsigned NOT NULL COMMENT '粮温趋势主配置ID',
  `layer_config_id` bigint(20) unsigned NOT NULL COMMENT '粮温趋势分层配置ID',
  `cable_id` bigint(20) unsigned NOT NULL COMMENT '电缆ID',
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
  UNIQUE KEY `uk_wh_temp_trend_layer_cable_tenant_layer_cable` (`tenant`,`layer_config_id`,`cable_id`,`deleted`),
  KEY `idx_wh_temp_trend_layer_cable_warehouse_id` (`warehouse_id`),
  KEY `idx_wh_temp_trend_layer_cable_config_id` (`config_id`),
  KEY `idx_wh_temp_trend_layer_cable_layer_id` (`layer_config_id`),
  KEY `idx_wh_temp_trend_layer_cable_cable_id` (`cable_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='仓库粮温趋势分层线缆配置表';
