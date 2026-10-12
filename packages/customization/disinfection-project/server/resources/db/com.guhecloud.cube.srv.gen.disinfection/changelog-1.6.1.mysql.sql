-- liquibase formatted sql


-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_task_check_in_record' AND column_name='check_user_name') AS column_exists;
ALTER TABLE disinfection_task_check_in_record ADD check_user_name varchar(100) NULL COMMENT '打卡用户名称';
ALTER TABLE disinfection_task_check_in_record CHANGE check_user_name check_user_name varchar(100) NULL COMMENT '打卡用户名称' AFTER id;

-- changeset chx:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_task_check_in_record' AND column_name='task_name') AS column_exists;
ALTER TABLE disinfection_task_check_in_record ADD task_name varchar(100) NULL COMMENT '任务名';
ALTER TABLE disinfection_task_check_in_record CHANGE task_name task_name varchar(100) NULL COMMENT '任务名' AFTER id;

-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_task_check_in_record' AND column_name='task_id') AS column_exists;
ALTER TABLE disinfection_task_check_in_record ADD task_id BIGINT(20) NULL COMMENT '任务id';
ALTER TABLE disinfection_task_check_in_record CHANGE task_id task_id BIGINT(20) NULL COMMENT '任务id' AFTER id;

-- changeset chx:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'disinfection_task_check_in_record' AND column_name='project_id') AS column_exists;
ALTER TABLE disinfection_task_check_in_record ADD project_id BIGINT NULL COMMENT '项目id';
ALTER TABLE disinfection_task_check_in_record CHANGE project_id project_id BIGINT NULL COMMENT '项目id' AFTER id;



