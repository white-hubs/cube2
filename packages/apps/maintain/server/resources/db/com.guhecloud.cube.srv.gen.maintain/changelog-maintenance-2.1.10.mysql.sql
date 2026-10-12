-- liquibase formatted sql

-- changeset zengyadong:insert-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'maintenance_plan' AND column_name='way' ) AS column_exists;
ALTER TABLE `cube-srv-gen-maintain`.`maintenance_plan` ADD COLUMN `way` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成任务方式' ;