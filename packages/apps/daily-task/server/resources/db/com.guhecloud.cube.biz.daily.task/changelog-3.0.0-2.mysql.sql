-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_item' AND column_name='operation_guide') AS column_exists;
ALTER TABLE `daily_item` ADD COLUMN `operation_guide` varchar(500) DEFAULT NULL COMMENT '操作指引';
