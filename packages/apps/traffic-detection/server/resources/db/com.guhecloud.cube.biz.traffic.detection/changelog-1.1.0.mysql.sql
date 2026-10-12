-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `traffic_detection_area` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `traffic_detection_area` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `traffic_detection_area` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `traffic_detection_area` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area_device' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `traffic_detection_area_device` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area_device' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `traffic_detection_area_device` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area_device' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `traffic_detection_area_device` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area_device' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `traffic_detection_area_device` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area_visual' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `traffic_detection_area_visual` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area_visual' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `traffic_detection_area_visual` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area_visual' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `traffic_detection_area_visual` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_detection_area_visual' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `traffic_detection_area_visual` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
