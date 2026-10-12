-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cascade_dictionary` MODIFY COLUMN `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号';
ALTER TABLE `cascade_dictionary` ADD COLUMN `permission_state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限（可编辑、可删除）';
ALTER TABLE `data_dictionary` MODIFY COLUMN `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号';
ALTER TABLE `dictionary_item` MODIFY COLUMN `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编码';
ALTER TABLE `dictionary_item` ADD UNIQUE INDEX `唯一索引`(`code`) USING BTREE;
ALTER TABLE `sys_dictionary` MODIFY COLUMN `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号';