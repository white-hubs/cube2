-- liquibase formatted sql

-- changeset disinfection:member-group-device-bind-lat-lon-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_group_device' AND column_name = 'latitude') AS column_exists;
ALTER TABLE `member_group_device` ADD COLUMN `latitude` double DEFAULT NULL COMMENT '绑定设备时所在纬度';

-- changeset disinfection:member-group-device-bind-lat-lon-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_group_device' AND column_name = 'longitude') AS column_exists;
ALTER TABLE `member_group_device` ADD COLUMN `longitude` double DEFAULT NULL COMMENT '绑定设备时所在经度';
