-- liquibase formatted sql

-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_repair_relation') AS column_exists;
CREATE TABLE IF NOT EXISTS `inspect_repair_relation` (
  `id` bigint unsigned NOT NULL COMMENT '巡检记录ID',

  `organization_id` bigint unsigned DEFAULT NULL COMMENT '组织机构ID',
  `organization_name` varchar(100) DEFAULT NULL COMMENT '组织机构名',
  `position_id` bigint unsigned DEFAULT NULL COMMENT '岗位ID',
  `position_name` varchar(100) DEFAULT NULL COMMENT '岗位名',
  `user_id` bigint unsigned DEFAULT NULL COMMENT '用户ID',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(100) DEFAULT NULL COMMENT '用户姓名',

  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` bigint unsigned DEFAULT NULL COMMENT '删除标志',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',

  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant` (`tenant`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_organization_id` (`organization_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='巡检-维修关联关系表';