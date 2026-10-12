-- liquibase formatted sql

-- changeset qqq:1686103358461-1

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for center_biz_device
-- ----------------------------
CREATE TABLE `center_biz_device`  (
  `id` bigint(20) NOT NULL COMMENT '物联网设备ID',
  `device_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '设备名称',
  `device_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '设备编码(孪生编码)',
  `device_iot_prod_id` bigint(20) NULL DEFAULT NULL COMMENT '设备物联产品ID ',
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '归属部门',
  `department_name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '归属部门名称',
  `area_id` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '区域ID,-分割',
  `area_name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '区域ID名称,--分割',
  `area_last_id` bigint(20) NULL DEFAULT NULL COMMENT '最末级的区域ID',
  `device_state` int(11) NULL DEFAULT NULL COMMENT '0 离线 1在线',
  `communication_time` datetime NULL DEFAULT NULL COMMENT '最后通信时间',
  `manager_ids` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '管理人员ids,-分割',
  `manager_names` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '管理人员名称,--分割',
  `thirdparty_id` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '三方设备ID',
  `thirdparty_cfg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '三方设备配置',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `device_name`(`device_name`) USING BTREE COMMENT '设备名称'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '设备实例' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_biz_device_expand
-- ----------------------------
CREATE TABLE `center_biz_device_expand`  (
  `id` bigint(20) UNSIGNED ZEROFILL NOT NULL COMMENT '设备表中的id',
  `security_salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '设备秘钥的盐(占位)',
  `security_pwd` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '设备安全密码',
  `useful_life` int(11) NULL DEFAULT NULL COMMENT '使用寿命',
  `daily_work_time` int(11) NULL DEFAULT NULL COMMENT '每日开机时长',
  `lnt_and_lat` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '设备经纬度',
  `unit_price` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '设备单价',
  `purchase_date` date NULL DEFAULT NULL COMMENT '采购日期',
  `picture` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '安装图片',
  `expand_form` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '扩展表单配置(以JSON存放),eg: {tag_code:A001, input:\"aaa\"}',
  `install_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装单位',
  `install_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装电话',
  `install_date` date NULL DEFAULT NULL COMMENT '安装时间',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '是否删除',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `production_date` date NULL DEFAULT NULL COMMENT '生产日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '设备实例属性扩展表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_biz_device_ref
-- ----------------------------
CREATE TABLE `center_biz_device_ref`  (
  `device_id` bigint(20) NOT NULL,
  `ref_device_id` bigint(20) NOT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '是否删除',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '设备关联引用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_biz_device_state_log
-- ----------------------------
CREATE TABLE `center_biz_device_state_log`  (
  `device_id` bigint(20) NOT NULL COMMENT '物联网设备ID',
  `device_state` int(11) NULL DEFAULT NULL COMMENT '0 离线 1在线',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `device_id`(`device_id`) USING BTREE COMMENT '设备id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '设备实例状态更新记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_biz_tag
-- ----------------------------
CREATE TABLE `center_biz_tag`  (
  `id` bigint(20) NOT NULL,
  `tag_type_id` bigint(20) NULL DEFAULT NULL COMMENT '标签类别',
  `tag_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签code',
  `tag_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `level` int(10) UNSIGNED NOT NULL DEFAULT 1 COMMENT '标签层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父类主键',
  `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务含义描述',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签定义' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_biz_tag_ref
-- ----------------------------
CREATE TABLE `center_biz_tag_ref`  (
  `id` bigint(20) NOT NULL,
  `tag_type_id` bigint(20) NULL DEFAULT NULL COMMENT '设备类型ID',
  `tag_id` bigint(20) NULL DEFAULT NULL COMMENT '标签ID',
  `business_key` bigint(30) NULL DEFAULT NULL COMMENT '业务键',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '是否删除',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `refkey`(`tag_id`, `business_key`) USING BTREE COMMENT '唯一索引',
  INDEX `business_key`(`business_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签与设备的应用关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_biz_tag_type
-- ----------------------------
CREATE TABLE `center_biz_tag_type`  (
  `id` bigint(20) NOT NULL,
  `tag_type_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签类型',
  `tag_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签类别' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_device_scene_config
-- ----------------------------
CREATE TABLE `center_device_scene_config`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `scene_type` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '场景类型',
  `scene_type_sequence` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型序列号',
  `business_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_business_id_scene_type_tenant_deleted`(`scene_type`, `business_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设施业务场景' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_ext_device_attr_cmd
-- ----------------------------
CREATE TABLE `center_ext_device_attr_cmd`  (
  `id` bigint(20) NOT NULL,
  `command_seq` bigint(20) NULL DEFAULT NULL COMMENT '命令ID',
  `batch_cmd_id` bigint(20) NULL DEFAULT NULL COMMENT '批次ID',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备ID',
  `attribute_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性名称',
  `attribute_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性code',
  `execute_result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行结果',
  `original_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原始值',
  `expected_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '期望修改为的值',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备属性命令表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_ext_device_attr_cmd_step
-- ----------------------------
CREATE TABLE `center_ext_device_attr_cmd_step`  (
  `id` bigint(20) NOT NULL,
  `step` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行步骤',
  `command_seq` bigint(20) NULL DEFAULT NULL COMMENT '命令ID',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备属性执行详情表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_ext_device_cmd
-- ----------------------------
CREATE TABLE `center_ext_device_cmd`  (
  `id` bigint(20) NOT NULL,
  `batch_cmd_seq` bigint(20) NULL DEFAULT NULL COMMENT '命令ID',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备命令表' ROW_FORMAT = DYNAMIC;
