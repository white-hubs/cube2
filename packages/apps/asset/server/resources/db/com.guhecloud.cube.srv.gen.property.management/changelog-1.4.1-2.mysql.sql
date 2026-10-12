-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `property_detailed` MODIFY COLUMN `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户' AFTER `deleted`;
ALTER TABLE `property_detailed` MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `create_by`;