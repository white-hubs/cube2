-- liquibase formatted sql

-- changeset grid:ddl-1.13.2-1-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT CASE WHEN EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record' AND index_name = 'idx_grid_area_status') THEN 1 ELSE 0 END AS index_exists;
ALTER TABLE `grid_handover_record` DROP INDEX `idx_grid_area_status`;

-- changeset grid:ddl-1.13.2-1-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT CASE WHEN EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record' AND column_name = 'grid_area_id') AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record' AND column_name = 'grid_area_ids') THEN 1 ELSE 0 END AS need_change_column;
ALTER TABLE `grid_handover_record` CHANGE COLUMN `grid_area_id` `grid_area_ids` varchar(500) NOT NULL COMMENT '网格区域id集合（逗号分隔）';

-- changeset grid:ddl-1.13.2-1-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record' AND column_name = 'shift_sign_file_id') AS column_exists;
ALTER TABLE `grid_handover_record` ADD COLUMN `shift_sign_file_id` bigint(20) unsigned DEFAULT NULL COMMENT '交班人签名文件ID' AFTER `remarks`;

-- changeset grid:ddl-1.13.2-1-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record' AND column_name = 'take_sign_file_id') AS column_exists;
ALTER TABLE `grid_handover_record` ADD COLUMN `take_sign_file_id` bigint(20) unsigned DEFAULT NULL COMMENT '接班人签名文件ID' AFTER `shift_sign_file_id`;

-- changeset grid:ddl-1.13.2-1-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record' AND column_name = 'before_manager_ids') AS column_exists;
ALTER TABLE `grid_handover_record` DROP COLUMN `before_manager_ids`;

-- changeset grid:ddl-1.13.2-1-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record' AND column_name = 'after_manager_ids') AS column_exists;
ALTER TABLE `grid_handover_record` DROP COLUMN `after_manager_ids`;

-- changeset grid:ddl-1.13.2-1-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record' AND column_name = 'record_name') AS column_exists;
ALTER TABLE `grid_handover_record` ADD COLUMN `record_name` varchar(50) DEFAULT NULL COMMENT '记录名称' AFTER `id`;
