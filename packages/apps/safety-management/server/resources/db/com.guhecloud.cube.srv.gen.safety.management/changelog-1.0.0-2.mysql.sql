-- liquibase formatted sql

-- changeset qqq:1686103358461-1

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `cube_srv_safety_management`.`alarm_notice`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alarm_level_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警级别编号',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `value` int(5) UNSIGNED NULL DEFAULT NULL COMMENT '条件值',
  `scene_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息场景编号',
  `enabled` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否启用',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_alarm_level_code_business_type_tenant_deleted`(`alarm_level_code`, `business_type`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警通知配置' ROW_FORMAT = DYNAMIC;

CREATE TABLE `cube_srv_safety_management`.`alarm_notice_obj`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `alarm_notice_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '报警通知配置主键',
  `obj_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知对象类型',
  `obj_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知对象主键',
  `obj_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '通知对象信息',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警通知对象' ROW_FORMAT = DYNAMIC;

CREATE TABLE `cube_srv_safety_management`.`alarm_notice_way`  (
  `id` bigint(20) NOT NULL,
  `alarm_notice_id` bigint(20) NULL DEFAULT NULL COMMENT '报警消息配置id',
  `notice_way` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知方式',
  `template_id` bigint(20) NULL DEFAULT NULL COMMENT '模版id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警通知方式' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
