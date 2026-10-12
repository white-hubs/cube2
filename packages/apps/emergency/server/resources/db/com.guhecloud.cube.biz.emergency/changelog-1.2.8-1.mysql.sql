-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE IF NOT EXISTS `cube-biz-emergency`.`emergency_rule_writer_event`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规则主键',
  `emergency_rule_time_id` bigint(20) NULL DEFAULT NULL COMMENT '规则时段主键',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备主键',
  `device_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `event_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件编号',
  `threshold_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件参数名',
  `threshold_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件参数值',
  `write_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '写值状态',
  `start_time` datetime NULL DEFAULT NULL COMMENT '写值有效开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '写值有效结束时间',
  `retry_count` int(2) NULL DEFAULT NULL COMMENT '重试次数',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '事件写值日志' ROW_FORMAT = Dynamic;

CREATE TABLE IF NOT EXISTS `cube-biz-emergency`.`emergency_rule_writer_event_log`  (
  `id` bigint(20) NOT NULL,
  `emergency_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规则主键',
  `emergency_rule_time_id` bigint(20) NULL DEFAULT NULL COMMENT '规则时段主键',
  `emergency_rule_writer_event_id` bigint(20) NULL DEFAULT NULL COMMENT '事件写值记录主键',
  `iot_write_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联写入状态',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '事件写值日志' ROW_FORMAT = Dynamic;