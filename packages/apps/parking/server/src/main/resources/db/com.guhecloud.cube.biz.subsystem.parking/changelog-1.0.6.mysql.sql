-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `vehicle_info` (
                                `id` bigint(20) NOT NULL,
                                `vehicle_no` varchar(50) DEFAULT NULL COMMENT '车牌号',
                                `vehicle_type_code` varchar(100) DEFAULT NULL COMMENT '车辆类型编号',
                                `vehicle_type_name` varchar(50) DEFAULT NULL COMMENT '车辆类型名称',
                                `customer_name` varchar(100) DEFAULT NULL COMMENT '车主姓名',
                                `customer_mobile` varchar(20) DEFAULT NULL COMMENT '车主电话',
                                `tenant` int(11) DEFAULT NULL COMMENT '租户编号',
                                `deleted` bigint(20) DEFAULT NULL COMMENT '是否删除',
                                `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='车辆信息';