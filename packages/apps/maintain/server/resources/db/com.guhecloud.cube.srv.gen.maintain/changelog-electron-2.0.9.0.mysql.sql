-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-maintain`.`patrol_plan` ADD COLUMN `patrol_way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡更方式' AFTER `last_date`;
ALTER TABLE `cube-srv-gen-maintain`.`patrol_task` ADD COLUMN `patrol_way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡更方式' AFTER `end_date`;
ALTER TABLE `cube-srv-gen-maintain`.`patrol_task` ADD COLUMN `affirm_state` tinyint(1) NOT NULL DEFAULT 0 COMMENT '确认状态';
ALTER TABLE `cube-srv-gen-maintain`.`patrol_location` ADD COLUMN `bluetooth_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '蓝牙编号' AFTER `status`;
ALTER TABLE `cube-srv-gen-maintain`.`patrol_location` ADD COLUMN `device_id` bigint(20) DEFAULT NULL COMMENT '巡更设备id';
ALTER TABLE `cube-srv-gen-maintain`.`patrol_nfc_details` ADD COLUMN `bluetooth_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '蓝牙编号' AFTER `status`;
ALTER TABLE `cube-srv-gen-maintain`.`patrol_nfc_details` ADD COLUMN `device_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '蓝牙编号' AFTER `status`;