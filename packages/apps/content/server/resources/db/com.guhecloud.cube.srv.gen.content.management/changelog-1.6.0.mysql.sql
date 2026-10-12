-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE IF NOT EXISTS `knowledge_type` (
  `id` bigint(20) NOT NULL,
  `name` varchar(200) DEFAULT NULL COMMENT '类型名称',
  `description` text DEFAULT NULL COMMENT '描述',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `level` int(11) DEFAULT '1' COMMENT '层级',
  `sequence` text COMMENT '序列号',
  `tenant` int(10) DEFAULT NULL COMMENT '租户',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='知识文档类型';


CREATE TABLE IF NOT EXISTS `knowledge_base` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '知识文档名称',
  `type_id` varchar(100) DEFAULT NULL COMMENT '知识文档类型id',
  `dept_id` bigint(20) unsigned DEFAULT NULL COMMENT '所属部门主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '所属产品id',
  `ai_library_id` varchar(100) DEFAULT NULL COMMENT '知识文档类型id',
  `ai_document_id` varchar(100) DEFAULT NULL COMMENT '知识文档类型id',
  `tenant` int(10) DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='知识文档表';
