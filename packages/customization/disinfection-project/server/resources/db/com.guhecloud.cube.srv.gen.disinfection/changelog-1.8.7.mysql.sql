-- liquibase formatted sql

-- changeset disinfection:member-device-manual-control-setting-creator-id
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_device_manual_control_setting' AND column_name = 'creator_id') AS column_exists;
ALTER TABLE `member_device_manual_control_setting` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';

-- changeset disinfection:member-device-manual-control-setting-creator-type
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_device_manual_control_setting' AND column_name = 'creator_type') AS column_exists;
ALTER TABLE `member_device_manual_control_setting` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';

-- changeset disinfection:member-device-manual-control-setting-updater-id
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_device_manual_control_setting' AND column_name = 'updater_id') AS column_exists;
ALTER TABLE `member_device_manual_control_setting` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';

-- changeset disinfection:member-device-manual-control-setting-updater-type
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'member_device_manual_control_setting' AND column_name = 'updater_type') AS column_exists;
ALTER TABLE `member_device_manual_control_setting` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
