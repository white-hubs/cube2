CREATE DATABASE IF NOT EXISTS`cube_base_iot_center` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube_base_iot_center`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for center_biz_device
-- ----------------------------
CREATE TABLE IF NOT EXISTS `center_biz_device`  (
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
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `device_name`(`device_name`) USING BTREE COMMENT '设备名称'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '设备实例' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_biz_device_expand
-- ----------------------------
CREATE TABLE IF NOT EXISTS `center_biz_device_expand`  (
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
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `production_date` date NULL DEFAULT NULL COMMENT '生产日期',
  `location_des` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '位置描述信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '设备实例属性扩展表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_biz_device_ref
-- ----------------------------
CREATE TABLE  IF NOT EXISTS `center_biz_device_ref`  (
  `device_id` bigint(20) NOT NULL,
  `ref_device_id` bigint(20) NOT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '是否删除',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '设备关联引用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of center_biz_device_ref
-- ----------------------------

-- ----------------------------
-- Table structure for center_biz_device_state_log
-- ----------------------------
CREATE TABLE  IF NOT EXISTS `center_biz_device_state_log`  (
  `device_id` bigint(20) NOT NULL COMMENT '物联网设备ID',
  `device_state` int(11) NULL DEFAULT NULL COMMENT '0 离线 1在线',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX `device_id`(`device_id`) USING BTREE COMMENT '设备id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '设备实例状态更新记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for center_biz_tag
-- ----------------------------
CREATE TABLE IF NOT EXISTS `center_biz_tag`  (
  `id` bigint(20) NOT NULL,
  `tag_type_id` bigint(20) NULL DEFAULT NULL COMMENT '标签类别',
  `tag_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签code',
  `tag_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `level` int(10) UNSIGNED NOT NULL DEFAULT 1 COMMENT '标签层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父类主键',
  `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务含义描述',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `enable_status` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '启用状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique_tag_code`(`tag_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签定义' ROW_FORMAT = DYNAMIC;

INSERT INTO `cube_base_iot_center`.`center_biz_tag` (`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1752159255340785666, NULL, 'builtin.usage::ups', 'ups设备', 1, NULL, '1752159255340785665-', NULL, 0, 'admin', '2024-01-30 10:38:15', 'admin', '2024-01-30 10:38:15', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `cube_base_iot_center`.`center_biz_tag` (`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1752250816433266679, 1747887169581457410, 'builtin.usage::camera', '视频标签', 1, NULL, '1752250816433266689-', NULL, 0, '17311369782', '2024-01-30 16:42:05', '17311369782', '2024-01-30 16:42:05', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `cube_base_iot_center`.`center_biz_tag` (`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1752874934121652227, NULL, 'builtin.usage::battery', '蓄电池', 1, NULL, '1752874934121652226-', NULL, 0, 'admin', '2024-02-01 10:02:06', 'admin', '2024-02-01 10:02:06', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `cube_base_iot_center`.`center_biz_tag` (`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1752874934121652228, NULL, 'builtin.usage::temp', '温度计', 1, NULL, '1752874934121652226-', NULL, 0, 'admin', '2024-02-01 10:02:06', 'admin', '2024-02-01 10:02:06', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `cube_base_iot_center`.`center_biz_tag` (`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1752874934121652229, NULL, 'builtin.usage::fire', '消防设备', 1, NULL, '1752874934121652226-', NULL, 0, 'admin', '2024-02-01 10:02:06', 'admin', '2024-02-01 10:02:06', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `cube_base_iot_center`.`center_biz_tag` (`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1753605141759827971, NULL, 'builtin.usage::combustible_gas_monitoring.device', '用气设备', 1, NULL, '1753605141759827970-', NULL, 0, '17311369782', '2024-02-03 10:23:41', '17311369782', '2024-02-03 10:23:41', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `cube_base_iot_center`.`center_biz_tag` (`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1763459549691682818, NULL, 'builtin.usage::fire.water_pressure.device', '消防水压检测设备', 1, NULL, '1763459549691682818-', NULL, 0, '17311369782', '2024-03-01 15:01:35', '17311369782', '2024-03-01 15:01:35', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `cube_base_iot_center`.`center_biz_tag` (`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1791063259642671107, NULL, 'builtin.usage::combustible.gas.monitor', '可燃气体监测设备', 1, NULL, '1791063259642671107-', NULL, 0, '17311369782', '2024-06-05 16:38:08', '17311369782', '2024-06-05 16:38:15', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `cube_base_iot_center`.`center_biz_tag` (`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1791063259642671108, NULL, 'builtin.usage::electrical.monitor', '电气检测设备', 1, NULL, '1791063259642671108-', NULL, 0, '17311369782', '2024-06-05 16:39:08', '17311369782', '2024-06-05 16:39:10', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();


-- ----------------------------
-- Table structure for center_biz_tag_ref
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `center_biz_tag_ref`  (
  `id` bigint(20) NOT NULL,
  `tag_type_id` bigint(20) NULL DEFAULT NULL COMMENT '设备类型ID',
  `tag_id` bigint(20) NULL DEFAULT NULL COMMENT '标签ID',
  `business_key` bigint(30) NULL DEFAULT NULL COMMENT '业务键',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '是否删除',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `refkey`(`tag_id`, `business_key`) USING BTREE COMMENT '唯一索引',
  INDEX `business_key`(`business_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签与设备的应用关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of center_biz_tag_ref
-- ----------------------------

-- ----------------------------
-- Table structure for center_biz_tag_type
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `center_biz_tag_type`  (
  `id` bigint(20) NOT NULL,
  `tag_type_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签类型',
  `tag_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签类别' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of center_biz_tag_type
-- ----------------------------

-- ----------------------------
-- Table structure for center_device_scene_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `center_device_scene_config`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `scene_type` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '场景类型',
  `scene_type_sequence` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型序列号',
  `business_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_business_id_scene_type_tenant_deleted`(`scene_type`, `business_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设施业务场景' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of center_device_scene_config
-- ----------------------------

-- ----------------------------
-- Table structure for center_ext_device_attr_cmd
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `center_ext_device_attr_cmd`  (
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
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备属性命令表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of center_ext_device_attr_cmd
-- ----------------------------

-- ----------------------------
-- Table structure for center_ext_device_attr_cmd_step
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `center_ext_device_attr_cmd_step`  (
  `id` bigint(20) NOT NULL,
  `step` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行步骤',
  `command_seq` bigint(20) NULL DEFAULT NULL COMMENT '命令ID',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备属性执行详情表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of center_ext_device_attr_cmd_step
-- ----------------------------

-- ----------------------------
-- Table structure for center_ext_device_cmd
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `center_ext_device_cmd`  (
  `id` bigint(20) NOT NULL,
  `batch_cmd_seq` bigint(20) NULL DEFAULT NULL COMMENT '命令ID',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备命令表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of center_ext_device_cmd
-- ----------------------------

-- ----------------------------
-- Table structure for databasechangelog
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `databasechangelog`  (
  `ID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `AUTHOR` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `FILENAME` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `MD5SUM` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `DESCRIPTION` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `COMMENTS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `TAG` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `LIQUIBASE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `CONTEXTS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `LABELS` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SELECT COUNT(1) IndexIsThere
FROM INFORMATION_SCHEMA.STATISTICS
WHERE table_schema=DATABASE()
AND   table_name='databasechangelog'
AND   index_name='unique_id_file_name';

SET @IndexIsThere =
(SELECT COUNT(1) IndexIsThere
 FROM INFORMATION_SCHEMA.STATISTICS
 WHERE table_schema=DATABASE()
 AND   table_name='databasechangelog'
 AND   index_name='unique_id_file_name');

SET @sqlstmt =
IF (@IndexIsThere = 0,
    'ALTER TABLE `cube_base_iot_center`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

INSERT IGNORE INTO `cube_base_iot_center`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.device.center/changelog-0.0.1.mysql.sql', '2024-04-23 01:49:46', 1, 'EXECUTED', '8:84f00f7fafa7a13f5cd2e8b2c6df0ccc', 'sql', '', NULL, '4.3.1', NULL, NULL, '3808186122');
INSERT IGNORE INTO `cube_base_iot_center`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.base.device.center/changelog-0.0.2.mysql.sql', '2024-04-23 01:49:46', 2, 'EXECUTED', '8:d8551364138880377d76964f26fe3b6a', 'sql', '', NULL, '4.3.1', NULL, NULL, '3808186329');
INSERT IGNORE INTO `cube_base_iot_center`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('raw', 'includeAll', 'classpath:db/com.guhecloud.cube.base.device.center/changelog-0.0.3.mysql.sql', '2024-05-27 20:26:06', 3, 'EXECUTED', '8:d2ecb5dea59c27ca10b462330b44899d', 'sql', '', NULL, '4.3.1', NULL, NULL, '6812766069');
INSERT IGNORE INTO `cube_base_iot_center`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('raw', 'includeAll', 'classpath:db/com.guhecloud.cube.base.device.center/changelog-2.1.9.mysql.sql', '2024-06-05 10:18:20', 4, 'EXECUTED', '8:aeec1469f3029525f351bb3b4f935247', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553900645');
INSERT IGNORE INTO `cube_base_iot_center`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('raw', 'includeAll', 'classpath:db/com.guhecloud.cube.base.device.center/initSysPreTag.mysql.sql', '2024-06-17 11:59:54', 5, 'EXECUTED', '8:055f668a9558d73de6716cfe92bacefa', 'sql', '', NULL, '4.3.1', NULL, NULL, '8596794591');

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of databasechangeloglock
-- ----------------------------
INSERT IGNORE INTO `databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for iot_dev_acc_protocol
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_dev_acc_protocol`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '协议名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '协议编码',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '环境配置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_dev_acc_protocol
-- ----------------------------

-- ----------------------------
-- Table structure for iot_dev_device
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_dev_device`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `device_parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父设备ID',
  `device_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '设备KEY,后续扩展',
  `device_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '编码,唯一',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '设备名称',
  `status` tinyint(1) NOT NULL DEFAULT 2 COMMENT '状态:0未激活,1在线,2离线,3已禁用',
  `device_type` tinyint(1) NULL DEFAULT 1 COMMENT '设备类型:1真实设备,0虚拟设备',
  `activation_time` datetime NULL DEFAULT NULL COMMENT '激活时间',
  `online_time` datetime NULL DEFAULT NULL COMMENT '最近上线时间',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '设备描述',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '预留透明字段(可下发至设备的配置)',
  `raw_device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `auth_algorithm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '认证算法',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '盐(只有使用签名算法时有效)',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `number_index`(`device_number`) USING BTREE COMMENT '设备编码索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '设备表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_dev_device
-- ----------------------------

-- ----------------------------
-- Table structure for iot_dev_device_auth
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_dev_device_auth`  (
  `id` bigint(20) NULL DEFAULT NULL,
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备ID',
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '账号',
  `sign_algorithm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '签名算法',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '盐'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_dev_device_auth
-- ----------------------------

-- ----------------------------
-- Table structure for iot_dev_task
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_dev_task`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `task_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_dev_task
-- ----------------------------

-- ----------------------------
-- Table structure for iot_dev_task_command
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_dev_task_command`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `command_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务id',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `msg_body` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务状态',
  `result_code` int(100) NULL DEFAULT NULL COMMENT '结果码',
  `result_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结果信息',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_dev_task_command
-- ----------------------------

-- ----------------------------
-- Table structure for iot_link_condition_meta
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_link_condition_meta`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `rule_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '规则id',
  `rule_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '规则名称',
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联动类别',
  `device_id` bigint(20) NOT NULL COMMENT '设备id',
  `prop_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '属性id',
  `compare_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '比较类型',
  `compare_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '比较直',
  `delay_time` int(11) NOT NULL COMMENT '延时时间,单位分',
  `transparent_statement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '联动条件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_link_condition_meta
-- ----------------------------

-- ----------------------------
-- Table structure for iot_link_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_link_config`  (
  `id` bigint(22) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联动名称',
  `number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联动唯一编号',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '联动配置',
  `trigger_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '联动状态:0关闭,1开启',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '规则联动' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_link_config
-- ----------------------------

-- ----------------------------
-- Table structure for iot_link_trigger_meta
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_link_trigger_meta`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `rule_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '规则id',
  `rule_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `device_id` bigint(20) NOT NULL COMMENT '设备id',
  `prop_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '属性Code',
  `need_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '需要的值',
  `transparent_statement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `link_number_index`(`rule_num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '联动触发' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_link_trigger_meta
-- ----------------------------

-- ----------------------------
-- Table structure for iot_md_product
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_md_product`  (
  `id` bigint(20) NOT NULL COMMENT '产品ID',
  `prod_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品编号',
  `prod_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '产品名称',
  `is_custom` int(11) NOT NULL COMMENT '是否自定义品类',
  `prod_category_id` bigint(20) NOT NULL COMMENT '产品品类ID',
  `node_type` bigint(20) NOT NULL COMMENT '节点类型: 1直连设备 2网关设备 3网关子设备',
  `prod_brand_id` bigint(20) NOT NULL COMMENT '产品品牌id',
  `prod_model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品型号',
  `access_type_id` bigint(20) NULL DEFAULT NULL COMMENT '接入方式Id',
  `protocol_id` bigint(20) NULL DEFAULT NULL COMMENT '消息协议ID（非字典表）',
  `status` int(11) NULL DEFAULT 0 COMMENT '产品状态: 1正常 0禁用',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品描述',
  `extend_config` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '产品扩展字段,更多描述的可能',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `admin_id` bigint(20) NULL DEFAULT NULL COMMENT '产品库中对应的ID',
  `prod_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'v1.0.1' COMMENT '产品版本号',
  `prod_icon` bigint(20) NULL DEFAULT NULL COMMENT '图标',
  `prod_pic` bigint(20) NULL DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '产品定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for iot_md_product_event
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_md_product_event`  (
  `id` bigint(20) NOT NULL COMMENT '产品事件id',
  `prod_id` bigint(20) NOT NULL COMMENT '产品id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '事件名称',
  `event_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'v2添加字段,事件编码',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '触发条件',
  `category_id` bigint(20) NULL DEFAULT 0 COMMENT '事件类别',
  `remark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态: 0 启用 1禁用',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `event_code_index`(`event_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '事件定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_md_product_event
-- ----------------------------

-- ----------------------------
-- Table structure for iot_md_product_event_meta
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_md_product_event_meta`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `event_id` bigint(20) NOT NULL COMMENT '规则id',
  `prod_id` bigint(20) NULL DEFAULT NULL COMMENT '产品id',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id',
  `prop_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性CODE',
  `compare_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '比较类型',
  `compare_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '比较值',
  `transparent_statement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `is_enabled` int(11) NULL DEFAULT NULL COMMENT '是否启用:0未启用, 1已启用',
  `delay_time` int(11) NOT NULL COMMENT '延时时间,单位秒',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '事件元定义(针对属性模拟事件、单设备事件使用)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_md_product_event_meta
-- ----------------------------

-- ----------------------------
-- Table structure for iot_md_product_prop
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_md_product_prop`  (
  `id` bigint(20) NOT NULL,
  `prod_id` bigint(20) NOT NULL COMMENT '产品ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '属性名称',
  `prop_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性Code,产品下唯一',
  `data_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '数据类型',
  `precision` int(11) NULL DEFAULT 4 COMMENT '数据精度',
  `unit_id` bigint(20) NULL DEFAULT NULL COMMENT '数据单位id',
  `data_write` tinyint(1) NULL DEFAULT 0 COMMENT '读写类型:0只读:1读写',
  `data_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'json数组[0,100] 或 [{\"value\":\"0\",\"label\":\"开\"},{\"value\":\"1\",\"label\":\"关\"}]',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '属性描述',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '产品属性表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_md_product_prop
-- ----------------------------

-- ----------------------------
-- Table structure for iot_mock_rule
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_mock_rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模拟规则id',
  `mock_env` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '规则JSon',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `fail_reason` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_mock_rule
-- ----------------------------

-- ----------------------------
-- Table structure for iot_sys_cfg
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_sys_cfg`  (
  `id` bigint(20) NOT NULL,
  `cfg_type` int(11) NULL DEFAULT NULL COMMENT '配置类型: 1 告警配置',
  `cfg` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '配置(json-data)',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_sys_cfg
-- ----------------------------

-- ----------------------------
-- Table structure for iot_sys_dict
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_sys_dict`  (
  `id` bigint(20) NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典编码别名(用于查询)',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态0正常 1停用',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '字典类型中类型:0系统字典,1业务字典',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for iot_sys_dict_item
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_sys_dict_item`  (
  `id` bigint(20) NOT NULL COMMENT '字典编码',
  `dict_id` bigint(20) NULL DEFAULT NULL COMMENT '字典类型id',
  `item_parent_id` bigint(20) NULL DEFAULT 0 COMMENT '字典上级Id',
  `item_sort` int(11) NULL DEFAULT 1 COMMENT '字典排序',
  `item_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '层级',
  `item_label` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `item_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `status` tinyint(4) NULL DEFAULT 0 COMMENT '状态 0正常 1停用',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `admin_id` bigint(20) NULL DEFAULT NULL COMMENT '产品库中对应的ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for iot_sys_dict_item_ref
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_sys_dict_item_ref`  (
  `id` bigint(20) NOT NULL COMMENT '字典编码',
  `source_dict_id` bigint(20) NULL DEFAULT NULL COMMENT '源字典类型ID',
  `source_item_id` bigint(20) NULL DEFAULT NULL COMMENT '源字典项ID',
  `target_dict_id` bigint(20) NULL DEFAULT NULL COMMENT '目标字典类型ID',
  `target_item_id` bigint(20) NULL DEFAULT NULL COMMENT '目标字典项ID',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `admin_id` bigint(20) NULL DEFAULT NULL COMMENT '产品库中对应的ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典项关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of iot_sys_dict_item_ref
-- ----------------------------

-- ----------------------------
-- Table structure for iot_sys_dict_temp_table
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `iot_sys_dict_temp_table`  (
  `id` bigint(20) NOT NULL,
  `item_level` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `item_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `item_value` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `item_level_concat` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `item_label_concat` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `item_id_concat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for iot_category_view
-- ----------------------------
CREATE OR REPLACE VIEW `iot_category_view` AS select `item`.`id` AS `category_id`,`item`.`item_parent_id` AS `category_parent_id`,`item`.`item_level` AS `category_code`,`item`.`item_label` AS `category_name`,`item_temp`.`item_label_concat` AS `category_name_concat`,`item_temp`.`item_level_concat` AS `category_code_concat`,`item`.`tenant` AS `tenant`,`item`.`deleted` AS `deleted` from (`iot_sys_dict_item` `item` left join `iot_sys_dict_temp_table` `item_temp` on((`item`.`id` = `item_temp`.`id`))) where (`item`.`dict_id` = 1648234964497735681);

-- ----------------------------
-- View structure for iot_prod_view
-- ----------------------------
CREATE OR REPLACE VIEW `iot_prod_view` AS select `prod`.`id` AS `id`,`prod`.`prod_name` AS `prod_name`,`prod_category`.`item_level` AS `prod_category_code`,`prod_category`.`item_level_concat` AS `prod_category_concat`,`prod_category`.`item_label` AS `prod_category_name`,`prod_category`.`item_label_concat` AS `prod_category_name_concat`,`prod_brand`.`item_label` AS `prod_brand_name`,`prod`.`prod_model` AS `prod_model_name`,`prod`.`remark` AS `remark`,`prod`.`prod_version` AS `prod_version`,`prod`.`prod_icon` AS `prod_icon`,`prod`.`prod_pic` AS `prod_pic`,`prod`.`deleted` AS `deleted`,`prod`.`tenant` AS `tenant` from ((`iot_md_product` `prod` left join `iot_sys_dict_temp_table` `prod_category` on((`prod`.`prod_category_id` = `prod_category`.`id`))) left join `iot_sys_dict_item` `prod_brand` on((`prod`.`prod_brand_id` = `prod_brand`.`id`)));

-- ----------------------------
-- View structure for iot_sys_unit
-- ----------------------------
CREATE TABLE IF NOT EXISTS `iot_sys_unit` (
                                              `id` bigint(20) NOT NULL COMMENT 'id',
    `unit_code` varchar(100) DEFAULT '' COMMENT '单位code',
    `unit_name` varchar(20) DEFAULT NULL COMMENT '单位名称',
    `unit_type` varchar(20) DEFAULT NULL COMMENT '单位类型',
    `unit_status` tinyint(1) DEFAULT '0' COMMENT '状态0正常 1停用',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `deleted` bigint(20) DEFAULT NULL,
    `create_by` varchar(100) NOT NULL COMMENT '创建用户',
    `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
    `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) NOT NULL COMMENT '更新用户',
    `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
    `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='单位表';

INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1810643242846289956, 'unit002', '个/m3', null,'0',null,0, 'system', '2024-07-09 19:52:44', 'system', '2024-07-09 19:52:44')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1602493505731637252, 'unit003', '℃', null,'0',null,0, '15181147301', '2022-08-03 00:00:00', '17311369782', '2023-01-09 00:00:00')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441745, 'unit004', 'Ω', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441733, 'unit005', '°C', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1808699139443163171, 'unit006', 'year', null,'0',null,0, 'system', '2024-07-04 11:07:34', 'system', '2024-07-04 11:07:34')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441748, 'unit007', 'Wb', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441742, 'unit008', 'W', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1562002842242330631, 'unit009', 'V', null,'0',null,0, '15181147301', '2022-08-03 00:00:00', '17311369782', '2023-03-31 00:00:00')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441749, 'unit010', 'T', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441731, 'unit011', 's', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1562003097700610050, 'unit012', 'Rpm', null,'0',null,0, '15181147301', '2022-08-03 00:00:00', '17311369782', '2023-01-09 00:00:00')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1572409012761214978, 'unit013', 'PPM', null,'0',null,0, '15181147301', '2022-08-03 00:00:00', '17311369782', '2023-01-09 00:00:00')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1808699139443163167, 'unit014', 'ppm', null,'0',null,0, 'system', '2024-07-04 11:07:34', 'system', '2024-07-04 11:07:34')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441739, 'unit015', 'Pa', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441738, 'unit016', 'N', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1808699139443163165, 'unit017', 'm3/h', null,'0',null,0, 'system', '2024-07-04 11:07:34', 'system', '2024-07-04 11:07:34')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441751, 'unit018', 'm3', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441750, 'unit019', 'm2', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441735, 'unit020', 'mol', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1808699139443163168, 'unit021', 'min', null,'0',null,0, 'system', '2024-07-04 11:07:34', 'system', '2024-07-04 11:07:34')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1808699139443163166, 'unit022', 'mg/m3', null,'0',null,0, 'system', '2024-07-04 11:07:34', 'system', '2024-07-04 11:07:34')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441753, 'unit023', 'm/s2', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441752, 'unit024', 'm/s', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441729, 'unit025', 'm', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1808699139443163163, 'unit026', 'lux', null,'0',null,0, 'system', '2024-07-04 11:07:34', 'system', '2024-07-04 11:07:34')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441754, 'unit027', 'L', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1826098741465088038, 'unit028', 'kWh', null,'0',null,0, 'system', '2024-08-21 11:27:22', 'system', '2024-08-21 11:27:22')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1562003097700610051, 'unit029', 'Kpa', null,'0',null,0, '15181147301', '2022-08-03 00:00:00', '17311369782', '2023-01-09 00:00:00')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441730, 'unit030', 'kg', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441734, 'unit031', 'K', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441741, 'unit032', 'J', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441737, 'unit033', 'Hz', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1808699139443163169, 'unit034', 'hour', null,'0',null,0, 'system', '2024-07-04 11:07:34', 'system', '2024-07-04 11:07:34')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441747, 'unit035', 'H', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441746, 'unit036', 'F', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1808699139443163164, 'unit037', 'dB', null,'0',null,0, 'system', '2024-07-04 11:07:34', 'system', '2024-07-04 11:07:34')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1808699139443163170, 'unit038', 'day', null,'0',null,0, 'system', '2024-07-04 11:07:34', 'system', '2024-07-04 11:07:34')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1562002842242330626, 'unit039', 'cosΦ', null,'0',null,0, '15181147301', '2022-08-03 00:00:00', '17311369782', '2023-03-31 00:00:00')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441736, 'unit040', 'cd', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441743, 'unit041', 'C', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1562002842242330627, 'unit042', 'A', null,'0',null,0, '15181147301', '2022-08-03 00:00:00', '17311369782', '2023-03-31 00:00:00')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1767532231482441728, 'unit043', '%', null,'0',null,0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);
INSERT INTO `iot_sys_unit`(`id`, `unit_code`, `unit_name`, `unit_type`, `unit_status`, `remark`,  `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1811409748760756263, 'unit044', 'Mpa', NULL, 0, NULL, 0, NULL, '2024-03-12 20:44:58', NULL, '2024-03-12 20:44:58')ON DUPLICATE KEY UPDATE `unit_code` = VALUES(`unit_code`),`unit_name` = VALUES(`unit_name`),`unit_type` = VALUES(`unit_type`),`unit_status` = VALUES(`unit_status`),`remark` = VALUES(`remark`),`deleted` = VALUES(`deleted`),`create_by` = VALUES(`create_by`),`create_time` = VALUES(`create_time`),`update_by` = VALUES(`update_by`),`update_time` = VALUES(`update_time`);

INSERT INTO `center_biz_tag`(`id`, `tag_type_id`, `tag_code`, `tag_name`, `level`, `parent_id`, `sequence`, `description`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `enable_status`) VALUES (1853333504683192323, NULL, 'builtin.usage::artificial_intelligence', '人工智能', 1, NULL, '1853333504683192323-', NULL, 0, '17311369782', '2024-11-04 15:08:35', '17311369782', '2024-11-04 15:08:35', 1) ON DUPLICATE KEY UPDATE `update_time` = NOW();

SET FOREIGN_KEY_CHECKS = 1;
