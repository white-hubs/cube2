-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'area_scene_config' AND column_name = 'project_id' ) AS column_exists;
ALTER TABLE `area_scene_config` ADD COLUMN `project_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '项目主键' AFTER `id`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'business_panel_config' AND index_name = 'idx_business_id_business_type_panel_position_tenant_deleted') AS index_exists;
ALTER TABLE `business_panel_config` DROP INDEX `idx_business_id_business_type_panel_position_tenant_deleted`;

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'business_panel_config' AND column_name = 'project_id' ) AS column_exists;
ALTER TABLE `business_panel_config` ADD COLUMN `project_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '项目主键' AFTER `id`;

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'business_panel_config' AND index_name = 'idx_p_id_b_id_b_type_p_position_tenant_deleted') AS index_exists;
ALTER TABLE `business_panel_config` ADD UNIQUE INDEX `idx_p_id_b_id_b_type_p_position_tenant_deleted`(`project_id`, `business_type`, `business_id`, `panel_position`, `tenant`, `deleted`) USING BTREE;

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'scene_type' AND column_name = 'project_id' ) AS column_exists;
ALTER TABLE `scene_type` ADD COLUMN `project_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '项目主键' AFTER `id`;

-- changeset lanchun:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'universal_panel' AND column_name = 'project_id' ) AS column_exists;
ALTER TABLE `universal_panel` ADD COLUMN `project_id` bigint(20) NULL DEFAULT NULL COMMENT '项目主键' AFTER `id`;

-- changeset lanchun:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'business_panel_config' AND index_name = 'idx_business_id_scene_type_tenant_deleted') AS index_exists;
ALTER TABLE `business_scene_config` DROP INDEX `idx_business_id_scene_type_tenant_deleted`;

-- changeset lanchun:ddl-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'business_scene_config' AND column_name = 'project_id' ) AS column_exists;
ALTER TABLE `business_scene_config` ADD COLUMN `project_id` bigint(20) NULL DEFAULT NULL COMMENT '项目id' AFTER `id`;
