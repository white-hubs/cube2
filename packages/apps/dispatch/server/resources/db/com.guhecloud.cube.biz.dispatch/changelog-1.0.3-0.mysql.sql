-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- 增加sort字段
ALTER TABLE `cube-biz-dispatch`.`dispatch_group`
ADD COLUMN `sort` int(11) NULL COMMENT '排序号' AFTER `valid_time`;

-- 创建组的扩展表
CREATE TABLE `dispatch_group_extend`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组ID',
  `is_record` tinyint(1) NULL DEFAULT NULL COMMENT '是否录音',
  `is_video` tinyint(1) NULL DEFAULT NULL COMMENT '是否录像',
  `group_level` int(11) NULL DEFAULT NULL COMMENT '组级别',
  `intercom_idle_time_out` int(11) NULL DEFAULT NULL COMMENT '对讲空闲超时',
  `intercom_power_time_count` int(11) NULL DEFAULT NULL COMMENT '话权方总超时',
  `queue_max_num` int(11) NULL DEFAULT NULL COMMENT '排队人数限制',
  `intercom_state_space` int(11) NULL DEFAULT NULL COMMENT '对讲状态通知时间间隔',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `group_id_tenant_index`(`group_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 增加密码字段
ALTER TABLE `cube-biz-dispatch`.`dispatch_device`
ADD COLUMN `password` varchar(256) NULL COMMENT '密码；通常与code相同' AFTER `code`;

-- 设备扩展表
CREATE TABLE `dispatch_device_extend`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `device_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备ID',
  `is_record` tinyint(1) NULL DEFAULT NULL COMMENT '是否录音',
  `is_video` tinyint(1) NULL DEFAULT NULL COMMENT '是否录像',
  `device_level` int(11) NULL DEFAULT NULL COMMENT '设备级别',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `device_tenant_index`(`device_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;