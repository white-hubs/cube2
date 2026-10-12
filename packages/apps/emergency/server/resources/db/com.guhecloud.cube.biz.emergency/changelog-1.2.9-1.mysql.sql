-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-emergency`.`emergency_file` ADD COLUMN `level_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '级别编号' AFTER `name`;

ALTER TABLE `cube-biz-emergency`.`emergency_file` ADD COLUMN `expiration_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到期状态' AFTER `level_code`;

ALTER TABLE `cube-biz-emergency`.`emergency_file` ADD COLUMN `expiration_date` datetime NULL DEFAULT NULL COMMENT '到期日期' AFTER `expiration_status`;

ALTER TABLE `cube-biz-emergency`.`emergency_file` ADD COLUMN `emergency_file_type_id` bigint(20) NULL DEFAULT NULL COMMENT '文件类型主键' AFTER `expiration_date`;

ALTER TABLE `cube-biz-emergency`.`emergency_rule` ADD COLUMN `notify_command_dispatch` tinyint(1) NULL DEFAULT NULL COMMENT '是否推送到调度中心' AFTER `appoint_device`;

CREATE TABLE `cube-biz-emergency`.`emergency_file_notice`  (
  `id` bigint(20) NOT NULL,
  `scene_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景编号',
  `notice_way` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知通道',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_scene_code_tenant_deleted`(`scene_code`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案文件通知配置' ROW_FORMAT = Dynamic;

CREATE TABLE `cube-biz-emergency`.`emergency_file_notice_obj`  (
  `id` bigint(20) NOT NULL,
  `scene_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息场景编号',
  `emergency_file_type_id` bigint(20) NULL DEFAULT NULL COMMENT '文件类型主键',
  `obj_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送对象类型',
  `obj_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发送对象主键',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案文件消息推送人员配置' ROW_FORMAT = Dynamic;

CREATE TABLE `cube-biz-emergency`.`emergency_file_type`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预案文件类型' ROW_FORMAT = Dynamic;