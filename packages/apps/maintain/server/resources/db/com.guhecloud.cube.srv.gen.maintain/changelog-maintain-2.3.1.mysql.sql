-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_plan' AND column_name='receive_role_ids') AS column_exists;
ALTER TABLE inspect_plan ADD receive_role_ids varchar(1024) NULL COMMENT '接收人角色ids';
ALTER TABLE inspect_plan CHANGE receive_role_ids receive_role_ids varchar(1024) NULL COMMENT '接收人角色ids' AFTER receive_post_ids;

-- changeset chx:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_plan' AND column_name='check_role_ids') AS column_exists;
ALTER TABLE inspect_plan ADD check_role_ids varchar(1024) NULL COMMENT '稽查人角色ids';
ALTER TABLE inspect_plan CHANGE check_role_ids check_role_ids varchar(1024) NULL COMMENT '稽查人角色ids' AFTER check_post_ids;

-- changeset chx:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND column_name='receive_role_ids') AS column_exists;
ALTER TABLE inspect_task ADD receive_role_ids varchar(1024) NULL COMMENT '接收人角色ids';
ALTER TABLE inspect_task CHANGE receive_role_ids receive_role_ids varchar(1024) NULL COMMENT '接收人角色ids' AFTER receive_post_ids;

