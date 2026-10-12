-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE meter_device ADD product_brand_name varchar(100) NULL COMMENT '设备品牌(产品信息->品牌名称)';
ALTER TABLE meter_device CHANGE product_brand_name product_brand_name varchar(100) NULL COMMENT '设备品牌(产品信息->品牌名称)' AFTER device_type;
ALTER TABLE meter_device ADD product_name varchar(100) NULL COMMENT '产品名称';
ALTER TABLE meter_device CHANGE product_name product_name varchar(100) NULL COMMENT '产品名称' AFTER product_brand_name;

ALTER TABLE meter_device ADD product_id BIGINT(20) NULL COMMENT '产品id';
ALTER TABLE meter_device ADD product_category_path_code varchar(200) NULL COMMENT '产品分类code全路径';
ALTER TABLE meter_device ADD product_category_name varchar(100) NULL COMMENT '产品分类名称';

ALTER TABLE meter_device CHANGE product_category_path_code product_category_path_code varchar(200) NULL COMMENT '产品分类code全路径' AFTER product_brand_name;
ALTER TABLE meter_device CHANGE product_category_name product_category_name varchar(100) NULL COMMENT '产品分类名称' AFTER product_category_path_code;
ALTER TABLE meter_device CHANGE product_id product_id BIGINT(20) NULL COMMENT '产品id' AFTER product_category_name;

ALTER TABLE meter_device ADD last_area_id BIGINT(20) NULL COMMENT '最后一级区域id';
ALTER TABLE meter_device CHANGE last_area_id last_area_id BIGINT(20) NULL COMMENT '最后一级区域id' AFTER device_area;
ALTER TABLE meter_device ADD last_area_name varchar(100) NULL COMMENT '最后一级区域名称';
ALTER TABLE meter_device CHANGE last_area_name last_area_name varchar(100) NULL COMMENT '最后一级区域名称' AFTER last_area_id;

-- 燃料类型表
CREATE TABLE `fuel_discharge_standard` (
                                           `id` bigint(20) NOT NULL,
                                           `code` varchar(100) NOT NULL COMMENT '编码',
                                           `fuel_name` varchar(50) DEFAULT NULL COMMENT '燃料名称',
                                           `unit` varchar(20) DEFAULT NULL COMMENT '计量单位',
                                           `carbon_factor` decimal(10,5) DEFAULT NULL COMMENT '碳排因子',
                                           `calculate_proportion` decimal(10,5) DEFAULT NULL COMMENT '计算系数',
                                           `tenant` int(11) DEFAULT '0' COMMENT '租户',
                                           `create_by` varchar(32) DEFAULT NULL,
                                           `create_time` datetime DEFAULT NULL,
                                           `update_by` varchar(32) DEFAULT NULL,
                                           `update_time` datetime DEFAULT NULL,
                                           `deleted` bigint(20) DEFAULT '0',
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `fuel_discharge_standard_code_IDX` (`code`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE energy_collect_config ADD fuel_code varchar(100) NULL COMMENT '燃料类型code';
ALTER TABLE energy_collect_config CHANGE full_code fuel_code varchar(100) NULL COMMENT '燃料类型code' AFTER cust_code;

CREATE TABLE `year_energy` (
                               `id` bigint(20) NOT NULL,
                               `year` varchar(8) NOT NULL COMMENT '年份',
                               `date_year` datetime DEFAULT NULL COMMENT '日期类型的年份',
                               `instrument_type` varchar(50) DEFAULT NULL COMMENT '用能类型',
                               `instrument_name` varchar(50) DEFAULT NULL COMMENT '用能类型名称',
                               `energy_value` decimal(10,2) DEFAULT NULL COMMENT '用量',
                               `dc_value` decimal(10,2) DEFAULT NULL COMMENT '碳排',
                               `zh_value` decimal(10,2) DEFAULT NULL COMMENT '综合能耗',
                               `source_type` varchar(50) DEFAULT NULL COMMENT '数据来源(自动auto，人工录入hand)',
                               `fuel_name` varchar(50) DEFAULT NULL COMMENT '燃料名称',
                               `unit` varchar(20) DEFAULT NULL COMMENT '计量单位',
                               `carbon_factor` decimal(10,5) DEFAULT NULL COMMENT '碳排因子',
                               `calculate_proportion` decimal(10,5) DEFAULT NULL COMMENT '计算系数',
                               `tenant` int(11) DEFAULT '0' COMMENT '租户',
                               `create_by` varchar(32) DEFAULT NULL,
                               `create_time` datetime DEFAULT NULL,
                               `update_by` varchar(32) DEFAULT NULL,
                               `update_time` datetime DEFAULT NULL,
                               `deleted` bigint(20) DEFAULT '0',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `fuel_discharge_standard_code_IDX` (`year`,`instrument_type`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年用能表(手动配置记录)';

CREATE TABLE `energy_daily_summary` (
                                        `id` bigint(20) NOT NULL,
                                        `stat_date` date DEFAULT NULL COMMENT '日期',
                                        `date_str` varchar(12) DEFAULT NULL COMMENT '字符串的日期',
                                        `dd_value` decimal(10,2) DEFAULT NULL COMMENT '用量',
                                        `dc_value` decimal(10,2) DEFAULT NULL COMMENT '碳排',
                                        `zh_value` decimal(10,2) DEFAULT NULL COMMENT '综合能耗',
                                        `f_value` decimal(10,2) DEFAULT NULL COMMENT '峰时用量',
                                        `p_value` decimal(10,2) DEFAULT NULL COMMENT '平时用量',
                                        `g_value` decimal(10,2) DEFAULT NULL COMMENT '谷时用量',
                                        `j_value` decimal(10,2) DEFAULT NULL COMMENT '尖时用量',
                                        `tenant` int(11) DEFAULT '0' COMMENT '租户',
                                        `create_by` varchar(100) DEFAULT NULL,
                                        `create_time` datetime DEFAULT NULL,
                                        `update_by` varchar(100) DEFAULT NULL,
                                        `update_time` datetime DEFAULT NULL,
                                        `deleted` bigint(20) DEFAULT NULL,
                                        PRIMARY KEY (`id`) USING BTREE,
                                        UNIQUE KEY `itype_date_tenant_index` (`stat_date`,`tenant`) USING BTREE COMMENT '日期唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='每日汇总用量统计，每天一条数据';