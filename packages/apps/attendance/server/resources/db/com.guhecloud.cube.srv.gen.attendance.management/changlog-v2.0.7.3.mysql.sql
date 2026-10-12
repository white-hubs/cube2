-- liquibase formatted sql

-- changeset qqq:1686103358461-1


ALTER TABLE `att_clock_in_records` ADD COLUMN `mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识：数据清洗用，无具体意义' AFTER `clock_before_time`;
