-- liquibase formatted sql

-- changeset grid:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_area_extend') AS column_exists;
CREATE TABLE `grid_area_extend`
(
    `id`                    bigint(20) unsigned NOT NULL COMMENT '主键',
    `grid_area_id`          bigint(20) unsigned NULL DEFAULT NULL COMMENT '网格区域id',
    `grid_department_id`    bigint(20) unsigned NULL DEFAULT NULL COMMENT '网格管理部门id',
    `grid_manager_ids`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网格管理长id（可多人，逗号分隔）',
    `tenant`                int(10) unsigned NULL DEFAULT NULL COMMENT '租户',
    `deleted`               bigint(20) unsigned NULL DEFAULT NULL COMMENT '删除标记',
    `create_by` varchar(100) NOT NULL COMMENT '创建用户',
    `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
    `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) NOT NULL COMMENT '更新用户',
    `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
    `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_grid_area_id` (`grid_area_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '区域网格扩展表' ROW_FORMAT = COMPACT;

-- changeset grid:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_relation_member') AS column_exists;
CREATE TABLE `grid_relation_member`
(
    `id`            bigint(20) unsigned NOT NULL COMMENT '主键',
    `grid_area_id`  bigint(20) unsigned NULL DEFAULT NULL COMMENT '网格区域id',
    `relation_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联类型（DEPARTMENT:部门, ROLE:角色, USER:人员, POST:岗位, POSITION:职位）',
    `relation_id`   bigint(20) unsigned NOT NULL COMMENT '关联id',
    `tenant`        int(10) unsigned NULL DEFAULT NULL COMMENT '租户',
    `deleted`       bigint(20) unsigned NULL DEFAULT NULL COMMENT '删除标记',
    `create_by`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_grid_area_id` (`grid_area_id`) USING BTREE,
    INDEX `idx_relation_type_id` (`relation_type`, `relation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '网格关联成员表' ROW_FORMAT = COMPACT;

