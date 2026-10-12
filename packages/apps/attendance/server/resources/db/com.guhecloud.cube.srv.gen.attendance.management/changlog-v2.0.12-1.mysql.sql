-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `att_approve_log` ADD COLUMN `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '版本号';