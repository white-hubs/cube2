-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cultivate_task' AND column_name = 'late_status' ) AS column_exists;
ALTER TABLE `cultivate_task` ADD COLUMN `late_status` tinyint(4) DEFAULT NULL COMMENT '迟到状态';
