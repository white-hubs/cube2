-- liquibase formatted sql

-- changeset changsha:warehouse-location-card-position-profile-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_position_profile') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_location_card_position_profile` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `position_code` varchar(100) DEFAULT NULL COMMENT '货位编号',
  `flat_warehouse_no` varchar(100) DEFAULT NULL COMMENT '平房仓号',
  `length` decimal(24,6) DEFAULT NULL COMMENT '长度，单位m',
  `width` decimal(24,6) DEFAULT NULL COMMENT '宽度，单位m',
  `height` decimal(24,6) DEFAULT NULL COMMENT '高度，单位m',
  `digital_grain_volume` decimal(24,6) DEFAULT NULL COMMENT '数字粮体积，单位m3',
  `deducted_volume` decimal(24,6) DEFAULT NULL COMMENT '扣除体积，单位m3',
  `storage_bag_count` int(11) DEFAULT NULL COMMENT '包存量包数',
  `average_bag_weight` decimal(24,6) DEFAULT NULL COMMENT '平均包量',
  `origin_place` varchar(100) DEFAULT NULL COMMENT '产地',
  `harvest_year` varchar(100) DEFAULT NULL COMMENT '收获年度',
  `keeper` varchar(100) DEFAULT NULL COMMENT '保管员',
  `filing_date` varchar(100) DEFAULT NULL COMMENT '填卡日期',
  `authorized_unit` varchar(255) DEFAULT NULL COMMENT '授权单位',
  `storage_site` varchar(255) DEFAULT NULL COMMENT '储存库点',
  `inbound_time` varchar(100) DEFAULT NULL COMMENT '入仓时间',
  `storage_nature` varchar(100) DEFAULT NULL COMMENT '储存性质',
  `storage_mode` varchar(100) DEFAULT NULL COMMENT '保管方式',
  `constant_temperature_rate` decimal(24,6) DEFAULT NULL COMMENT '恒温率指标',
  `bulk_density_or_roughness` decimal(24,6) DEFAULT NULL COMMENT '容重/粗糙率',
  `converted_moisture` decimal(24,6) DEFAULT NULL COMMENT '折合水分',
  `whole_or_imperfect_grain` decimal(24,6) DEFAULT NULL COMMENT '整精米粒/不完善粒',
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
  UNIQUE KEY `uk_wh_location_card_position_tenant_wh_deleted` (`tenant`,`warehouse_id`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='货位卡仓位档案表';

-- changeset changsha:warehouse-location-card-quality-inspection-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_quality_inspection') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_location_card_quality_inspection` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `inspection_time` datetime NOT NULL COMMENT '检测时间',
  `moisture` decimal(24,6) DEFAULT NULL COMMENT '水分，单位%',
  `roughness` decimal(24,6) DEFAULT NULL COMMENT '粗糙率，单位%',
  `fatty_acid` decimal(24,6) DEFAULT NULL COMMENT '脂肪酸，单位mgKOH/100g',
  `color_smell` varchar(100) DEFAULT NULL COMMENT '色泽气味',
  `inspector` varchar(100) DEFAULT NULL COMMENT '检测人',
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
  KEY `idx_wh_location_card_quality_wh_time` (`warehouse_id`,`inspection_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='货位卡质量检测记录表';

-- changeset changsha:warehouse-location-card-field-change-log-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_field_change_log') AS table_exists;
CREATE TABLE IF NOT EXISTS `warehouse_location_card_field_change_log` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '智慧仓库粮仓ID',
  `biz_type` varchar(100) NOT NULL COMMENT '业务类型',
  `biz_id` bigint(20) DEFAULT NULL COMMENT '业务记录ID',
  `change_summary` varchar(1000) DEFAULT NULL COMMENT '变更摘要，多个字段名用、拼接',
  `change_content` json DEFAULT NULL COMMENT '变更明细JSON',
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
  KEY `idx_wh_location_card_change_wh_time` (`warehouse_id`,`create_time`),
  KEY `idx_wh_location_card_change_biz` (`biz_type`,`biz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='货位卡字段变更记录表';

-- changeset changsha:warehouse-location-card-field-change-log-add-summary-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_field_change_log' AND column_name = 'change_summary';
ALTER TABLE `warehouse_location_card_field_change_log`
  ADD COLUMN `change_summary` varchar(1000) DEFAULT NULL COMMENT '变更摘要，多个字段名用、拼接' AFTER `biz_id`;

-- changeset changsha:warehouse-location-card-field-change-log-add-content-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_field_change_log' AND column_name = 'change_content';
ALTER TABLE `warehouse_location_card_field_change_log`
  ADD COLUMN `change_content` json DEFAULT NULL COMMENT '变更明细JSON' AFTER `change_summary`;

-- changeset changsha:warehouse-location-card-field-change-log-field-name-nullable-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_field_change_log' AND column_name = 'field_name';
ALTER TABLE `warehouse_location_card_field_change_log`
  MODIFY COLUMN `field_name` varchar(100) DEFAULT NULL COMMENT '字段名';

-- changeset changsha:warehouse-location-card-field-change-log-fill-content-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:6 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_field_change_log' AND column_name IN ('field_name', 'field_label', 'old_value', 'new_value', 'change_summary', 'change_content');
UPDATE `warehouse_location_card_field_change_log`
SET `change_summary` = IFNULL(`change_summary`, COALESCE(NULLIF(`field_label`, ''), `field_name`)),
    `change_content` = IFNULL(`change_content`, JSON_ARRAY(JSON_OBJECT(
      'fieldName', `field_name`,
      'fieldLabel', `field_label`,
      'oldValue', `old_value`,
      'newValue', `new_value`
    )))
WHERE `field_name` IS NOT NULL;

-- changeset changsha:warehouse-location-card-position-profile-drop-position-no-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_position_profile' AND column_name = 'position_no';
ALTER TABLE `warehouse_location_card_position_profile`
  DROP COLUMN `position_no`;
