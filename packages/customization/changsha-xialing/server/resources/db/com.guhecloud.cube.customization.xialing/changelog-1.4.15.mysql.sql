-- liquibase formatted sql

-- changeset changsha:warehouse-location-card-position-profile-add-temperature-range-min-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_position_profile' AND column_name = 'temperature_range_min';
ALTER TABLE `warehouse_location_card_position_profile`
  ADD COLUMN `temperature_range_min` decimal(24,6) DEFAULT NULL COMMENT '温度范围最小值，单位℃' AFTER `temperature_threshold`;

-- changeset changsha:warehouse-location-card-position-profile-add-temperature-range-max-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_position_profile' AND column_name = 'temperature_range_max';
ALTER TABLE `warehouse_location_card_position_profile`
  ADD COLUMN `temperature_range_max` decimal(24,6) DEFAULT NULL COMMENT '温度范围最大值，单位℃' AFTER `temperature_range_min`;
