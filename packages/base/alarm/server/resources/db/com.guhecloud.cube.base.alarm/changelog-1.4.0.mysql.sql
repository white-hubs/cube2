-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'alarm_record' AND column_name = 'snapshot' ) AS column_exists;
ALTER TABLE `alarm_record` MODIFY COLUMN `snapshot` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '物联快照' AFTER `application_name`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'alarm_record' AND column_name = 'business_snapshot' ) AS column_exists;
ALTER TABLE `alarm_record` ADD COLUMN `business_snapshot` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '业务快照' AFTER `alarm_snapshot`;

