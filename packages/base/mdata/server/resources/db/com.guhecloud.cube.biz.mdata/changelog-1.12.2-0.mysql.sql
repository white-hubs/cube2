-- liquibase formatted sql

-- changeset grid:ddl-1.12.2-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_model_config') AS column_exists;
CREATE TABLE `grid_model_config`
(
    `id`            bigint(20) unsigned NOT NULL COMMENT '主键',
    `model_item_id` bigint(20) unsigned NULL DEFAULT NULL COMMENT '模型项目id',
    `tenant`        int(10) unsigned NULL DEFAULT NULL COMMENT '租户',
    `deleted`       bigint(20) unsigned NULL DEFAULT NULL COMMENT '删除标记',
    `create_by` varchar(100) NOT NULL COMMENT '创建用户',
    `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
    `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) NOT NULL COMMENT '更新用户',
    `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
    `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网格模型全局配置表' ROW_FORMAT = COMPACT;

