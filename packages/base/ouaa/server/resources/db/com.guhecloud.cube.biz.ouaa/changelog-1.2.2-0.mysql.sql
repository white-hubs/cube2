-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_behavior') AS column_exists;
CREATE TABLE `user_behavior`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户主键',
  `terminal` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端编号',
  `behavior_config` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '用户行为配置',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_id_terminal_tenant_deleted`(`user_id`, `terminal`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户行为配置' ROW_FORMAT = Dynamic;
