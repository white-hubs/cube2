-- liquibase formatted sql

-- changeset disinfection:member-log-add-device-id
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_log' AND column_name = 'device_id') AS column_exists;
ALTER TABLE `member_log` ADD COLUMN `device_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '设备id' AFTER `title`;

