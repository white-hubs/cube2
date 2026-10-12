-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `att_approve_log` ADD COLUMN `scene_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景名称' AFTER `version`;
