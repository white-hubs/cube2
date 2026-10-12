-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

CREATE TABLE `work_plan`  (
  `id` bigint(20) NOT NULL,
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `type` bigint(20) NULL DEFAULT NULL COMMENT '类型',
  `publisher_id` bigint(20) NULL DEFAULT NULL COMMENT '发布人ID',
  `publisher_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布人',
  `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '发布部门ID',
  `dept_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布部门名称',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布内容',
  `file_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件IDS',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `tenant` int(11) NULL DEFAULT NULL COMMENT 'tenant',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'create_by',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'update_by',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT 'update_time',
  `deleted` bigint(40) NULL DEFAULT NULL COMMENT 'deleted 是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工作安排' ROW_FORMAT = Dynamic;

CREATE TABLE `work_plan_type`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态 0 禁用 1 启用',
  `tenant` int(11) NULL DEFAULT NULL COMMENT 'tenant',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'create_by',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create_time',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'update_by',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT 'update_time',
  `deleted` bigint(40) NULL DEFAULT NULL COMMENT 'deleted 是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;