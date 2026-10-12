-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'webhook_config') AS column_exists;
CREATE TABLE `webhook_config` (
                                  `id` bigint(20) NOT NULL COMMENT '主键ID',
                                  `from_flow_id` varchar(64) NOT NULL COMMENT '流程表单id',
                                  `trigger_node` varchar(255) NOT NULL COMMENT '回调节点',
                                  `url` varchar(255) NOT NULL COMMENT '回调地址',
                                  `token` varchar(128) DEFAULT NULL COMMENT '密钥',
                                  `tenant` int(10) NOT NULL COMMENT '租户主键',
                                  `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                  `create_time` datetime NOT NULL COMMENT '创建时间',
                                  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                  `update_time` datetime NOT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务回调配置表';