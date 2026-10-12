-- liquibase formatted sql

-- changeset daily-task:add-daily-record-status-column
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_record' AND column_name = 'record_status';
ALTER TABLE `daily_record`
    ADD COLUMN `record_status` varchar(50) DEFAULT NULL COMMENT '执行记录状态' AFTER `execution_result`;