-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'emergency_rule_condition_device' AND column_name = 'enabled' ) AS column_exists;
ALTER TABLE `emergency_rule_condition_device` ADD COLUMN `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否开启' AFTER `device_area_name`;
