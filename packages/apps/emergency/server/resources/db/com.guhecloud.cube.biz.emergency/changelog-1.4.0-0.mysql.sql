-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'emergency_rule_disable') AS column_exists;
CREATE TABLE `emergency_rule_disable`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `rule_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '规则id',
  `duration` int(4) UNSIGNED NULL DEFAULT NULL COMMENT '禁用时长(小时)',
  `open_time` datetime NULL DEFAULT NULL COMMENT '打开时间',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_rule_id_tenant_deleted`(`rule_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规则禁用配置' ROW_FORMAT = Dynamic;
