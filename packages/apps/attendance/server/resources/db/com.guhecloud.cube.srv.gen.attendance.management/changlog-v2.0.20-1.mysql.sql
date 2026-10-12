-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `att_clock_in_ledger_details` ADD COLUMN `ledger_belong_date` date NULL DEFAULT NULL COMMENT '日期' AFTER `start_clock_in_time`;
ALTER TABLE `att_clock_in_ledger_details` ADD COLUMN `user_id` bigint(20) NULL DEFAULT NULL AFTER `ledger_belong_date`;
ALTER TABLE `att_clock_in_ledger_details` ADD COLUMN `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `user_id`;
ALTER TABLE `att_clock_in_ledger_details` ADD COLUMN `end_clock_in_time` datetime NULL DEFAULT NULL COMMENT '下班时间' AFTER `user_name`;
ALTER TABLE `att_clock_in_ledger_details` ADD COLUMN `actual_start_clock_in_time` datetime NULL DEFAULT NULL COMMENT '上班打卡时间' AFTER `end_clock_in_time`;
ALTER TABLE `att_clock_in_ledger_details` ADD COLUMN `actual_end_clock_in_time` datetime NULL DEFAULT NULL COMMENT '下班打卡时间' AFTER `actual_start_clock_in_time`;
ALTER TABLE `att_clock_in_ledger_details` ADD COLUMN `classes_details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次信息' AFTER `actual_end_clock_in_time`;
