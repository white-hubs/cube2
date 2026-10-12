-- liquibase formatted sql

-- changeset disinfection:member-group-device-add-district-location
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_group_device' AND column_name = 'district_location') AS column_exists;
ALTER TABLE `member_group_device` ADD COLUMN `district_location` varchar(255) DEFAULT NULL COMMENT '区县位置';

-- changeset disinfection:member-group-device-add-location-detail
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_group_device' AND column_name = 'location_detail') AS column_exists;
ALTER TABLE `member_group_device` ADD COLUMN `location_detail` varchar(500) DEFAULT NULL COMMENT '详细位置';

