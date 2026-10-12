-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'subject_permission' AND column_name = 'permission_extent_config' ) AS column_exists;
ALTER TABLE `subject_permission` MODIFY COLUMN `permission_extent_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '权限配置扩充信息' AFTER `permission_code`;
