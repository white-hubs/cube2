-- liquibase formatted sql
--
-- changeset ht:1686103358461-1

CREATE TABLE IF NOT EXISTS `tag_business` (
  `id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '描述',
  `parent_id` bigint DEFAULT '0' COMMENT '父级id',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `deleted` bigint DEFAULT NULL,
  `tenant` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `tag_ref` (
  `id` bigint NOT NULL,
  `tag_id` bigint NOT NULL COMMENT '标签id',
  `ref_id` bigint NOT NULL COMMENT '业务id',
  `ref_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务类型',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `deleted` bigint DEFAULT NULL,
  `tenant` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;