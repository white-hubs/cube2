-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `vehicle_flow_monitoring` (
                                           `id` bigint(20) NOT NULL,
                                           `record_id` varchar(255) DEFAULT NULL COMMENT '记录',
                                           `data_time` datetime DEFAULT NULL COMMENT '时间',
                                           `direction` varchar(255) DEFAULT NULL COMMENT '方向',
                                           `lane` varchar(255) DEFAULT NULL COMMENT '车道',
                                           `traffic_volume` int(10) DEFAULT NULL COMMENT '车流量',
                                           `average_speed` int(10) DEFAULT NULL COMMENT '平均车速',
                                           `time_occupancy_rate` int(10) DEFAULT NULL COMMENT '时间占有率',
                                           `average_headway` int(10) DEFAULT NULL COMMENT '平均车头时距',
                                           `average_time_interval` int(10) DEFAULT NULL COMMENT '平均车间时距',
                                           `device_id` varchar(50) DEFAULT NULL COMMENT '三方设备id',
                                           `deleted` bigint(20) DEFAULT NULL,
                                           `tenant` int(11) DEFAULT NULL,
                                           `create_by` varchar(255) DEFAULT NULL,
                                           `create_time` datetime DEFAULT NULL,
                                           `update_by` varchar(255) DEFAULT NULL,
                                           `update_time` datetime DEFAULT NULL,
                                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `traffic_light` (
                                 `id` bigint(20) NOT NULL,
                                 `record_id` varchar(50) DEFAULT NULL COMMENT '记录id',
                                 `device_id` varchar(50) DEFAULT NULL COMMENT '三方id',
                                 `red` varchar(255) DEFAULT NULL COMMENT '红灯',
                                 `green` varchar(255) DEFAULT NULL COMMENT '绿灯',
                                 `yellow` varchar(255) DEFAULT NULL COMMENT '黄灯',
                                 `data_time` datetime DEFAULT NULL COMMENT '时间',
                                 `deleted` bigint(20) DEFAULT NULL,
                                 `tenant` int(11) DEFAULT NULL,
                                 `create_by` varchar(255) DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 `update_by` varchar(255) DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `emergency_phone_number` (
                                          `id` bigint(20) NOT NULL COMMENT 'id',
                                          `record_id` varchar(50) DEFAULT NULL COMMENT '记录id',
                                          `data_time` datetime DEFAULT NULL COMMENT '使用时间',
                                          `device_id` varchar(50) DEFAULT NULL COMMENT '第三方设备id',
                                          `file_url` varchar(100) DEFAULT NULL COMMENT '文件地址',
                                          `tenant` int(10) unsigned NOT NULL COMMENT '租户编号',
                                          `deleted` bigint(20) NOT NULL COMMENT '是否删除',
                                          `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
                                          `create_time` datetime NOT NULL COMMENT '创建时间',
                                          `update_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '更新用户',
                                          `update_time` datetime NOT NULL COMMENT '更新时间',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
