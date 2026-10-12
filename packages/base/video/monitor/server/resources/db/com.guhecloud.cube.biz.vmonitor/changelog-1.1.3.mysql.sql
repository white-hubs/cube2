-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-vmonitor`.`multiple_video_collect_camera` ADD COLUMN `source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收藏来源' AFTER `sort`;

