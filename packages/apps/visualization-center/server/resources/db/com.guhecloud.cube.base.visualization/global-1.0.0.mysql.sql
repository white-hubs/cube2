-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- 建表
CREATE TABLE `instance_manage`
(
    `id`            bigint(20) NOT NULL,
    `project_id`    bigint(20) NOT NULL COMMENT '项目id',
    `instance_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例名称',
    `path`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接地址',
    `status`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '状态',
    `deleted`       bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`        int(10) NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发布实例' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_manage
-- ----------------------------
CREATE TABLE `model_manage`
(
    `id`          bigint(20) NOT NULL,
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型名称',
    `model_type`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型类型',
    `address`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型地址',
    `file`        bigint(20) NULL DEFAULT NULL COMMENT '模型封面',
    `deleted`     bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`      int(10) NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模型管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_route
-- ----------------------------
CREATE TABLE `model_route`
(
    `id`          bigint(20) UNSIGNED NOT NULL COMMENT '主键',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型导航名称',
    `code`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型导航编号',
    `level`       int(10) UNSIGNED NOT NULL COMMENT '层级',
    `parent_id`   bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父类主键',
    `serial`      int(10) NOT NULL COMMENT '序号',
    `tenant`      int(10) UNSIGNED NOT NULL COMMENT '租户编号',
    `deleted`     bigint(20) NOT NULL COMMENT '是否删除',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
    `create_time` datetime                                                NOT NULL COMMENT '创建时间',
    `update_by`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
    `update_time` datetime                                                NOT NULL COMMENT '更新时间',
    `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型地址',
    `directory`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型导航目录',
    `model_id`    bigint(20) NOT NULL COMMENT '模型id',
    `tag`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for projcet_canvas
-- ----------------------------
CREATE TABLE `projcet_canvas`
(
    `id`          bigint(20) NOT NULL,
    `canvas_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `project_id`  bigint(20) NOT NULL COMMENT '项目id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_manage
-- ----------------------------
CREATE TABLE `project_manage`
(
    `id`             bigint(20) NOT NULL,
    `project_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
    `canvas`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '画布比例',
    `main_structure` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主体结构',
    `deleted`        bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
    `update_time`    datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`         int(10) NOT NULL COMMENT '租户主键',
    `thumbnail`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缩略图',
    `model_ids`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目管理' ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;

-- 字典
-- 字典项
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`,
                                                      `update_by`, `update_time`)
VALUES (3, 'file-minio-bucket', 'minio桶名称', 0, '1', '2023-07-10 10:01:42', '1', '2023-07-10 10:01:42');
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`,
                                                      `update_by`, `update_time`)
VALUES (1696091959617867777, 'visualization-modelType', '三维可视化-模型类型', 0, '17311369782', '2023-08-28 17:26:50',
        '17311369782', '2023-08-28 17:26:50');
-- 系统字典
INSERT INTO `cube-base-dictionary`.`sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`,
                                                     `item_code`, `create_by`, `create_time`, `update_by`,
                                                     `update_time`, `status`, `deleted`)
VALUES (1696094549569798146, 'UE5', 'unreal-engine-five', '模型类型-UE5', 'v1.0.0', NULL, 'visualization-modelType',
        '17311369782', '2023-08-28 17:37:07', '17311369782', '2023-08-28 17:37:07', 'ENABLE', 0);
INSERT INTO `cube-base-dictionary`.`sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`,
                                                     `item_code`, `create_by`, `create_time`, `update_by`,
                                                     `update_time`, `status`, `deleted`)
VALUES (1696096274284380161, '二维组态', 'two-dimensional-config', '模型类型-二维组态', 'v1.0.0', NULL,
        'visualization-modelType', '17311369782', '2023-08-28 17:43:58', '17311369782', '2023-08-28 17:43:58', 'ENABLE',
        0);
INSERT INTO `cube-base-dictionary`.`sys_dictionary` (`id`, `name`, `code`, `remarks`, `version`, `variable`,
                                                     `item_code`, `create_by`, `create_time`, `update_by`,
                                                     `update_time`, `status`, `deleted`)
VALUES (1696427627169284097, 'visualconflg', 'VISUALCONFIG', '可视化中心bucket', 'v1.0.0', NULL, 'file-minio-bucket',
        '17311369782', '2023-08-29 15:40:39', '17311369782', '2023-08-29 15:40:39', 'ENABLE', 0);
