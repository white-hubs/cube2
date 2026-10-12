-- liquibase formatted sql

-- changeset lijie:ALTER-1

ALTER TABLE `patrol_location` MODIFY COLUMN `nfc_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'nfc芯片编号';
ALTER TABLE `patrol_location` MODIFY COLUMN `patrol_longitude` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度';
ALTER TABLE `patrol_location` MODIFY COLUMN `patrol_dimension` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度';