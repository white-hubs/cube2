-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-mdata`.`stall` ADD COLUMN `visualization_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可视化模型编号' AFTER `master_phone`;

ALTER TABLE `cube-biz-mdata`.`stall` ADD UNIQUE INDEX `idx_stall_tenant_deleted_visualization_code`(`tenant`, `deleted`, `visualization_code`) USING BTREE;
