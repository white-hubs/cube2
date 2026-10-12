-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'terminal_config') AS column_exists;
CREATE TABLE `terminal_config` (
                                   `id` bigint(20) unsigned NOT NULL COMMENT '主键',
                                   `code` varchar(100) NOT NULL COMMENT '访问端编号',
                                   `default_role` bigint(100) DEFAULT NULL COMMENT '默认角色',
                                   `alias` varchar(200) DEFAULT NULL COMMENT '访问端别名',
                                   `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
                                   `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `tenant` bigint(20) NOT NULL,
                                   PRIMARY KEY (`id`) USING BTREE,
                                   KEY `idx_terminal_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='访问端信息表';

