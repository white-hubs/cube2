-- liquibase formatted sql

-- changeset grid:alter-1.13.1-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'grid_area_extend' AND column_name = 'engineering_manager' ) AS column_exists;
ALTER TABLE `grid_area_extend` ADD COLUMN `engineering_manager` bigint(20) unsigned NULL DEFAULT NULL COMMENT '工程负责人id';

-- changeset grid:alter-1.13.1-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'grid_area_extend' AND column_name = 'safety_manager' ) AS column_exists;
ALTER TABLE `grid_area_extend` ADD COLUMN `safety_manager` bigint(20) unsigned NULL DEFAULT NULL COMMENT '安全负责人id';

-- changeset grid:alter-1.13.1-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'grid_area_extend' AND column_name = 'cleaning_manager' ) AS column_exists;
ALTER TABLE `grid_area_extend` ADD COLUMN `cleaning_manager` bigint(20) unsigned NULL DEFAULT NULL COMMENT '保洁负责人id';

-- changeset grid:alter-1.13.1-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'grid_area_extend' AND column_name = 'metering_manager' ) AS column_exists;
ALTER TABLE `grid_area_extend` ADD COLUMN `metering_manager` bigint(20) unsigned NULL DEFAULT NULL COMMENT '计量管理负责人id';

-- changeset grid:alter-1.13.1-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'grid_area_extend' AND column_name = 'metering_staff' ) AS column_exists;
ALTER TABLE `grid_area_extend` ADD COLUMN `metering_staff` bigint(20) unsigned NULL DEFAULT NULL COMMENT '计量管理人员id';
