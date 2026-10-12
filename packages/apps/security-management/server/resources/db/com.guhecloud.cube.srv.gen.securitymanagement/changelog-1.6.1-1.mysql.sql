-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `environmental_monitoring` (
                                            `id` bigint(20) NOT NULL,
                                            `co` int(5) DEFAULT NULL COMMENT 'co浓度',
                                            `visibility` int(10) DEFAULT NULL COMMENT '能见度',
                                            `brightness` int(10) DEFAULT NULL COMMENT '亮度',
                                            `wind_speed` int(10) DEFAULT NULL COMMENT '风速',
                                            `data_time` datetime DEFAULT NULL COMMENT '数据时间',
                                            `paragraph` varchar(255) DEFAULT NULL COMMENT '段',
                                            `deleted` bigint(20) DEFAULT NULL,
                                            `tenant` int(11) DEFAULT NULL,
                                            `create_by` varchar(255) DEFAULT NULL,
                                            `create_time` datetime DEFAULT NULL,
                                            `update_by` varchar(255) DEFAULT NULL,
                                            `update_time` datetime DEFAULT NULL,
                                            `co_time` int(2) DEFAULT NULL COMMENT '持续时间',
                                            `visibility_time` int(2) DEFAULT NULL COMMENT '持续时间',
                                            `brightness_time` int(2) DEFAULT NULL COMMENT '持续时间',
                                            `wind_speed_time` int(2) DEFAULT NULL COMMENT '持续时间',
                                            `record_id` varchar(100) DEFAULT NULL COMMENT '记录id',
                                            `device_id` varchar(50) DEFAULT NULL COMMENT '三方设备id',
                                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;