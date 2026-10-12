-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'alarm_push_config' AND column_name = 'timeout' ) AS column_exists;
ALTER TABLE `alarm_push_config` ADD COLUMN `timeout` tinyint(1) NULL DEFAULT NULL COMMENT '超时配置' AFTER `update_time`;

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'safety_alarm_record' AND column_name = 'displayed' ) AS column_exists;
ALTER TABLE `safety_alarm_record` ADD COLUMN `displayed` tinyint(1) NULL DEFAULT NULL COMMENT '是否显示' AFTER `application_name`;