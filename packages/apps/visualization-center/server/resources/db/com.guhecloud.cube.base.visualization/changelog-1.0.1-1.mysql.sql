-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `model_configuration` (
                                       `id` bigint(20) NOT NULL,
                                       `model_configuration` mediumtext COMMENT '配置',
                                       `tenant` int(10) unsigned NOT NULL COMMENT '租户编号',
                                       `deleted` bigint(20) NOT NULL COMMENT '是否删除',
                                       `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `update_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '更新用户',
                                       `update_time` datetime NOT NULL COMMENT '更新时间',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
