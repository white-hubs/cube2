-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'event' AND column_name = 'trigger_target_id') AS column_exists;
ALTER TABLE `event` ADD COLUMN `trigger_target_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '触发对象唯一标识' AFTER `trigger_time`;
