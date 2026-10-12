-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_app_device') AS column_exists;
CREATE TABLE `safety_app_device` (
  `id` bigint(20) DEFAULT NULL COMMENT '设备id',
  `application_code` varchar(50) DEFAULT NULL COMMENT '应用编号',
  UNIQUE KEY `idx_id_application_code` (`id`,`application_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='安全应用设备';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'device_manage' AND column_name='application_code') AS column_exists;
ALTER TABLE `device_manage` MODIFY COLUMN `application_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '应用code,多个用逗号隔开' AFTER `department_name`;