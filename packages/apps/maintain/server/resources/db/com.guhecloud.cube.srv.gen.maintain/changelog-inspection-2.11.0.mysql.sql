-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_plan_relation' AND column_name='target_code') AS column_exists;
ALTER TABLE inspect_plan_relation ADD target_code varchar(255) NULL COMMENT '巡检对象编码';
ALTER TABLE inspect_plan_relation CHANGE target_code target_code varchar(255) NULL COMMENT '巡检对象编码' AFTER target_id;


-- changeset chx:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task_target_relation' AND column_name='target_code') AS column_exists;
ALTER TABLE inspect_task_target_relation ADD target_code varchar(255) NULL COMMENT '巡检对象编码';
ALTER TABLE inspect_task_target_relation CHANGE target_code target_code varchar(255) NULL COMMENT '巡检对象编码' AFTER target_id;

