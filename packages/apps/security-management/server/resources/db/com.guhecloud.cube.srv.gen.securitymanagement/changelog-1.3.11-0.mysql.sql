-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- 净水使用量小时表
CREATE TABLE `treated_water_usage_hour`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `date_hour` datetime(0) NULL DEFAULT NULL COMMENT '2023-10-30 10:00:00 每小时的开始时间；也可以是2023-10-30 00:00:00 表示每天的用量；以此类推',
  `water_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '净化水的类型',
  `water_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `water_usage` decimal(8, 2) NULL DEFAULT NULL COMMENT '使用量',
  `usage_unit` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用量单位',
  `device_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备ID',
  `device_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `area_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域ID',
  `area_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  `dept_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- 增加字段
ALTER TABLE `cube-srv-gen-securitymanagement`.`group_control_log` ADD COLUMN `opt_time` datetime NULL COMMENT '操作时间';

CREATE TABLE `lift_record` (
                               `id` bigint(20) NOT NULL,
                               `device_id` varchar(50) DEFAULT NULL COMMENT '设备id',
                               `device_name` varchar(50) DEFAULT NULL COMMENT '设备名称',
                               `transport_way` varchar(50) DEFAULT NULL COMMENT '运输方式',
                               `device_type` varchar(50) DEFAULT NULL COMMENT '设备名称',
                               `area_name` varchar(100) DEFAULT NULL COMMENT '区域',
                               `run_time` datetime DEFAULT NULL COMMENT '运行时间',
                               `floor` varchar(100) DEFAULT NULL COMMENT '楼层',
                               `area_id` varchar(0) DEFAULT NULL COMMENT '区域id',
                               `tenant` int(11) DEFAULT NULL,
                               `create_by` varchar(255) DEFAULT NULL,
                               `create_time` datetime DEFAULT NULL,
                               `update_by` varchar(255) DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `deleted` bigint(20) DEFAULT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `boiler_data` (
                               `id` bigint(20) NOT NULL,
                               `device_id` bigint(20) DEFAULT NULL COMMENT '设备id',
                               `water_value` decimal(10,2) DEFAULT '0.00' COMMENT '水量',
                               `water_last_value` decimal(10,2) DEFAULT '0.00' COMMENT '上次水读数',
                               `temperature` decimal(10,2) DEFAULT '0.00' COMMENT '温度',
                               `electricity_value` decimal(10,2) DEFAULT '0.00' COMMENT '电量',
                               `electricity_last_value` decimal(10,2) DEFAULT '0.00' COMMENT '上次电读数',
                               `deleted` bigint(20) DEFAULT NULL,
                               `tenant` int(11) DEFAULT NULL,
                               `create_by` varchar(255) DEFAULT NULL,
                               `create_time` datetime DEFAULT NULL,
                               `update_by` varchar(255) DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `collect_time` datetime DEFAULT NULL COMMENT '采集时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `medical_gas_data` (
                                    `id` bigint(20) NOT NULL,
                                    `name` varchar(100) DEFAULT NULL COMMENT '气体名称',
                                    `gas_type` varchar(100) DEFAULT NULL COMMENT '气体类型',
                                    `collection_time` datetime DEFAULT NULL COMMENT '时间',
                                    `data_value` decimal(10,2) DEFAULT NULL COMMENT '数据采集数据',
                                    `third_device_id` varchar(50) DEFAULT NULL COMMENT '第三方id',
                                    `tenant` varchar(32) DEFAULT NULL COMMENT '租户号',
                                    `deleted` varchar(32) DEFAULT NULL COMMENT '删除标识',
                                    `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
                                    `create_time` date DEFAULT NULL COMMENT '创建时间',
                                    `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
                                    `update_time` date DEFAULT NULL COMMENT '更新时间',
                                    `device_id` bigint(20) DEFAULT NULL COMMENT '设备id',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `play_record` (
                               `id` bigint(20) NOT NULL,
                               `record_id` varchar(50) DEFAULT NULL COMMENT '记录id',
                               `client_id` varchar(64) DEFAULT NULL COMMENT '终端ID',
                               `client_name` varchar(255) DEFAULT NULL COMMENT '终端设备(设备名称)',
                               `item_name` varchar(255) DEFAULT NULL COMMENT '内容（节目名称）',
                               `start_time` datetime DEFAULT NULL COMMENT '开始时间',
                               `stop_time` datetime DEFAULT NULL COMMENT '结束时间',
                               `total_time` int(20) DEFAULT NULL COMMENT '总时间',
                               `total_circle` int(20) DEFAULT NULL COMMENT '次数',
                               `area_id` varchar(64) DEFAULT NULL COMMENT '区域ID',
                               `area_name` varchar(32) DEFAULT NULL COMMENT '区域名称',
                               `tenant` int(20) DEFAULT NULL,
                               `create_by` varchar(32) DEFAULT NULL,
                               `create_time` datetime DEFAULT NULL,
                               `update_by` varchar(32) DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `deleted` tinyint(4) DEFAULT NULL,
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
