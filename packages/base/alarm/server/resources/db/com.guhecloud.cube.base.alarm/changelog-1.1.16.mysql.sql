-- liquibase formatted sql

-- changeset qqq:1686103358461-1


ALTER TABLE `cube-base-alarm`.`alarm_record` ADD COLUMN `filter_scope` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '过滤报警作用域' AFTER `alarm_snapshot`;