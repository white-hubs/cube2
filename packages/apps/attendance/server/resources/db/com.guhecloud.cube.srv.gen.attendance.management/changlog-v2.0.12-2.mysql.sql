-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `work_overtime_rule` MODIFY COLUMN `units` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计算单位(半小时、小时、天)' AFTER `name`;