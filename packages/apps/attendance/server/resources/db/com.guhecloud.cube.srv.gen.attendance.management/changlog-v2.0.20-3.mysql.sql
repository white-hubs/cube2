-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `att_clock_in_records` ADD INDEX `clock_after_time`(`clock_after_time`) USING BTREE;
