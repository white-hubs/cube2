-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'task_feedback_log') AS column_exists;
CREATE TABLE `task_feedback_log` (
                                     `id` bigint(20) NOT NULL COMMENT '主键ID',
                                     `process_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
                                     `task_node_key` varchar(50) DEFAULT NULL COMMENT '任务节点编码',
                                     `feedback_info` text COMMENT '反馈信息',
                                     `task_id` varchar(128) DEFAULT NULL COMMENT '密钥',
                                     `tenant` int(10) NOT NULL COMMENT '租户主键',
                                     `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                     `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                     `create_time` datetime NOT NULL COMMENT '创建时间',
                                     `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                     `update_time` datetime NOT NULL COMMENT '更新时间',
                                     `feedback_user` bigint(20) DEFAULT NULL,
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务反馈记录';
