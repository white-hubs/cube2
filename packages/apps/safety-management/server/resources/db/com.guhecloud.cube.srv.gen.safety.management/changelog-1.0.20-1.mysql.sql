-- liquibase formatted sql

-- changeset qqq:1686103358461-1


ALTER TABLE `property_filing` ADD COLUMN `property_sub_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产类别' AFTER `property_sub_type`;
ALTER TABLE `property_filing` ADD COLUMN `serial_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '风险源编码' AFTER `property_sub_type_name`;
ALTER TABLE `property_filing` ADD COLUMN `receive_dep_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收部门名称' AFTER `serial_number`;
ALTER TABLE `property_filing` ADD COLUMN `maintenance_dep_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维护部门' AFTER `receive_dep_name`;
