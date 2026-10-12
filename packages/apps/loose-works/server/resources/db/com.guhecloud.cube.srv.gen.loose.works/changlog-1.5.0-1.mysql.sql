-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule_extend' AND column_name='safety_dock_user_id') AS column_exists;
ALTER TABLE `project_schedule_extend` ADD COLUMN `safety_dock_user_id` bigint(20) NULL DEFAULT NULL COMMENT '安全负责人';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_reform' AND column_name='process_ins_name') AS column_exists;
ALTER TABLE `project_reform` ADD COLUMN `process_ins_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程表单名';

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_reform' AND column_name='project_schedule_name') AS column_exists;
ALTER TABLE `project_reform` ADD COLUMN `project_schedule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工程名称';
