-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'user_dictionary') AS column_exists;
CREATE TABLE `user_dictionary` (
                                   `id` bigint(20) NOT NULL COMMENT '主键',
                                   `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                   `name` varchar(100) NOT NULL COMMENT '名称',
                                   `code` varchar(100) NOT NULL COMMENT '编号',
                                   `variable` longtext COMMENT '变量',
                                   `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
                                   `tenant` int(11) NOT NULL COMMENT '租户id',
                                   `version` varchar(20) NOT NULL COMMENT '版本号',
                                   `item_code` varchar(100) NOT NULL COMMENT '字典项编号',
                                   `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                   `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '修改人',
                                   `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                                   `status` varchar(255) NOT NULL COMMENT '状态：启用：enable；禁用：disable',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户字典';