-- liquibase formatted sql

-- changeset changsha:warehouse-location-card-position-profile-split-length-width-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_position_profile' AND column_name = 'length';
ALTER TABLE `warehouse_location_card_position_profile`
  ADD COLUMN `length` decimal(24,6) DEFAULT NULL COMMENT '长度，单位m' AFTER `flat_warehouse_no`,
  ADD COLUMN `width` decimal(24,6) DEFAULT NULL COMMENT '宽度，单位m' AFTER `length`;

-- changeset changsha:warehouse-location-card-position-profile-drop-length-width-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'warehouse_location_card_position_profile' AND column_name = 'length_width';
ALTER TABLE `warehouse_location_card_position_profile`
  DROP COLUMN `length_width`;
