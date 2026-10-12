-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND column_name='target_ids') AS column_exists;
ALTER TABLE inspect_task MODIFY COLUMN target_ids TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '巡检对象ids';
