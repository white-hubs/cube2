-- liquibase formatted sql

-- changeset changsha:warehouse-measure-history-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_measure_history') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_measure_history` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `acquisition_date` datetime NOT NULL COMMENT '采集时间',
  `material_volume` decimal(24,6) DEFAULT NULL COMMENT '物料体积',
  `remaining_volume` decimal(24,6) DEFAULT NULL COMMENT '剩余体积',
  `total_volume` decimal(24,6) DEFAULT NULL COMMENT '总体积',
  `weight` decimal(24,6) DEFAULT NULL COMMENT '物料重量，单位t',
  `material_height` decimal(24,6) DEFAULT NULL COMMENT '物料高度',
  `remaining_height` decimal(24,6) DEFAULT NULL COMMENT '剩余高度',
  `total_height` decimal(24,6) DEFAULT NULL COMMENT '总高度',
  `usage_rate` decimal(24,6) DEFAULT NULL COMMENT '仓容利用率',
  `remote_update_time` bigint(20) DEFAULT NULL COMMENT '三方更新时间',
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
  UNIQUE KEY `uk_wh_measure_tenant_wh_acq_deleted` (`tenant`,`warehouse_id`,`acquisition_date`,`deleted`),
  KEY `idx_wh_measure_warehouse_time` (`warehouse_id`,`acquisition_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粮仓测量信息历史表';
