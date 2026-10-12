-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND column_name = 'expire_todo') AS column_exists;
ALTER TABLE inspect_task ADD expire_todo TINYINT DEFAULT 0 NULL COMMENT '过期待办处理标识';
ALTER TABLE inspect_task CHANGE expire_todo expire_todo TINYINT DEFAULT 0 NULL COMMENT '过期待办处理标识' AFTER `method`;
