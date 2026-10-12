-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-base-alarm`.`alarm_record` ADD COLUMN `alarm_event_type_id` bigint(20) NULL DEFAULT NULL COMMENT '报警事件分类主键' AFTER `alarm_event_name`;

ALTER TABLE `cube-base-alarm`.`alarm_record` ADD COLUMN `alarm_event_type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警事件分类编号' AFTER `alarm_event_type_id`;

ALTER TABLE `cube-base-alarm`.`alarm_record` ADD COLUMN `alarm_event_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警事件分类名称' AFTER `alarm_event_type_code`;

ALTER TABLE `cube-base-alarm`.`alarm_record_operator_log` MODIFY COLUMN `operator_content` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容' AFTER `operator_time`;

ALTER TABLE `cube-base-alarm`.`alarm_record_target` DROP COLUMN `terminal`;