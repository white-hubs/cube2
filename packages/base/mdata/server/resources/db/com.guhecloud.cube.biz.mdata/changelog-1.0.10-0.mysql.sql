-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-mdata`.`area` ADD COLUMN `visualization_area_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可视化区域类型' AFTER `tenant`;
