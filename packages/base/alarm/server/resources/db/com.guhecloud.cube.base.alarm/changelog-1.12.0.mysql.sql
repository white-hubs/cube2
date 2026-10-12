-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'alarm_record' AND column_name = 'rule_id') AS column_exists;
ALTER TABLE `alarm_record` ADD COLUMN `rule_id` bigint(20)  DEFAULT NULL COMMENT '规则ID';