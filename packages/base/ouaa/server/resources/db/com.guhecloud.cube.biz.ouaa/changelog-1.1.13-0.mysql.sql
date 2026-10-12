-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-ouaa`.`user_organization` DROP INDEX `idx_user_organization_user_id_organization_id_position_id`;

ALTER TABLE `cube-biz-ouaa`.`user_organization` ADD COLUMN `appointment_id` varchar(255) DEFAULT NULL COMMENT '职位ID';

ALTER TABLE `cube-biz-ouaa`.`user_organization` ADD UNIQUE INDEX `idx_user_organization_user_id_organization_id_position_id`(`user_id`, `organization_id`, `position_id`, `appointment_id`) USING BTREE;

CREATE TABLE `cube-biz-ouaa`.`appointment`  (
                                                `id` bigint(20) NOT NULL COMMENT '主键',
                                                `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '职位名称',
                                                `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位描述',
                                                `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '启停用状态',
                                                `tenant` int(10) NOT NULL COMMENT '租户主键',
                                                `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
                                                `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
                                                `create_time` datetime NOT NULL COMMENT '创建时间',
                                                `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
                                                `update_time` datetime NOT NULL COMMENT '更新时间',
                                                PRIMARY KEY (`id`) USING BTREE
)