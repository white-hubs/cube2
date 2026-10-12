-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-mdata`.`stall` ADD COLUMN `master_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主名称' AFTER `bim_bind`;

ALTER TABLE `cube-biz-mdata`.`stall` ADD COLUMN `master_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主手机号' AFTER `master_name`;

