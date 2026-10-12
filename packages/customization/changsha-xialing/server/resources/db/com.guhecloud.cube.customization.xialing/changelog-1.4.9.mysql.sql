-- liquibase formatted sql

-- changeset changsha:warehouse-temperature-alert-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_alert_global_config';
CREATE TABLE IF NOT EXISTS `warehouse_temperature_alert_global_config` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `consecutive_days` int(11) DEFAULT NULL COMMENT '连续上升天数',
  `min_daily_increase` decimal(10,2) DEFAULT NULL COMMENT '日均最小增幅',
  `warning_threshold` decimal(10,2) DEFAULT NULL COMMENT '预警阈值',
  `approach_margin` decimal(10,2) DEFAULT NULL COMMENT '逼近余量',
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粮仓温度趋势预警全局默认配置';

-- changeset changsha:warehouse-temperature-alert-ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_alert_config';
CREATE TABLE IF NOT EXISTS `warehouse_temperature_alert_config` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `use_global_default` tinyint(1) DEFAULT 1 COMMENT '是否使用全局默认',
  `consecutive_days` int(11) DEFAULT NULL COMMENT '连续上升天数',
  `min_daily_increase` decimal(10,2) DEFAULT NULL COMMENT '日均最小增幅',
  `warning_threshold` decimal(10,2) DEFAULT NULL COMMENT '预警阈值',
  `approach_margin` decimal(10,2) DEFAULT NULL COMMENT '逼近余量',
  `popup_enabled` tinyint(1) DEFAULT 1 COMMENT '弹窗开关',
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
  UNIQUE KEY `uk_warehouse_temperature_alert_config_warehouse_id` (`warehouse_id`,`tenant`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粮仓温度趋势预警单仓配置';

-- changeset changsha:warehouse-temperature-alert-ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_sample';
CREATE TABLE IF NOT EXISTS `warehouse_temperature_sample` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '粮仓ID',
  `sample_time` datetime NOT NULL COMMENT '采样时间',
  `max_temperature` decimal(10,2) DEFAULT NULL COMMENT '仓内最高温',
  `sample_count` int(11) DEFAULT NULL COMMENT '参与计算的测温点数量',
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
  UNIQUE KEY `uk_warehouse_temperature_sample_time` (`warehouse_id`,`sample_time`,`tenant`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粮仓温度趋势采样明细';

-- changeset changsha:warehouse-temperature-alert-ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_daily_avg_max';
CREATE TABLE IF NOT EXISTS `warehouse_temperature_daily_avg_max` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '粮仓ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `avg_max_temperature` decimal(10,2) DEFAULT NULL COMMENT '日平均最高温',
  `sample_count` int(11) DEFAULT NULL COMMENT '采样次数',
  `complete` tinyint(1) DEFAULT 0 COMMENT '是否完整日数据',
  `calculated_at` datetime DEFAULT NULL COMMENT '计算时间',
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
  UNIQUE KEY `uk_warehouse_temperature_daily_avg_max_date` (`warehouse_id`,`stat_date`,`tenant`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粮仓温度趋势日均最高温';

-- changeset changsha:warehouse-temperature-alert-ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'warehouse_temperature_alert_record';
CREATE TABLE IF NOT EXISTS `warehouse_temperature_alert_record` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '粮仓ID',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓名称快照',
  `grain_type` varchar(100) DEFAULT NULL COMMENT '品种快照',
  `triggered_at` datetime NOT NULL COMMENT '触发时间',
  `current_avg_temperature` decimal(10,2) DEFAULT NULL COMMENT '触发时日平均最高温',
  `warning_threshold` decimal(10,2) DEFAULT NULL COMMENT '阈值',
  `approach_margin` decimal(10,2) DEFAULT NULL COMMENT '逼近余量',
  `distance_to_threshold` decimal(10,2) DEFAULT NULL COMMENT '距阈值差值',
  `daily_increase` decimal(10,2) DEFAULT NULL COMMENT '日均增幅',
  `consecutive_days` int(11) DEFAULT NULL COMMENT '连续上升天数',
  `status` varchar(32) DEFAULT NULL COMMENT 'ACTIVE/CLEARED',
  `cleared_at` datetime DEFAULT NULL COMMENT '消除时间',
  `cleared_reason` varchar(32) DEFAULT NULL COMMENT 'TREND_REVERSED/LEFT_DANGER_ZONE',
  `popup_enabled` tinyint(1) DEFAULT 1 COMMENT '弹窗开关快照',
  `popup_pushed` tinyint(1) DEFAULT 0 COMMENT '是否已弹窗',
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
  KEY `idx_warehouse_temperature_alert_record_query` (`triggered_at`,`status`,`warehouse_id`,`tenant`,`deleted`),
  KEY `idx_warehouse_temperature_alert_record_active` (`warehouse_id`,`status`,`tenant`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粮仓温度趋势预警记录';
