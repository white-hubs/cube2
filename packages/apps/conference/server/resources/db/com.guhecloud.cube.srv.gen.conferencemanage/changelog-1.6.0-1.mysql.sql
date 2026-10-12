-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'room_screen_config' AND column_name='program_id') AS column_exists;
ALTER TABLE `room_screen_config` ADD COLUMN `program_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节目id';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'room_screen_config' AND column_name='manufacturer_code') AS column_exists;
ALTER TABLE `room_screen_config` ADD COLUMN `manufacturer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂家编码';