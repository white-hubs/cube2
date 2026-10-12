-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'alarm_push_config' AND column_name = 'confirmed' ) AS column_exists;
ALTER TABLE `alarm_push_config` ADD COLUMN `confirmed` tinyint(1) NULL DEFAULT NULL COMMENT '确认配置' AFTER `update_time`;

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'safety_alarm_record' AND column_name = 'assist_confirm_result' ) AS column_exists;
ALTER TABLE `safety_alarm_record` ADD COLUMN `assist_confirm_result` varchar(50) NULL DEFAULT NULL COMMENT '辅助确认结果' AFTER `displayed`;
