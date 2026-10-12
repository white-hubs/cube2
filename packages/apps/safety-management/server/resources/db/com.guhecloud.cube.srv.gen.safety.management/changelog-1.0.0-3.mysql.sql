-- liquibase formatted sql

-- changeset qqq:1686103358461-1

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE `cube_srv_safety_management`.`safety_risk_index` ADD COLUMN `risk_index_detail_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '风险明细json' AFTER `market_name`;

SET FOREIGN_KEY_CHECKS = 1;
