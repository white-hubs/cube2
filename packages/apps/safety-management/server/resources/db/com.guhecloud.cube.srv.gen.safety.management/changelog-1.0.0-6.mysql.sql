-- liquibase formatted sql

-- changeset qqq:1686103358461-1


ALTER TABLE `cube_srv_safety_management`.`device_manage` DROP COLUMN `genre_name`;

ALTER TABLE `cube_srv_safety_management`.`device_manage` DROP COLUMN `model_name`;

ALTER TABLE `cube_srv_safety_management`.`device_manage` ADD COLUMN `iot_device_prod_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备品类名称' AFTER `status`;

ALTER TABLE `cube_srv_safety_management`.`device_manage` ADD COLUMN `iot_device_prod_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称' AFTER `iot_device_prod_category_name`;