-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `cube-base-alarm`.`safety_risk_score`  (
  `id` bigint(11) UNSIGNED NOT NULL COMMENT 'id',
  `index_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指标类型',
  `section_start` double(6, 2) NULL DEFAULT NULL COMMENT '区间开始值',
  `section_end` double(6, 2) NULL DEFAULT NULL COMMENT '区间结束值',
  `slope` double(6, 2) NULL DEFAULT NULL COMMENT '斜率',
  `score_start` int(11) NULL DEFAULT NULL COMMENT '分数开始值',
  `score_end` int(11) NULL DEFAULT NULL COMMENT '分值结束值',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分值区间' ROW_FORMAT = Dynamic;

CREATE TABLE `cube-base-alarm`.`weight_allot`  (
  `id` bigint(11) UNSIGNED NOT NULL COMMENT 'id',
  `index_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指标类型',
  `index_item` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指标项',
  `weight` int(11) NULL DEFAULT NULL COMMENT '权重占比',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权重分配' ROW_FORMAT = Dynamic;