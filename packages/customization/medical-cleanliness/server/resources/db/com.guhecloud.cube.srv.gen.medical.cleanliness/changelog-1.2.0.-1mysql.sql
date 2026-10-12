-- liquibase formatted sql

-- changeset chx:ddl-1
CREATE TABLE IF NOT EXISTS `machines` (
                          `id` bigint(20) NOT NULL,
                          `code` varchar(100) NOT NULL COMMENT '编码',
                          `arguments` text COMMENT '参数',
                          `file_ids` varchar(220) DEFAULT NULL COMMENT '文件ids',
                          `room_ids` text COMMENT '关联的手术室ids',
                          `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
                          `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
                          `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
                          `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          `tenant` int(10) NOT NULL COMMENT '租户主键',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `machines_code_IDX` (`code`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset chx:ddl-2
CREATE TABLE IF NOT EXISTS  `surgery_item` (
                              `id` bigint(20) NOT NULL,
                              `device_id` bigint(20) NOT NULL COMMENT '所属设备id',
                              `device_code` varchar(255) NOT NULL COMMENT '所属设备编码',
                              `device_name` varchar(300) DEFAULT NULL COMMENT '所属设备名称',
                              `name` varchar(220) DEFAULT NULL COMMENT '物品名称',
                              `size` varchar(220) DEFAULT NULL COMMENT '物品尺寸',
                              `number` varchar(220) DEFAULT NULL COMMENT '物品数量',
                              `manufacturer` varchar(220) DEFAULT NULL COMMENT '物品厂家',
                              `model` varchar(220) DEFAULT NULL COMMENT '物品型号',
                              `machines_id` bigint(20) DEFAULT NULL COMMENT '机组id',
                              `machines_code` varchar(220) DEFAULT NULL COMMENT '机组编号',
                              `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
                              `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
                              `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `tenant` int(10) NOT NULL COMMENT '租户主键',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;