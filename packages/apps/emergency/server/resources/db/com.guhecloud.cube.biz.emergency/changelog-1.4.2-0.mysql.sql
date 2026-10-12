-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'emergency_rule_condition' AND column_name = 'event_config_type' ) AS column_exists;
ALTER TABLE `emergency_rule_condition` ADD COLUMN `event_config_type` VARCHAR(32) NULL DEFAULT NULL COMMENT '事件配置类型' AFTER `device_event_type`;
