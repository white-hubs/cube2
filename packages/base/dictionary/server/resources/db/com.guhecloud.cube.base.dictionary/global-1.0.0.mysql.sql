-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `cascade_dictionary`  (
                                       `id` bigint(20) NOT NULL COMMENT '主键',
                                       `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                                       `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                                       `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                       `tenant` int(11) NOT NULL COMMENT '租户id',
                                       `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
                                       `variable` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变量',
                                       `item_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编码',
                                       `level` int(11) NOT NULL DEFAULT 1 COMMENT '级别',
                                       `superiors_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '上一级id',
                                       `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
                                       `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                       `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                       `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：启用：enable；禁用：disable',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '级联字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for cascade_tree
-- ----------------------------
CREATE TABLE `cascade_tree`  (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `dictionary_id` bigint(20) NOT NULL COMMENT '字典id',
                                 `superiors_ids` tinytext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所有父级id',
                                 `next_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '所有子集id',
                                 `level` int(11) NOT NULL COMMENT '层级',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1687362741420261379 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
CREATE TABLE `data_dictionary`  (
                                    `id` bigint(20) NOT NULL COMMENT '主键',
                                    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                                    `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                                    `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                    `tenant` int(11) NOT NULL COMMENT '租户id',
                                    `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
                                    `variable` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变量',
                                    `item_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编号',
                                    `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
                                    `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：启用：enable；禁用：disable',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通用字典' ROW_FORMAT = DYNAMIC;
-- ----------------------------
-- Table structure for dictionary_item
-- ----------------------------
CREATE TABLE `dictionary_item`  (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编码',
                                    `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '备注',
                                    `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
                                    `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE INDEX `唯一索引`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1701534728015151106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
CREATE TABLE `sys_dictionary`  (
                                   `id` bigint(20) NOT NULL COMMENT '主键',
                                   `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                                   `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                                   `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                   `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '版本号',
                                   `variable` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变量',
                                   `item_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编号',
                                   `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
                                   `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：启用（ENABLE）、禁用（DISABLE）、删除（DELETE）',
                                   `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统字典' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;


-- 初始化文件桶
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (3, 'file-minio-bucket', 'minio桶名称', 0, '1', '2023-07-10 10:01:42', '1', '2023-07-10 10:01:42');
