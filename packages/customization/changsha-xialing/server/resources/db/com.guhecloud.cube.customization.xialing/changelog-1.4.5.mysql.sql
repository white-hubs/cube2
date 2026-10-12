-- liquibase formatted sql

-- changeset changsha:warehouse-location-card-position-profile-add-extra-fields-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_position_profile' AND column_name = 'digital_grain_volume';
ALTER TABLE `warehouse_location_card_position_profile`
  ADD COLUMN `digital_grain_volume` decimal(24,6) DEFAULT NULL COMMENT '数字粮体积，单位m3' AFTER `height`,
  ADD COLUMN `deducted_volume` decimal(24,6) DEFAULT NULL COMMENT '扣除体积，单位m3' AFTER `digital_grain_volume`,
  ADD COLUMN `storage_bag_count` int(11) DEFAULT NULL COMMENT '包存量包数' AFTER `deducted_volume`,
  ADD COLUMN `average_bag_weight` decimal(24,6) DEFAULT NULL COMMENT '平均包量' AFTER `storage_bag_count`,
  ADD COLUMN `origin_place` varchar(100) DEFAULT NULL COMMENT '产地' AFTER `average_bag_weight`,
  ADD COLUMN `harvest_year` varchar(100) DEFAULT NULL COMMENT '收获年度' AFTER `origin_place`;
