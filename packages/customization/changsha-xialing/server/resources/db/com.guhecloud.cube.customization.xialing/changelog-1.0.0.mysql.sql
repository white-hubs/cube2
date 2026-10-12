-- liquibase formatted sql

-- changeset changsha:radar-warehouse-binding-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'radar_warehouse_binding') AS table_exists;
CREATE TABLE `radar_warehouse_binding` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '粮仓（智慧仓库）ID',
  `radar_device_id` bigint(20) NOT NULL COMMENT '雷达设备ID',
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
  UNIQUE KEY `uk_radar_warehouse_binding_warehouse_id` (`warehouse_id`),
  UNIQUE KEY `uk_radar_warehouse_binding_radar_device_id` (`radar_device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粮仓-雷达绑定表';

-- changeset changsha:warehouse-device-binding-ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_device_binding') AS table_exists;
CREATE TABLE `warehouse_device_binding` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '粮仓（智慧仓库）ID',
  `device_id` bigint(20) NOT NULL COMMENT '业务设备ID',
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
  UNIQUE KEY `uk_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='仓库绑定设备表';

-- changeset changsha:warehouse-goods-history-ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_goods_history') AS table_exists;
CREATE TABLE `warehouse_goods_history` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `radar_device_id` bigint(20) DEFAULT NULL COMMENT '雷达设备ID（与绑定关系一致，用于业务侧按雷达去重）',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库仓库ID',
  `acquisition_date` datetime NOT NULL COMMENT '采集时间',
  `weight_value` decimal(24,6) DEFAULT NULL COMMENT '重量',
  `volume_value` decimal(24,6) DEFAULT NULL COMMENT '体积',
  `weight_difference` decimal(24,6) DEFAULT NULL COMMENT '重量差',
  `volume_difference` decimal(24,6) DEFAULT NULL COMMENT '体积差',
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
  UNIQUE KEY `uk_warehouse_goods_wh_acq` (`warehouse_id`,`acquisition_date`),
  KEY `idx_warehouse_goods_radar` (`radar_device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='仓库货量历史（智慧仓库 warehouseGoodsInfo 同步）';
