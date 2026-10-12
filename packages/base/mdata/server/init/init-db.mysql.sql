CREATE DATABASE IF NOT EXISTS`cube-biz-mdata` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-biz-mdata`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for area
-- ----------------------------

CREATE TABLE IF NOT EXISTS `area`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域编号',
  `level` int(10) UNSIGNED NOT NULL COMMENT '区域层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父类主键',
  `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列号',
  `built_up_area` decimal(20, 5) NULL DEFAULT NULL COMMENT '占地面积',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `alias_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域别名',
  `sort` int(10) NULL DEFAULT NULL COMMENT '序号',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `visualization_area_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可视化区域类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `area_manager_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域管理人id',
  `area_department_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域管理部门id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_area_tenant_deleted_code`(`tenant`, `deleted`, `code`) USING BTREE,
  UNIQUE INDEX `idx_area_tenant_deleted_name_parent_id`(`tenant`, `deleted`, `name`, `parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of area
-- ----------------------------

-- ----------------------------
-- Table structure for area_group
-- ----------------------------

CREATE TABLE IF NOT EXISTS `area_group`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `level` int(10) UNSIGNED NULL DEFAULT NULL,
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父级主键',
  `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '序列号',
  `sort` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '序号',
  `enabled` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否启用',
  `tenant` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of area_group
-- ----------------------------

-- ----------------------------
-- Table structure for area_group_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `area_group_config`  (
  `id` bigint(20) NOT NULL,
  `area_group_id` bigint(20) NULL DEFAULT NULL COMMENT '区域组主键',
  `area_group` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域组序列号',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域主键',
  `area` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域序列号',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_area_group_areas_tenant_deleted_area_group_id_area_id`(`tenant`, `deleted`, `area_group_id`, `area_id`) USING BTREE,
  UNIQUE INDEX `idx_area_group_areas_tenant_deleted_area_group_area`(`tenant`, `deleted`, `area_group`, `area_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of area_group_config
-- ----------------------------

-- ----------------------------
-- Table structure for area_roaming_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `area_roaming_config`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '漫游名称',
  `scene_type` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '场景类型主键',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '区域主键',
  `areas` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域序列号',
  `roaming_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '漫游配置',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '区域漫游配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of area_roaming_config
-- ----------------------------

-- ----------------------------
-- Table structure for area_scene_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `area_scene_config`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `area_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '区域主键',
  `scene_type` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '场景类型',
  `scene_type_sequence` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型序列号',
  `left_panel` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '左面板',
  `right_panel` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '右面板',
  `bim_view_bind_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '模型视角绑定主键集合',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_area_id_scenet_type_tenant_deleted`(`area_id`, `scene_type`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '区域场景配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of area_scene_config
-- ----------------------------

-- ----------------------------
-- Table structure for authentication
-- ----------------------------

CREATE TABLE IF NOT EXISTS `authentication`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `merchant_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户主键',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '经营性质',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `identity_card` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '身份证号',
  `subject_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营主体类型',
  `subject_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主体名称',
  `license_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经营证照号',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核状态',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核不通过原因',
  `submit_time` datetime NOT NULL COMMENT '提交时间',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) NOT NULL COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of authentication
-- ----------------------------

-- ----------------------------
-- Table structure for bank_card_valid_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `bank_card_valid_config`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `app_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方APPCODE',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(255) UNSIGNED NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_tenant_deleted`(`tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bank_card_valid_config
-- ----------------------------

-- ----------------------------
-- Table structure for biz_log_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `biz_log_record`  (
  `id` bigint(20) NOT NULL,
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '定义为模块名称或（应用名称）',
  `type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `sub_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务功能编码',
  `sub_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能名称',
  `operation_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `biz_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务主键，可以为空',
  `action` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作描述',
  `extra` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其他信息',
  `code_variable` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作的类信息',
  `fail` tinyint(255) NULL DEFAULT NULL COMMENT 'false 成功，true 失败；失败的信息也再action中',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of biz_log_record
-- ----------------------------

-- ----------------------------
-- Table structure for business_panel_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `business_panel_config`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `business_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设施类型',
  `business_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设施主键',
  `panel_position` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '面板定位',
  `panel_config` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '面板配置',
  `tenant` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
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
  UNIQUE INDEX `idx_business_id_business_type_panel_position_tenant_deleted`(`business_type`, `business_id`, `panel_position`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of business_panel_config
-- ----------------------------

-- ----------------------------
-- Table structure for business_scene_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `business_scene_config`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `scene_type` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '场景类型',
  `scene_type_sequence` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型序列号',
  `business_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '业务主键',
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
-- Records of business_scene_config
-- ----------------------------

-- ----------------------------
-- Table structure for cost_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `cost_rule`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `cost_tmp_id` bigint(20) NULL DEFAULT NULL COMMENT '费用模板主键',
  `charge_project_id` bigint(20) NULL DEFAULT NULL COMMENT '收费项目主键',
  `calculation_mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计算方式',
  `unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `billing_cycle` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计费周期',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '费用规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cost_rule
-- ----------------------------

-- ----------------------------
-- Table structure for cost_tmp
-- ----------------------------

CREATE TABLE IF NOT EXISTS `cost_tmp`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '费用模板' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cost_tmp
-- ----------------------------

-- ----------------------------
-- Table structure for databasechangelog
-- ----------------------------

CREATE TABLE IF NOT EXISTS `databasechangelog`  (
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
    'ALTER TABLE `cube-biz-mdata`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.mdata/test001.mysql.sql', '2024-04-23 01:27:29', 1, 'EXECUTED', '8:a22c41eff120bacd02d7933b4f54d351', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806849851');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.mdata/changelog-1.0.4-0.mysql.sql', '2024-04-23 01:27:29', 2, 'EXECUTED', '8:5b85bbe7e69f8f079a5ca970f69f28eb', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806849920');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.mdata/changelog-1.0.5-0.mysql.sql', '2024-04-23 01:27:29', 3, 'EXECUTED', '8:84967110013a6888792691f54b468413', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806849978');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.mdata/changelog-1.0.6-0.mysql.sql', '2024-04-23 01:27:30', 4, 'EXECUTED', '8:d1d774e0e508bfac7c409d0d5cf1b3f0', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806850042');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.mdata/changelog-1.0.7-0.mysql.sql', '2024-04-23 01:27:30', 5, 'EXECUTED', '8:e3348f05d58bfa30df1803d0c44f5d4d', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806850141');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.mdata/changelog-1.0.7-1.mysql.sql', '2024-04-23 01:27:30', 6, 'EXECUTED', '8:be6ed9cbbbfeeef9a55faf673ea79852', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806850215');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.mdata/changelog-1.0.8-0.mysql.sql', '2024-04-23 01:27:30', 7, 'EXECUTED', '8:e9dc65003f2ed234d8aa5bf2cfe77d71', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806850455');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.mdata/changelog-1.0.9-0.mysql.sql', '2024-04-25 15:43:02', 8, 'EXECUTED', '8:4ed62eaa412980ff22de24e8dca14ee5', 'sql', '', NULL, '4.3.1', NULL, NULL, '4030981805');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.mdata/changelog-1.0.10-0.mysql.sql', '2024-04-25 15:43:02', 8, 'EXECUTED', '8:4ed62eaa412980ff22de24e8dca14ee5', 'sql', '', NULL, '4.3.1', NULL, NULL, '4030981805');

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------

CREATE TABLE IF NOT EXISTS `databasechangeloglock`  (
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
-- Table structure for device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `top_type_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型顶级Code',
  `brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌型号',
  `area` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属区域',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '具体地址',
  `install_time` datetime NULL DEFAULT NULL COMMENT '安装时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `ops_dept` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运维部门',
  `ops_user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '运维人员',
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `iot_bind` tinyint(1) NULL DEFAULT 0 COMMENT '物联绑定',
  `bim_bind` tinyint(1) NULL DEFAULT NULL COMMENT '模型绑定',
  `online` tinyint(1) NULL DEFAULT 0 COMMENT '在线状态',
  `tags` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备标签',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `attrs` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性',
  `identification` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一标识 冗余',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device
-- ----------------------------

-- ----------------------------
-- Table structure for device_collect
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_collect`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户主键',
  `device_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备主键',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_id_device_id_tenant_deleted`(`user_id`, `device_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备收藏' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_collect
-- ----------------------------

-- ----------------------------
-- Table structure for device_iot
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_iot`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备主键',
  `iot_brand_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联品牌主键',
  `iot_brand_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联品牌名称',
  `iot_model_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联型号主键',
  `iot_model_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物联型号名称',
  `identification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `iot_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '物联配置模板',
  `version` int(11) NULL DEFAULT NULL COMMENT '配置版本号',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_identification_deleted`(`identification`, `deleted`) USING BTREE,
  INDEX `idx_device_iot_device_id`(`device_id`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物联配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_iot
-- ----------------------------

-- ----------------------------
-- Table structure for device_relation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_relation`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `source_device_id` bigint(20) UNSIGNED NOT NULL COMMENT '被绑设备主键',
  `target_device_id` bigint(20) UNSIGNED NOT NULL COMMENT '绑定设备主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_relation
-- ----------------------------

-- ----------------------------
-- Table structure for device_stream_media
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_stream_media`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `device_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '设备主键',
  `steam_media_config_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '流媒体服务器配置主键',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流媒体摄像头token',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_device_id_tenant_deleted`(`device_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流媒体配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_stream_media
-- ----------------------------

-- ----------------------------
-- Table structure for device_tag
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_tag`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签编号',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_tag
-- ----------------------------
INSERT INTO `device_tag` VALUES (1783766149662584833, '猪肉滑轨', 'MEAT_TRADE', 68547128, 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_tag` VALUES (1783766149691944962, '电气安全', 'ELECTRIC_SAFETY', 68547128, 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_tag` VALUES (1783766149696139266, '安防监控', 'SECURITY_MONITOR', 68547128, 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_tag` VALUES (1783766149696139267, '进门收费', 'ENTER_CHARGE', 68547128, 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_tag` VALUES (1783766149704527874, '来货稽查', 'REPORT_CHECK', 68547128, 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for device_tag_relation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_tag_relation`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `device_id` bigint(20) UNSIGNED NOT NULL COMMENT '设备主键',
  `device_tag_id` bigint(20) UNSIGNED NOT NULL COMMENT '设备标签主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备标签关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_tag_relation
-- ----------------------------

-- ----------------------------
-- Table structure for device_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_type`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型编号',
  `level` int(10) NULL DEFAULT NULL COMMENT '层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父类主键',
  `sequence` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列号',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_type
-- ----------------------------
INSERT INTO `device_type` VALUES (1783766148689506306, '肉类理货机', 'MEAT_TRADE_CATEGORY', 1, NULL, '1783766148689506306-', NULL, 68547128, 0, 'admin', '2024-04-26 15:52:45', 'admin', '2024-04-26 15:52:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_type` VALUES (1783766148718866433, '肉类理货机', 'GH-IN-AIO-S01', 2, 1783766148689506306, '1783766148689506306-1783766148718866433-', NULL, 68547128, 0, 'admin', '2024-04-26 15:52:45', 'admin', '2024-04-26 15:52:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_type` VALUES (1783766148823724034, '进门收费一体机', 'ENTER_CHARGE_CATEGORY', 1, NULL, '1783766148823724034-', NULL, 68547128, 0, 'admin', '2024-04-26 15:52:45', 'admin', '2024-04-26 15:52:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_type` VALUES (1783766148840501250, '进门收费一体机', 'GH-IN-AIO-C01', 2, 1783766148823724034, '1783766148823724034-1783766148840501250-', NULL, 68547128, 0, 'admin', '2024-04-26 15:52:45', 'admin', '2024-04-26 15:52:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_type` VALUES (1783766148945358850, '进门称重一体机', 'ENTER_WEIGHT_CATEGORY', 1, NULL, '1783766148945358850-', NULL, 68547128, 0, 'admin', '2024-04-26 15:52:45', 'admin', '2024-04-26 15:52:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_type` VALUES (1783766149004079105, '进门称重一体机', 'GH-IN-AIO-W01', 2, 1783766148945358850, '1783766148945358850-1783766149004079105-', NULL, 68547128, 0, 'admin', '2024-04-26 15:52:45', 'admin', '2024-04-26 15:52:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_type` VALUES (1783766149075382273, '监控摄像头', 'VIDEO_MONITOR_CATEGORY', 1, NULL, '1783766149075382273-', NULL, 68547128, 0, 'admin', '2024-04-26 15:52:45', 'admin', '2024-04-26 15:52:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `device_type` VALUES (1783766149377372162, '海康DS-2CD3126FWDV3-I', '海康DS-2CD3126FWDV3-I', 2, 1783766149075382273, '1783766149075382273-1783766149377372162-', NULL, 68547128, 0, 'admin', '2024-04-26 15:52:45', 'admin', '2024-04-26 15:52:45') ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for flow
-- ----------------------------

CREATE TABLE IF NOT EXISTS `flow`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程名称',
  `flow_type_id` bigint(20) NULL DEFAULT NULL COMMENT '流程类型',
  `enabled` tinyint(4) NULL DEFAULT NULL COMMENT '可用状态',
  `process_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程键',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of flow
-- ----------------------------

-- ----------------------------
-- Table structure for flow_event
-- ----------------------------

CREATE TABLE IF NOT EXISTS `flow_event`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_id` bigint(20) NULL DEFAULT NULL COMMENT '业务对象主键',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务对象类型',
  `node` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件类型',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '处理人主键',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人账号',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人昵称',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理联系方式',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结果',
  `process_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `duty_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转单对象',
  `score` int(11) NULL DEFAULT NULL COMMENT '评分',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程事件记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of flow_event
-- ----------------------------

-- ----------------------------
-- Table structure for flow_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `flow_type`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `enabled` tinyint(4) NULL DEFAULT 0 COMMENT '可用状态',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of flow_type
-- ----------------------------

-- ----------------------------
-- Table structure for goods_category
-- ----------------------------

CREATE TABLE IF NOT EXISTS `goods_category`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品分类编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品分类名称',
  `level` int(10) UNSIGNED NOT NULL COMMENT '商品分类层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父类主键',
  `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列号',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_goods_category_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品品类信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of goods_category
-- ----------------------------

-- ----------------------------
-- Table structure for iot_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `iot_config`  (
  `id` bigint(20) NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API编号',
  `value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API地址',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of iot_config
-- ----------------------------
INSERT INTO `iot_config` VALUES (1783766150031683586, 68547128, 'IOT_PLATFORM_ADDR', 'http://192.168.0.194:9096/api/', 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `iot_config` VALUES (1783766150065238018, 68547128, 'IOT_PROJECT_URI', 'v2/me/projects?pageNo=%s&pageSize=%s', 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `iot_config` VALUES (1783766150069432322, 68547128, 'IOT_BRAND_URI', 'v2/project/{projectId}/brand', 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `iot_config` VALUES (1783766150069432323, 68547128, 'IOT_MODEL_URI', 'v2/project/{projectId}/prod?brand=%s&category=%s', 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `iot_config` VALUES (1783766150073626626, 68547128, 'IOT_DEVICE_ADD_UPDATE_URI', 'v2/project/{projectId}/device', 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for lease_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `lease_config`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `business_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `lease_flow_id` bigint(20) NULL DEFAULT NULL COMMENT '租赁流程主键',
  `rent_flow_id` bigint(20) NULL DEFAULT NULL COMMENT '退租流程主键',
  `processed` tinyint(1) NULL DEFAULT NULL COMMENT '是否是默认流程',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_business_type_processed_tenant_deleted`(`business_type`, `processed`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租赁配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lease_config
-- ----------------------------

-- ----------------------------
-- Table structure for lease_mode
-- ----------------------------

CREATE TABLE IF NOT EXISTS `lease_mode`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `lease_mode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '承租方式',
  `duration_unit` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时长单位',
  `lease_duration` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租赁时长',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_business_type_lease_mode_tenant_deleted`(`business_type`, `lease_mode`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '承租方式' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lease_mode
-- ----------------------------

-- ----------------------------
-- Table structure for merchant
-- ----------------------------

CREATE TABLE IF NOT EXISTS `merchant`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `tenant` int(10) UNSIGNED NOT NULL DEFAULT 1 COMMENT '租户编号',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商户名称',
  `owner` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `seller` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为卖家',
  `buyer` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为买家',
  `liaison` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号码',
  `area` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '详细地址',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核状态',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `risk_level` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '风险级别',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_merchant_user_id_tenant`(`user_id`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of merchant
-- ----------------------------

-- ----------------------------
-- Table structure for merchant_seft
-- ----------------------------

CREATE TABLE IF NOT EXISTS `merchant_seft`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `merchant_id` bigint(20) NULL DEFAULT NULL,
  `merchant_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家分类：司机、经营者',
  `refer_merchant_id` bigint(20) NULL DEFAULT NULL,
  `auth_purchase_report` bit(1) NULL DEFAULT NULL COMMENT '授权来货报备',
  `auth_wallet_pay` bit(1) NULL DEFAULT NULL COMMENT '授权钱包支付',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of merchant_seft
-- ----------------------------

-- ----------------------------
-- Table structure for merchant_stall
-- ----------------------------

CREATE TABLE IF NOT EXISTS `merchant_stall`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `merchant_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户主键',
  `stall_id` bigint(20) UNSIGNED NOT NULL COMMENT '档位主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户档位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of merchant_stall
-- ----------------------------

-- ----------------------------
-- Table structure for purchaser
-- ----------------------------

CREATE TABLE IF NOT EXISTS `purchaser`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `tenant` int(11) NOT NULL COMMENT '租户编号',
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
  UNIQUE INDEX `idx_purchaser_user_id_tenant`(`user_id`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of purchaser
-- ----------------------------

-- ----------------------------
-- Table structure for purchaser_authentication
-- ----------------------------

CREATE TABLE IF NOT EXISTS `purchaser_authentication`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `purchaser_id` bigint(20) UNSIGNED NOT NULL COMMENT '采购商主键',
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '认证状态',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '认证类型',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '认证类型名称',
  `type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '认证类型编码',
  `delegate_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代表人姓名',
  `delegate_identity_card` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代表人身份证号码',
  `license_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '营业执照文件主键',
  `front_identity_card_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '身份证正面文件主键',
  `back_identity_card_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '身份证反面文件主键',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `source` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源：PC、MP',
  `tenant` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市场租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采购商认证' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of purchaser_authentication
-- ----------------------------

-- ----------------------------
-- Table structure for risk_level
-- ----------------------------

CREATE TABLE IF NOT EXISTS `risk_level`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '风险级别类别',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '风险级别数值',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of risk_level
-- ----------------------------
INSERT INTO `risk_level`
(id, `type`, value, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1783766150404976641, 'LOW', '0', 68547128, 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `risk_level`
(id, `type`, value, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1783766150442725378, 'MEDIUM', '2', 68547128, 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `risk_level`
(id, `type`, value, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1783766150446919681, 'HIGH', '2', 68547128, 0, 'admin', '2024-04-26 15:52:46', 'admin', '2024-04-26 15:52:46') ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for scene_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `scene_type`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型编号',
  `level` int(10) NULL DEFAULT NULL COMMENT '层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父类主键',
  `sequence` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '序列号',
  `sort` int(4) NULL DEFAULT NULL COMMENT '序号',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `scenario_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '三维场景' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of scene_type
-- ----------------------------

-- ----------------------------
-- Table structure for seller_authentication
-- ----------------------------

CREATE TABLE IF NOT EXISTS `seller_authentication`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `merchant_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户主键',
  `goods_category` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品品类',
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '不通过原因',
  `submit_time` datetime NOT NULL COMMENT '提交时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of seller_authentication
-- ----------------------------

-- ----------------------------
-- Table structure for stall
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位名称',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位类型',
  `area` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位区域',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '具体位置',
  `built_up_area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '建筑面积',
  `practical_area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实用面积',
  `dept_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门主键',
  `dept_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `lease_type` tinyint(1) NULL DEFAULT 0 COMMENT '出租类型',
  `leased` tinyint(1) NULL DEFAULT NULL COMMENT '是否出租',
  `bim_bind` tinyint(1) NULL DEFAULT NULL COMMENT '模型绑定',
  `master_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主名称',
  `master_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主手机号',
  `visualization_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可视化模型编号',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `enabled` tinyint(3) UNSIGNED NULL DEFAULT 0 COMMENT '是否启用',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_stall_tenant_deleted_code`(`tenant`, `deleted`, `code`) USING BTREE,
  UNIQUE INDEX `idx_stall_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE,
  UNIQUE INDEX `idx_stall_tenant_deleted_visualization_code`(`tenant`, `deleted`, `visualization_code`) USING BTREE,
  INDEX `booth_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stall
-- ----------------------------

-- ----------------------------
-- Table structure for stall_category
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_category`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位分类名称',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
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
  UNIQUE INDEX `idx_stall_category_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stall_category
-- ----------------------------
INSERT INTO `stall_category`
(id, name, enabled, tenant, deleted, create_by, create_time, update_by, update_time)
VALUES (1795341575217758209, '临棚', 1, 68547128, 0, '17311369782', '2024-05-28 14:29:22', '17311369782', '2024-05-28 14:29:22') ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- ----------------------------
-- Table structure for stall_cost
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_cost`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `stall_id` bigint(20) NULL DEFAULT NULL COMMENT '挡位主键',
  `charge_project_id` bigint(20) NULL DEFAULT NULL COMMENT '收费项目主键',
  `calculation_mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计算方式',
  `unit_price` decimal(10, 8) NULL DEFAULT NULL COMMENT '单价',
  `billing_cycle` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计费周期',
  `amount` decimal(28, 8) NULL DEFAULT NULL COMMENT '金额',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位费用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stall_cost
-- ----------------------------

-- ----------------------------
-- Table structure for stall_lease
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_lease`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `lease_config_id` bigint(20) NULL DEFAULT NULL COMMENT '租赁配置主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据编号',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据状态',
  `progress` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据进度',
  `contract_id` bigint(20) NULL DEFAULT NULL COMMENT '合同主键',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商家主键',
  `merchant_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家名称',
  `merchant_liaison` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家联系人',
  `merchant_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家联系人电话',
  `merchant_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家地址',
  `goods_category_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家主营品种',
  `goods_category_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家主营品种名称',
  `main_source_goods_addaress` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主要货源地',
  `average_day_count` int(11) NULL DEFAULT NULL COMMENT '日均交易量',
  `average_day_money` decimal(28, 8) NULL DEFAULT NULL COMMENT '日均交易额',
  `own_brand` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自有品牌',
  `own_base` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自有基地',
  `lease_count` int(11) NULL DEFAULT NULL COMMENT '租赁数量',
  `lease_purpose` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租赁用途',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位租赁信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stall_lease
-- ----------------------------

-- ----------------------------
-- Table structure for stall_rent
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_rent`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `lease_config_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '租赁配置主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据编号',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据状态',
  `progress` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据进度',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商家主键',
  `merchant_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家名称',
  `merchant_liaison` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家联系人',
  `merchant_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家联系人电话',
  `merchant_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家地址',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位退租信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stall_rent
-- ----------------------------

-- ----------------------------
-- Table structure for stall_rent_contract
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_rent_contract`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `contract_id` bigint(20) NULL DEFAULT NULL COMMENT '合同主键',
  `stall_rent_id` bigint(20) NULL DEFAULT NULL COMMENT '档位退租信息主键',
  `rent_time` date NULL DEFAULT NULL COMMENT '退租时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退租原因',
  `total_money` decimal(28, 8) NULL DEFAULT NULL COMMENT '退费总额',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位退租合同' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stall_rent_contract
-- ----------------------------

-- ----------------------------
-- Table structure for stall_rent_contract_target
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_rent_contract_target`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `contract_id` bigint(20) NULL DEFAULT NULL COMMENT '合同主键',
  `contract_lease_id` bigint(20) NULL DEFAULT NULL COMMENT '合同租赁信息主键',
  `contract_lease_target_id` bigint(20) NULL DEFAULT NULL COMMENT '合同租赁对象信息主键',
  `stall_rent_contract_id` bigint(20) NULL DEFAULT NULL COMMENT '档位退租关联合同主键',
  `stall_id` bigint(20) NULL DEFAULT NULL COMMENT '档位主键',
  `stall_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位名称',
  `stall_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位编号',
  `total_money` decimal(28, 8) NULL DEFAULT NULL COMMENT '总金额',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备设施情况',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位退租对象信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stall_rent_contract_target
-- ----------------------------

-- ----------------------------
-- Table structure for stall_rent_cost_detail
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stall_rent_cost_detail`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `contract_id` bigint(20) NULL DEFAULT NULL COMMENT '合同主键',
  `contract_lease_id` bigint(20) NULL DEFAULT NULL COMMENT '合同租赁信息主键',
  `contract_cost_detail` bigint(20) NULL DEFAULT NULL COMMENT '合同费用明细主键',
  `stall_rent_contract_target_id` bigint(20) NULL DEFAULT NULL COMMENT '档位退租对象信息主键',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用状态',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '退费方信息主键',
  `merchant_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退费方(商家)名称',
  `charge_project_id` bigint(20) NULL DEFAULT NULL COMMENT '收费项目主键',
  `charge_project_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收费项目名称',
  `calculation_mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计算方式',
  `unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `period_number` int(11) NULL DEFAULT NULL COMMENT '期数',
  `period_start_time` date NULL DEFAULT NULL COMMENT '周期开始时间',
  `period_end_time` date NULL DEFAULT NULL COMMENT '周期结束时间',
  `project_money` decimal(28, 8) NULL DEFAULT NULL COMMENT '缴费金额',
  `billing_cycle` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计费周期',
  `billing_cycle_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计费周期名称',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位退租费用明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stall_rent_cost_detail
-- ----------------------------

-- ----------------------------
-- Table structure for stream_media_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stream_media_config`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `ip_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外网ip地址',
  `intranet_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内网地址',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `encrypt_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加密后的密码',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
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
  UNIQUE INDEX `idx_name_tenant_deleted`(`name`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stream_media_config
-- ----------------------------

-- ----------------------------
-- Table structure for supplier
-- ----------------------------

CREATE TABLE IF NOT EXISTS `supplier`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商家名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商家编号',
  `admin_user_id` bigint(20) NULL DEFAULT NULL COMMENT '管理员用户主键',
  `admin_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员用户名称',
  `admin_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员手机号',
  `liaison` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人手机号',
  `area` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在地区',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `category` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主营品种',
  `risk_level` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '风险级别',
  `risk_amount` int(11) NOT NULL DEFAULT 0 COMMENT '风险次数',
  `supplier_tenant` int(11) NOT NULL COMMENT '批发商租户编号',
  `tenant` int(11) NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `shift_master` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主',
  `shift_master_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主联系电话',
  `shift_master_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主身份证',
  `other_contacts` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其他联系人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_supplier_tenant_tenant_deleted`(`supplier_tenant`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '批发商表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of supplier
-- ----------------------------

-- ----------------------------
-- Table structure for supplier_authentication
-- ----------------------------

CREATE TABLE IF NOT EXISTS `supplier_authentication`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '批发商主键',
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '认证状态',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '认证类型',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '认证类型名称',
  `type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '认证类型编码',
  `delegate_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代表人姓名',
  `delegate_identity_card` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代表人身份证号码',
  `license_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '营业执照文件主键',
  `front_identity_card_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '身份证正面文件主键',
  `back_identity_card_Id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '身份证反面文件主键',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `source` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源：PC、MP',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '批发商认证表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of supplier_authentication
-- ----------------------------

-- ----------------------------
-- Table structure for supplier_stall
-- ----------------------------

CREATE TABLE IF NOT EXISTS `supplier_stall`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '批发商主键',
  `stall_id` bigint(20) UNSIGNED NOT NULL COMMENT '档位主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '批发商档位表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of supplier_stall
-- ----------------------------

-- ----------------------------
-- Table structure for supplier_user
-- ----------------------------

CREATE TABLE IF NOT EXISTS `supplier_user`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户主键',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `supplier_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家分类：司机、经营者',
  `supplier_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家分类联系人名称',
  `supplier_category_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家分类联系人电话',
  `supplier_stall_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家分类档位',
  `supplier_stall_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家分类档位名称',
  `auth_purchase_report` bit(1) NOT NULL DEFAULT b'1' COMMENT '授权来货报备',
  `auth_wallet_pay` bit(1) NOT NULL DEFAULT b'1' COMMENT '授权余额支付',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_merchant_user_supplier_id_user_id_supplier_category`(`supplier_id`, `user_id`, `supplier_category`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户用户关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of supplier_user
-- ----------------------------

-- ----------------------------
-- Table structure for universal_panel
-- ----------------------------

CREATE TABLE IF NOT EXISTS `universal_panel`  (
  `id` bigint(20) NOT NULL,
  `table_one_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图表一名称',
  `table_one` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图表一',
  `table_two_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图表二名称',
  `table_two` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图表二',
  `table_three_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图表三名称',
  `table_three` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图表三',
  `business_id` bigint(20) NULL DEFAULT NULL COMMENT '业务建',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of universal_panel
-- ----------------------------

-- ----------------------------
-- Table structure for weixin_pay_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `weixin_pay_config`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用ID',
  `app_secret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用密码',
  `mch_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '直连商户号',
  `notify_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付回调地址',
  `api_v3_Key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'API v3密钥',
  `mch_serial_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户证书序列号',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '是否删除',
   `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_tenant_deleted`(`tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信支付配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of weixin_pay_config
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
