-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'center_biz_tag_rule') AS table_exists;
CREATE TABLE `center_biz_tag_rule` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键',
  `tag_code` varchar(100) DEFAULT NULL COMMENT '标签编码',
  `rule` text COMMENT '规则',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_tag_code_tenant_deleted` (`tag_code`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备标签规则';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'center_biz_group') AS table_exists;
CREATE TABLE `center_biz_group` (
  `id` bigint(20) unsigned NOT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '名称',
  `remark` varchar(500) DEFAULT NULL COMMENT '描叙',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name_tenant_deleted` (`name`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备分组';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'center_biz_group_ref') AS table_exists;
CREATE TABLE `center_biz_group_ref` (
  `id` bigint(20) unsigned NOT NULL,
  `group_id` bigint(20) unsigned DEFAULT NULL COMMENT '分组id',
  `device_id` bigint(20) unsigned DEFAULT NULL COMMENT '设备id',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分组关联设备';
