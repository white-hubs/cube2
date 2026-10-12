-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- 创建二维码三方链接的表
CREATE TABLE `qrcode_third_link`  (
  `id` bigint(20) NOT NULL,
  `qrcode_id` bigint(20) NULL DEFAULT NULL COMMENT '二维码ID',
  `third_link_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方链接名称',
  `third_link_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '三方链接URL',
  `tenant` int(255) NULL DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 二维码应用
CREATE TABLE `qrcode_application`  (
  `id` bigint(20) NOT NULL,
  `application_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编码',
  `application_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
