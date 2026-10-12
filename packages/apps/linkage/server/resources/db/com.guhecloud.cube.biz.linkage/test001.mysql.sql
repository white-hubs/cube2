-- liquibase formatted sql

-- changeset qqq:1686103358461-1

DROP TABLE IF EXISTS `test001`;
CREATE TABLE `test001`
(
    `id`                 bigint(11) NOT NULL,
    `patrol_route_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更路线名称',
    `patrol_location_id` bigint(11) NOT NULL COMMENT '巡更点id',
    `rule_order`         int(10) NOT NULL COMMENT '顺序',
    `deleted`            bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time`        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
    `update_time`        datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`             int(10) NOT NULL COMMENT '租户主键',
    `status`             varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '删除状态',
    `uuid`               bigint(20) NULL DEFAULT NULL COMMENT '唯一标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡更路线' ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `test002`;
CREATE TABLE `test002`
(
    `id`                 bigint(11) NOT NULL,
    `patrol_route_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '巡更路线名称',
    `patrol_location_id` bigint(11) NOT NULL COMMENT '巡更点id',
    `rule_order`         int(10) NOT NULL COMMENT '顺序',
    `deleted`            bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time`        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
    `update_time`        datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`             int(10) NOT NULL COMMENT '租户主键',
    `status`             varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '删除状态',
    `uuid`               bigint(20) NULL DEFAULT NULL COMMENT '唯一标识',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡更路线' ROW_FORMAT = DYNAMIC;

