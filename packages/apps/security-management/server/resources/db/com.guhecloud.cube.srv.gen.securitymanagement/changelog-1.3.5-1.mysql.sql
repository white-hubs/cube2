-- liquibase formatted sql

-- changeset qqq:1686103358461-1
ALTER TABLE `cube-srv-gen-securitymanagement`.`water_data_year`
ADD COLUMN `flow` decimal(10, 2) NULL COMMENT '流量' AFTER `department`,
ADD COLUMN `duration` decimal(10, 2) NULL COMMENT '时长' AFTER `flow`;

CREATE TABLE `operate_log` (
                               `id` bigint(20) NOT NULL,
                               `name` varchar(255) DEFAULT NULL COMMENT '名称',
                               `department` varchar(255) DEFAULT NULL COMMENT '部门',
                               `log_time` datetime DEFAULT NULL COMMENT '时间',
                               `remark` varchar(255) DEFAULT NULL COMMENT '内容',
                               `tenant` int(11) DEFAULT NULL,
                               `deleted` bigint(20) DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `update_by` varchar(255) DEFAULT NULL,
                               `create_time` datetime DEFAULT NULL,
                               `create_by` varchar(255) DEFAULT NULL,
                               `device_id` bigint(20) DEFAULT NULL COMMENT '设备id',
                               `log_type` varchar(20) DEFAULT NULL COMMENT '日志类型',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `cold_water_attribute` (
                                        `id` bigint(20) NOT NULL,
                                        `attribute_code` varchar(255) DEFAULT NULL COMMENT '属性',
                                        `update_time` datetime DEFAULT NULL,
                                        `create_by` varchar(255) DEFAULT NULL,
                                        `create_time` datetime DEFAULT NULL,
                                        `update_by` varchar(50) DEFAULT NULL,
                                        `tenant` varchar(255) DEFAULT NULL,
                                        `deleted` bigint(20) DEFAULT NULL,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `clod_water_attribute_map` (
                                            `id` bigint(20) DEFAULT NULL,
                                            `attribute_code` varchar(100) DEFAULT NULL COMMENT '属性code',
                                            `translate_name` varchar(100) DEFAULT NULL COMMENT '属性翻译',
                                            `tenant` varchar(255) DEFAULT NULL,
                                            `create_by` varchar(50) DEFAULT NULL,
                                            `create_time` datetime DEFAULT NULL,
                                            `update_by` varchar(50) DEFAULT NULL,
                                            `update_time` datetime DEFAULT NULL,
                                            `deleted` bigint(20) DEFAULT NULL,
                                            `value_type` varchar(2) DEFAULT NULL COMMENT '值类型0开关，1数值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `cold_water_electricity` (
                                          `id` bigint(20) NOT NULL,
                                          `quantity_electricity` decimal(10,2) DEFAULT NULL COMMENT '电量',
                                          `device_id` bigint(20) DEFAULT NULL COMMENT '设备id',
                                          `last_reade` decimal(10,2) DEFAULT NULL COMMENT '上次读数',
                                          `collection_time` varchar(50) DEFAULT NULL COMMENT '时间',
                                          `deleted` bigint(20) DEFAULT NULL,
                                          `tenant` int(11) DEFAULT NULL,
                                          `create_by` varchar(255) DEFAULT NULL,
                                          `create_time` datetime DEFAULT NULL,
                                          `update_by` varchar(255) DEFAULT NULL,
                                          `update_time` datetime DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `cube-srv-gen-securitymanagement`.`clod_water_attribute_map`(`id`, `attribute_code`, `translate_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`, `value_type`) VALUES (122423422223499, 'control_box_temperture', '面板目标温度', '58197260', '123245', '2023-10-13 13:49:00', '432334', '2023-10-13 13:49:05', 0, '1');
INSERT INTO `cube-srv-gen-securitymanagement`.`clod_water_attribute_map`(`id`, `attribute_code`, `translate_name`, `tenant`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`, `value_type`) VALUES (1245654656, 'control_box', '1-控制终端开机,0-控制终端关机', '58197260', '4324', '2023-10-13 13:54:32', '45325', '2023-10-13 13:54:40', 0, '0');

INSERT INTO `cube-srv-gen-securitymanagement`.`cold_water_attribute`(`id`, `attribute_code`, `update_time`, `create_by`, `create_time`, `update_by`, `tenant`, `deleted`) VALUES (13453465677, 'ti_bubbles_temperature', '2023-10-13 11:20:48', '546543', '2023-10-13 11:20:53', '65464', '58197260', 0);
INSERT INTO `cube-srv-gen-securitymanagement`.`cold_water_attribute`(`id`, `attribute_code`, `update_time`, `create_by`, `create_time`, `update_by`, `tenant`, `deleted`) VALUES (132312432342, 'water_temperature', '2023-10-13 11:20:07', '324132', '2023-10-13 11:20:11', '34324', '58197260', 0);



