-- liquibase formatted sql

-- changeset parking:alter-access-record-image-url-length
ALTER TABLE `parking_access_record` MODIFY COLUMN `image_url` varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片路径';
