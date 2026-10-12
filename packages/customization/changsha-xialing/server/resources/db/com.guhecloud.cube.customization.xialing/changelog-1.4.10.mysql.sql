-- liquibase formatted sql

-- changeset changsha:warehouse-location-card-position-profile-add-temperature-threshold-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_position_profile' AND column_name = 'temperature_threshold';
ALTER TABLE `warehouse_location_card_position_profile`
  ADD COLUMN `temperature_threshold` decimal(24,6) DEFAULT NULL COMMENT '温度阈值，单位℃' AFTER `height`;
