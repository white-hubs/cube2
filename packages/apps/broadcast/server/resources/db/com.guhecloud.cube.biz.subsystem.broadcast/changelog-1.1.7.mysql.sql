-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE IF NOT EXISTS `broadcast_play_rule`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '启用状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则描述',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '广播播放规则' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `broadcast_play_rule_device`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规则主键',
  `device_prod_category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号',
  `device_prod_category_code_concat` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类编号序列号',
  `device_prod_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品品类名称',
  `device_prod_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备产品主键',
  `device_prod_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备产品名称',
  `device_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备主键',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_area_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备所属区域',
  `device_area_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备所属区域名称',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '广播播放规则设备' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `broadcast_play_rule_template`  (
  `id` bigint(20) NOT NULL,
  `rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规则主键',
  `template_id` bigint(20) NULL DEFAULT NULL COMMENT '模板主键',
  `template_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `template_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规则关联模板' ROW_FORMAT = Dynamic;