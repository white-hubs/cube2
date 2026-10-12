-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_reform') AS column_exists;
CREATE TABLE `project_reform` (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `project_schedule_id` varchar(100) NOT NULL COMMENT '整改工程',
                                  `process_ins_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
                                  `start_user` bigint(20) DEFAULT NULL COMMENT '申请人',
                                  `start_dep` bigint(20) DEFAULT NULL COMMENT '申请部门',
                                  `pass_time` datetime DEFAULT NULL COMMENT '审批通过时间',
                                  `approve_result` varchar(32) DEFAULT NULL COMMENT '审批结果',
                                  `reform_num` varchar(32) DEFAULT NULL COMMENT '工单编号',
                                  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
                                  `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
                                  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `tenant` int(10) NOT NULL COMMENT '租户主键',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工程整改流程表';