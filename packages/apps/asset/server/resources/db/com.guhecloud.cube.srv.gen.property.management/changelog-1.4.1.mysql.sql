-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE property_detailed MODIFY COLUMN `maintenance_time` int(6) NULL DEFAULT NULL COMMENT '维保期限（月）' AFTER `purchase_date`;
ALTER TABLE property_detailed MODIFY COLUMN `usage_time` int(6) NULL DEFAULT NULL COMMENT '使用期限（月）' AFTER `maintenance_date`;
ALTER TABLE property_detailed MODIFY COLUMN `depository_numbers` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库单号（RK+日期）' AFTER `update_time`;