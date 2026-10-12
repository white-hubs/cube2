-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'boiler_data' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `boiler_data` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'boiler_data' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `boiler_data` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'boiler_data' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `boiler_data` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'boiler_data' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `boiler_data` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'bed_file' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `bed_file` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'bed_file' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `bed_file` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'bed_file' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `bed_file` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'bed_file' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `bed_file` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'call_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `call_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'call_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `call_record` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'call_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `call_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'call_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `call_record` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'processing_records' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `processing_records` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-14
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'processing_records' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `processing_records` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-15
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'processing_records' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `processing_records` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-16
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'processing_records' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `processing_records` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-17
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'clod_water_attribute_map' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `clod_water_attribute_map` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-18
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'clod_water_attribute_map' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `clod_water_attribute_map` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-19
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'clod_water_attribute_map' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `clod_water_attribute_map` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-20
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'clod_water_attribute_map' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `clod_water_attribute_map` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-21
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_water_attribute' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `cold_water_attribute` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-22
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_water_attribute' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `cold_water_attribute` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-23
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_water_attribute' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `cold_water_attribute` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-24
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_water_attribute' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `cold_water_attribute` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-25
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_water_electricity' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `cold_water_electricity` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-26
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_water_electricity' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `cold_water_electricity` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-27
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_water_electricity' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `cold_water_electricity` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-28
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_water_electricity' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `cold_water_electricity` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-29
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_wholesaler' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `cold_wholesaler` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-30
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_wholesaler' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `cold_wholesaler` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-31
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_wholesaler' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `cold_wholesaler` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-32
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cold_wholesaler' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `cold_wholesaler` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-33
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_position' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `device_position` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-34
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_position' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `device_position` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-35
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_position' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `device_position` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-36
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_position' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `device_position` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-37
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'gear_position' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `gear_position` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-38
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'gear_position' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `gear_position` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-39
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'gear_position' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `gear_position` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-40
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'gear_position' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `gear_position` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-41
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'operate_log' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `operate_log` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-42
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'operate_log' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `operate_log` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-43
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'operate_log' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `operate_log` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-44
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'operate_log' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `operate_log` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-45
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_day' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `water_data_day` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-46
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_day' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `water_data_day` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-47
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_day' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `water_data_day` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-48
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_day' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `water_data_day` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-49
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_month' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `water_data_month` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-50
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_month' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `water_data_month` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-51
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_month' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `water_data_month` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-52
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_month' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `water_data_month` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-53
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_year' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `water_data_year` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-54
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_year' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `water_data_year` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-55
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_year' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `water_data_year` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-56
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_data_year' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `water_data_year` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-57
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_map_type' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `water_map_type` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-58
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_map_type' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `water_map_type` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-59
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_map_type' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `water_map_type` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-60
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'water_map_type' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `water_map_type` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-61
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'command_dispatch_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `command_dispatch_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-62
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'command_dispatch_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `command_dispatch_record` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-63
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'command_dispatch_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `command_dispatch_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-64
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'command_dispatch_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `command_dispatch_record` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-65
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_collection' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `device_collection` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-66
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_collection' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `device_collection` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-67
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_collection' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `device_collection` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-68
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_collection' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `device_collection` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-69
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_monitor' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `device_monitor` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-70
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_monitor' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `device_monitor` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-71
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_monitor' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `device_monitor` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-72
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'device_monitor' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `device_monitor` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-73
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_base_details' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `group_base_details` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-74
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_base_details' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `group_base_details` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-75
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_base_details' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `group_base_details` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-76
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_base_details' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `group_base_details` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-77
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_base_info' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `group_base_info` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-78
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_base_info' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `group_base_info` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-79
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_base_info' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `group_base_info` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-80
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_base_info' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `group_base_info` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-81
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'security_device' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `security_device` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-82
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'security_device' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `security_device` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-83
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'security_device' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `security_device` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-84
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'security_device' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `security_device` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-85
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'attributive_group' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `attributive_group` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-86
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'attributive_group' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `attributive_group` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-87
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'attributive_group' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `attributive_group` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-88
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'attributive_group' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `attributive_group` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-89
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'collect_configure' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `collect_configure` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-90
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'collect_configure' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `collect_configure` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-91
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'collect_configure' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `collect_configure` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-92
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'collect_configure' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `collect_configure` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-93
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_attributive_details' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `group_attributive_details` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-94
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_attributive_details' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `group_attributive_details` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-95
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_attributive_details' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `group_attributive_details` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-96
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_attributive_details' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `group_attributive_details` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-97
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'power_distribution_box' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `power_distribution_box` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-98
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'power_distribution_box' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `power_distribution_box` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-99
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'power_distribution_box' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `power_distribution_box` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-100
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'power_distribution_box' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `power_distribution_box` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-101
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'transformer_distribution' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `transformer_distribution` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-102
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'transformer_distribution' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `transformer_distribution` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-103
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'transformer_distribution' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `transformer_distribution` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-104
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'transformer_distribution' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `transformer_distribution` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-105
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'transformer_meter' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `transformer_meter` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-106
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'transformer_meter' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `transformer_meter` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-107
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'transformer_meter' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `transformer_meter` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-108
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'transformer_meter' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `transformer_meter` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-109
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'emergency_phone_number' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `emergency_phone_number` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-110
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'emergency_phone_number' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `emergency_phone_number` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-111
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'emergency_phone_number' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `emergency_phone_number` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-112
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'emergency_phone_number' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `emergency_phone_number` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-113
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'environmental_monitoring' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `environmental_monitoring` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-114
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'environmental_monitoring' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `environmental_monitoring` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-115
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'environmental_monitoring' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `environmental_monitoring` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-116
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'environmental_monitoring' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `environmental_monitoring` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-117
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'environment_hour_stat' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `environment_hour_stat` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-118
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'environment_hour_stat' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `environment_hour_stat` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-119
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'environment_hour_stat' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `environment_hour_stat` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-120
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'environment_hour_stat' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `environment_hour_stat` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-121
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'information_plan' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `information_plan` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-122
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'information_plan' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `information_plan` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-123
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'information_plan' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `information_plan` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-124
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'information_plan' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `information_plan` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-125
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'info_screen_schedule_assoc' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `info_screen_schedule_assoc` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-126
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'info_screen_schedule_assoc' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `info_screen_schedule_assoc` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-127
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'info_screen_schedule_assoc' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `info_screen_schedule_assoc` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-128
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'info_screen_schedule_assoc' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `info_screen_schedule_assoc` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-129
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'play_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `play_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-130
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'play_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `play_record` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-131
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'play_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `play_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-132
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'play_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `play_record` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-133
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'program' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `program` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-134
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'program' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `program` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-135
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'program' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `program` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-136
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'program' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `program` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-137
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'program_schedule' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `program_schedule` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-138
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'program_schedule' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `program_schedule` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-139
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'program_schedule' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `program_schedule` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-140
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'program_schedule' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `program_schedule` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-141
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `time_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-142
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `time_record` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-143
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `time_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-144
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `time_record` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-145
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_control_log' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `group_control_log` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-146
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_control_log' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `group_control_log` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-147
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_control_log' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `group_control_log` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-148
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'group_control_log' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `group_control_log` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-149
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_details' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `time_control_details` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-150
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_details' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `time_control_details` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-151
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_details' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `time_control_details` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-152
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_details' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `time_control_details` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-153
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_info' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `time_control_info` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-154
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_info' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `time_control_info` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-155
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_info' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `time_control_info` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-156
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_info' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `time_control_info` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-157
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_log' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `time_control_log` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-158
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_log' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `time_control_log` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-159
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_log' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `time_control_log` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-160
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'time_control_log' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `time_control_log` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-161
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'five_call_log' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `five_call_log` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-162
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'five_call_log' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `five_call_log` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-163
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'five_call_log' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `five_call_log` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-164
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'five_call_log' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `five_call_log` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-165
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'lift_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `lift_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-166
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'lift_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `lift_record` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-167
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'lift_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `lift_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-168
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'lift_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `lift_record` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-169
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'medical_gas_data' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `medical_gas_data` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-170
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'medical_gas_data' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `medical_gas_data` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-171
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'medical_gas_data' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `medical_gas_data` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-172
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'medical_gas_data' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `medical_gas_data` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-173
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'call_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `call_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-174
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'call_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `call_record` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-175
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'call_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `call_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-176
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'call_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `call_record` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-177
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting_apply' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `meeting_apply` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-178
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting_apply' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `meeting_apply` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-179
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting_apply' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `meeting_apply` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-180
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting_apply' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `meeting_apply` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-181
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting_apply_person' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `meeting_apply_person` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-182
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting_apply_person' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `meeting_apply_person` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-183
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting_apply_person' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `meeting_apply_person` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-184
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting_apply_person' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `meeting_apply_person` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-185
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `meeting` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-186
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `meeting` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-187
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `meeting` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-188
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meeting' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `meeting` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-189
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'security_panel_config' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `security_panel_config` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-190
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'security_panel_config' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `security_panel_config` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-191
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'security_panel_config' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `security_panel_config` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-192
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'security_panel_config' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `security_panel_config` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-193
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'passenger_flow_record' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `passenger_flow_record` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-194
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'passenger_flow_record' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `passenger_flow_record` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-195
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'passenger_flow_record' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `passenger_flow_record` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-196
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'passenger_flow_record' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `passenger_flow_record` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-197
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'person_roll_call' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `person_roll_call` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-198
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'person_roll_call' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `person_roll_call` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-199
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'person_roll_call' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `person_roll_call` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-200
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'person_roll_call' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `person_roll_call` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-201
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'person_roll_call_task' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `person_roll_call_task` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-202
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'person_roll_call_task' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `person_roll_call_task` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-203
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'person_roll_call_task' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `person_roll_call_task` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-204
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'person_roll_call_task' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `person_roll_call_task` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-205
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sewage_data' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `sewage_data` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-206
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sewage_data' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `sewage_data` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-207
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sewage_data' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `sewage_data` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-208
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sewage_data' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `sewage_data` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-209
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sewage_device_config' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `sewage_device_config` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-210
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sewage_device_config' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `sewage_device_config` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-211
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sewage_device_config' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `sewage_device_config` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-212
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'sewage_device_config' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `sewage_device_config` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-213
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'surgical_demonstration' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `surgical_demonstration` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-214
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'surgical_demonstration' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `surgical_demonstration` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-215
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'surgical_demonstration' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `surgical_demonstration` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-216
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'surgical_demonstration' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `surgical_demonstration` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-217
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_light' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `traffic_light` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-218
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_light' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `traffic_light` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-219
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_light' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `traffic_light` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-220
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'traffic_light' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `traffic_light` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-221
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'treated_water_usage_hour' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `treated_water_usage_hour` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-222
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'treated_water_usage_hour' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `treated_water_usage_hour` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-223
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'treated_water_usage_hour' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `treated_water_usage_hour` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-224
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'treated_water_usage_hour' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `treated_water_usage_hour` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

-- changeset lanchun:alter-225
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'vehicle_flow_monitoring' AND column_name = 'creator_id' ) AS column_exists;
ALTER TABLE `vehicle_flow_monitoring` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset lanchun:alter-226
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'vehicle_flow_monitoring' AND column_name = 'creator_type' ) AS column_exists;
ALTER TABLE `vehicle_flow_monitoring` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset lanchun:alter-227
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'vehicle_flow_monitoring' AND column_name = 'updater_id' ) AS column_exists;
ALTER TABLE `vehicle_flow_monitoring` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset lanchun:alter-228
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'vehicle_flow_monitoring' AND column_name = 'updater_type' ) AS column_exists;
ALTER TABLE `vehicle_flow_monitoring` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
