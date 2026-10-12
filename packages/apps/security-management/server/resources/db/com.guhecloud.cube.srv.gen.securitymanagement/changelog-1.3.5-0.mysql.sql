-- liquibase formatted sql

-- changeset qqq:1686103358461-1
ALTER TABLE `cube-srv-gen-securitymanagement`.`security_device`
ADD COLUMN `code` varchar(255) NULL COMMENT '设备编码' AFTER `area_name`;

CREATE TABLE `cold_wholesaler` (
                                   `id` bigint(20) NOT NULL,
                                   `wholesaler_id` bigint(20) DEFAULT NULL COMMENT '批发商',
                                   `wholesaler_name` varchar(255) DEFAULT NULL COMMENT '批发商姓名',
                                   `code` varchar(255) DEFAULT NULL COMMENT '商家编号',
                                   `shift_master` varchar(255) DEFAULT NULL COMMENT '批发商档主',
                                   `shift_master_phone` varchar(255) DEFAULT NULL COMMENT '档主电话',
                                   `shift_master_card` varchar(255) DEFAULT NULL COMMENT '档主身份证',
                                   `other_contacts` varchar(255) DEFAULT NULL COMMENT '其他联系方人',
                                   `deleted` bigint(20) DEFAULT NULL,
                                   `tenant` int(11) DEFAULT NULL,
                                   `create_by` varchar(255) DEFAULT NULL,
                                   `create_time` datetime DEFAULT NULL,
                                   `update_by` varchar(255) DEFAULT NULL,
                                   `update_time` datetime DEFAULT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `gear_position` (
                                 `id` bigint(20) NOT NULL,
                                 `wholesaler` bigint(20) DEFAULT NULL COMMENT '批发商id',
                                 `gear_position_id` bigint(20) DEFAULT NULL COMMENT '档位id',
                                 `gear_position_name` varchar(255) DEFAULT NULL COMMENT '档位名称',
                                 `gear_position_area` varchar(255) DEFAULT NULL COMMENT '区域',
                                 `department` varchar(255) DEFAULT NULL COMMENT '部门',
                                 `gear_position_area_id` varchar(255) DEFAULT NULL COMMENT '区域id',
                                 `department_id` varchar(255) DEFAULT NULL COMMENT '部门id',
                                 `tenant` int(11) DEFAULT NULL,
                                 `deleted` bigint(20) DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                 `update_by` varchar(255) DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 `create_by` varchar(255) DEFAULT NULL,
                                 `billable_time` datetime DEFAULT NULL COMMENT '计费费时间',
                                 `gear_position_code` varchar(255) DEFAULT NULL COMMENT '档位code',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `device_position` (
                                   `id` bigint(20) NOT NULL,
                                   `cold_water_device` bigint(20) DEFAULT NULL COMMENT '设备',
                                   `gear_position_id` bigint(20) DEFAULT NULL COMMENT '档位',
                                   `unit_price` varchar(255) DEFAULT NULL COMMENT '单价',
                                   `emplace_temperature` varchar(50) DEFAULT NULL COMMENT '设定温度',
                                   `unit_traffic` varchar(20) DEFAULT NULL COMMENT '冷媒体单位流量',
                                   `update_time` datetime DEFAULT NULL,
                                   `create_by` varchar(255) DEFAULT NULL,
                                   `create_time` datetime DEFAULT NULL,
                                   `update_by` varchar(50) DEFAULT NULL,
                                   `tenant` varchar(255) DEFAULT NULL,
                                   `gear_position_code` varchar(255) DEFAULT NULL COMMENT '档位比编号',
                                   `gear_position_area` varchar(255) DEFAULT NULL COMMENT '档位区域',
                                   `gear_position_name` varchar(255) DEFAULT NULL COMMENT '档位',
                                   `deleted` bigint(20) DEFAULT NULL,
                                   `gear_position_area_id` varchar(100) DEFAULT NULL COMMENT '区域',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `water_data_day` (
                                  `id` bigint(20) NOT NULL,
                                  `statistical_flag` varchar(255) DEFAULT NULL COMMENT '统计标识',
                                  `shift_master` varchar(255) DEFAULT NULL COMMENT '档主姓名',
                                  `gear_position_code` varchar(255) DEFAULT NULL COMMENT '档位号',
                                  `department_id` varchar(50) DEFAULT NULL COMMENT '部门',
                                  `gear_position_area_id` varchar(100) DEFAULT NULL COMMENT '区域',
                                  `device_code` varchar(255) DEFAULT NULL COMMENT '设备编号',
                                  `unit_traffic` varchar(255) DEFAULT NULL COMMENT '媒体单位流量',
                                  `duration` decimal(10,2) DEFAULT '0.00' COMMENT '时长',
                                  `flow` decimal(10,2) DEFAULT '0.00' COMMENT '流量',
                                  `unit_price` decimal(10,2) DEFAULT NULL COMMENT '单价',
                                  `total` decimal(10,2) DEFAULT '0.00' COMMENT '合计',
                                  `data_time` datetime DEFAULT NULL COMMENT '数据时间',
                                  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                  `deleted` bigint(20) DEFAULT NULL COMMENT '删除',
                                  `tenant` int(11) DEFAULT NULL COMMENT '租户',
                                  `code` varchar(255) DEFAULT NULL COMMENT '档主编号',
                                  `gear_position_area` varchar(255) DEFAULT NULL COMMENT '区域',
                                  `department` varchar(255) DEFAULT NULL COMMENT '部门',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `water_data_month` (
                                    `id` bigint(20) NOT NULL,
                                    `statistical_flag` varchar(255) DEFAULT NULL COMMENT '统计标识',
                                    `statistical_cycle` varchar(255) DEFAULT NULL COMMENT '统计周期',
                                    `shift_master` varchar(255) DEFAULT NULL COMMENT '档主姓名',
                                    `gear_position_code` varchar(255) DEFAULT NULL COMMENT '档位号',
                                    `department_id` varchar(50) CHARACTER SET utf16 DEFAULT NULL COMMENT '部门',
                                    `gear_position_area_id` varchar(100) DEFAULT NULL COMMENT '区域',
                                    `device_code` varchar(255) DEFAULT NULL COMMENT '设备编号',
                                    `unit_traffic` varchar(255) DEFAULT NULL COMMENT '媒体单位流量',
                                    `duration` decimal(10,2) DEFAULT '0.00' COMMENT '时长',
                                    `flow` decimal(10,2) DEFAULT '0.00' COMMENT '流量',
                                    `unit_price` decimal(10,2) DEFAULT '0.00' COMMENT '单价',
                                    `total` decimal(10,2) DEFAULT '0.00' COMMENT '合计',
                                    `data_time` varchar(20) DEFAULT NULL COMMENT '数据时间',
                                    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `deleted` bigint(20) DEFAULT NULL COMMENT '删除',
                                    `tenant` int(11) DEFAULT NULL COMMENT '租户',
                                    `code` varchar(255) DEFAULT NULL COMMENT '档主编号',
                                    `gear_position_area` varchar(255) DEFAULT NULL COMMENT '区域',
                                    `department` varchar(255) DEFAULT NULL COMMENT '部门',
                                    `shift_master_phone` varchar(255) DEFAULT NULL COMMENT '档主电话',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `water_data_year` (
                                   `id` bigint(20) NOT NULL,
                                   `code` varchar(255) DEFAULT NULL COMMENT '档主编号',
                                   `shift_master` varchar(255) DEFAULT NULL COMMENT '档主姓名',
                                   `gear_position_code` varchar(255) DEFAULT NULL COMMENT '档位号',
                                   `department_id` varchar(50) DEFAULT NULL COMMENT '部门',
                                   `gear_position_area_id` varchar(100) DEFAULT NULL COMMENT '区域',
                                   `device_code` varchar(255) DEFAULT NULL COMMENT '设备编号',
                                   `unit_traffic` varchar(255) DEFAULT NULL COMMENT '媒体单位流量',
                                   `one` decimal(10,2) DEFAULT '0.00' COMMENT '一月',
                                   `two` decimal(10,2) DEFAULT '0.00' COMMENT '二月',
                                   `three` decimal(10,2) DEFAULT '0.00' COMMENT '三月',
                                   `four` decimal(10,2) DEFAULT '0.00' COMMENT '四月',
                                   `five` decimal(10,2) DEFAULT '0.00' COMMENT '五月',
                                   `six` decimal(10,2) DEFAULT '0.00' COMMENT '六月',
                                   `seven` decimal(10,2) DEFAULT '0.00' COMMENT '七月',
                                   `eight` decimal(10,2) DEFAULT '0.00' COMMENT '八月',
                                   `nine` decimal(10,2) DEFAULT '0.00' COMMENT '九月',
                                   `ten` decimal(10,2) DEFAULT '0.00' COMMENT '十月',
                                   `eleven` decimal(10,2) DEFAULT '0.00' COMMENT '十一月',
                                   `twelve` decimal(10,2) DEFAULT '0.00' COMMENT '十二月',
                                   `total` decimal(10,2) DEFAULT '0.00' COMMENT '合计',
                                   `data_time` varchar(20) DEFAULT NULL COMMENT '数据时间',
                                   `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `deleted` bigint(20) DEFAULT NULL COMMENT '删除',
                                   `tenant` int(11) DEFAULT NULL COMMENT '租户',
                                   `gear_position_area` varchar(255) DEFAULT NULL COMMENT '区域',
                                   `department` varchar(255) DEFAULT NULL COMMENT '部门',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `cube-biz-ouaa`.`application`(`id`, `code`, `product_line_id`, `application_group_id`, `name`, `icon`, `remark`, `enabled`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701199784952823809, 'COOLINGWATER', NULL, 1564072705521692673, '冷冻水', '/resource/app-icons/净水系统/main.png', '', 1, 0, 'admin', '2023-09-11 19:43:30', 'admin', '2023-09-11 19:58:49');


INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701200783872458754, 1528568123400142850, 1701199784952823809, NULL, 'shgl', '商户管理', '', '', 1, '', 1, 1, NULL, '1701200783872458754-', 0, 'admin', '2023-09-11 19:47:28', 'admin', '2023-09-12 11:10:11');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701433351465177090, 1528568123400142850, 1701199784952823809, NULL, 'bbmx', '报表明细', NULL, '/portal/cooling-water/report-detail', 2, '', 1, 2, 1701200783872458754, '1701200783872458754-1701433351465177090-', 0, 'admin', '2023-09-12 11:11:37', 'admin', '2023-09-12 11:11:37');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701433710250135554, 1528568123400142850, 1701199784952823809, NULL, 'shmx', '商户明细', NULL, '/portal/cooling-water/merchant-details', 3, '', 1, 2, 1701200783872458754, '1701200783872458754-1701433710250135554-', 0, 'admin', '2023-09-12 11:13:02', 'admin', '2023-09-12 11:13:02');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701434080112250881, 1528568123400142850, 1701199784952823809, NULL, 'sbzx', '设备中心', NULL, NULL, 2, '', 1, 1, NULL, '1701434080112250881-', 0, 'admin', '2023-09-12 11:14:30', 'admin', '2023-09-12 11:14:30');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701434273549357057, 1528568123400142850, 1701199784952823809, NULL, 'sbjc', '设备监测', '', '/portal/cooling-water/device-monitor', 1, '', 1, 2, 1701434080112250881, '1701434080112250881-1701434273549357057-', 0, 'admin', '2023-09-12 11:15:17', 'admin', '2023-09-14 19:26:18');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701434402159300609, 1528568123400142850, 1701199784952823809, NULL, 'ywpz', '业务配置', NULL, NULL, 3, '', 1, 1, NULL, '1701434402159300609-', 0, 'admin', '2023-09-12 11:15:47', 'admin', '2023-09-12 11:15:47');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701434841932075009, 1528568123400142850, 1701199784952823809, NULL, 'sbpz', '设备配置', '', '/portal/cooling-water/device-config', 1, '', 1, 2, 1701434402159300609, '1701434402159300609-1701434841932075009-', 0, 'admin', '2023-09-12 11:17:32', 'admin', '2023-09-14 17:47:01');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1701435013399416833, 1528568123400142850, 1701199784952823809, NULL, 'shpz', '商户配置', '', '/portal/cooling-water/merchant-config', 2, '', 1, 2, 1701434402159300609, '1701434402159300609-1701435013399416833-', 0, 'admin', '2023-09-12 11:18:13', 'admin', '2023-09-14 17:47:12');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1704067189639053314, 1528568123400142850, 1701199784952823809, NULL, 'dqsb', '冷冻水设备', '', '/portal/genericEquipmentMonitor/applicationEquipment', 0, '', 1, 2, 1701434402159300609, '1701434402159300609-1704067189639053314-', 0, 'admin', '2023-09-19 17:37:33', 'admin', '2023-09-19 17:37:33');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1704389298881921026, 1537258466647814145, 1701199784952823809, NULL, 'COOLINGWATER_DEVICE', '设备监测', ' /resource/app-icons/冷冻水/main.png', '/baseMonitorDevice?applicationCode=COOLINGWATER', 1, '', 1, 1, NULL, '1704389298881921026-', 0, 'admin', '2023-09-20 14:57:30', 'admin', '2023-09-20 14:59:57');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1704389694685806593, 1537258466647814145, 1701199784952823809, NULL, 'COOLINGWATER_ALARM', '设备报警', '/resource/app-icons/报警中心/报警记录.png', '/baseAlarmRecords?applicationCode=COOLINGWATER', 2, '', 1, 1, NULL, '1704389694685806593-', 0, 'admin', '2023-09-20 14:59:04', 'admin', '2023-09-20 14:59:28');
INSERT INTO `cube-biz-ouaa`.`menu`(`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`, `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1704390207305252866, 1537258466647814145, 1701199784952823809, NULL, 'COOLINGWATER_ANALYSE', '统计分析', ' /resource/app-icons/冷冻水/统计分析.png', '/agricultureChilledWaterAnalyse', 3, '', 1, 1, NULL, '1704390207305252866-', 0, 'admin', '2023-09-20 15:01:06', 'admin', '2023-09-20 15:01:06');


