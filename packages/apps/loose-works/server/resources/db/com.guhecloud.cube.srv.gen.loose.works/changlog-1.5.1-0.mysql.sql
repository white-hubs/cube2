-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule_extend' AND column_name='safety_dock_user_id') AS column_exists;
ALTER TABLE `project_schedule_extend` MODIFY COLUMN `safety_dock_user_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '安全负责人';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule' AND column_name='supervise_dep') AS column_exists;
ALTER TABLE `project_schedule` MODIFY COLUMN `supervise_dep` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '监督部门';

