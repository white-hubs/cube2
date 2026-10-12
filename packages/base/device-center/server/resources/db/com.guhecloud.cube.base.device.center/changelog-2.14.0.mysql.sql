-- liquibase formatted sql

-- changeset antigravity:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_command_record' AND column_name = 'device_id' ) AS column_exists;
ALTER TABLE `iot_command_record` ADD COLUMN `device_id` bigint(20) DEFAULT NULL COMMENT '设备ID';