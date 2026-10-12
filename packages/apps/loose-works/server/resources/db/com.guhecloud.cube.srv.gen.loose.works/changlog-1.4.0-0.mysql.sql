-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule_extend') AS column_exists;
CREATE TABLE `project_schedule_extend` (
                                           `id` bigint(20) NOT NULL COMMENT 'id',
                                           `party_b_mobile` varchar(100) DEFAULT NULL COMMENT '施工方联系方式',
                                           `stall_code` varchar(100) DEFAULT NULL COMMENT '档位号',
                                           `stall_liaison` varchar(100) DEFAULT NULL COMMENT '档位联系人',
                                           `stall_mobile` varchar(100) DEFAULT NULL COMMENT '档位联系方式',
                                           `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
                                           `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
                                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
                                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           `tenant` int(10) NOT NULL COMMENT '租户主键',
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工程监管扩展表';