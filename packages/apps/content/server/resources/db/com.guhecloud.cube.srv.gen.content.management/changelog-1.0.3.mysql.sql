-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-content-management`.`device_document` DROP COLUMN `device_type_id`;

ALTER TABLE `cube-srv-gen-content-management`.`device_document` DROP COLUMN `device_type_name`;

ALTER TABLE `cube-srv-gen-content-management`.`device_document` DROP COLUMN `device_id`;

ALTER TABLE `cube-srv-gen-content-management`.`device_document` ADD COLUMN `device_prod_category_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号' AFTER `name`;

ALTER TABLE `cube-srv-gen-content-management`.`device_document` ADD COLUMN `device_prod_category_code_concat` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类序列号' AFTER `device_prod_category_code`;

ALTER TABLE `cube-srv-gen-content-management`.`device_document` ADD COLUMN `device_prod_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称' AFTER `device_prod_category_code_concat`;

ALTER TABLE `cube-srv-gen-content-management`.`device_document` ADD COLUMN `device_prod_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称' AFTER `device_prod_category_name`;

CREATE TABLE `cube-srv-gen-content-management`.`device_document_ref_product`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `device_document_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '知识文档主键',
  `device_prod_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备产品主键',
  `device_prod_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称',
  `deleted` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(10) UNSIGNED NOT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识文档关联设备产品' ROW_FORMAT = Dynamic;