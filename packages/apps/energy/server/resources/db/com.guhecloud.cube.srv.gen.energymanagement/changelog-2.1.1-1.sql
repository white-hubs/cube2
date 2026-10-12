-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE area_project ADD parent_id BIGINT(20) NULL COMMENT '父级id';
ALTER TABLE area_project CHANGE parent_id parent_id BIGINT(20) NULL COMMENT '父级id' AFTER id;
ALTER TABLE area_project ADD `level` varchar(255) NULL COMMENT '层级';
ALTER TABLE area_project CHANGE `level` `level` varchar(255) NULL COMMENT '层级' AFTER `parent_id`;
ALTER TABLE area_project ADD `sequence` varchar(128) NULL COMMENT '序列id';
ALTER TABLE area_project CHANGE `sequence` `sequence` varchar(128) NULL COMMENT '序列id' AFTER `level`;

ALTER TABLE energy_project ADD code varchar(64) NULL COMMENT '分项编码';
ALTER TABLE energy_project CHANGE code code varchar(64) NULL COMMENT '分项编码' FIRST;

CREATE TABLE `energy_collect_config`  (
  `id` bigint(20) NOT NULL,
  `category_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备分类code',
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备分类名称',
  `collect_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采集方式：auto 自动/hand 手动录入',
  `collect_on` tinyint(1) NULL DEFAULT NULL COMMENT '1 开启 0 关闭 是否开启采集',
  `cust_code` varchar(64) NULL COMMENT '自定义编码',
  `tenant` int(11) NULL DEFAULT 100 COMMENT '租户',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE `cube-srv-gen-energy-management`.`energy_project`
MODIFY COLUMN `project_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分项名称' AFTER `id`,
ADD COLUMN `parent_id` bigint NULL COMMENT '父级ID' AFTER `instrument_type`,
ADD COLUMN `level` int NULL COMMENT '层级' AFTER `parent_id`,
ADD COLUMN `sequence` varchar(128) NULL COMMENT '路径序列' AFTER `level`;


ALTER TABLE `cube-srv-gen-energy-management`.`meter_device`
ADD COLUMN `init_value` decimal(65, 2) NULL COMMENT '初始读数' AFTER `parent_id`;


CREATE TABLE `energy_daily_usage`  (
  `id` bigint(20) NOT NULL,
  `stat_date` date NULL DEFAULT NULL COMMENT '日期',
  `instrument_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仪表类型编码',
  `instrument_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仪表类型名称',
  `d_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '用量',
  `f_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '峰时用量',
  `p_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '平时用量',
  `g_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '谷时用量',
  `j_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '尖时用量',
  `dc_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '碳排',
  `zh_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '综合能耗',
  `tenant` int(11) NULL DEFAULT 100 COMMENT '租户',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `itype_date_tenant_index`(`stat_date`, `instrument_type`, `tenant`) USING BTREE COMMENT '日期和类型组成唯一键'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `energy_daily_usage_project`  (
  `id` bigint(20) NOT NULL,
  `stat_date` date NULL DEFAULT NULL COMMENT '日期',
  `instrument_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仪表类型编码',
  `instrument_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仪表类型名称',
  `project_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ITEM(分项) AREA(区域) ROUTE（支路）',
  `project_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目ID',
  `project_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目编号',
  `project_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目名称给',
  `d_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '用量',
  `dc_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '碳排量',
  `zh_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '综合能耗',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `itype_date_tenant_index`(`stat_date`, `instrument_type`, `project_category`, `project_id`, `tenant`) USING BTREE COMMENT '日期和类型组成唯一键'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `energy_collect_period`  (
  `id` bigint(11) NOT NULL,
  `instrument_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仪表类型',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时段标识：FV\\PV\\GV\\JV',
  `start_time` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `sort` int(1) NULL DEFAULT NULL COMMENT '排序号',
  `tenant` int(11) NULL DEFAULT 100 COMMENT '租户',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

CREATE TABLE `meter_device_change_log`  (
  `id` bigint(20) NOT NULL,
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备ID',
  `init_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '设备当前读数',
  `changed_device_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '被换设备当前读数',
  `opt_user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '换表人姓名',
  `opt_time` datetime(0) NULL DEFAULT NULL COMMENT '换表时间',
  `tenant` int(11) NULL DEFAULT 100 COMMENT '租户',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


