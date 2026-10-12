-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'emergency_rule' AND column_name = 'turn_warning_sound' ) AS column_exists;
ALTER TABLE `emergency_rule` ADD COLUMN `turn_warning_sound` tinyint(1) NULL DEFAULT NULL COMMENT '是否开启警告声音' AFTER `notify_command_dispatch`;
