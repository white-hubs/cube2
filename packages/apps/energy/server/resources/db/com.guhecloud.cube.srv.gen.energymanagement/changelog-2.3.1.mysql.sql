-- liquibase formatted sql

-- changeset chx:ddl-1
-- ----------------------------
-- Table structure for energy_charge_rules
-- ----------------------------
CREATE table IF NOT EXISTS `energy_charge_rules` (
    `id` bigint(20) NOT NULL,
    `rule_name` varchar(100) DEFAULT NULL COMMENT '规则名称',
    `instrument_type` varchar(100) DEFAULT NULL COMMENT '用量类型code',
    `instrument_type_name` varchar(100) DEFAULT NULL COMMENT '用量类型名称',
    `effective_time` datetime DEFAULT NULL COMMENT '生效时间',
    `complex_fee_rate` tinyint(1) DEFAULT NULL COMMENT '是否复费率(true为付费率，其他为单费率)',
    `stairs_energy_type` varchar(100) DEFAULT NULL COMMENT '阶梯用量类型',
    `stairs_energy_contents` text DEFAULT NULL COMMENT '阶梯用量内容',
    `tenant` int(11) DEFAULT NULL,
    `deleted` bigint(20) NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- changeset chx:ddl-2
-- ----------------------------
-- Table structure for energy_charge_report
-- ----------------------------
CREATE TABLE `energy_charge_report` (
    `id` bigint(20) NOT NULL,
    `stat_date` datetime DEFAULT NULL COMMENT '统计日期',
    `stat_date_str` varchar(100) DEFAULT NULL COMMENT '统计日期字符串',
    `instrument_type` varchar(100) DEFAULT NULL COMMENT '用量类型code',
    `instrument_type_name` varchar(100) DEFAULT NULL COMMENT '用量类型名称',
    `area_id` bigint(20) DEFAULT NULL COMMENT '分区id',
    `area_name` varchar(200) DEFAULT NULL COMMENT '分区名称',
    `sequence` varchar(300) DEFAULT NULL COMMENT '区域序列号',
    `total_cost` decimal(15,2) DEFAULT '0.00' COMMENT '总费用',
    `dv_cost` decimal(15,2) DEFAULT '0.00' COMMENT '费用',
    `fv_cost` decimal(15,2) DEFAULT '0.00' COMMENT '峰费用',
    `pv_cost` decimal(15,2) DEFAULT '0.00' COMMENT '平费用',
    `gv_cost` decimal(15,2) DEFAULT '0.00' COMMENT '谷费用',
    `jv_cost` decimal(15,2) DEFAULT '0.00' COMMENT '尖费用',
    `tenant` int(11) DEFAULT NULL,
    `deleted` bigint(20) NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `energy_charge_report_stat_date_IDX` (`stat_date`) USING BTREE,
    KEY `energy_charge_report_instrument_type_IDX` (`instrument_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC
