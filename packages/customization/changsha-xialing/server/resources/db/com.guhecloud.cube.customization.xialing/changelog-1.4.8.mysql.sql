-- liquibase formatted sql

-- changeset changsha:warehouse-info-cache-ddl-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_info_cache') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_info_cache` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '三方粮仓ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '父级ID',
  `name` varchar(100) DEFAULT NULL COMMENT '粮仓名称',
  `category_name` varchar(100) DEFAULT NULL COMMENT '仓型或品类名称',
  `total_volume` double DEFAULT NULL COMMENT '总容积',
  `quality_grade` varchar(100) DEFAULT NULL COMMENT '储粮品质等级',
  `warehouse_height` double DEFAULT NULL COMMENT '仓房高度',
  `enabled_scan` tinyint(1) DEFAULT NULL COMMENT '是否启用扫描',
  `scan_corn` varchar(100) DEFAULT NULL COMMENT '扫描粮食品种',
  `material_volume` double DEFAULT NULL COMMENT '物料体积',
  `material_weight` double DEFAULT NULL COMMENT '物料重量',
  `usage_rate` double DEFAULT NULL COMMENT '仓容利用率',
  `camera_setting` text DEFAULT NULL COMMENT '摄像头配置',
  `setting` text DEFAULT NULL COMMENT '粮仓配置',
  `device_list_json` longtext DEFAULT NULL COMMENT '三方设备列表JSON',
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
  UNIQUE KEY `uk_wh_info_cache_tenant_wh_deleted` (`tenant`,`warehouse_id`,`deleted`),
  KEY `idx_wh_info_cache_warehouse_id` (`warehouse_id`),
  KEY `idx_wh_info_cache_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='三方粮仓信息缓存表';
