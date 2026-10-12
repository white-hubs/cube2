-- liquibase formatted sql

-- changeset changsha:warehouse-cable-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_cable') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_cable` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `cable_no` varchar(100) NOT NULL COMMENT '业务电缆编号',
  `point_count` int(11) NOT NULL COMMENT '点位数量',
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
  UNIQUE KEY `uk_wh_cable_tenant_wh_no` (`tenant`,`warehouse_id`,`cable_no`,`deleted`),
  KEY `idx_wh_cable_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粮仓电缆表';

-- changeset changsha:warehouse-cable-point-ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_cable_point') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_cable_point` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `cable_id` bigint(20) unsigned NOT NULL COMMENT '电缆ID',
  `point_no` int(11) NOT NULL COMMENT '点位序号',
  `device_id` bigint(20) DEFAULT NULL COMMENT '映射的设备中心设备ID',
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
  UNIQUE KEY `uk_wh_cable_point_tenant_cable_point` (`tenant`,`cable_id`,`point_no`,`deleted`),
  UNIQUE KEY `uk_wh_cable_point_device_id` (`device_id`),
  KEY `idx_wh_cable_point_warehouse_id` (`warehouse_id`),
  KEY `idx_wh_cable_point_cable_id` (`cable_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粮仓电缆点位设备映射表';
