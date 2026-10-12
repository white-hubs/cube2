-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'pork_arrange_push_data' AND column_name = 'next_retry_time' ) AS column_exists;
ALTER TABLE `pork_arrange_push_data` ADD COLUMN `next_retry_time` datetime NULL DEFAULT NULL COMMENT '下次推送时间' AFTER `status`;