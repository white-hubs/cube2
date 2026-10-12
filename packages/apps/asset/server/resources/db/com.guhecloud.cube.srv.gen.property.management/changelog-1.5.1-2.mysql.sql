-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `consumable_check` MODIFY COLUMN `end_time` date NULL DEFAULT NULL COMMENT '盘点结束时间' AFTER `start_time`;
ALTER TABLE `consumable_check` ADD COLUMN `reality_end_time` datetime NULL DEFAULT NULL COMMENT '实际盘点完成时间' AFTER `previous_check_end_time`;
