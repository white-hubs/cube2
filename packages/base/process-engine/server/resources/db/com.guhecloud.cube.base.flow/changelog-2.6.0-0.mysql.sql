-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_flow_group') AS column_exists;
CREATE TABLE `form_flow_group` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '分组名称',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='表单分组';

-- changeset zengyadong:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_permission') AS column_exists;
CREATE TABLE `form_permission` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `form_flow_id` varchar(100) NOT NULL COMMENT '表单流程id',
  `permission_id` varchar(50) NOT NULL COMMENT '权限id',
  `permission_type` varchar(50) DEFAULT NULL COMMENT '权限类型',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='自定义表单权限';

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_flow' AND column_name='form_group_id') AS column_exists;
ALTER TABLE `form_flow` ADD COLUMN `form_group_id` bigint(20) NULL DEFAULT NULL COMMENT '自定义表单分组id';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_flow' AND column_name='short_name') AS column_exists;
ALTER TABLE `form_flow` ADD COLUMN `short_name` varchar(100) NULL DEFAULT NULL COMMENT '自定义表单简称';
