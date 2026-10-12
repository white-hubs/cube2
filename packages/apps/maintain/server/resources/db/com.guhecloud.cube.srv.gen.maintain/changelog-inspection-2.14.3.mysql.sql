-- liquibase formatted sql

-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_plan_relation' AND column_name='target_area_id') AS column_exists;
ALTER TABLE inspect_plan_relation ADD target_area_id varchar(300) NULL COMMENT '巡检对象区域层级id';
ALTER TABLE inspect_plan_relation CHANGE target_area_id target_area_id varchar(300) NULL COMMENT '巡检对象区域层级id' AFTER target_name;

-- changeset chx:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_plan_relation' AND column_name='target_area_name') AS column_exists;
ALTER TABLE inspect_plan_relation ADD target_area_name varchar(500) NULL COMMENT '巡检对象区域名称';
ALTER TABLE inspect_plan_relation CHANGE target_area_name target_area_name varchar(500) NULL COMMENT '巡检对象区域名称' AFTER target_area_id;

-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task_target_relation' AND column_name='target_area_id') AS column_exists;
ALTER TABLE inspect_task_target_relation ADD target_area_id varchar(300) NULL COMMENT '巡检对象区域层级id';
ALTER TABLE inspect_task_target_relation CHANGE target_area_id target_area_id varchar(300) NULL COMMENT '巡检对象区域层级id' AFTER target_name;

-- changeset chx:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task_target_relation' AND column_name='target_area_name') AS column_exists;
ALTER TABLE inspect_task_target_relation ADD target_area_name varchar(500) NULL COMMENT '巡检对象区域名称';
ALTER TABLE inspect_task_target_relation CHANGE target_area_name target_area_name varchar(500) NULL COMMENT '巡检对象区域名称' AFTER target_area_id;
