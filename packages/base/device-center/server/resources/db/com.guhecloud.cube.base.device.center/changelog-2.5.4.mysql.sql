-- liquibase formatted sql

-- changeset zhongyuanli:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_dev_device' AND column_name='main_device_id') AS column_exists;
ALTER TABLE `iot_dev_device` ADD COLUMN `main_device_id` BIGINT UNSIGNED NULL DEFAULT NULL COMMENT '关联主设备ID';

-- changeset zhongyuanli:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_dev_device' AND column_name='slave_device_ids') AS column_exists;
ALTER TABLE `iot_dev_device` ADD COLUMN `slave_device_ids` VARCHAR(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关联子设备IDs（使用,分隔）';