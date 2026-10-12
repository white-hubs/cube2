-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'iot_event_consume') AS column_exists;
CREATE TABLE `iot_event_consume`  (
  `id` bigint(20) NOT NULL,
  `iot_event_instance_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联网事件实例主键',
  `additional` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '附件信息',
  `consumed` tinyint(1) NULL DEFAULT NULL COMMENT '是否消费',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户信息',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'IOT事件附件消息' ROW_FORMAT = Dynamic;