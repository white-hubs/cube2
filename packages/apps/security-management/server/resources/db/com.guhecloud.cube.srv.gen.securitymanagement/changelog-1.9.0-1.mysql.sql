-- liquibase formatted sql

-- changeset chx:ddl-1
-- ----------------------------
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'information_plan' AND column_name='open_job_task_id') AS column_exists;
ALTER TABLE information_plan MODIFY COLUMN open_job_task_id varchar(100) NULL COMMENT '开屏的xxljob的任务id';


-- changeset chx:ddl-2
-- ----------------------------
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'information_plan' AND column_name='close_job_task_id') AS column_exists;
ALTER TABLE information_plan MODIFY COLUMN close_job_task_id varchar(100) NULL COMMENT '关屏的xxljob的任务id';

