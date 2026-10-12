-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-property-management`.`risk_source_ledger` MODIFY COLUMN `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '风险源名称' AFTER `serial_number`;