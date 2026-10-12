-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- ----------------------------
-- 此脚本仅包含iot部分
-- ----------------------------


-- ----------------------------
-- Table structure for iot_dev_acc_protocol
-- ----------------------------
CREATE TABLE `iot_dev_acc_protocol` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '协议名称',
  `code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '协议编码',
  `config` text COLLATE utf8mb4_bin COMMENT '环境配置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for iot_dev_device
-- ----------------------------
CREATE TABLE `iot_dev_device` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `device_parent_id` bigint(20) DEFAULT NULL COMMENT '父设备ID',
  `device_key` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '设备KEY,后续扩展',
  `device_number` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '编码,唯一',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '设备名称',
  `status` tinyint(1) NOT NULL DEFAULT '2' COMMENT '状态:0未激活,1在线,2离线,3已禁用',
  `device_type` tinyint(1) DEFAULT '1' COMMENT '设备类型:1真实设备,0虚拟设备',
  `activation_time` datetime DEFAULT NULL COMMENT '激活时间',
  `online_time` datetime DEFAULT NULL COMMENT '最近上线时间',
  `remark` text COLLATE utf8mb4_bin COMMENT '设备描述',
  `config` text COLLATE utf8mb4_bin COMMENT '预留透明字段(可下发至设备的配置)',
  `raw_device_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `auth_algorithm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '认证算法',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '盐(只有使用签名算法时有效)',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `number_index` (`device_number`) USING BTREE COMMENT '设备编码索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='设备表';

-- ----------------------------
-- Table structure for iot_dev_device_auth
-- ----------------------------
CREATE TABLE `iot_dev_device_auth` (
  `id` bigint(20) DEFAULT NULL,
  `device_id` bigint(20) DEFAULT NULL COMMENT '设备ID',
  `account` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '账号',
  `sign_algorithm` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '签名算法',
  `salt` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '盐'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for iot_dev_task
-- ----------------------------
CREATE TABLE `iot_dev_task` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `task_name` varchar(50) DEFAULT NULL COMMENT '任务名称',
  `state` varchar(255) DEFAULT NULL COMMENT '状态',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `create_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for iot_dev_task_command
-- ----------------------------
CREATE TABLE `iot_dev_task_command` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `command_name` varchar(100) NOT NULL DEFAULT '',
  `task_id` bigint(20) DEFAULT NULL COMMENT '任务id',
  `type` varchar(100) DEFAULT NULL COMMENT '类型',
  `msg_body` varchar(4000) DEFAULT NULL COMMENT '消息',
  `state` varchar(255) DEFAULT NULL COMMENT '任务状态',
  `result_code` int(100) DEFAULT NULL COMMENT '结果码',
  `result_msg` varchar(2000) DEFAULT NULL COMMENT '结果信息',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for iot_link_condition_meta
-- ----------------------------
CREATE TABLE `iot_link_condition_meta` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `rule_num` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '规则id',
  `rule_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '规则名称',
  `category_id` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '联动类别',
  `device_id` bigint(20) NOT NULL COMMENT '设备id',
  `prop_code` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '属性id',
  `compare_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '比较类型',
  `compare_value` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '比较直',
  `delay_time` int(11) NOT NULL COMMENT '延时时间,单位分',
  `transparent_statement` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='联动条件';

-- ----------------------------
-- Table structure for iot_link_config
-- ----------------------------
CREATE TABLE `iot_link_config` (
  `id` bigint(22) NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '联动名称',
  `number` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '联动唯一编号',
  `config` text COLLATE utf8mb4_bin COMMENT '联动配置',
  `trigger_type` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL COMMENT '联动状态:0关闭,1开启',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='规则联动';

-- ----------------------------
-- Table structure for iot_link_trigger_meta
-- ----------------------------
CREATE TABLE `iot_link_trigger_meta` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `rule_num` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '规则id',
  `rule_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `device_id` bigint(20) NOT NULL COMMENT '设备id',
  `prop_code` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '属性Code',
  `need_value` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '需要的值',
  `transparent_statement` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `link_number_index` (`rule_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='联动触发';

-- ----------------------------
-- Table structure for iot_md_product
-- ----------------------------
CREATE TABLE `iot_md_product` (
  `id` bigint(20) NOT NULL COMMENT '产品ID',
  `prod_number` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品编号',
  `prod_name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '产品名称',
  `is_custom` int(11) NOT NULL COMMENT '是否自定义品类',
  `prod_category_id` bigint(20) NOT NULL COMMENT '产品品类ID',
  `node_type` bigint(20) NOT NULL COMMENT '节点类型: 1直连设备 2网关设备 3网关子设备',
  `prod_brand_id` bigint(20) NOT NULL COMMENT '产品品牌id',
  `prod_model` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品型号',
  `access_type_id` bigint(20) DEFAULT NULL COMMENT '接入方式Id',
  `protocol_id` bigint(20) DEFAULT NULL COMMENT '消息协议ID（非字典表）',
  `status` int(11) DEFAULT '0' COMMENT '产品状态: 1正常 0禁用',
  `remark` varchar(500) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品描述',
  `extend_config` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '产品扩展字段,更多描述的可能',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `admin_id` bigint(20) DEFAULT NULL COMMENT '产品库中对应的ID',
  `prod_version` varchar(255) COLLATE utf8mb4_bin DEFAULT 'v1.0.1' COMMENT '产品版本号',
  `prod_icon` bigint(20) DEFAULT NULL COMMENT '图标',
  `prod_pic` bigint(20) DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='产品定义';

-- ----------------------------
-- Table structure for iot_md_product_event
-- ----------------------------
CREATE TABLE `iot_md_product_event` (
  `id` bigint(20) NOT NULL COMMENT '产品事件id',
  `prod_id` bigint(20) NOT NULL COMMENT '产品id',
  `name` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '事件名称',
  `event_code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'v2添加字段,事件编码',
  `config` text COLLATE utf8mb4_bin COMMENT '触发条件',
  `category_id` bigint(20) DEFAULT '0' COMMENT '事件类别',
  `remark` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `status` int(11) DEFAULT NULL COMMENT '状态: 0 启用 1禁用',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `event_code_index` (`event_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='事件定义';

-- ----------------------------
-- Table structure for iot_md_product_event_meta
-- ----------------------------
CREATE TABLE `iot_md_product_event_meta` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `event_id` bigint(20) NOT NULL COMMENT '规则id',
  `prod_id` bigint(20) DEFAULT NULL COMMENT '产品id',
  `device_id` bigint(20) DEFAULT NULL COMMENT '设备id',
  `prop_code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '属性CODE',
  `compare_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '比较类型',
  `compare_value` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '比较值',
  `transparent_statement` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `is_enabled` int(11) DEFAULT NULL COMMENT '是否启用:0未启用, 1已启用',
  `delay_time` int(11) NOT NULL COMMENT '延时时间,单位秒',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='事件元定义(针对属性模拟事件、单设备事件使用)';

-- ----------------------------
-- Table structure for iot_md_product_prop
-- ----------------------------
CREATE TABLE `iot_md_product_prop` (
  `id` bigint(20) NOT NULL,
  `prod_id` bigint(20) NOT NULL COMMENT '产品ID',
  `name` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '属性名称',
  `prop_code` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '属性Code,产品下唯一',
  `data_type` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '数据类型',
  `precision` int(11) DEFAULT '4' COMMENT '数据精度',
  `unit_id` bigint(20) DEFAULT NULL COMMENT '数据单位id',
  `data_write` tinyint(1) DEFAULT '0' COMMENT '读写类型:0只读:1读写',
  `data_config` text COLLATE utf8mb4_bin COMMENT 'json数组[0,100] 或 [{"value":"0","label":"开"},{"value":"1","label":"关"}]',
  `remark` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '属性描述',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='产品属性表';

-- ----------------------------
-- Table structure for iot_mock_rule
-- ----------------------------
CREATE TABLE `iot_mock_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模拟规则id',
  `mock_env` mediumtext COLLATE utf8mb4_bin COMMENT '规则JSon',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `fail_reason` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1213 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for iot_sys_cfg
-- ----------------------------
CREATE TABLE `iot_sys_cfg` (
  `id` bigint(20) NOT NULL,
  `cfg_type` int(11) DEFAULT NULL COMMENT '配置类型: 1 告警配置',
  `cfg` varchar(4000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '配置(json-data)',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for iot_sys_dict
-- ----------------------------
CREATE TABLE `iot_sys_dict` (
  `id` bigint(20) NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_code` varchar(20) DEFAULT NULL COMMENT '字典编码别名(用于查询)',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态0正常 1停用',
  `type` tinyint(1) DEFAULT '0' COMMENT '字典类型中类型:0系统字典,1业务字典',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型';

-- ----------------------------
-- Table structure for iot_sys_dict_item
-- ----------------------------
CREATE TABLE `iot_sys_dict_item` (
  `id` bigint(20) NOT NULL COMMENT '字典编码',
  `dict_id` bigint(20) DEFAULT NULL COMMENT '字典类型id',
  `item_parent_id` bigint(20) DEFAULT '0' COMMENT '字典上级Id',
  `item_sort` int(11) DEFAULT '1' COMMENT '字典排序',
  `item_level` varchar(255) DEFAULT NULL COMMENT '层级',
  `item_label` varchar(2000) DEFAULT '' COMMENT '字典标签',
  `item_value` varchar(2000) DEFAULT '' COMMENT '字典键值',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态 0正常 1停用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `admin_id` bigint(20) DEFAULT NULL COMMENT '产品库中对应的ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型值';

-- ----------------------------
-- Table structure for iot_sys_dict_item_ref
-- ----------------------------
CREATE TABLE `iot_sys_dict_item_ref` (
  `id` bigint(20) NOT NULL COMMENT '字典编码',
  `source_dict_id` bigint(20) DEFAULT NULL COMMENT '源字典类型ID',
  `source_item_id` bigint(20) DEFAULT NULL COMMENT '源字典项ID',
  `target_dict_id` bigint(20) DEFAULT NULL COMMENT '目标字典类型ID',
  `target_item_id` bigint(20) DEFAULT NULL COMMENT '目标字典项ID',
  `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `admin_id` bigint(20) DEFAULT NULL COMMENT '产品库中对应的ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典项关联';

-- ----------------------------
-- Table structure for iot_sys_dict_temp_table
-- ----------------------------
CREATE TABLE `iot_sys_dict_temp_table` (
  `id` bigint(20) NOT NULL,
  `item_level` varchar(400) DEFAULT NULL,
  `item_label` varchar(255) DEFAULT NULL,
  `item_value` varchar(400) DEFAULT NULL,
  `item_level_concat` varchar(800) DEFAULT NULL,
  `item_label_concat` varchar(800) DEFAULT NULL,
  `item_id_concat` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE OR REPLACE ALGORITHM = UNDEFINED DEFINER = `root`@`%` SQL SECURITY DEFINER VIEW `iot_category_view` AS select `item`.`id` AS `category_id`,`item`.`item_parent_id` AS `category_parent_id`,`item`.`item_level` AS `category_code`,`item`.`item_label` AS `category_name`,`item_temp`.`item_label_concat` AS `category_name_concat`,`item_temp`.`item_level_concat` AS `category_code_concat`,`item`.`tenant` AS `tenant`,`item`.`deleted` AS `deleted` from (`iot_sys_dict_item` `item` left join `iot_sys_dict_temp_table` `item_temp` on((`item`.`id` = `item_temp`.`id`))) where (`item`.`dict_id` = 1648234964497735681);

CREATE OR REPLACE ALGORITHM = UNDEFINED DEFINER = `root`@`%` SQL SECURITY DEFINER VIEW `iot_prod_view` AS select `prod`.`id` AS `id`,`prod`.`prod_name` AS `prod_name`,`prod_category`.`item_level` AS `prod_category_code`,`prod_category`.`item_level_concat` AS `prod_category_concat`,`prod_category`.`item_label` AS `prod_category_name`,`prod_category`.`item_label_concat` AS `prod_category_name_concat`,`prod_brand`.`item_label` AS `prod_brand_name`,`prod`.`prod_model` AS `prod_model_name`,`prod`.`remark` AS `remark`,`prod`.`prod_version` AS `prod_version`,`prod`.`prod_icon` AS `prod_icon`,`prod`.`prod_pic` AS `prod_pic`,`prod`.`deleted` AS `deleted`,`prod`.`tenant` AS `tenant` from ((`iot_md_product` `prod` left join `iot_sys_dict_temp_table` `prod_category` on((`prod`.`prod_category_id` = `prod_category`.`id`))) left join `iot_sys_dict_item` `prod_brand` on((`prod`.`prod_brand_id` = `prod_brand`.`id`)));