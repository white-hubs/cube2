-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'alarm_record_target' AND INDEX_NAME = 'idx_alarm_record_id' ) AS column_exists;
ALTER TABLE `alarm_record_target` ADD INDEX `idx_alarm_record_id`(`alarm_record_id`) USING BTREE;