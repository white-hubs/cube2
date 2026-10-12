-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `universal_panel` (
                                   `id` bigint(20) NOT NULL,
                                   `table_one_name` varchar(255) DEFAULT NULL COMMENT '图表一名称',
                                   `table_one` varchar(255) DEFAULT NULL COMMENT '图表一',
                                   `table_two_name` varchar(255) DEFAULT NULL COMMENT '图表二名称',
                                   `table_two` varchar(255) DEFAULT NULL COMMENT '图表二',
                                   `table_three_name` varchar(255) DEFAULT NULL COMMENT '图表三名称',
                                   `table_three` varchar(255) DEFAULT NULL COMMENT '图表三',
                                   `business_id` bigint(20) DEFAULT NULL COMMENT '业务建',
                                   `tenant` int(10) unsigned NOT NULL COMMENT '租户编号',
                                   `deleted` bigint(20) NOT NULL COMMENT '是否删除',
                                   `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `update_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '更新用户',
                                   `update_time` datetime NOT NULL COMMENT '更新时间',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
