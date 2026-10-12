-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube_srv_safety_management`.`market_monthly_report` DROP COLUMN `report_style`;

ALTER TABLE `cube_srv_safety_management`.`market_monthly_report` ADD COLUMN `report_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '填报数据(前端使用)' AFTER `status`;

ALTER TABLE `cube_srv_safety_management`.`monthly_report` DROP COLUMN `report_style`;

ALTER TABLE `cube_srv_safety_management`.`monthly_report` ADD COLUMN `report_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '填报数据(前端使用)' AFTER `status`;