-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `consumable_check` ADD COLUMN `previous_check_start_time` date NULL DEFAULT NULL COMMENT '上次盘点开始时间' AFTER `deleted`;
ALTER TABLE `consumable_check` ADD COLUMN `previous_check_end_time` date NULL DEFAULT NULL COMMENT '上次盘点结束时间' AFTER `previous_check_start_time`;
