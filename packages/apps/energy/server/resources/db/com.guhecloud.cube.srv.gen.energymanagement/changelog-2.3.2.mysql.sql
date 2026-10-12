-- liquibase formatted sql

-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meter_reade_cycle' AND column_name = 'task_id' ) AS column_exists;
ALTER TABLE meter_reade_cycle MODIFY COLUMN task_id VARCHAR(100) NULL COMMENT '任务id';


