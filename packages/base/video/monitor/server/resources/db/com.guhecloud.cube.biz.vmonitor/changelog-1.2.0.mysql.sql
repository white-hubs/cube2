-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'auth_group') AS table_exists;
CREATE TABLE `auth_group` (
  `id` bigint NOT NULL COMMENT '主键',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='权限组';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'auth_scope') AS table_exists;
CREATE TABLE `auth_scope` (
  `id` bigint NOT NULL COMMENT '主键',
  `scope_type` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '范围类型',
  `scope_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '范围名称',
  `scope_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '范围id',
  `auth_group_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '权限组id',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='权限范围';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'auth_grant_target') AS table_exists;
CREATE TABLE `auth_grant_target` (
  `id` bigint NOT NULL COMMENT '主键',
  `target_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对象类型',
  `target_id` bigint NOT NULL COMMENT '对象id',
  `auth_group_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '权限组id',
  `deleted` bigint unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='授权对象';
