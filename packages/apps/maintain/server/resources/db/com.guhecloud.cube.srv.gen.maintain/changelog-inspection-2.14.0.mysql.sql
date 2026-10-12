-- liquibase formatted sql

-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_plan_relation' AND column_name='target_parent_id') AS column_exists;
ALTER TABLE inspect_plan_relation ADD target_parent_id BIGINT(20) NULL COMMENT '主对象id(有值表示是被关联的对象)';
ALTER TABLE inspect_plan_relation CHANGE target_parent_id target_parent_id BIGINT(20) NULL COMMENT '主对象id(有值表示是被关联的对象)' AFTER target_name;

-- changeset chx:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_plan_relation' AND column_name='target_address') AS column_exists;
ALTER TABLE inspect_plan_relation ADD target_address varchar(200) NULL COMMENT '巡检对象具体地址';
ALTER TABLE inspect_plan_relation CHANGE target_address target_address varchar(200) NULL COMMENT '巡检对象具体地址' AFTER target_name;

-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task_target_relation' AND column_name='target_parent_id') AS column_exists;
ALTER TABLE inspect_task_target_relation ADD target_parent_id BIGINT(20) NULL COMMENT '父级对象id(有表示是关联对象)';
ALTER TABLE inspect_task_target_relation CHANGE target_parent_id target_parent_id BIGINT(20) NULL COMMENT '父级对象id(有表示是关联对象)' AFTER target_name;

-- changeset chx:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task_target_relation' AND column_name='target_address') AS column_exists;
ALTER TABLE inspect_task_target_relation ADD target_address varchar(200) NULL COMMENT '巡检对象具体地址';
ALTER TABLE inspect_task_target_relation CHANGE target_address target_address varchar(200) NULL COMMENT '巡检对象具体地址' AFTER target_name;
