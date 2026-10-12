-- liquibase formatted sql

-- changeset qqq:1686103358461-1
ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
    ADD COLUMN `device_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备状态' AFTER `code`,
ADD COLUMN `communication_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '在线离线状态' AFTER `device_status`,
ADD COLUMN `third_party_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方设备id' AFTER `communication_status`;