-- liquibase formatted sql


-- changeset ht:ddl-1
-- ----------------------------
-- Table structure for detection_basic
-- ----------------------------
CREATE table IF NOT EXISTS `detection_basic` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '检测依据名称',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测依据';

-- changeset ht:ddl-2
-- ----------------------------
-- Table structure for detection_company
-- ----------------------------
CREATE table IF NOT EXISTS `detection_company` (
  `id` bigint(20) NOT NULL,
  `detection_company` varchar(50) DEFAULT NULL COMMENT '检测公司',
  `detection_address` varchar(255) DEFAULT NULL COMMENT '检测地址',
  `detection_phone` varchar(50) DEFAULT NULL COMMENT '检测电话',
  `detection_statement` varchar(500) DEFAULT NULL COMMENT '检测声明',
  `detection_stamp_file_id` bigint(20) NOT NULL COMMENT '检测的印章文件id',
  `calibration_expire_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '校准期到期时间',
  `template_content` text COMMENT '模板内容',
  `tenant_id` int(10) DEFAULT NULL COMMENT '受检租户id',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测公司';

-- changeset ht:ddl-3
-- ----------------------------
-- Table structure for detection_property
-- ----------------------------
CREATE table IF NOT EXISTS `detection_property` (
  `id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  `product_name` varchar(100) DEFAULT NULL COMMENT '产品名称',
  `product_property_name` varchar(100) DEFAULT NULL COMMENT '属性名称',
  `product_property_code` varchar(50) NOT NULL COMMENT '产品属性code',
  `product_property_unit` varchar(50) NOT NULL COMMENT '产品属性单位',
  `product_property_tenant` int(10) NOT NULL COMMENT '产品属性租户id',
  `basic_id` bigint(20) NOT NULL COMMENT '检测依据id',
  `standard_id` bigint(20) NOT NULL COMMENT '检测标准id',
  `max_val` decimal(10,2) NOT NULL COMMENT '阈值上限',
  `min_val` decimal(10,2) NOT NULL COMMENT '阈值下限',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测属性';


-- changeset ht:ddl-4
-- ----------------------------
-- Table structure for detection_report_record
-- ----------------------------
CREATE table IF NOT EXISTS `detection_report_record` (
  `id` bigint(20) NOT NULL,
  `code` varchar(50) NOT NULL COMMENT '编号',
  `sort` int(11) DEFAULT NULL COMMENT '序号',
  `company` varchar(255) DEFAULT NULL COMMENT '检测公司',
  `detection_date` datetime DEFAULT NULL COMMENT '检测日期',
  `detection_date_period` varchar(100) DEFAULT NULL COMMENT '检测时间范围',
  `area` text COMMENT '检测区域',
  `statement` varchar(255) DEFAULT NULL COMMENT '检测声明',
  `company_address` varchar(255) DEFAULT NULL COMMENT '检测公司地址',
  `phone` varchar(255) DEFAULT NULL COMMENT '检测电话',
  `tenant_name` varchar(255) DEFAULT NULL COMMENT '受检租户名称',
  `tenant_address` varchar(255) DEFAULT NULL COMMENT '（受检地址）租户地址',
  `property_name` text COMMENT '检测项目',
  `basic_name` varchar(255) DEFAULT NULL COMMENT '依据名称',
  `result_appraise` text COMMENT '结果评价',
  `stamp_info_list` text COMMENT '采样信息列表',
  `source_list` text COMMENT '检测数据源列表',
  `tenant` int(10) DEFAULT NULL COMMENT '租户id',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测报告记录';


-- changeset ht:ddl-5
-- ----------------------------
-- Table structure for detection_standard
-- ----------------------------
CREATE table IF NOT EXISTS `detection_standard` (
  `id` bigint(20) NOT NULL,
  `basic_id` bigint(20) NOT NULL COMMENT '检测依据id',
  `name` varchar(50) NOT NULL COMMENT '标准名称',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测标准';

-- changeset ht:ddl-6
-- ----------------------------
-- Table structure for detection_report_company
-- ----------------------------
CREATE TABLE IF NOT EXISTS `detection_report_company` (
  `id` bigint(20) NOT NULL,
  `detection_report_id` bigint(20) NOT NULL COMMENT '检测报表的id',
  `detection_company` varchar(50) DEFAULT NULL COMMENT '检测公司',
  `detection_address` varchar(255) DEFAULT NULL COMMENT '检测地址',
  `detection_phone` varchar(50) DEFAULT NULL COMMENT '检测电话',
  `detection_statement` varchar(500) DEFAULT NULL COMMENT '检测声明',
  `detection_stamp_file_id` bigint(20) NOT NULL COMMENT '检测的印章文件id',
  `calibration_expire_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '校准期到期时间',
  `template_content` text COMMENT '模板内容',
  `tenant_id` int(10) DEFAULT NULL COMMENT '受检租户id',
  `tenant` int(10) DEFAULT NULL COMMENT '租户id',
  `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报表检测公司';