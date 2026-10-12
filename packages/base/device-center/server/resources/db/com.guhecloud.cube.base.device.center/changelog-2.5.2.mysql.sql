-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'center_device_scene_config' AND column_name='project_id') AS column_exists;
ALTER TABLE `center_device_scene_config` ADD COLUMN `project_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '项目id' AFTER `id`;