-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-ouaa`.`tenant` ADD COLUMN `service_hot_line` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务热线电话' AFTER `floor_area`;

ALTER TABLE `cube-biz-ouaa`.`tenant` ADD COLUMN `lnt_and_lat` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经纬度信息' AFTER `logo_id`;