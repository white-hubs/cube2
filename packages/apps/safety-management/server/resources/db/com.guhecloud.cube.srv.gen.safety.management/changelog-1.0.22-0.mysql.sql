-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `cube_srv_safety_management`.`education_market_task_url`  (
  `id` bigint(20) NOT NULL,
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT '市场配置主键',
  `market_tenant` int(10) NULL DEFAULT NULL COMMENT '市场编号',
  `task_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型(区分是考试or培训)',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '启用状态',
  `tenant` int(10) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '总部市场任务链接信息' ROW_FORMAT = DYNAMIC;
