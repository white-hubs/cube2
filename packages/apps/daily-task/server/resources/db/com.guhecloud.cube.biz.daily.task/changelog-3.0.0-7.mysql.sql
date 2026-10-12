-- liquibase formatted sql

-- changeset daily-task:add-daily-task-item-operation-guide
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task_item' AND column_name = 'operation_guide';
ALTER TABLE `daily_task_item`
    ADD COLUMN `operation_guide` varchar(500) DEFAULT NULL COMMENT '操作指引' AFTER `item_content`;
