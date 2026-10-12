-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `property_detailed` MODIFY COLUMN `purchase_date` date NULL DEFAULT NULL COMMENT '购入时间';
ALTER TABLE `property_detailed` ADD COLUMN `maintenance_dep_id` bigint(20) NULL DEFAULT NULL COMMENT '维护部门';
ALTER TABLE `property_detailed` ADD COLUMN `maintenance_dep_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维护部门';