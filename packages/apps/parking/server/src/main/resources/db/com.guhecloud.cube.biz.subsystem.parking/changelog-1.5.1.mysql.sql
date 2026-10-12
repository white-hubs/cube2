-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'vehicle_info' AND column_name = 'vehicle_no_color' ) AS column_exists;
ALTER TABLE `vehicle_info` ADD COLUMN `vehicle_no_color` varchar(50) DEFAULT NULL COMMENT '车牌颜色';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'illegal_parking_blacklist' AND column_name = 'parking_lot' ) AS column_exists;
ALTER TABLE `illegal_parking_blacklist` ADD COLUMN `parking_lot` varchar(50) DEFAULT NULL COMMENT '车场编号';

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'illegal_parking_blacklist' AND column_name = 'start_time' ) AS column_exists;
ALTER TABLE `illegal_parking_blacklist` ADD COLUMN `start_time` datetime DEFAULT NULL COMMENT '开始时间';


-- changeset zengyadong:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'illegal_parking_blacklist' AND column_name = 'end_time' ) AS column_exists;
ALTER TABLE `illegal_parking_blacklist` ADD COLUMN `end_time` datetime DEFAULT NULL COMMENT '结束时间';
