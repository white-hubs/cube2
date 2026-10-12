-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'model_configuration' AND column_name='project_id') AS column_exists;
ALTER TABLE `model_configuration` ADD COLUMN `project_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '项目主键' AFTER `id`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'model_configuration' AND index_name = 'idx_project_id_tenant_deleted') AS index_exists;
ALTER TABLE `model_configuration` ADD UNIQUE INDEX `idx_project_id_tenant_deleted`(`project_id`, `tenant`, `deleted`) USING BTREE;