-- liquibase formatted sql

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule' AND column_name='duration') AS column_exists;
ALTER TABLE `project_schedule` ADD COLUMN `duration` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工期';