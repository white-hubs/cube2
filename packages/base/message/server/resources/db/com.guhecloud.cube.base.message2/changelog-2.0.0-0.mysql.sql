-- liquibase formatted sql

-- changeset qqq:1686103358461-1

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for msg_channel
-- ----------------------------
DROP TABLE IF EXISTS `msg_channel`;
CREATE TABLE `msg_channel`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息通道' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for msg_channel_manufacturer
-- ----------------------------
DROP TABLE IF EXISTS `msg_channel_manufacturer`;
CREATE TABLE `msg_channel_manufacturer`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通道编号',
  `manufacturer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '厂家编号',
  `account_rule` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号规则',
  `account_rule_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '账号规则定义描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通道关联厂商' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for msg_manufacturer
-- ----------------------------
DROP TABLE IF EXISTS `msg_manufacturer`;
CREATE TABLE `msg_manufacturer`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息厂家' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for msg_record
-- ----------------------------
DROP TABLE IF EXISTS `msg_record`;
CREATE TABLE `msg_record`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `msg_scene_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息场景编号',
  `msg_scene_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息场景名称',
  `msg_content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `msg_tmp_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '消息模板主键',
  `msg_tmp_version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息模板版本',
  `msg_params` varchar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息参数',
  `msg_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息类型',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务主键',
  `business_principal` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '业务主体信息',
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息通道编号',
  `manufacturer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂家编号',
  `send_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送状态',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `effective_days` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '有效天数',
  `expiration_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for msg_record_target
-- ----------------------------
DROP TABLE IF EXISTS `msg_record_target`;
CREATE TABLE `msg_record_target`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道编号',
  `msg_record_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '消息记录主键',
  `user_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '用户主键',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `user_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `user_email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `receiver` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收者标识(可能是手机号,用户id,openId等)',
  `send_status` tinyint(1) NULL DEFAULT NULL COMMENT '是否发送成功',
  `send_status_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送状态描述',
  `error_describe` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误信息',
  `has_read` tinyint(1) NULL DEFAULT NULL COMMENT '是否已读',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `send_times` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '发送次数',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_msg_record_id_channel_type_user_id_deleted_tenant`(`msg_record_id`, `channel_code`, `user_id`, `deleted`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for msg_record_terminal
-- ----------------------------
DROP TABLE IF EXISTS `msg_record_terminal`;
CREATE TABLE `msg_record_terminal`  (
  `id` bigint(20) NOT NULL,
  `msg_record_id` bigint(20) NULL DEFAULT NULL COMMENT '消息记录主键',
  `terminal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端编号',
  `terminal_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端名称',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for msg_scene
-- ----------------------------
DROP TABLE IF EXISTS `msg_scene`;
CREATE TABLE `msg_scene`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息名称',
  `msg_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景消息类型',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_code_deleted`(`code`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '全局消息场景定义' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for msg_scene_url
-- ----------------------------
DROP TABLE IF EXISTS `msg_scene_url`;
CREATE TABLE `msg_scene_url`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `msg_scene_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息场景编号',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `terminal` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端编号',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_sat_msg_define_code_application_code_terminal_deleted`(`msg_scene_code`, `application_code`, `terminal`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息场景跳转路径配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for msg_tmp
-- ----------------------------
DROP TABLE IF EXISTS `msg_tmp`;
CREATE TABLE `msg_tmp`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `channel_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道编号',
  `msg_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息类型',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
  `msg_scene_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型编号',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属应用编号',
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属应用名称',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '全局消息模板基础数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_msg_channel
-- ----------------------------
DROP TABLE IF EXISTS `sat_msg_channel`;
CREATE TABLE `sat_msg_channel`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通道编号',
  `channel_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道名称',
  `sat_msg_manufacturer_id` bigint(20) NOT NULL COMMENT '供应商配置主键',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_channel_code_deleted_tenant`(`channel_code`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户通道配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_msg_manufacturer
-- ----------------------------
DROP TABLE IF EXISTS `sat_msg_manufacturer`;
CREATE TABLE `sat_msg_manufacturer`  (
  `id` bigint(20) NOT NULL,
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道编号',
  `manufacturer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂家编号',
  `config` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂家配置信息',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户供应商配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_msg_scene_receive
-- ----------------------------
DROP TABLE IF EXISTS `sat_msg_scene_receive`;
CREATE TABLE `sat_msg_scene_receive`  (
  `id` bigint(20) NOT NULL,
  `msg_scene_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景编号',
  `msg_scene_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景名称',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `receive_mode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收模式',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_scene_code_tenant_deleted`(`msg_scene_code`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户消息场景配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_msg_scene_receive_channel
-- ----------------------------
DROP TABLE IF EXISTS `sat_msg_scene_receive_channel`;
CREATE TABLE `sat_msg_scene_receive_channel`  (
  `id` bigint(20) NOT NULL,
  `sat_msg_scene_receive_id` bigint(20) NULL DEFAULT NULL COMMENT '场景接收配置主键',
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道编号',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户消息场景渠道配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_msg_scene_receive_user
-- ----------------------------
DROP TABLE IF EXISTS `sat_msg_scene_receive_user`;
CREATE TABLE `sat_msg_scene_receive_user`  (
  `id` bigint(20) NOT NULL,
  `sat_msg_scene_receive_id` bigint(20) NULL DEFAULT NULL COMMENT '场景接收配置主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户主键',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户消息场景接收人配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_msg_tmp
-- ----------------------------
DROP TABLE IF EXISTS `sat_msg_tmp`;
CREATE TABLE `sat_msg_tmp`  (
  `id` bigint(20) NOT NULL,
  `msg_tmp_id` bigint(20) NULL DEFAULT NULL COMMENT '运营端模板主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道编号',
  `msg_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息类型',
  `enabled` tinyint(4) NULL DEFAULT NULL COMMENT '是否启用',
  `msg_scene_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景编号',
  `msg_scene_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景名称',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户消息模板配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_msg_tmp_manufacturer
-- ----------------------------
DROP TABLE IF EXISTS `sat_msg_tmp_manufacturer`;
CREATE TABLE `sat_msg_tmp_manufacturer`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `sat_msg_tmp_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '模板主键',
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道编号',
  `sat_msg_manufacturer_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '租户厂家配置主键',
  `manufacturer_tmp_config` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户模板配置',
  `manufacturer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂家编号',
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `status_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态描述',
  `tenant` int(11) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户模板通道配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sat_msg_tmp_version
-- ----------------------------
DROP TABLE IF EXISTS `sat_msg_tmp_version`;
CREATE TABLE `sat_msg_tmp_version`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '模板主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `channel_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道编号',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
  `msg_scene_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型编号',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属应用编号',
  `application_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属应用名称',
  `manufacturer_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂商编号',
  `manufacturer_tmp_config` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂商模板配置',
  `manufacturer_tmp_schema` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '厂商模板校验后的配置',
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息模板配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_channel
-- ----------------------------
INSERT INTO `msg_channel` VALUES (1, 'CHANNEL_SITE', '站内信');
INSERT INTO `msg_channel` VALUES (2, 'CHANNEL_SMS', '短信');
INSERT INTO `msg_channel` VALUES (3, 'CHANNEL_APP', 'APP推送');
INSERT INTO `msg_channel` VALUES (4, 'CHANNEL_VOICE', '语音通知');

-- ----------------------------
-- Records of msg_manufacturer
-- ----------------------------
INSERT INTO `msg_manufacturer` VALUES (1, 'GUHE_YUN', '古河云');
INSERT INTO `msg_manufacturer` VALUES (2, 'JI_GUANG', '极光');
INSERT INTO `msg_manufacturer` VALUES (3, 'ALI_YUN', '阿里云');

-- ----------------------------
-- Records of msg_channel_manufacturer
-- ----------------------------
INSERT INTO `msg_channel_manufacturer` VALUES (1527972132965060610, 'CHANNEL_SITE', 'GUHE_YUN', NULL, NULL);
INSERT INTO `msg_channel_manufacturer` VALUES (1527972132965060611, 'CHANNEL_SMS', 'JI_GUANG', '{\"title\":\"极光短信发送配置\",\"description\":\"极光短信发送配置\",\"type\":\"object\",\"properties\":{\"appKey\":{\"type\":\"string\"},\"secret\":{\"type\":\"string\"}},\"required\": [\"appKey\",\"secret\"]}', NULL);
INSERT INTO `msg_channel_manufacturer` VALUES (1527972133023780865, 'CHANNEL_APP', 'JI_GUANG', '{\"title\":\"极光推送发送配置\",\"description\":\"极光推送发送配置\",\"type\":\"object\",\"properties\":{\"appKey\":{\"type\":\"string\"},\"secret\":{\"type\":\"string\"}},\"required\": [\"appKey\",\"secret\"]}', NULL);
INSERT INTO `msg_channel_manufacturer` VALUES (1542388323491975170, 'CHANNEL_VOICE', 'ALI_YUN', '{\"title\":\"阿里语音发送配置\",\"description\":\"阿里语音发送配置\",\"type\":\"object\",\"properties\":{\"accessKeyId\":{\"type\":\"string\"},\"accessKeySecret\":{\"type\":\"string\"}},\"required\": [\"accessKeyId\", \"accessKeySecret\"]}', NULL);

-- ----------------------------
-- Records of msg_scene
-- ----------------------------
INSERT INTO `msg_scene` VALUES (0, 'MAINTAIN_MAINTANCE_RECEIPT_CONFIRM', '维保任务通知', NULL, 'MAINTENACE', '维保作业', 0, '1', '2022-07-27 10:05:51', '1', '2022-07-27 10:05:51');
INSERT INTO `msg_scene` VALUES (146574656541313, 'MAINTAIN_HIDDENDANGER_MSG_CONFIRM', '隐患确认通知', NULL, 'HIDDENDANGER', '隐患排查', 0, '1', '2022-07-27 09:54:22', '1', '2022-07-27 09:54:22');
INSERT INTO `msg_scene` VALUES (1457156742145151191, 'TASKS_TODO_MSG', '通知公告消息', NULL, 'TASKS_TODO', '通知公告', 0, '1', '2022-08-15 11:53:39', '1', '2022-08-15 11:53:48');
INSERT INTO `msg_scene` VALUES (1461236295383699455, 'PRICE_WARN_PUBLISHED', '采价预警提醒', NULL, 'GOODS_PRICING', '商品采价', 0, 'admin', '2023-06-07 15:25:57', 'admin', '2023-06-07 15:26:01');
INSERT INTO `msg_scene` VALUES (1461236295383699456, 'PRICING_TASK_PUBLISHED', '采价任务提醒', NULL, 'GOODS_PRICING', '商品采价', 0, '13980439583', '2023-06-07 11:13:14', '13980439583', '2023-06-07 11:13:16');
INSERT INTO `msg_scene` VALUES (1462724672721276845, 'MAINTAIN_OUTSOURCE_REPAIR_HANDLED', '委外维修工单已处理通知', NULL, 'OUTSOURCE_REPAIR', '委外维修', 0, '13980439583', '2022-06-29 11:07:31', '13980439583', '2022-06-29 11:07:31');
INSERT INTO `msg_scene` VALUES (1462724672721276846, 'MAINTAIN_OUTSOURCE_REPAIR_RECEIVED', '委外维修工单已接收通知', NULL, 'OUTSOURCE_REPAIR', '委外维修', 0, '13980439583', '2022-06-29 11:07:31', '13980439583', '2022-06-29 11:07:31');
INSERT INTO `msg_scene` VALUES (1462724672721276847, 'MAINTAIN_OUTSOURCE_REPAIR_CONFIRM', '委外维修确认通知', NULL, 'OUTSOURCE_REPAIR', '委外维修', 0, '13980439583', '2022-06-29 11:07:31', '13980439583', '2022-06-29 11:07:31');
INSERT INTO `msg_scene` VALUES (1462724672721276925, 'MAINTAIN_RETROFIT_RECEIPT_ASSESSMENT', '更新改造评估通知', NULL, 'RETROFIT', '更新改造', 0, '1', '2022-07-20 14:20:41', '1', '2022-07-20 14:20:41');
INSERT INTO `msg_scene` VALUES (1462724672721276926, 'MAINTAIN_RETROFIT_RECEIPT_CONFIRM', '更新改造确认通知', NULL, 'RETROFIT', '更新改造', 0, '1', '2022-07-20 14:20:41', '1', '2022-07-20 14:20:41');
INSERT INTO `msg_scene` VALUES (1462724672721276927, 'MAINTAIN_OUTSOURCE_REPAIR_SOLVE', '委外维修处理工单通知', NULL, 'OUTSOURCE_REPAIR', '委外维修', 0, '13980439583', '2022-06-29 11:07:31', '13980439583', '2022-06-29 11:07:31');
INSERT INTO `msg_scene` VALUES (1462724672721276928, 'MAINTAIN_OUTSOURCE_REPAIR_DISTRIBUTED', '委外维修派发工单通知', NULL, 'OUTSOURCE_REPAIR', '委外维修', 0, '13980439583', '2022-06-25 13:51:01', '13980439583', '2022-06-25 13:51:01');
INSERT INTO `msg_scene` VALUES (1462724672721276930, 'ENERGY_ALARM', '能耗报警通知', NULL, 'ENERGY_MANAGE', '能耗管理', 0, '1', '2022-07-25 10:23:41', '1', '2022-07-25 10:23:41');
INSERT INTO `msg_scene` VALUES (1462724672721276931, 'DEVICE_HEALTH_ALARM', '设备健康报警通知', NULL, 'EQUIPMENT_SUPERVISION', '设备监管', 0, '1', '2022-07-25 10:24:36', '1', '2022-07-25 10:24:36');
INSERT INTO `msg_scene` VALUES (1470587369957724161, 'ENTER_CHARGE_PAYMENT_SUCCESS_BALANCE', '支付完成(余额支付)', NULL, '1455126663468806145', '进场收费', 0, '13980439583', '2021-12-14 10:52:06', '13980439583', '2021-12-14 10:52:06');
INSERT INTO `msg_scene` VALUES (1470633190896013314, 'MERCHANT_RECHARGE_SUCCESS', '充值成功', NULL, '1455126499844812802', '商家管理', 0, '13980439583', '2021-12-14 13:54:10', '13980439583', '2021-12-14 13:54:10');
INSERT INTO `msg_scene` VALUES (1470633398870577154, 'MERCHANT_WITHDRAWAL_SUCCESS', '提现通知', NULL, '1455126499844812802', '商家管理', 0, '13980439583', '2021-12-14 13:55:00', '13980439583', '2021-12-14 13:55:00');
INSERT INTO `msg_scene` VALUES (1470633398870577155, 'MERCHANT_WITHDRAWAL_FAIL', '提现通知', NULL, '1455126499844812802', '商家管理', 0, '13980439583', '2021-12-14 13:55:00', '13980439583', '2021-12-14 13:55:00');
INSERT INTO `msg_scene` VALUES (1470633398870577156, 'MERCHANT_WITHDRAWAL_FINISH', '提现通知', NULL, '1455126499844812802', '商家管理', 0, '13980439583', '2021-12-14 13:55:00', '13980439583', '2021-12-14 13:55:00');
INSERT INTO `msg_scene` VALUES (1470636245066448898, 'MEATTRADE_PROK_REPORT_ADOPT', '报备审核通过商家通知', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 14:06:18', '13980439583', '2021-12-14 14:06:18');
INSERT INTO `msg_scene` VALUES (1470636375685464065, 'MEATTRADE_PROK_REPORT_FAIL_ADOPT', '报备审核不通过商家通知', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 14:06:50', '13980439583', '2021-12-14 14:06:50');
INSERT INTO `msg_scene` VALUES (1470637071398862850, 'MEATTRADE_TALLY_FAIL_ADOPT', '理货审核不通过商家通知', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 14:09:36', '13980439583', '2021-12-14 14:09:36');
INSERT INTO `msg_scene` VALUES (1470637122472902657, 'MEATTRADE_TALLY_ADOPT', '理货审核通过商家通知', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 14:09:48', '13980439583', '2021-12-14 14:09:48');
INSERT INTO `msg_scene` VALUES (1470637542226264066, 'MEATTRADE_ORDER_REMIND', '订单提醒', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 14:11:28', '13980439583', '2021-12-14 14:11:28');
INSERT INTO `msg_scene` VALUES (1470638141642637314, 'MEATTRADE_ORDER_PAYMENT_SUCCESS', '订单支付完成', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 14:13:51', '13980439583', '2021-12-14 14:13:51');
INSERT INTO `msg_scene` VALUES (1470638300879388673, 'MEATTRADE_ORDER_SETTLEMENT_SUCCESS', '订单结算完成', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 14:14:29', '13980439583', '2021-12-14 14:14:29');
INSERT INTO `msg_scene` VALUES (1470677444561231873, 'MEATTRADE_SUPPLIER_PASSWORD_SET', '密码设置完成', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 16:50:01', '13980439583', '2021-12-14 16:50:01');
INSERT INTO `msg_scene` VALUES (1485792363335565673, 'MAINTAIN_HIDDENDANGER_MSG_CLOSE', '隐患关闭通知', NULL, 'HIDDENDANGER', '隐患排查', 0, '1', '2022-07-27 09:56:41', '1', '2022-07-27 09:56:41');
INSERT INTO `msg_scene` VALUES (1534546384588798116, 'MAINTAIN_FAULT_MSG', '故障通知', NULL, 'FAULT', '故障报修', 0, '1', '2022-07-19 16:54:32', '1', '2022-07-19 16:54:32');
INSERT INTO `msg_scene` VALUES (1544493856882524162, 'ONE_DEVICE_ALARM', '设备报警通知', NULL, 'EQUIPMENT_SUPERVISION', '设备监管', 0, '15281496976', '2022-07-06 09:30:05', '15281496976', '2022-07-06 09:30:05');
INSERT INTO `msg_scene` VALUES (1544493856882524163, 'LINKAGE_CONDITION_EVENT', '条件联动', NULL, 'LINKAGE_STRATEGY', '联动控制', 0, '1', '2022-07-18 11:44:29', '1', '2022-07-18 11:44:32');
INSERT INTO `msg_scene` VALUES (1544493856882524164, 'LINKAGE_SCENE_EVENT', '场景联动', NULL, 'LINKAGE_STRATEGY', '联动控制', 0, '1', '2022-07-18 11:44:29', '1', '2022-07-18 11:44:32');
INSERT INTO `msg_scene` VALUES (1544493856882524165, 'LINKAGE_TIMING_EVENT', '定时联动', NULL, 'LINKAGE_STRATEGY', '联动控制', 0, '1', '2022-07-18 11:44:29', '1', '2022-07-18 11:44:32');
INSERT INTO `msg_scene` VALUES (1544564545645413146, 'MAINTAIN_FAULT_MSG_CONFIRM', '故障确认通知', NULL, 'FAULT', '故障报修', 0, '1', '2022-07-20 10:46:17', '1', '2022-07-20 10:46:17');
INSERT INTO `msg_scene` VALUES (1746411346643656212, 'INSPECT_TASK_MSG_EXECUTING', '设备巡检任务待处理通知', NULL, 'INSPECTION', '日常巡检', 0, '1', '2022-07-20 10:48:58', '1', '2022-07-20 10:48:58');
INSERT INTO `msg_scene` VALUES (1746411346643656279, 'MAINTAIN_FAULT_MSG_CLOSE', '故障关闭通知', NULL, 'FAULT', '故障报修', 0, '1', '2022-07-20 10:48:58', '1', '2022-07-20 10:48:58');
INSERT INTO `msg_scene` VALUES (1746411346643656288, 'PROCESS_TASK_HANDLED', '流程任务处理完成通知', NULL, 'BUSINESS_CONFIG', '业务设置', 0, '1', '2022-07-20 10:48:58', '1', '2022-07-20 10:48:58');
INSERT INTO `msg_scene` VALUES (1746411346643656289, 'PROCESS_TASK_WAIT_HANDLE', '流程任务待处理通知', NULL, 'BUSINESS_CONFIG', '业务设置', 0, '1', '2022-07-20 10:48:58', '1', '2022-07-20 10:48:58');
INSERT INTO `msg_scene` VALUES (1746411346643656290, 'TASK_COPY', '任务抄送', NULL, 'FLOW', '流程中心', 0, '1', '2022-11-01 10:39:01', '1', '2022-11-01 10:39:01');
INSERT INTO `msg_scene` VALUES (1746411346643656291, 'PROCESS_UNDO', '流程撤销', NULL, 'FLOW', '流程中心', 0, '1', '2022-11-01 10:40:42', '1', '2022-11-01 10:40:42');
INSERT INTO `msg_scene` VALUES (1746411346643656292, 'TASK_TRANSFER', '任务转交', NULL, 'FLOW', '流程中心', 0, '1', '2022-11-01 10:41:52', '1', '2022-11-01 10:41:52');
INSERT INTO `msg_scene` VALUES (1746411346643656293, 'APPROVAL_PASSED_START_USER', '审批通过通知发起人', NULL, 'FLOW', '流程中心', 0, '1', '2022-11-01 10:43:37', '0', '2022-11-01 10:43:37');
INSERT INTO `msg_scene` VALUES (1746411346643656294, 'TASK_BACKLOG', '任务待办', NULL, 'FLOW', '流程中心', 0, '1', '2022-11-01 10:49:21', '1', '2022-11-01 10:49:21');
INSERT INTO `msg_scene` VALUES (1746411346643656295, 'TASK_REFUSE', '任务驳回', NULL, 'FLOW', '流程中心', 0, '1', '2022-11-01 11:04:11', '1', '2022-11-01 11:04:11');
INSERT INTO `msg_scene` VALUES (1746411346643656296, 'EMERGENCY_ALARM', '预案报警抄送通知', 'ALARM_NOTICE', 'EMERGENCY_CENTER', '预案中心', 0, '1', '2022-11-04 09:36:09', '1', '2022-11-04 09:36:09');
INSERT INTO `msg_scene` VALUES (1746411346643656297, 'EMERGENCY_ALARM_TIMEOUT', '预案报警超时抄送通知', 'ALARM_NOTICE', 'EMERGENCY_CENTER', '预案中心', 0, '1', '2022-11-04 09:37:51', '1', '2022-11-04 09:37:51');
INSERT INTO `msg_scene` VALUES (1746411346643656298, 'MAINTENANCE_OVERDUE', '资产维保到期通知', NULL, 'PROPERTY_MANAGEMENT', '资产管理', 0, '1', '2023-04-19 15:53:36', '1', '2023-04-19 15:53:36');
INSERT INTO `msg_scene` VALUES (1746411346643657111, 'TASK_GENERATE', '任务生成通知', NULL, 'INSPECTION', '日常巡检', 0, '1', '2023-09-06 15:11:42', '1', '2023-09-06 15:11:50');
INSERT INTO `msg_scene` VALUES (1746411346643657112, 'TASK_LAPSE', '任务失效通知', NULL, 'INSPECTION', '日常巡检', 0, '1', '2023-09-06 15:11:44', '1', '2023-09-06 15:11:52');
INSERT INTO `msg_scene` VALUES (1746411346643657113, 'TASK_LAPSE_UNFINISHED', '任务失效未完成', NULL, 'INSPECTION', '日常巡检', 0, '1', '2023-09-06 15:11:46', '1', '2023-09-06 15:11:53');
INSERT INTO `msg_scene` VALUES (1746411346643657487, 'CONSUMABLE', '耗材库存', NULL, 'PROPERTY_MANAGEMENT', '资产管理', 0, '1', '2023-04-24 20:27:21', '1', '2023-04-24 20:27:21');
INSERT INTO `msg_scene` VALUES (1746411346643657488, 'OPERATE_OVERDUE', '资产使用到期', NULL, 'PROPERTY_MANAGEMENT', '资产管理', 0, '1', '2023-04-24 20:27:21', '1', '2023-04-24 20:27:21');
INSERT INTO `msg_scene` VALUES (1746411346643657489, 'ELECTRON_TASK_RECEIVE', '任务接收到期提醒', NULL, 'ELECTRONIC_PATROL', '电子巡更', 0, '1', '2023-04-24 20:27:21', '1', '2023-04-24 20:27:21');
INSERT INTO `msg_scene` VALUES (1746411346643657490, 'ELECTRON_TASK_EXPIRE', '任务到期提醒', NULL, 'ELECTRONIC_PATROL', '电子巡更', 0, '1', '2023-04-24 20:27:21', '1', '2023-04-24 20:27:21');
INSERT INTO `msg_scene` VALUES (1746411346643657491, 'ELECTRON_TASK_ABNORMAL', '任务异常提醒', NULL, 'ELECTRONIC_PATROL', '电子巡更', 0, '1', '2023-04-24 20:27:21', '1', '2023-04-24 20:27:21');
INSERT INTO `msg_scene` VALUES (1746411346643657492, 'PROPERTY_TASK', '资产、耗材盘点任务通知', NULL, 'PROPERTY_MANAGEMENT', '资产管理', 0, '1', '2023-04-24 20:27:21', '1', '2023-04-24 20:27:21');
INSERT INTO `msg_scene` VALUES (1746411346643658002, 'EXAM_TASK_PUBLISHED_CHARGE_NOTIFY', '考试任务发布负责人通知', NULL, 'SECURE_EDU', '安全教育', 0, '1', '2023-09-01 10:25:51', '1', '2023-09-01 10:25:55');
INSERT INTO `msg_scene` VALUES (1746411346643658003, 'EXAM_TASK_PUBLISHED_USER_NOTIFY', '考试任务发布考试人员通知', NULL, 'SECURE_EDU', '安全教育', 0, '1', '2023-09-01 10:26:27', '1', '2023-09-01 10:26:31');
INSERT INTO `msg_scene` VALUES (1746411346643658004, 'CULTIVATE_TASK_PUBLISHED_USER_NOTIFY', '培训任务发布培训人员通知', NULL, 'SECURE_EDU', '安全教育', 0, '1', '2023-09-01 10:26:27', '1', '2023-09-01 10:26:31');
INSERT INTO `msg_scene` VALUES (1746411346643658005, 'CULTIVATE_TASK_PUBLISHED_CHARGE_NOTIFY', '培训任务发布负责人通知', NULL, 'SECURE_EDU', '安全教育', 0, '1', '2023-09-01 10:26:27', '1', '2023-09-01 10:26:31');
INSERT INTO `msg_scene` VALUES (1746411346643658006, 'MERCHANT_AUTH_ADOPT_MARKET_NOTIFY', '商户认证通过市场通知', NULL, 'SJL', '商家管理', 0, '13980439583', '2021-11-18 15:34:16', '13980439583', '2021-11-18 15:34:16');
INSERT INTO `msg_scene` VALUES (1746411346643658007, 'MERCHANT_AUTH_ADOPT_SUPPLIER_NOTIFY', '商户认证通过商家通知', NULL, 'SJL', '商家管理', 0, '13980439583', '2021-11-18 15:34:16', '13980439583', '2021-11-18 15:34:16');
INSERT INTO `msg_scene` VALUES (1746411346643658008, 'MERCHANT_STAY_AUDIT_MARKET_NOTIFY', '商户待审核市场通知', NULL, 'SJL', '商家管理', 0, '13980439583', '2021-11-22 18:08:33', '13980439583', '2021-11-22 18:08:33');
INSERT INTO `msg_scene` VALUES (1746411346643658009, 'MERCHANT_STAY_AUDIT_SUPPLIER_NOTIFY', '商户待审核商家通知', NULL, 'SJL', '商家管理', 0, '13980439583', '2021-11-22 18:08:33', '13980439583', '2021-11-22 18:08:33');
INSERT INTO `msg_scene` VALUES (1746411346643658010, 'MERCHANT_FAIL_ADOPT_MARKET_NOTIFY', '商户认证不通过市场通知', NULL, 'SJL', '商家管理', 0, '13980439583', '2021-11-22 18:09:24', '13980439583', '2021-11-22 18:09:24');
INSERT INTO `msg_scene` VALUES (1746411346643658011, 'MERCHANT_FAIL_ADOPT_SUPPLIER_NOTIFY', '商户认证不通过商家通知', NULL, 'SJL', '商家管理', 0, '13980439583', '2021-11-22 18:09:24', '13980439583', '2021-11-22 18:09:24');
INSERT INTO `msg_scene` VALUES (1746411346643658012, 'MEATTRADE_CUSTOMER_ORDER_CANCEL_CUSTOMER_NOTIFY', '买家订单取消买家通知', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 14:11:52', '13980439583', '2021-12-14 14:11:52');
INSERT INTO `msg_scene` VALUES (1746411346643658013, 'MEATTRADE_CUSTOMER_ORDER_CANCEL_SUPPLIER_NOTIFY', '买家订单取消商家通知', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 15:11:29', '13980439583', '2021-12-14 15:11:29');
INSERT INTO `msg_scene` VALUES (1746411346643658014, 'MEATTRADE_SUPPLIER_ORDER_CANCEL_CUSTOMER_NOTIFY', '卖家订单取消买家通知', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 14:11:52', '13980439583', '2021-12-14 14:11:52');
INSERT INTO `msg_scene` VALUES (1746411346643658015, 'MEATTRADE_SUPPLIER_ORDER_CANCEL_SUPPLIER_NOTIFY', '卖家订单取消商家通知', NULL, 'MEAT_TRADE', '肉类交易', 0, '13980439583', '2021-12-14 15:11:29', '13980439583', '2021-12-14 15:11:29');
INSERT INTO `msg_scene` VALUES (1746411346643658016, 'PROJECT_PLAN_DEADLINE', '项目计划截至提醒', NULL, 'XRXPROGECT', '项目管理', 0, '13980439583', '2021-12-14 15:11:29', '13980439583', '2021-12-14 15:11:29');
INSERT INTO `msg_scene` VALUES (1746411346643658017, 'PROJECT_REPORT', '项目汇报提醒', NULL, 'XRXPROGECT', '项目管理', 0, '13980439583', '2021-12-14 15:11:29', '13980439583', '2021-12-14 15:11:29');
INSERT INTO `msg_scene` VALUES (1746411346643658018, 'ALARM_ASSIGN_NOTIFY', '报警任务通知', 'ALARM_NOTICE', 'EMERGENCY_CENTER', '预案中心', 0, '13980439583', '2021-12-14 15:11:29', '13980439583', '2021-12-14 15:11:29');
INSERT INTO `msg_scene` VALUES (1746411346643658019, 'ALARM_TIMEOUT_NOTIFY', '报警任务超时通知', 'ALARM_NOTICE', 'EMERGENCY_CENTER', '预案中心', 0, '13980439583', '2021-12-14 15:11:29', '13980439583', '2021-12-14 15:11:29');
INSERT INTO `msg_scene` VALUES (1746411346643658020, 'SAFETY_ALARM_NOTIFY', '总部触发报警事件后通知', 'ALARM_NOTICE', 'SAFETY_MANAGE_HEAD', '安全管理(总部)', 0, '13980439583', '2021-12-14 15:11:29', '13980439583', '2021-12-14 15:11:29');
INSERT INTO `msg_scene` VALUES (1746411346643658022, 'SAFETY_ALARM_TIMEOUT_NOTIFY1', '总部报警事件超时未处理通知', 'ALARM_NOTICE', 'SAFETY_MANAGE_HEAD', '安全管理(总部)', 0, '13980439583', '2021-12-14 15:11:29', '13980439583', '2021-12-14 15:11:29');

-- ----------------------------
-- Records of msg_scene_url
-- ----------------------------
INSERT INTO `msg_scene_url` VALUES (1, 'MAINTAIN_MAINTANCE_RECEIPT_CONFIRM', 'MAINTENACE', 'bimops-manage-app', '/maintenanceTaskDetails', 0, '1', '2022-08-09 16:03:10', '1', '2022-08-09 16:03:13');
INSERT INTO `msg_scene_url` VALUES (2, 'MAINTAIN_HIDDENDANGER_MSG_CLOSE', 'HIDDENDANGER', 'bimops-manage-app', '/hiddenDangerDetails', 0, '1', '2022-08-09 16:04:12', '1', '2022-08-09 16:04:15');
INSERT INTO `msg_scene_url` VALUES (3, 'MAINTAIN_HIDDENDANGER_MSG_CONFIRM', 'HIDDENDANGER', 'bimops-manage-app', '/hiddenDangerDetails', 0, '1', '2022-08-09 16:04:27', '1', '2022-08-09 16:04:29');
INSERT INTO `msg_scene_url` VALUES (4, 'DEVICE_HEALTH_ALARM', 'EQUIPMENT_SUPERVISION', 'bimops-manage-app', NULL, 0, '1', '2022-08-09 16:04:43', '1', '2022-08-09 16:04:45');
INSERT INTO `msg_scene_url` VALUES (5, 'ENERGY_ALARM', 'ENERGY_MANAGE', 'bimops-manage-app', '/energyAlarmDetails', 0, '1', '2022-08-09 16:05:14', '1', '2022-08-09 16:05:16');
INSERT INTO `msg_scene_url` VALUES (6, 'MAINTAIN_RETROFIT_RECEIPT_ASSESSMENT', 'RETROFIT', 'bimops-manage-app', '/renovationDetails', 0, '1', '2022-08-09 16:05:46', '1', '2022-08-09 16:05:49');
INSERT INTO `msg_scene_url` VALUES (7, 'MAINTAIN_RETROFIT_RECEIPT_CONFIRM', 'RETROFIT', 'bimops-manage-app', '/renovationDetails', 0, '1', '2022-08-09 16:06:04', '1', '2022-08-09 16:06:07');
INSERT INTO `msg_scene_url` VALUES (8, 'INSPECT_TASK_MSG_EXECUTING', 'INSPECTION', 'bimops-manage-app', '/patrolInspectTaskDetails', 0, '1', '2022-08-09 16:08:32', '1', '2022-08-09 16:08:34');
INSERT INTO `msg_scene_url` VALUES (9, 'MAINTAIN_FAULT_MSG_CLOSE', 'FAULT', 'bimops-manage-app', '/faultReportDetails', 0, '1', '2022-08-09 16:09:07', '1', '2022-08-09 16:09:10');
INSERT INTO `msg_scene_url` VALUES (10, 'PROCESS_TASK_HANDLED', 'PROCESS', 'bimops-manage-app', NULL, 0, '1', '2022-08-09 16:09:32', '1', '2022-08-09 16:09:34');
INSERT INTO `msg_scene_url` VALUES (11, 'PROCESS_TASK_WAIT_HANDLE', 'PROCESS', 'bimops-manage-app', NULL, 0, '1', '2022-08-09 16:09:47', '1', '2022-08-09 16:09:50');
INSERT INTO `msg_scene_url` VALUES (12, 'MAINTAIN_FAULT_MSG_CONFIRM', 'FAULT', 'bimops-manage-app', '/faultReportDetails', 0, '1', '2022-08-09 16:10:09', '1', '2022-08-09 16:10:11');
INSERT INTO `msg_scene_url` VALUES (13, 'MAINTAIN_FAULT_MSG', 'FAULT', 'bimops-manage-app', '/faultReportDetails', 0, '1', '2022-08-09 16:10:23', '1', '2022-08-09 16:10:25');
INSERT INTO `msg_scene_url` VALUES (14, 'LINKAGE_CONDITION_EVENT', 'LINKAGE_STRATEGY', 'bimops-manage-app', NULL, 0, '1', '2022-08-09 16:10:37', '1', '2022-08-09 16:10:47');
INSERT INTO `msg_scene_url` VALUES (15, 'LINKAGE_SCENE_EVENT', 'LINKAGE_STRATEGY', 'bimops-manage-app', NULL, 0, '1', '2022-08-09 16:11:07', '1', '2022-08-09 16:11:10');
INSERT INTO `msg_scene_url` VALUES (16, 'LINKAGE_TIMING_EVENT', 'LINKAGE_STRATEGY', 'bimops-manage-app', NULL, 0, '1', '2022-08-09 16:11:23', '1', '2022-08-09 16:11:25');
INSERT INTO `msg_scene_url` VALUES (17, 'ONE_DEVICE_ALARM', 'EQUIPMENT_SUPERVISION', 'bimops-manage-app', '/deviceAlarmDetails', 0, '1', '2022-08-09 16:11:42', '1', '2022-08-09 16:11:44');
INSERT INTO `msg_scene_url` VALUES (18, 'MAINTAIN_OUTSOURCE_REPAIR_HANDLED', 'OUTSOURCE_REPAIR', 'bimops-manage-app', '/outsourceRepairDetails', 0, '1', '2022-08-09 16:12:20', '1', '2022-08-09 16:12:22');
INSERT INTO `msg_scene_url` VALUES (19, 'MAINTAIN_OUTSOURCE_REPAIR_RECEIVED', 'OUTSOURCE_REPAIR', 'bimops-manage-app', '/outsourceRepairDetails', 0, '1', '2022-08-09 16:40:23', '1', '2022-08-09 16:40:25');
INSERT INTO `msg_scene_url` VALUES (20, 'MAINTAIN_OUTSOURCE_REPAIR_CONFIRM', 'OUTSOURCE_REPAIR', 'bimops-manage-app', '/outsourceRepairDetails', 0, '1', '2022-08-09 16:40:35', '1', '2022-08-09 16:40:38');
INSERT INTO `msg_scene_url` VALUES (21, 'MAINTAIN_OUTSOURCE_REPAIR_SOLVE', 'OUTSOURCE_REPAIR', 'bimops-manage-app', '/outsourceRepairDetails', 0, '1', '2022-08-09 16:40:49', '1', '2022-08-09 16:40:51');
INSERT INTO `msg_scene_url` VALUES (22, 'MAINTAIN_OUTSOURCE_REPAIR_DISTRIBUTED', 'OUTSOURCE_REPAIR', 'bimops-manage-app', '/outsourceRepairDetails', 0, '1', '2022-08-09 16:41:09', '1', '2022-08-09 16:41:12');
INSERT INTO `msg_scene_url` VALUES (23, 'MAINTAIN_MAINTANCE_RECEIPT_CONFIRM', 'MAINTENACE', 'bimops-manage-web', '/portal/maintenance/maintenanceTaskDetails', 0, '1', '2022-08-11 14:14:21', '1', '2022-08-11 14:14:23');
INSERT INTO `msg_scene_url` VALUES (24, 'MAINTAIN_HIDDENDANGER_MSG_CLOSE', 'HIDDENDANGER', 'bimops-manage-web', '/portal/problems/problemsDetails', 0, '1', '2022-08-11 14:14:46', '1', '2022-08-11 14:14:48');
INSERT INTO `msg_scene_url` VALUES (25, 'MAINTAIN_HIDDENDANGER_MSG_CONFIRM', 'HIDDENDANGER', 'bimops-manage-web', '/portal/problems/problemsDetails', 0, '1', '2022-08-11 14:15:02', '1', '2022-08-11 14:15:05');
INSERT INTO `msg_scene_url` VALUES (26, 'DEVICE_HEALTH_ALARM', 'EQUIPMENT_SUPERVISION', 'bimops-manage-web', NULL, 0, '1', '2022-08-11 14:15:16', '1', '2022-08-11 14:15:18');
INSERT INTO `msg_scene_url` VALUES (27, 'ENERGY_ALARM', 'ENERGY_MANAGE', 'bimops-manage-web', 'portal/energyManagement/energyAlarmDetail', 0, '1', '2022-08-11 14:16:08', '1', '2022-08-11 14:16:10');
INSERT INTO `msg_scene_url` VALUES (28, 'MAINTAIN_RETROFIT_RECEIPT_ASSESSMENT', 'RETROFIT', 'bimops-manage-web', '/portal/renovation/renovationDetails', 0, '1', '2022-08-11 14:16:41', '1', '2022-08-11 14:16:43');
INSERT INTO `msg_scene_url` VALUES (29, 'MAINTAIN_RETROFIT_RECEIPT_CONFIRM', 'RETROFIT', 'bimops-manage-web', '/portal/renovation/renovationDetails', 0, '1', '2022-08-11 14:16:56', '1', '2022-08-11 14:16:59');
INSERT INTO `msg_scene_url` VALUES (30, 'INSPECT_TASK_MSG_EXECUTING', 'INSPECTION', 'bimops-manage-web', '/portal/inspection/inspectionTaskDetails', 0, '1', '2022-08-11 14:17:33', '1', '2022-08-11 14:17:34');
INSERT INTO `msg_scene_url` VALUES (31, 'MAINTAIN_FAULT_MSG_CLOSE', 'FAULT', 'bimops-manage-web', '/portal/fault/faultDetails', 0, '1', '2022-08-11 14:18:06', '1', '2022-08-11 14:18:08');
INSERT INTO `msg_scene_url` VALUES (32, 'PROCESS_TASK_HANDLED', 'PROCESS', 'bimops-manage-web', NULL, 0, '1', '2022-08-11 14:18:22', '1', '2022-08-11 14:18:24');
INSERT INTO `msg_scene_url` VALUES (33, 'PROCESS_TASK_WAIT_HANDLE', 'PROCESS', 'bimops-manage-web', NULL, 0, '1', '2022-08-11 14:18:35', '1', '2022-08-11 14:18:38');
INSERT INTO `msg_scene_url` VALUES (34, 'MAINTAIN_FAULT_MSG_CONFIRM', 'FAULT', 'bimops-manage-web', '/portal/fault/faultDetails', 0, '1', '2022-08-11 14:19:07', '1', '2022-08-11 14:19:09');
INSERT INTO `msg_scene_url` VALUES (35, 'MAINTAIN_FAULT_MSG', 'FAULT', 'bimops-manage-web', '/portal/fault/faultDetails', 0, '1', '2022-08-11 14:19:35', '1', '2022-08-11 14:19:37');
INSERT INTO `msg_scene_url` VALUES (36, 'LINKAGE_CONDITION_EVENT', 'LINKAGE_STRATEGY', 'bimops-manage-web', NULL, 0, '1', '2022-08-11 14:19:56', '1', '2022-08-11 14:19:58');
INSERT INTO `msg_scene_url` VALUES (37, 'LINKAGE_SCENE_EVENT', 'LINKAGE_STRATEGY', 'bimops-manage-web', NULL, 0, '1', '2022-08-11 14:20:10', '1', '2022-08-11 14:20:13');
INSERT INTO `msg_scene_url` VALUES (38, 'LINKAGE_TIMING_EVENT', 'LINKAGE_STRATEGY', 'bimops-manage-web', NULL, 0, '1', '2022-08-11 14:20:28', '1', '2022-08-11 14:20:31');
INSERT INTO `msg_scene_url` VALUES (39, 'ONE_DEVICE_ALARM', 'EQUIPMENT_SUPERVISION', 'bimops-manage-web', '/portal/equipmentSupervision/equipmentAlarmDetails', 0, '1', '2022-08-11 14:21:14', '1', '2022-08-11 14:21:17');
INSERT INTO `msg_scene_url` VALUES (40, 'MAINTAIN_OUTSOURCE_REPAIR_HANDLED', 'OUTSOURCE_REPAIR', 'bimops-manage-web', '/portal/outsourcing/repairListDetails', 0, '1', '2022-08-11 14:21:45', '1', '2022-08-11 14:21:47');
INSERT INTO `msg_scene_url` VALUES (41, 'MAINTAIN_OUTSOURCE_REPAIR_RECEIVED', 'OUTSOURCE_REPAIR', 'bimops-manage-web', '/portal/outsourcing/repairListDetails', 0, '1', '2022-08-11 14:21:59', '1', '2022-08-11 14:22:01');
INSERT INTO `msg_scene_url` VALUES (42, 'MAINTAIN_OUTSOURCE_REPAIR_CONFIRM', 'OUTSOURCE_REPAIR', 'bimops-manage-web', '/portal/outsourcing/repairListDetails', 0, '1', '2022-08-11 14:22:14', '1', '2022-08-11 14:22:16');
INSERT INTO `msg_scene_url` VALUES (43, 'MAINTAIN_OUTSOURCE_REPAIR_SOLVE', 'OUTSOURCE_REPAIR', 'bimops-manage-web', '/portal/outsourcing/repairListDetails', 0, '1', '2022-08-11 14:22:28', '1', '2022-08-11 14:22:30');
INSERT INTO `msg_scene_url` VALUES (44, 'MAINTAIN_OUTSOURCE_REPAIR_DISTRIBUTED', 'OUTSOURCE_REPAIR', 'bimops-manage-web', '/portal/outsourcing/repairListDetails', 0, '1', '2022-08-11 14:22:28', '1', '2022-08-11 14:22:30');
INSERT INTO `msg_scene_url` VALUES (45, 'TASKS_TODO_MSG', 'TASKS_TODO', 'bimops-manage-app', '/noticeDetails', 0, '1', '2022-11-21 09:16:44', '1', '2022-11-21 09:16:46');
INSERT INTO `msg_scene_url` VALUES (46, 'EMERGENCY_ALARM', 'SECURITY_MONITORING', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:37:25', '1', '2022-11-29 10:37:28');
INSERT INTO `msg_scene_url` VALUES (47, 'EMERGENCY_ALARM', 'SECURITY_MONITORING', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:38:57', '1', '2022-11-29 10:39:00');
INSERT INTO `msg_scene_url` VALUES (48, 'EMERGENCY_ALARM_TIMEOUT', 'SECURITY_MONITORING', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:39:27', '1', '2022-11-29 10:39:30');
INSERT INTO `msg_scene_url` VALUES (49, 'EMERGENCY_ALARM_TIMEOUT', 'SECURITY_MONITORING', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:39:49', '1', '2022-11-29 10:39:51');
INSERT INTO `msg_scene_url` VALUES (50, 'EMERGENCY_ALARM', 'ELECTRICMANAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:37:25', '1', '2022-11-29 10:37:28');
INSERT INTO `msg_scene_url` VALUES (51, 'EMERGENCY_ALARM', 'ELECTRICMANAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:38:57', '1', '2022-11-29 10:39:00');
INSERT INTO `msg_scene_url` VALUES (52, 'EMERGENCY_ALARM_TIMEOUT', 'ELECTRICMANAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:39:27', '1', '2022-11-29 10:39:30');
INSERT INTO `msg_scene_url` VALUES (53, 'EMERGENCY_ALARM_TIMEOUT', 'ELECTRICMANAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:39:49', '1', '2022-11-29 10:39:51');
INSERT INTO `msg_scene_url` VALUES (54, 'EMERGENCY_ALARM', 'ROTATINGMANAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:37:25', '1', '2022-11-29 10:37:28');
INSERT INTO `msg_scene_url` VALUES (55, 'EMERGENCY_ALARM', 'ROTATINGMANAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:38:57', '1', '2022-11-29 10:39:00');
INSERT INTO `msg_scene_url` VALUES (56, 'EMERGENCY_ALARM_TIMEOUT', 'ROTATINGMANAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:39:27', '1', '2022-11-29 10:39:30');
INSERT INTO `msg_scene_url` VALUES (57, 'EMERGENCY_ALARM_TIMEOUT', 'ROTATINGMANAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:39:49', '1', '2022-11-29 10:39:51');
INSERT INTO `msg_scene_url` VALUES (58, 'EMERGENCY_ALARM', 'AIRCONDITIONER', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:37:25', '1', '2022-11-29 10:37:28');
INSERT INTO `msg_scene_url` VALUES (59, 'EMERGENCY_ALARM', 'AIRCONDITIONER', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:38:57', '1', '2022-11-29 10:39:00');
INSERT INTO `msg_scene_url` VALUES (60, 'EMERGENCY_ALARM_TIMEOUT', 'AIRCONDITIONER', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:39:27', '1', '2022-11-29 10:39:30');
INSERT INTO `msg_scene_url` VALUES (61, 'EMERGENCY_ALARM_TIMEOUT', 'AIRCONDITIONER', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:39:49', '1', '2022-11-29 10:39:51');
INSERT INTO `msg_scene_url` VALUES (62, 'EMERGENCY_ALARM', 'FIRECONTROL', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:37:25', '1', '2022-11-29 10:37:28');
INSERT INTO `msg_scene_url` VALUES (63, 'EMERGENCY_ALARM', 'FIRECONTROL', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:38:57', '1', '2022-11-29 10:39:00');
INSERT INTO `msg_scene_url` VALUES (64, 'EMERGENCY_ALARM_TIMEOUT', 'FIRECONTROL', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:39:27', '1', '2022-11-29 10:39:30');
INSERT INTO `msg_scene_url` VALUES (65, 'EMERGENCY_ALARM_TIMEOUT', 'FIRECONTROL', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:39:49', '1', '2022-11-29 10:39:51');
INSERT INTO `msg_scene_url` VALUES (66, 'EMERGENCY_ALARM', 'WATERSUPPLYDRAINAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:37:25', '1', '2022-11-29 10:37:28');
INSERT INTO `msg_scene_url` VALUES (67, 'EMERGENCY_ALARM', 'WATERSUPPLYDRAINAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:38:57', '1', '2022-11-29 10:39:00');
INSERT INTO `msg_scene_url` VALUES (68, 'EMERGENCY_ALARM_TIMEOUT', 'WATERSUPPLYDRAINAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:39:27', '1', '2022-11-29 10:39:30');
INSERT INTO `msg_scene_url` VALUES (69, 'EMERGENCY_ALARM_TIMEOUT', 'WATERSUPPLYDRAINAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:39:49', '1', '2022-11-29 10:39:51');
INSERT INTO `msg_scene_url` VALUES (70, 'EMERGENCY_ALARM', 'AIRSUPPLYANDEXHAUST', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:37:25', '1', '2022-11-29 10:37:28');
INSERT INTO `msg_scene_url` VALUES (71, 'EMERGENCY_ALARM', 'AIRSUPPLYANDEXHAUST', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:38:57', '1', '2022-11-29 10:39:00');
INSERT INTO `msg_scene_url` VALUES (72, 'EMERGENCY_ALARM_TIMEOUT', 'AIRSUPPLYANDEXHAUST', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2022-11-29 10:39:27', '1', '2022-11-29 10:39:30');
INSERT INTO `msg_scene_url` VALUES (73, 'EMERGENCY_ALARM_TIMEOUT', 'AIRSUPPLYANDEXHAUST', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2022-11-29 10:39:49', '1', '2022-11-29 10:39:51');
INSERT INTO `msg_scene_url` VALUES (74, 'ALARM_ASSIGN_NOTIFY', 'AIRSUPPLYANDEXHAUST', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (75, 'ALARM_ASSIGN_NOTIFY', 'AIRSUPPLYANDEXHAUST', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (76, 'ALARM_TIMEOUT_NOTIFY', 'AIRSUPPLYANDEXHAUST', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (77, 'ALARM_TIMEOUT_NOTIFY', 'AIRSUPPLYANDEXHAUST', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (78, 'ALARM_ASSIGN_NOTIFY', 'WATERSUPPLYDRAINAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (79, 'ALARM_ASSIGN_NOTIFY', 'WATERSUPPLYDRAINAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (80, 'ALARM_TIMEOUT_NOTIFY', 'WATERSUPPLYDRAINAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (81, 'ALARM_TIMEOUT_NOTIFY', 'WATERSUPPLYDRAINAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (82, 'ALARM_ASSIGN_NOTIFY', 'FIRECONTROL', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (83, 'ALARM_ASSIGN_NOTIFY', 'FIRECONTROL', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (84, 'ALARM_TIMEOUT_NOTIFY', 'FIRECONTROL', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (85, 'ALARM_TIMEOUT_NOTIFY', 'FIRECONTROL', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (86, 'ALARM_ASSIGN_NOTIFY', 'AIRCONDITIONER', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (87, 'ALARM_ASSIGN_NOTIFY', 'AIRCONDITIONER', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (88, 'ALARM_TIMEOUT_NOTIFY', 'AIRCONDITIONER', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (89, 'ALARM_TIMEOUT_NOTIFY', 'AIRCONDITIONER', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (90, 'ALARM_ASSIGN_NOTIFY', 'ROTATINGMANAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (91, 'ALARM_ASSIGN_NOTIFY', 'ROTATINGMANAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (92, 'ALARM_TIMEOUT_NOTIFY', 'ROTATINGMANAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (93, 'ALARM_TIMEOUT_NOTIFY', 'ROTATINGMANAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (94, 'ALARM_ASSIGN_NOTIFY', 'ELECTRICMANAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (95, 'ALARM_ASSIGN_NOTIFY', 'ELECTRICMANAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (96, 'ALARM_TIMEOUT_NOTIFY', 'ELECTRICMANAGE', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (97, 'ALARM_TIMEOUT_NOTIFY', 'ELECTRICMANAGE', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (98, 'ALARM_ASSIGN_NOTIFY', 'SECURITY_MONITORING', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (99, 'ALARM_ASSIGN_NOTIFY', 'SECURITY_MONITORING', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (100, 'ALARM_TIMEOUT_NOTIFY', 'SECURITY_MONITORING', 'bimops-manage-web', '/portal/securityMonitoring/alarmDetail', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');
INSERT INTO `msg_scene_url` VALUES (101, 'ALARM_TIMEOUT_NOTIFY', 'SECURITY_MONITORING', 'bimops-manage-app', '/baseAlarmDetails', 0, '1', '2023-09-26 16:13:37', '1', '2023-09-26 16:13:41');

-- ----------------------------
-- Records of msg_tmp
-- ----------------------------
INSERT INTO `msg_tmp` VALUES (1704050846726430722, '故障确认通知', '您有一条来源于${faultEvent}${faultName}的确认事项，请及时处理', 'CHANNEL_SITE', '', 1, 'MAINTAIN_FAULT_MSG_CONFIRM', 'FAULT', '故障报修', 0, 'admin', '2023-09-19 16:32:36', 'admin', '2023-09-19 16:40:36');
INSERT INTO `msg_tmp` VALUES (1704052260764721153, '故障关闭通知', '您有一条来源于${faultEvent}${faultName}的关闭事项，请及时处理', 'CHANNEL_SITE', '', 1, 'MAINTAIN_FAULT_MSG_CLOSE', 'FAULT', '故障报修', 0, 'admin', '2023-09-19 16:38:13', 'admin', '2023-09-19 16:40:30');
INSERT INTO `msg_tmp` VALUES (1704052671760375810, '委外维修派发工单通知', '${userName}您好，您有一条来自${tenantName}的委外维修工单，请点击下面的链接确认接收该工单：${address}', 'CHANNEL_SITE', '', 1, 'MAINTAIN_OUTSOURCE_REPAIR_DISTRIBUTED', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:39:51', 'admin', '2023-09-19 16:40:27');
INSERT INTO `msg_tmp` VALUES (1704052727003553794, '委外维修处理工单通知', '${userName}您好，您有一条来自${tenantName}的委外维修工单，请点击下面的链接进行处理结果提交：${address}', 'CHANNEL_SITE', '', 1, 'MAINTAIN_OUTSOURCE_REPAIR_SOLVE', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:40:05', 'admin', '2023-09-19 16:40:24');
INSERT INTO `msg_tmp` VALUES (1704052786738831362, '委外维修确认通知', '您有一条来自${outsourceEvent}${outsourceName}的确认事项，请及时处理', 'CHANNEL_SITE', NULL, 1, 'MAINTAIN_OUTSOURCE_REPAIR_CONFIRM', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:40:19', 'admin', '2023-09-19 16:40:19');
INSERT INTO `msg_tmp` VALUES (1704052936051859457, '委外维修工单已接收通知', '您好，来源于${outsourceEvent}，名称为：${outsourceName}已被${userName}接受，处理结果为：${handleResult}', 'CHANNEL_SITE', NULL, 1, 'MAINTAIN_OUTSOURCE_REPAIR_RECEIVED', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:40:54', 'admin', '2023-09-19 16:40:54');
INSERT INTO `msg_tmp` VALUES (1704052992586883074, '委外维修工单已处理通知', '您好，来源于${outsourceEvent}，名称为：${outsourceName}已被${userName}处理完成，处理结果为：${handleResult}', 'CHANNEL_SITE', NULL, 1, 'MAINTAIN_OUTSOURCE_REPAIR_HANDLED', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:41:08', 'admin', '2023-09-19 16:41:08');
INSERT INTO `msg_tmp` VALUES (1704053145620258817, '更新改造确认通知', '您有一条来源于${retrofitEvent}${retrofitName}的确认事项,请及时处理', 'CHANNEL_SITE', NULL, 0, 'MAINTAIN_RETROFIT_RECEIPT_CONFIRM', 'RETROFIT', '更新改造', 0, 'admin', '2023-09-19 16:41:44', 'admin', '2023-09-19 16:41:44');
INSERT INTO `msg_tmp` VALUES (1704053210908794882, '更新改造评估通知', '您有一条来源于${retrofitEvent}${retrofitName}的评估事项,请及时处理', 'CHANNEL_SITE', NULL, 0, 'MAINTAIN_RETROFIT_RECEIPT_ASSESSMENT', 'RETROFIT', '更新改造', 0, 'admin', '2023-09-19 16:42:00', 'admin', '2023-09-19 16:42:00');
INSERT INTO `msg_tmp` VALUES (1704053331411148801, '条件联动', '${triggerTime}系统检测条件联动策略${eventName}符合触发条件，系统自动执行', 'CHANNEL_SITE', NULL, 1, 'LINKAGE_CONDITION_EVENT', 'LINKAGE_STRATEGY', '联动控制', 0, 'admin', '2023-09-19 16:42:29', 'admin', '2023-09-19 16:42:29');
INSERT INTO `msg_tmp` VALUES (1704053411362971650, '场景联动', '${triggerTime}场景控制策略${eventName}被${operatorName}手动执行', 'CHANNEL_SITE', NULL, 0, 'LINKAGE_SCENE_EVENT', 'LINKAGE_STRATEGY', '联动控制', 0, 'admin', '2023-09-19 16:42:48', 'admin', '2023-09-19 16:42:48');
INSERT INTO `msg_tmp` VALUES (1704053476450181122, '定时联动', '${triggerTime}系统检测定时控制策略${eventName}符合触发条件，系统自动执行', 'CHANNEL_SITE', NULL, 0, 'LINKAGE_TIMING_EVENT', 'LINKAGE_STRATEGY', '联动控制', 0, 'admin', '2023-09-19 16:43:03', 'admin', '2023-09-19 16:43:03');
INSERT INTO `msg_tmp` VALUES (1704053932618489857, '维保任务通知', '您有一条来源于${maintenaceEvent}${maintenaceName}的维保事项，请及时处理', 'CHANNEL_SITE', NULL, 0, 'MAINTAIN_MAINTANCE_RECEIPT_CONFIRM', 'MAINTENACE', '维保作业', 0, 'admin', '2023-09-19 16:44:52', 'admin', '2023-09-19 16:44:52');
INSERT INTO `msg_tmp` VALUES (1704054011970527234, '隐患确认通知', '您有一条来源于${hiddenDangerEvent}${hiddenDangerName}的确认事项，请及时处理', 'CHANNEL_SITE', NULL, 0, 'MAINTAIN_HIDDENDANGER_MSG_CONFIRM', 'HIDDENDANGER', '隐患排查', 0, 'admin', '2023-09-19 16:45:11', 'admin', '2023-09-19 16:45:11');
INSERT INTO `msg_tmp` VALUES (1704054057843630081, '隐患关闭通知', '您有一条来源于${hiddenDangerEvent}${hiddenDangerName}的关闭事项，请及时处理', 'CHANNEL_SITE', NULL, 0, 'MAINTAIN_HIDDENDANGER_MSG_CLOSE', 'HIDDENDANGER', '隐患排查', 0, 'admin', '2023-09-19 16:45:22', 'admin', '2023-09-19 16:45:22');
INSERT INTO `msg_tmp` VALUES (1704054542751309825, '设备巡检任务待处理通知', '您有一条来源于${inspectTask}${inspectTaskName}的巡检事项，请及时处理', 'CHANNEL_SITE', NULL, 0, 'INSPECT_TASK_MSG_EXECUTING', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 16:47:17', 'admin', '2023-09-19 16:47:17');
INSERT INTO `msg_tmp` VALUES (1704054636766633986, '流程任务待处理通知', '您有一条来源于${applicationName} ，编号为：${receiptCode}的${nodeName}待办事项，请及时处理', 'CHANNEL_SITE', NULL, 0, 'PROCESS_TASK_WAIT_HANDLE', 'BUSINESS_CONFIG', '业务设置', 0, 'admin', '2023-09-19 16:47:40', 'admin', '2023-09-19 16:47:40');
INSERT INTO `msg_tmp` VALUES (1704054680370618370, '流程任务处理完成通知', '您好，来源于${applicationName} ，编号为：${receiptCode}的${nodeName}已被${handledUsers}处理，处理结果为：${handledResult}', 'CHANNEL_SITE', NULL, 0, 'PROCESS_TASK_HANDLED', 'BUSINESS_CONFIG', '业务设置', 0, 'admin', '2023-09-19 16:47:50', 'admin', '2023-09-19 16:47:50');
INSERT INTO `msg_tmp` VALUES (1704054754702073858, '设备报警通知', '${triggerTime} ${triggerTargetName} 发生 ${triggerTypeName} 类型的 ${triggerLevelName} 报警,报警原因：${eventName}', 'CHANNEL_SITE', NULL, 0, 'ONE_DEVICE_ALARM', 'EQUIPMENT_SUPERVISION', '设备监管', 0, 'admin', '2023-09-19 16:48:08', 'admin', '2023-09-19 16:48:08');
INSERT INTO `msg_tmp` VALUES (1704054844212715522, '设备健康报警通知', '${triggerTime}${triggerTargetName}发生${triggerTypeName}类型的${triggerLevelName}报警,报警原因：${eventName}', 'CHANNEL_SITE', NULL, 0, 'DEVICE_HEALTH_ALARM', 'EQUIPMENT_SUPERVISION', '设备监管', 0, 'admin', '2023-09-19 16:48:29', 'admin', '2023-09-19 16:48:29');
INSERT INTO `msg_tmp` VALUES (1704054916199555073, '能耗报警通知', '${triggerTime} ${triggerTargetName}发生${triggerTypeName}类型的${triggerLevelName}报警,报警原因：${eventName}', 'CHANNEL_SITE', NULL, 0, 'ENERGY_ALARM', 'ENERGY_MANAGE', '能耗管理', 0, 'admin', '2023-09-19 16:48:46', 'admin', '2023-09-19 16:48:46');
INSERT INTO `msg_tmp` VALUES (1704055891048079362, '培训任务发布培训人员通知', '您有一条来源于安全教育的消息，${cultivateTaskName}将在${cultivateStartTime}进行，请合理安排您的时间及时参加培训', 'CHANNEL_SITE', '', 0, 'CULTIVATE_TASK_PUBLISHED_USER_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:52:39', 'admin', '2023-09-25 11:59:21');
INSERT INTO `msg_tmp` VALUES (1704057520610680834, '培训任务发布负责人通知', '您有一条来源于安全教育的消息，您负责${cultivateTaskName}将在${cultivateStartTime}进行，请及时协调人员参加', 'CHANNEL_SITE', NULL, 0, 'CULTIVATE_TASK_PUBLISHED_CHARGE_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:59:07', 'admin', '2023-09-19 16:59:07');
INSERT INTO `msg_tmp` VALUES (1704057624386150401, '考试任务发布考试人员通知', '您有一条来源于安全教育的消息，${examTaskName}将在${examStartTime}进行，请合理安排您的时间及时参加考试', 'CHANNEL_SITE', NULL, 0, 'EXAM_TASK_PUBLISHED_USER_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:59:32', 'admin', '2023-09-19 16:59:32');
INSERT INTO `msg_tmp` VALUES (1704057702488285186, '考试任务发布负责人通知', '您有一条来源于安全教育的消息，您负责${examTaskName}将在${examStartTime}进行，请及时协调人员参加', 'CHANNEL_SITE', NULL, 0, 'EXAM_TASK_PUBLISHED_CHARGE_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:59:51', 'admin', '2023-09-19 16:59:51');
INSERT INTO `msg_tmp` VALUES (1704059096750432258, '耗材库存', '您有一条来源于[资产管理]的消息，${content}，请及时处理', 'CHANNEL_SITE', NULL, 0, 'CONSUMABLE', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:05:23', 'admin', '2023-09-19 17:05:23');
INSERT INTO `msg_tmp` VALUES (1704059143252680705, '资产维保到期通知', '您有一条来源于[资产管理]的消息，${content}，请及时处理', 'CHANNEL_SITE', NULL, 0, 'MAINTENANCE_OVERDUE', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:05:34', 'admin', '2023-09-19 17:05:34');
INSERT INTO `msg_tmp` VALUES (1704059212173484034, '资产使用到期z', '您有一条来源于[资产管理]的消息，${content}，请及时处理', 'CHANNEL_SITE', NULL, 0, 'OPERATE_OVERDUE', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:05:51', 'admin', '2023-09-19 17:05:51');
INSERT INTO `msg_tmp` VALUES (1704061313419124737, '任务生成通知', '您收到${taskName}巡检任务，任务将在${content}失效，请及时接收任务进行巡检', 'CHANNEL_SITE', NULL, 0, 'TASK_GENERATE', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 17:14:12', 'admin', '2023-09-19 17:14:12');
INSERT INTO `msg_tmp` VALUES (1704061393622605826, '任务失效通知', '您负责的${taskName}巡检任务，任务将在${content}分钟后失效，请及时接收任务进行巡检', 'CHANNEL_SITE', NULL, 0, 'TASK_LAPSE', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 17:14:31', 'admin', '2023-09-19 17:14:31');
INSERT INTO `msg_tmp` VALUES (1704061457787068418, '任务失效未完成', '由${content}负责的${taskName}没有完成且当前已经失效，请知悉', 'CHANNEL_SITE', NULL, 0, 'TASK_LAPSE_UNFINISHED', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 17:14:46', 'admin', '2023-09-19 17:14:46');
INSERT INTO `msg_tmp` VALUES (1704061558928515073, '任务接收到期提醒', '您有一条${taskName}还有即将在${taskTime}分钟开始，请及时接收任务避免影响巡更进度', 'CHANNEL_SITE', NULL, 0, 'ELECTRON_TASK_RECEIVE', 'ELECTRONIC_PATROL', '电子巡更', 0, 'admin', '2023-09-19 17:15:10', 'admin', '2023-09-19 17:15:10');
INSERT INTO `msg_tmp` VALUES (1704061603329417217, '任务到期提醒', '您有一条${taskName}还有${taskTime}分钟即将截止，请及时处理', 'CHANNEL_SITE', NULL, 0, 'ELECTRON_TASK_EXPIRE', 'ELECTRONIC_PATROL', '电子巡更', 0, 'admin', '2023-09-19 17:15:21', 'admin', '2023-09-19 17:15:21');
INSERT INTO `msg_tmp` VALUES (1704061663802892290, '任务异常提醒', '您有一条${taskName}已结束，任务完成异常请知晓', 'CHANNEL_SITE', NULL, 0, 'ELECTRON_TASK_ABNORMAL', 'ELECTRONIC_PATROL', '电子巡更', 0, 'admin', '2023-09-19 17:15:35', 'admin', '2023-09-19 17:15:35');
INSERT INTO `msg_tmp` VALUES (1704062087394041858, '盘点任务通知', '您收到${content}盘点任务，请及时盘点并提交盘点结果', 'CHANNEL_SITE', NULL, 0, 'PROPERTY_TASK', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:17:16', 'admin', '2023-09-19 17:17:16');
INSERT INTO `msg_tmp` VALUES (1704065516027846657, '商户待审核市场通知', '收到${supplierName}的商家审核申请，请进行审核！', 'CHANNEL_SITE', NULL, 0, 'MERCHANT_STAY_AUDIT_MARKET_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:30:54', 'admin', '2023-09-19 17:30:54');
INSERT INTO `msg_tmp` VALUES (1704065621472649217, '商户待审核商家通知', '您已成功提交${supplierName}的商家认证申请，请等待管理员审核。', 'CHANNEL_SITE', NULL, 0, 'MERCHANT_STAY_AUDIT_SUPPLIER_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:31:19', 'admin', '2023-09-19 17:31:19');
INSERT INTO `msg_tmp` VALUES (1704066068870668289, '商户认证不通过市场通知', '${supplierName}已审核为不通过！', 'CHANNEL_SITE', NULL, 0, 'MERCHANT_FAIL_ADOPT_MARKET_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:33:05', 'admin', '2023-09-19 17:33:05');
INSERT INTO `msg_tmp` VALUES (1704066291688873985, '商户认证不通过商家通知', '您提交的${supplierName}认证资料认证为不通过，请确认后再次提交认证！', 'CHANNEL_SITE', NULL, 0, 'MERCHANT_FAIL_ADOPT_SUPPLIER_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:33:59', 'admin', '2023-09-19 17:33:59');
INSERT INTO `msg_tmp` VALUES (1704066421733269506, '商户认证通过市场通知', '${supplierName}的认证审核已通过！', 'CHANNEL_SITE', NULL, 0, 'MERCHANT_AUTH_ADOPT_MARKET_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:34:30', 'admin', '2023-09-19 17:34:30');
INSERT INTO `msg_tmp` VALUES (1704066536678170626, '商户认证通过商家通知', '恭喜您，您提交的${supplierName}的认证资料已认证通过,可进行相应业务操作。', 'CHANNEL_SITE', NULL, 0, 'MERCHANT_AUTH_ADOPT_SUPPLIER_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:34:57', 'admin', '2023-09-19 17:34:57');
INSERT INTO `msg_tmp` VALUES (1704068169554272257, '项目计划截至提醒', '项目${projectName}中，${stepName}-${nodeName}计划即将截止，请及时处理。', 'CHANNEL_SITE', NULL, 0, 'PROJECT_PLAN_DEADLINE', 'XRXPROGECT', '项目管理', 0, 'admin', '2023-09-19 17:41:26', 'admin', '2023-09-19 17:41:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598018, '项目汇报提醒', '汇报提醒，关于[${projectName}]的汇报填写，请及时处理。', 'CHANNEL_SITE', NULL, 0, 'PROJECT_REPORT', 'XRXPROGECT', '项目管理', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598019, '报备审核通过商家通知', '尊敬的${supplierName}，报备批次${reportCode}已审核通过!', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_PROK_REPORT_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598020, '报备审核不通过商家通知', '尊敬的${supplierName}，报备批次${reportCode}审核不通过，请确认后再次报备!', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_PROK_REPORT_FAIL_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598021, '理货审核不通过商家通知', '尊敬的${supplierName}，您报备批次号${reportCode}理货审核失败，请到理货数据模块中查看原因!', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_TALLY_FAIL_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598022, '理货审核通过商家通知', '尊敬的${supplierName}，您报备批次号${reportCode}已理货审核通过，可进行开单交易!', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_TALLY_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598023, '密码设置完成', '尊敬的${userName}，您的支付密码已设置成功!', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_SUPPLIER_PASSWORD_SET', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:00:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598024, '订单提醒', '尊敬的${supplierName}，订单：${orderCode}还未完成付款，请及时通知买家完成支付!', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_ORDER_REMIND', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:00:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598025, '订单支付完成', '尊敬的${customerName}，您在${date}，成功支付${amount}元，当前余额为${balance}元!', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_ORDER_PAYMENT_SUCCESS', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:00:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598026, '订单结算完成', '尊敬的${supplierName}，订单：${orderCode}已完成结算，订单金额：${amount}元，市场佣金：${commission}元!', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_ORDER_SETTLEMENT_SUCCESS', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598027, '买家订单取消买家通知', '尊敬的${customerName}，您的订单：${orderCode}在${date}已被取消', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_CUSTOMER_ORDER_CANCEL_CUSTOMER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598028, '买家订单取消商家通知', '尊敬的${supplierName}，订单：${orderCode}在${date}已被取消', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_CUSTOMER_ORDER_CANCEL_SUPPLIER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598029, '卖家订单取消买家通知', '尊敬的${customerName}，您的订单：${orderCode}在${date}已被商家取消', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_SUPPLIER_ORDER_CANCEL_CUSTOMER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598030, '卖家订单取消商家通知', '尊敬的${supplierName}，订单：${orderCode}在${date}已被取消', 'CHANNEL_SITE', NULL, 0, 'MEATTRADE_SUPPLIER_ORDER_CANCEL_SUPPLIER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598031, '故障确认通知', '您有一条来源于${faultEvent}${faultName}的确认事项，请及时处理', 'CHANNEL_SMS', '', 1, 'MAINTAIN_FAULT_MSG_CONFIRM', 'FAULT', '故障报修', 0, 'admin', '2023-09-19 16:32:36', 'admin', '2023-09-19 16:40:36');
INSERT INTO `msg_tmp` VALUES (1704068231168598032, '故障关闭通知', '您有一条来源于${faultEvent}${faultName}的关闭事项，请及时处理', 'CHANNEL_SMS', '', 1, 'MAINTAIN_FAULT_MSG_CLOSE', 'FAULT', '故障报修', 0, 'admin', '2023-09-19 16:38:13', 'admin', '2023-09-19 16:40:30');
INSERT INTO `msg_tmp` VALUES (1704068231168598033, '委外维修派发工单通知', '${userName}您好，您有一条来自${tenantName}的委外维修工单，请点击下面的链接确认接收该工单：${address}', 'CHANNEL_SMS', '', 1, 'MAINTAIN_OUTSOURCE_REPAIR_DISTRIBUTED', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:39:51', 'admin', '2023-09-19 16:40:27');
INSERT INTO `msg_tmp` VALUES (1704068231168598034, '委外维修处理工单通知', '${userName}您好，您有一条来自${tenantName}的委外维修工单，请点击下面的链接进行处理结果提交：${address}', 'CHANNEL_SMS', '', 1, 'MAINTAIN_OUTSOURCE_REPAIR_SOLVE', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:40:05', 'admin', '2023-09-19 16:40:24');
INSERT INTO `msg_tmp` VALUES (1704068231168598035, '委外维修确认通知', '您有一条来自${outsourceEvent}${outsourceName}的确认事项，请及时处理', 'CHANNEL_SMS', NULL, 1, 'MAINTAIN_OUTSOURCE_REPAIR_CONFIRM', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:40:19', 'admin', '2023-09-19 16:40:19');
INSERT INTO `msg_tmp` VALUES (1704068231168598036, '委外维修工单已接收通知', '您好，来源于${outsourceEvent}，名称为：${outsourceName}已被${userName}接受，处理结果为：${handleResult}', 'CHANNEL_SMS', NULL, 1, 'MAINTAIN_OUTSOURCE_REPAIR_RECEIVED', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:40:54', 'admin', '2023-09-19 16:40:54');
INSERT INTO `msg_tmp` VALUES (1704068231168598037, '委外维修工单已处理通知', '您好，来源于${outsourceEvent}，名称为：${outsourceName}已被${userName}处理完成，处理结果为：${handleResult}', 'CHANNEL_SMS', NULL, 1, 'MAINTAIN_OUTSOURCE_REPAIR_HANDLED', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:41:08', 'admin', '2023-09-19 16:41:08');
INSERT INTO `msg_tmp` VALUES (1704068231168598038, '更新改造确认通知', '您有一条来源于${retrofitEvent}${retrofitName}的确认事项,请及时处理', 'CHANNEL_SMS', NULL, 0, 'MAINTAIN_RETROFIT_RECEIPT_CONFIRM', 'RETROFIT', '更新改造', 0, 'admin', '2023-09-19 16:41:44', 'admin', '2023-09-19 16:41:44');
INSERT INTO `msg_tmp` VALUES (1704068231168598039, '更新改造评估通知', '您有一条来源于${retrofitEvent}${retrofitName}的评估事项,请及时处理', 'CHANNEL_SMS', NULL, 0, 'MAINTAIN_RETROFIT_RECEIPT_ASSESSMENT', 'RETROFIT', '更新改造', 0, 'admin', '2023-09-19 16:42:00', 'admin', '2023-09-19 16:42:00');
INSERT INTO `msg_tmp` VALUES (1704068231168598040, '条件联动', '${triggerTime}系统检测条件联动策略${eventName}符合触发条件，系统自动执行', 'CHANNEL_SMS', NULL, 1, 'LINKAGE_CONDITION_EVENT', 'LINKAGE_STRATEGY', '联动控制', 0, 'admin', '2023-09-19 16:42:29', 'admin', '2023-09-19 16:42:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598041, '场景联动', '${triggerTime}场景控制策略${eventName}被${operatorName}手动执行', 'CHANNEL_SMS', NULL, 0, 'LINKAGE_SCENE_EVENT', 'LINKAGE_STRATEGY', '联动控制', 0, 'admin', '2023-09-19 16:42:48', 'admin', '2023-09-19 16:42:48');
INSERT INTO `msg_tmp` VALUES (1704068231168598042, '定时联动', '${triggerTime}系统检测定时控制策略${eventName}符合触发条件，系统自动执行', 'CHANNEL_SMS', NULL, 0, 'LINKAGE_TIMING_EVENT', 'LINKAGE_STRATEGY', '联动控制', 0, 'admin', '2023-09-19 16:43:03', 'admin', '2023-09-19 16:43:03');
INSERT INTO `msg_tmp` VALUES (1704068231168598043, '维保任务通知', '您有一条来源于${maintenaceEvent}${maintenaceName}的维保事项，请及时处理', 'CHANNEL_SMS', NULL, 0, 'MAINTAIN_MAINTANCE_RECEIPT_CONFIRM', 'MAINTENACE', '维保作业', 0, 'admin', '2023-09-19 16:44:52', 'admin', '2023-09-19 16:44:52');
INSERT INTO `msg_tmp` VALUES (1704068231168598044, '隐患确认通知', '您有一条来源于${hiddenDangerEvent}${hiddenDangerName}的确认事项，请及时处理', 'CHANNEL_SMS', NULL, 0, 'MAINTAIN_HIDDENDANGER_MSG_CONFIRM', 'HIDDENDANGER', '隐患排查', 0, 'admin', '2023-09-19 16:45:11', 'admin', '2023-09-19 16:45:11');
INSERT INTO `msg_tmp` VALUES (1704068231168598045, '隐患关闭通知', '您有一条来源于${hiddenDangerEvent}${hiddenDangerName}的关闭事项，请及时处理', 'CHANNEL_SMS', NULL, 0, 'MAINTAIN_HIDDENDANGER_MSG_CLOSE', 'HIDDENDANGER', '隐患排查', 0, 'admin', '2023-09-19 16:45:22', 'admin', '2023-09-19 16:45:22');
INSERT INTO `msg_tmp` VALUES (1704068231168598046, '设备巡检任务待处理通知', '您有一条来源于${inspectTask}${inspectTaskName}的巡检事项，请及时处理', 'CHANNEL_SMS', NULL, 0, 'INSPECT_TASK_MSG_EXECUTING', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 16:47:17', 'admin', '2023-09-19 16:47:17');
INSERT INTO `msg_tmp` VALUES (1704068231168598047, '流程任务待处理通知', '您有一条来源于${applicationName} ，编号为：${receiptCode}的${nodeName}待办事项，请及时处理', 'CHANNEL_SMS', NULL, 0, 'PROCESS_TASK_WAIT_HANDLE', 'BUSINESS_CONFIG', '业务设置', 0, 'admin', '2023-09-19 16:47:40', 'admin', '2023-09-19 16:47:40');
INSERT INTO `msg_tmp` VALUES (1704068231168598048, '流程任务处理完成通知', '您好，来源于${applicationName} ，编号为：${receiptCode}的${nodeName}已被${handledUsers}处理，处理结果为：${handledResult}', 'CHANNEL_SMS', NULL, 0, 'PROCESS_TASK_HANDLED', 'BUSINESS_CONFIG', '业务设置', 0, 'admin', '2023-09-19 16:47:50', 'admin', '2023-09-19 16:47:50');
INSERT INTO `msg_tmp` VALUES (1704068231168598049, '设备报警通知', '${triggerTime} ${triggerTargetName} 发生 ${triggerTypeName} 类型的 ${triggerLevelName} 报警,报警原因：${eventName}', 'CHANNEL_SMS', NULL, 0, 'ONE_DEVICE_ALARM', 'EQUIPMENT_SUPERVISION', '设备监管', 0, 'admin', '2023-09-19 16:48:08', 'admin', '2023-09-19 16:48:08');
INSERT INTO `msg_tmp` VALUES (1704068231168598050, '设备健康报警通知', '${triggerTime}${triggerTargetName}发生${triggerTypeName}类型的${triggerLevelName}报警,报警原因：${eventName}', 'CHANNEL_SMS', NULL, 0, 'DEVICE_HEALTH_ALARM', 'EQUIPMENT_SUPERVISION', '设备监管', 0, 'admin', '2023-09-19 16:48:29', 'admin', '2023-09-19 16:48:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598051, '能耗报警通知', '${triggerTime} ${triggerTargetName}发生${triggerTypeName}类型的${triggerLevelName}报警,报警原因：${eventName}', 'CHANNEL_SMS', NULL, 0, 'ENERGY_ALARM', 'ENERGY_MANAGE', '能耗管理', 0, 'admin', '2023-09-19 16:48:46', 'admin', '2023-09-19 16:48:46');
INSERT INTO `msg_tmp` VALUES (1704068231168598054, '培训任务发布考试人员通知', '您有一条来源于安全教育的消息，${cultivateTaskName}将在${cultivateStartTime}进行，请合理安排您的时间及时参加培训', 'CHANNEL_SMS', '', 0, 'CULTIVATE_TASK_PUBLISHED_USER_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:52:39', 'admin', '2023-09-19 16:58:39');
INSERT INTO `msg_tmp` VALUES (1704068231168598055, '培训任务发布负责人通知', '您有一条来源于安全教育的消息，您负责${cultivateTaskName}将在${cultivateStartTime}进行，请及时协调人员参加', 'CHANNEL_SMS', NULL, 0, 'CULTIVATE_TASK_PUBLISHED_CHARGE_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:59:07', 'admin', '2023-09-19 16:59:07');
INSERT INTO `msg_tmp` VALUES (1704068231168598056, '考试任务发布考试人员通知', '您有一条来源于安全教育的消息，${examTaskName}将在${examStartTime}进行，请合理安排您的时间及时参加考试', 'CHANNEL_SMS', NULL, 0, 'EXAM_TASK_PUBLISHED_USER_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:59:32', 'admin', '2023-09-19 16:59:32');
INSERT INTO `msg_tmp` VALUES (1704068231168598057, '考试任务发布负责人通知', '您有一条来源于安全教育的消息，您负责${examTaskName}将在${examStartTime}进行，请及时协调人员参加', 'CHANNEL_SMS', NULL, 0, 'EXAM_TASK_PUBLISHED_CHARGE_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:59:51', 'admin', '2023-09-19 16:59:51');
INSERT INTO `msg_tmp` VALUES (1704068231168598058, '耗材库存', '您有一条来源于[资产管理]的消息，${content}，请及时处理', 'CHANNEL_SMS', NULL, 0, 'CONSUMABLE', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:05:23', 'admin', '2023-09-19 17:05:23');
INSERT INTO `msg_tmp` VALUES (1704068231168598059, '资产维保到期通知', '您有一条来源于[资产管理]的消息，${content}，请及时处理', 'CHANNEL_SMS', NULL, 0, 'MAINTENANCE_OVERDUE', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:05:34', 'admin', '2023-09-19 17:05:34');
INSERT INTO `msg_tmp` VALUES (1704068231168598060, '资产使用到期z', '您有一条来源于[资产管理]的消息，${content}，请及时处理', 'CHANNEL_SMS', NULL, 0, 'OPERATE_OVERDUE', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:05:51', 'admin', '2023-09-19 17:05:51');
INSERT INTO `msg_tmp` VALUES (1704068231168598061, '任务生成通知', '您收到${taskName}巡检任务，任务将在${content}失效，请及时接收任务进行巡检', 'CHANNEL_SMS', NULL, 0, 'TASK_GENERATE', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 17:14:12', 'admin', '2023-09-19 17:14:12');
INSERT INTO `msg_tmp` VALUES (1704068231168598062, '任务失效通知', '您负责的${taskName}巡检任务，任务将在${content}分钟后失效，请及时接收任务进行巡检', 'CHANNEL_SMS', NULL, 0, 'TASK_LAPSE', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 17:14:31', 'admin', '2023-09-19 17:14:31');
INSERT INTO `msg_tmp` VALUES (1704068231168598063, '任务失效未完成', '由${content}负责的${taskName}没有完成且当前已经失效，请知悉', 'CHANNEL_SMS', NULL, 0, 'TASK_LAPSE_UNFINISHED', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 17:14:46', 'admin', '2023-09-19 17:14:46');
INSERT INTO `msg_tmp` VALUES (1704068231168598064, '任务接收到期提醒', '您有一条${taskName}还有即将在${taskTime}分钟开始，请及时接收任务避免影响巡更进度', 'CHANNEL_SMS', NULL, 0, 'ELECTRON_TASK_RECEIVE', 'ELECTRONIC_PATROL', '电子巡更', 0, 'admin', '2023-09-19 17:15:10', 'admin', '2023-09-19 17:15:10');
INSERT INTO `msg_tmp` VALUES (1704068231168598065, '任务到期提醒', '您有一条${taskName}还有${taskTime}分钟即将截止，请及时处理', 'CHANNEL_SMS', NULL, 0, 'ELECTRON_TASK_EXPIRE', 'ELECTRONIC_PATROL', '电子巡更', 0, 'admin', '2023-09-19 17:15:21', 'admin', '2023-09-19 17:15:21');
INSERT INTO `msg_tmp` VALUES (1704068231168598066, '任务异常提醒', '您有一条${taskName}已结束，任务完成异常请知晓', 'CHANNEL_SMS', NULL, 0, 'ELECTRON_TASK_ABNORMAL', 'ELECTRONIC_PATROL', '电子巡更', 0, 'admin', '2023-09-19 17:15:35', 'admin', '2023-09-19 17:15:35');
INSERT INTO `msg_tmp` VALUES (1704068231168598067, '盘点任务通知', '您收到${content}盘点任务，请及时盘点并提交盘点结果', 'CHANNEL_SMS', NULL, 0, 'PROPERTY_TASK', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:17:16', 'admin', '2023-09-19 17:17:16');
INSERT INTO `msg_tmp` VALUES (1704068231168598068, '商户待审核市场通知', '收到${supplierName}的商家审核申请，请进行审核！', 'CHANNEL_SMS', NULL, 0, 'MERCHANT_STAY_AUDIT_MARKET_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:30:54', 'admin', '2023-09-19 17:30:54');
INSERT INTO `msg_tmp` VALUES (1704068231168598069, '商户待审核商家通知', '您已成功提交${supplierName}的商家认证申请，请等待管理员审核。', 'CHANNEL_SMS', NULL, 0, 'MERCHANT_STAY_AUDIT_SUPPLIER_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:31:19', 'admin', '2023-09-19 17:31:19');
INSERT INTO `msg_tmp` VALUES (1704068231168598070, '商户认证不通过市场通知', '${supplierName}已审核为不通过！', 'CHANNEL_SMS', NULL, 0, 'MERCHANT_FAIL_ADOPT_MARKET_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:33:05', 'admin', '2023-09-19 17:33:05');
INSERT INTO `msg_tmp` VALUES (1704068231168598071, '商户认证不通过商家通知', '您提交的${supplierName}认证资料认证为不通过，请确认后再次提交认证！', 'CHANNEL_SMS', NULL, 0, 'MERCHANT_FAIL_ADOPT_SUPPLIER_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:33:59', 'admin', '2023-09-19 17:33:59');
INSERT INTO `msg_tmp` VALUES (1704068231168598072, '商户认证通过市场通知', '${supplierName}的认证审核已通过！', 'CHANNEL_SMS', NULL, 0, 'MERCHANT_AUTH_ADOPT_MARKET_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:34:30', 'admin', '2023-09-19 17:34:30');
INSERT INTO `msg_tmp` VALUES (1704068231168598073, '商户认证通过商家通知', '恭喜您，您提交的${supplierName}的认证资料已认证通过,可进行相应业务操作。', 'CHANNEL_SMS', NULL, 0, 'MERCHANT_AUTH_ADOPT_SUPPLIER_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:34:57', 'admin', '2023-09-19 17:34:57');
INSERT INTO `msg_tmp` VALUES (1704068231168598074, '项目计划截至提醒', '项目${projectName}中，${stepName}-${nodeName}计划即将截止，请及时处理。', 'CHANNEL_SMS', NULL, 0, 'PROJECT_PLAN_DEADLINE', 'XRXPROGECT', '项目管理', 0, 'admin', '2023-09-19 17:41:26', 'admin', '2023-09-19 17:41:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598075, '项目汇报提醒', '汇报提醒，关于[${projectName}]的汇报填写，请及时处理。', 'CHANNEL_SMS', NULL, 0, 'PROJECT_REPORT', 'XRXPROGECT', '项目管理', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598076, '报备审核通过商家通知', '尊敬的${supplierName}，报备批次${reportCode}已审核通过!', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_PROK_REPORT_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598077, '报备审核不通过商家通知', '尊敬的${supplierName}，报备批次${reportCode}审核不通过，请确认后再次报备!', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_PROK_REPORT_FAIL_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598078, '理货审核不通过商家通知', '尊敬的${supplierName}，您报备批次号${reportCode}理货审核失败，请到理货数据模块中查看原因!', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_TALLY_FAIL_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598079, '理货审核通过商家通知', '尊敬的${supplierName}，您报备批次号${reportCode}已理货审核通过，可进行开单交易!', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_TALLY_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598080, '密码设置完成', '尊敬的${userName}，您的支付密码已设置成功!', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_SUPPLIER_PASSWORD_SET', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:00:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598081, '订单提醒', '尊敬的${supplierName}，订单：${orderCode}还未完成付款，请及时通知买家完成支付!', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_ORDER_REMIND', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:00:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598082, '订单支付完成', '尊敬的${customerName}，您在${date}，成功支付${amount}元，当前余额为${balance}元!', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_ORDER_PAYMENT_SUCCESS', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:00:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598083, '订单结算完成', '尊敬的${supplierName}，订单：${orderCode}已完成结算，订单金额：${amount}元，市场佣金：${commission}元!', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_ORDER_SETTLEMENT_SUCCESS', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598084, '买家订单取消买家通知', '尊敬的${customerName}，您的订单：${orderCode}在${date}已被取消', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_CUSTOMER_ORDER_CANCEL_CUSTOMER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598085, '买家订单取消商家通知', '尊敬的${supplierName}，订单：${orderCode}在${date}已被取消', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_CUSTOMER_ORDER_CANCEL_SUPPLIER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598086, '卖家订单取消买家通知', '尊敬的${customerName}，您的订单：${orderCode}在${date}已被商家取消', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_SUPPLIER_ORDER_CANCEL_CUSTOMER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598087, '卖家订单取消商家通知', '尊敬的${supplierName}，订单：${orderCode}在${date}已被取消', 'CHANNEL_SMS', NULL, 0, 'MEATTRADE_SUPPLIER_ORDER_CANCEL_SUPPLIER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598088, '故障确认通知', '您有一条来源于${faultEvent}${faultName}的确认事项，请及时处理', 'CHANNEL_APP', '', 1, 'MAINTAIN_FAULT_MSG_CONFIRM', 'FAULT', '故障报修', 0, 'admin', '2023-09-19 16:32:36', 'admin', '2023-09-19 16:40:36');
INSERT INTO `msg_tmp` VALUES (1704068231168598089, '故障关闭通知', '您有一条来源于${faultEvent}${faultName}的关闭事项，请及时处理', 'CHANNEL_APP', '', 1, 'MAINTAIN_FAULT_MSG_CLOSE', 'FAULT', '故障报修', 0, 'admin', '2023-09-19 16:38:13', 'admin', '2023-09-19 16:40:30');
INSERT INTO `msg_tmp` VALUES (1704068231168598090, '委外维修派发工单通知', '${userName}您好，您有一条来自${tenantName}的委外维修工单，请点击下面的链接确认接收该工单：${address}', 'CHANNEL_APP', '', 1, 'MAINTAIN_OUTSOURCE_REPAIR_DISTRIBUTED', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:39:51', 'admin', '2023-09-19 16:40:27');
INSERT INTO `msg_tmp` VALUES (1704068231168598091, '委外维修处理工单通知', '${userName}您好，您有一条来自${tenantName}的委外维修工单，请点击下面的链接进行处理结果提交：${address}', 'CHANNEL_APP', '', 1, 'MAINTAIN_OUTSOURCE_REPAIR_SOLVE', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:40:05', 'admin', '2023-09-19 16:40:24');
INSERT INTO `msg_tmp` VALUES (1704068231168598092, '委外维修确认通知', '您有一条来自${outsourceEvent}${outsourceName}的确认事项，请及时处理', 'CHANNEL_APP', NULL, 1, 'MAINTAIN_OUTSOURCE_REPAIR_CONFIRM', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:40:19', 'admin', '2023-09-19 16:40:19');
INSERT INTO `msg_tmp` VALUES (1704068231168598093, '委外维修工单已接收通知', '您好，来源于${outsourceEvent}，名称为：${outsourceName}已被${userName}接受，处理结果为：${handleResult}', 'CHANNEL_APP', NULL, 1, 'MAINTAIN_OUTSOURCE_REPAIR_RECEIVED', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:40:54', 'admin', '2023-09-19 16:40:54');
INSERT INTO `msg_tmp` VALUES (1704068231168598094, '委外维修工单已处理通知', '您好，来源于${outsourceEvent}，名称为：${outsourceName}已被${userName}处理完成，处理结果为：${handleResult}', 'CHANNEL_APP', NULL, 1, 'MAINTAIN_OUTSOURCE_REPAIR_HANDLED', 'OUTSOURCE_REPAIR', '委外维修', 0, 'admin', '2023-09-19 16:41:08', 'admin', '2023-09-19 16:41:08');
INSERT INTO `msg_tmp` VALUES (1704068231168598095, '更新改造确认通知', '您有一条来源于${retrofitEvent}${retrofitName}的确认事项,请及时处理', 'CHANNEL_APP', NULL, 0, 'MAINTAIN_RETROFIT_RECEIPT_CONFIRM', 'RETROFIT', '更新改造', 0, 'admin', '2023-09-19 16:41:44', 'admin', '2023-09-19 16:41:44');
INSERT INTO `msg_tmp` VALUES (1704068231168598096, '更新改造评估通知', '您有一条来源于${retrofitEvent}${retrofitName}的评估事项,请及时处理', 'CHANNEL_APP', NULL, 0, 'MAINTAIN_RETROFIT_RECEIPT_ASSESSMENT', 'RETROFIT', '更新改造', 0, 'admin', '2023-09-19 16:42:00', 'admin', '2023-09-19 16:42:00');
INSERT INTO `msg_tmp` VALUES (1704068231168598097, '条件联动', '${triggerTime}系统检测条件联动策略${eventName}符合触发条件，系统自动执行', 'CHANNEL_APP', NULL, 1, 'LINKAGE_CONDITION_EVENT', 'LINKAGE_STRATEGY', '联动控制', 0, 'admin', '2023-09-19 16:42:29', 'admin', '2023-09-19 16:42:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598098, '场景联动', '${triggerTime}场景控制策略${eventName}被${operatorName}手动执行', 'CHANNEL_APP', NULL, 0, 'LINKAGE_SCENE_EVENT', 'LINKAGE_STRATEGY', '联动控制', 0, 'admin', '2023-09-19 16:42:48', 'admin', '2023-09-19 16:42:48');
INSERT INTO `msg_tmp` VALUES (1704068231168598099, '定时联动', '${triggerTime}系统检测定时控制策略${eventName}符合触发条件，系统自动执行', 'CHANNEL_APP', NULL, 0, 'LINKAGE_TIMING_EVENT', 'LINKAGE_STRATEGY', '联动控制', 0, 'admin', '2023-09-19 16:43:03', 'admin', '2023-09-19 16:43:03');
INSERT INTO `msg_tmp` VALUES (1704068231168598100, '维保任务通知', '您有一条来源于${maintenaceEvent}${maintenaceName}的维保事项，请及时处理', 'CHANNEL_APP', NULL, 0, 'MAINTAIN_MAINTANCE_RECEIPT_CONFIRM', 'MAINTENACE', '维保作业', 0, 'admin', '2023-09-19 16:44:52', 'admin', '2023-09-19 16:44:52');
INSERT INTO `msg_tmp` VALUES (1704068231168598101, '隐患确认通知', '您有一条来源于${hiddenDangerEvent}${hiddenDangerName}的确认事项，请及时处理', 'CHANNEL_APP', NULL, 0, 'MAINTAIN_HIDDENDANGER_MSG_CONFIRM', 'HIDDENDANGER', '隐患排查', 0, 'admin', '2023-09-19 16:45:11', 'admin', '2023-09-19 16:45:11');
INSERT INTO `msg_tmp` VALUES (1704068231168598102, '隐患关闭通知', '您有一条来源于${hiddenDangerEvent}${hiddenDangerName}的关闭事项，请及时处理', 'CHANNEL_APP', NULL, 0, 'MAINTAIN_HIDDENDANGER_MSG_CLOSE', 'HIDDENDANGER', '隐患排查', 0, 'admin', '2023-09-19 16:45:22', 'admin', '2023-09-19 16:45:22');
INSERT INTO `msg_tmp` VALUES (1704068231168598103, '设备巡检任务待处理通知', '您有一条来源于${inspectTask}${inspectTaskName}的巡检事项，请及时处理', 'CHANNEL_APP', NULL, 0, 'INSPECT_TASK_MSG_EXECUTING', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 16:47:17', 'admin', '2023-09-19 16:47:17');
INSERT INTO `msg_tmp` VALUES (1704068231168598104, '流程任务待处理通知', '您有一条来源于${applicationName} ，编号为：${receiptCode}的${nodeName}待办事项，请及时处理', 'CHANNEL_APP', NULL, 0, 'PROCESS_TASK_WAIT_HANDLE', 'BUSINESS_CONFIG', '业务设置', 0, 'admin', '2023-09-19 16:47:40', 'admin', '2023-09-19 16:47:40');
INSERT INTO `msg_tmp` VALUES (1704068231168598105, '流程任务处理完成通知', '您好，来源于${applicationName} ，编号为：${receiptCode}的${nodeName}已被${handledUsers}处理，处理结果为：${handledResult}', 'CHANNEL_APP', NULL, 0, 'PROCESS_TASK_HANDLED', 'BUSINESS_CONFIG', '业务设置', 0, 'admin', '2023-09-19 16:47:50', 'admin', '2023-09-19 16:47:50');
INSERT INTO `msg_tmp` VALUES (1704068231168598106, '设备报警通知', '${triggerTime} ${triggerTargetName} 发生 ${triggerTypeName} 类型的 ${triggerLevelName} 报警,报警原因：${eventName}', 'CHANNEL_APP', NULL, 0, 'ONE_DEVICE_ALARM', 'EQUIPMENT_SUPERVISION', '设备监管', 0, 'admin', '2023-09-19 16:48:08', 'admin', '2023-09-19 16:48:08');
INSERT INTO `msg_tmp` VALUES (1704068231168598107, '设备健康报警通知', '${triggerTime}${triggerTargetName}发生${triggerTypeName}类型的${triggerLevelName}报警,报警原因：${eventName}', 'CHANNEL_APP', NULL, 0, 'DEVICE_HEALTH_ALARM', 'EQUIPMENT_SUPERVISION', '设备监管', 0, 'admin', '2023-09-19 16:48:29', 'admin', '2023-09-19 16:48:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598108, '能耗报警通知', '${triggerTime} ${triggerTargetName}发生${triggerTypeName}类型的${triggerLevelName}报警,报警原因：${eventName}', 'CHANNEL_APP', NULL, 0, 'ENERGY_ALARM', 'ENERGY_MANAGE', '能耗管理', 0, 'admin', '2023-09-19 16:48:46', 'admin', '2023-09-19 16:48:46');
INSERT INTO `msg_tmp` VALUES (1704068231168598111, '培训任务发布考试人员通知', '您有一条来源于安全教育的消息，${cultivateTaskName}将在${cultivateStartTime}进行，请合理安排您的时间及时参加培训', 'CHANNEL_APP', '', 0, 'CULTIVATE_TASK_PUBLISHED_USER_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:52:39', 'admin', '2023-09-19 16:58:39');
INSERT INTO `msg_tmp` VALUES (1704068231168598112, '培训任务发布负责人通知', '您有一条来源于安全教育的消息，您负责${cultivateTaskName}将在${cultivateStartTime}进行，请及时协调人员参加', 'CHANNEL_APP', NULL, 0, 'CULTIVATE_TASK_PUBLISHED_CHARGE_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:59:07', 'admin', '2023-09-19 16:59:07');
INSERT INTO `msg_tmp` VALUES (1704068231168598113, '考试任务发布考试人员通知', '您有一条来源于安全教育的消息，${examTaskName}将在${examStartTime}进行，请合理安排您的时间及时参加考试', 'CHANNEL_APP', NULL, 0, 'EXAM_TASK_PUBLISHED_USER_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:59:32', 'admin', '2023-09-19 16:59:32');
INSERT INTO `msg_tmp` VALUES (1704068231168598114, '考试任务发布负责人通知', '您有一条来源于安全教育的消息，您负责${examTaskName}将在${examStartTime}进行，请及时协调人员参加', 'CHANNEL_APP', NULL, 0, 'EXAM_TASK_PUBLISHED_CHARGE_NOTIFY', 'SECURE_EDU', '安全教育', 0, 'admin', '2023-09-19 16:59:51', 'admin', '2023-09-19 16:59:51');
INSERT INTO `msg_tmp` VALUES (1704068231168598115, '耗材库存', '您有一条来源于[资产管理]的消息，${content}，请及时处理', 'CHANNEL_APP', NULL, 0, 'CONSUMABLE', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:05:23', 'admin', '2023-09-19 17:05:23');
INSERT INTO `msg_tmp` VALUES (1704068231168598116, '资产维保到期通知', '您有一条来源于[资产管理]的消息，${content}，请及时处理', 'CHANNEL_APP', NULL, 0, 'MAINTENANCE_OVERDUE', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:05:34', 'admin', '2023-09-19 17:05:34');
INSERT INTO `msg_tmp` VALUES (1704068231168598117, '资产使用到期z', '您有一条来源于[资产管理]的消息，${content}，请及时处理', 'CHANNEL_APP', NULL, 0, 'OPERATE_OVERDUE', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:05:51', 'admin', '2023-09-19 17:05:51');
INSERT INTO `msg_tmp` VALUES (1704068231168598118, '任务生成通知', '您收到${taskName}巡检任务，任务将在${content}失效，请及时接收任务进行巡检', 'CHANNEL_APP', NULL, 0, 'TASK_GENERATE', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 17:14:12', 'admin', '2023-09-19 17:14:12');
INSERT INTO `msg_tmp` VALUES (1704068231168598119, '任务失效通知', '您负责的${taskName}巡检任务，任务将在${content}分钟后失效，请及时接收任务进行巡检', 'CHANNEL_APP', NULL, 0, 'TASK_LAPSE', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 17:14:31', 'admin', '2023-09-19 17:14:31');
INSERT INTO `msg_tmp` VALUES (1704068231168598120, '任务失效未完成', '由${content}负责的${taskName}没有完成且当前已经失效，请知悉', 'CHANNEL_APP', NULL, 0, 'TASK_LAPSE_UNFINISHED', 'INSPECTION', '日常巡检', 0, 'admin', '2023-09-19 17:14:46', 'admin', '2023-09-19 17:14:46');
INSERT INTO `msg_tmp` VALUES (1704068231168598121, '任务接收到期提醒', '您有一条${taskName}还有即将在${taskTime}分钟开始，请及时接收任务避免影响巡更进度', 'CHANNEL_APP', NULL, 0, 'ELECTRON_TASK_RECEIVE', 'ELECTRONIC_PATROL', '电子巡更', 0, 'admin', '2023-09-19 17:15:10', 'admin', '2023-09-19 17:15:10');
INSERT INTO `msg_tmp` VALUES (1704068231168598122, '任务到期提醒', '您有一条${taskName}还有${taskTime}分钟即将截止，请及时处理', 'CHANNEL_APP', NULL, 0, 'ELECTRON_TASK_EXPIRE', 'ELECTRONIC_PATROL', '电子巡更', 0, 'admin', '2023-09-19 17:15:21', 'admin', '2023-09-19 17:15:21');
INSERT INTO `msg_tmp` VALUES (1704068231168598123, '任务异常提醒', '您有一条${taskName}已结束，任务完成异常请知晓', 'CHANNEL_APP', NULL, 0, 'ELECTRON_TASK_ABNORMAL', 'ELECTRONIC_PATROL', '电子巡更', 0, 'admin', '2023-09-19 17:15:35', 'admin', '2023-09-19 17:15:35');
INSERT INTO `msg_tmp` VALUES (1704068231168598124, '盘点任务通知', '您收到${content}盘点任务，请及时盘点并提交盘点结果', 'CHANNEL_APP', NULL, 0, 'PROPERTY_TASK', 'PROPERTY_MANAGEMENT', '资产管理', 0, 'admin', '2023-09-19 17:17:16', 'admin', '2023-09-19 17:17:16');
INSERT INTO `msg_tmp` VALUES (1704068231168598125, '商户待审核市场通知', '收到${supplierName}的商家审核申请，请进行审核！', 'CHANNEL_APP', NULL, 0, 'MERCHANT_STAY_AUDIT_MARKET_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:30:54', 'admin', '2023-09-19 17:30:54');
INSERT INTO `msg_tmp` VALUES (1704068231168598126, '商户待审核商家通知', '您已成功提交${supplierName}的商家认证申请，请等待管理员审核。', 'CHANNEL_APP', NULL, 0, 'MERCHANT_STAY_AUDIT_SUPPLIER_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:31:19', 'admin', '2023-09-19 17:31:19');
INSERT INTO `msg_tmp` VALUES (1704068231168598127, '商户认证不通过市场通知', '${supplierName}已审核为不通过！', 'CHANNEL_APP', NULL, 0, 'MERCHANT_FAIL_ADOPT_MARKET_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:33:05', 'admin', '2023-09-19 17:33:05');
INSERT INTO `msg_tmp` VALUES (1704068231168598128, '商户认证不通过商家通知', '您提交的${supplierName}认证资料认证为不通过，请确认后再次提交认证！', 'CHANNEL_APP', NULL, 0, 'MERCHANT_FAIL_ADOPT_SUPPLIER_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:33:59', 'admin', '2023-09-19 17:33:59');
INSERT INTO `msg_tmp` VALUES (1704068231168598129, '商户认证通过市场通知', '${supplierName}的认证审核已通过！', 'CHANNEL_APP', NULL, 0, 'MERCHANT_AUTH_ADOPT_MARKET_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:34:30', 'admin', '2023-09-19 17:34:30');
INSERT INTO `msg_tmp` VALUES (1704068231168598130, '商户认证通过商家通知', '恭喜您，您提交的${supplierName}的认证资料已认证通过,可进行相应业务操作。', 'CHANNEL_APP', NULL, 0, 'MERCHANT_AUTH_ADOPT_SUPPLIER_NOTIFY', 'SJL', '商家管理', 0, 'admin', '2023-09-19 17:34:57', 'admin', '2023-09-19 17:34:57');
INSERT INTO `msg_tmp` VALUES (1704068231168598131, '项目计划截至提醒', '项目${projectName}中，${stepName}-${nodeName}计划即将截止，请及时处理。', 'CHANNEL_APP', NULL, 0, 'PROJECT_PLAN_DEADLINE', 'XRXPROGECT', '项目管理', 0, 'admin', '2023-09-19 17:41:26', 'admin', '2023-09-19 17:41:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598132, '项目汇报提醒', '汇报提醒，关于[${projectName}]的汇报填写，请及时处理。', 'CHANNEL_APP', NULL, 0, 'PROJECT_REPORT', 'XRXPROGECT', '项目管理', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598133, '报备审核通过商家通知', '尊敬的${supplierName}，报备批次${reportCode}已审核通过!', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_PROK_REPORT_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598134, '报备审核不通过商家通知', '尊敬的${supplierName}，报备批次${reportCode}审核不通过，请确认后再次报备!', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_PROK_REPORT_FAIL_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598135, '理货审核不通过商家通知', '尊敬的${supplierName}，您报备批次号${reportCode}理货审核失败，请到理货数据模块中查看原因!', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_TALLY_FAIL_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598136, '理货审核通过商家通知', '尊敬的${supplierName}，您报备批次号${reportCode}已理货审核通过，可进行开单交易!', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_TALLY_ADOPT', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 17:41:41');
INSERT INTO `msg_tmp` VALUES (1704068231168598137, '密码设置完成', '尊敬的${userName}，您的支付密码已设置成功!', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_SUPPLIER_PASSWORD_SET', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:00:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598138, '订单提醒', '尊敬的${supplierName}，订单：${orderCode}还未完成付款，请及时通知买家完成支付!', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_ORDER_REMIND', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:00:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598139, '订单支付完成', '尊敬的${customerName}，您在${date}，成功支付${amount}元，当前余额为${balance}元!', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_ORDER_PAYMENT_SUCCESS', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:00:29');
INSERT INTO `msg_tmp` VALUES (1704068231168598140, '订单结算完成', '尊敬的${supplierName}，订单：${orderCode}已完成结算，订单金额：${amount}元，市场佣金：${commission}元!', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_ORDER_SETTLEMENT_SUCCESS', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598141, '买家订单取消买家通知', '尊敬的${customerName}，您的订单：${orderCode}在${date}已被取消', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_CUSTOMER_ORDER_CANCEL_CUSTOMER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598142, '买家订单取消商家通知', '尊敬的${supplierName}，订单：${orderCode}在${date}已被取消', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_CUSTOMER_ORDER_CANCEL_SUPPLIER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598143, '卖家订单取消买家通知', '尊敬的${customerName}，您的订单：${orderCode}在${date}已被商家取消', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_SUPPLIER_ORDER_CANCEL_CUSTOMER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1704068231168598144, '卖家订单取消商家通知', '尊敬的${supplierName}，订单：${orderCode}在${date}已被取消', 'CHANNEL_APP', NULL, 0, 'MEATTRADE_SUPPLIER_ORDER_CANCEL_SUPPLIER_NOTIFY', 'MEAT_TRADE', '肉类交易', 0, 'admin', '2023-09-19 17:41:41', 'admin', '2023-09-19 19:05:26');
INSERT INTO `msg_tmp` VALUES (1706230606156079105, '预案报警抄送通知', '${triggerTargetName}在${areaName}发生${eventName}报警事件，请知悉', 'CHANNEL_SITE', 'ALARM_NOTICE', 1, 'EMERGENCY_ALARM', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:54:11', 'admin', '2023-09-28 11:38:09');
INSERT INTO `msg_tmp` VALUES (1706230606156079595, '预案报警抄送通知', '${triggerTargetName}在${areaName}发生${eventName}报警事件，请知悉', 'CHANNEL_SMS', 'ALARM_NOTICE', 1, 'EMERGENCY_ALARM', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:54:11', 'admin', '2023-09-28 11:38:10');
INSERT INTO `msg_tmp` VALUES (1706230606156079599, '预案报警抄送通知', '${triggerTargetName}在${areaName}发生${eventName}报警事件，请知悉', 'CHANNEL_APP', 'ALARM_NOTICE', 1, 'EMERGENCY_ALARM', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:54:11', 'admin', '2023-09-28 11:38:11');
INSERT INTO `msg_tmp` VALUES (1706230606156079603, '预案报警抄送通知', '${areaName}发生了${eventName}请及时处理', 'CHANNEL_VOICE', 'ALARM_NOTICE', 1, 'EMERGENCY_ALARM', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:54:11', 'admin', '2023-09-28 11:38:12');
INSERT INTO `msg_tmp` VALUES (1706230786938970113, '预案报警超时抄送通知', '${triggerTime}${triggerTargetName}发生${triggerTypeName}类型的${triggerLevelName}报警,报警原因：${eventName}', 'CHANNEL_SITE', 'ALARM_NOTICE', 0, 'EMERGENCY_ALARM_TIMEOUT', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:54:54', 'admin', '2023-09-28 11:38:13');
INSERT INTO `msg_tmp` VALUES (1706230786938970596, '预案报警超时抄送通知', '${triggerTime}${triggerTargetName}发生${triggerTypeName}类型的${triggerLevelName}报警,报警原因：${eventName}', 'CHANNEL_SMS', 'ALARM_NOTICE', 0, 'EMERGENCY_ALARM_TIMEOUT', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:54:54', 'admin', '2023-09-28 11:38:13');
INSERT INTO `msg_tmp` VALUES (1706230786938970600, '预案报警超时抄送通知', '${triggerTime}${triggerTargetName}发生${triggerTypeName}类型的${triggerLevelName}报警,报警原因：${eventName}', 'CHANNEL_APP', 'ALARM_NOTICE', 0, 'EMERGENCY_ALARM_TIMEOUT', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:54:54', 'admin', '2023-09-28 11:38:14');
INSERT INTO `msg_tmp` VALUES (1706230786938970604, '预案报警超时抄送通知', '${areaName}发生了${eventName}请及时处理', 'CHANNEL_VOICE', 'ALARM_NOTICE', 0, 'EMERGENCY_ALARM_TIMEOUT', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:54:54', 'admin', '2023-09-28 11:38:15');
INSERT INTO `msg_tmp` VALUES (1706230901640601597, '报警任务通知', '${targetName}在${areaName}发生${eventName}报警事件，请及时处理', 'CHANNEL_SMS', 'ALARM_NOTICE', 1, 'ALARM_ASSIGN_NOTIFY', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:55:22', 'admin', '2023-09-28 11:38:17');
INSERT INTO `msg_tmp` VALUES (1706230901640601601, '报警任务通知', '${targetName}在${areaName}发生${eventName}报警事件，请及时处理', 'CHANNEL_SITE', 'ALARM_NOTICE', 1, 'ALARM_ASSIGN_NOTIFY', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:55:22', 'admin', '2023-09-28 11:38:17');
INSERT INTO `msg_tmp` VALUES (1706230901640601605, '报警任务通知', '${areaName}发生了${eventName}请及时处理', 'CHANNEL_VOICE', 'ALARM_NOTICE', 1, 'ALARM_ASSIGN_NOTIFY', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:55:22', 'admin', '2023-09-28 11:38:18');
INSERT INTO `msg_tmp` VALUES (1706230901640601611, '报警任务通知', '${targetName}在${areaName}发生${eventName}报警事件，请及时处理', 'CHANNEL_APP', 'ALARM_NOTICE', 1, 'ALARM_ASSIGN_NOTIFY', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:55:22', 'admin', '2023-09-28 11:38:19');
INSERT INTO `msg_tmp` VALUES (1706231008167534594, '报警任务超时通知', '${alarmTime}发生的${eventName}报警事件，将在${timeoutProcessTime}后超时，请及时处理', 'CHANNEL_SITE', 'ALARM_NOTICE', 0, 'ALARM_TIMEOUT_NOTIFY', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:55:47', 'admin', '2023-09-28 11:38:20');
INSERT INTO `msg_tmp` VALUES (1706231008167534598, '报警任务超时通知', '${alarmTime}发生的${eventName}报警事件，将在${timeoutProcessTime}后超时，请及时处理', 'CHANNEL_SMS', 'ALARM_NOTICE', 0, 'ALARM_TIMEOUT_NOTIFY', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:55:47', 'admin', '2023-09-28 11:38:20');
INSERT INTO `msg_tmp` VALUES (1706231008167534602, '报警任务超时通知', '${alarmTime}发生的${eventName}报警事件，将在${timeoutProcessTime}后超时，请及时处理', 'CHANNEL_APP', 'ALARM_NOTICE', 0, 'ALARM_TIMEOUT_NOTIFY', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:55:47', 'admin', '2023-09-28 11:38:21');
INSERT INTO `msg_tmp` VALUES (1706231008167534606, '报警任务超时通知', '${areaName}发生了${eventName}请及时处理', 'CHANNEL_VOICE', 'ALARM_NOTICE', 0, 'ALARM_TIMEOUT_NOTIFY', 'EMERGENCY_CENTER', '预案管理', 0, 'admin', '2023-09-25 16:55:47', 'admin', '2023-09-28 11:38:21');
INSERT INTO `msg_tmp` VALUES (1710854942083088385, '总部触发报警事件后通知', '${marketTenantName}在${areaName}发生${eventName}报警事件，请知悉！', 'CHANNEL_SITE', 'ALARM_NOTICE', 1, 'SAFETY_ALARM_NOTIFY', 'SAFETY_MANAGE_HEAD', '安全管理(总部)', 0, 'admin', '2023-10-08 11:09:39', 'admin', '2023-10-08 11:09:39');
INSERT INTO `msg_tmp` VALUES (1710855115299454978, '总部报警事件超时未处理通知', '${marketTenantName}在${areaName}发生${eventName}报警事件已超时${timeoutProcessTime}当前还未处理，请知悉！', 'CHANNEL_SITE', 'ALARM_NOTICE', 1, 'SAFETY_ALARM_TIMEOUT_NOTIFY1', 'SAFETY_MANAGE_HEAD', '安全管理(总部)', 0, 'admin', '2023-10-08 11:10:20', 'admin', '2023-10-08 11:10:20');
INSERT INTO `msg_tmp` VALUES (1710855637330919425, '总部触发报警事件后通知', '${marketTenantName}在${areaName}发生${eventName}报警事件，请知悉！', 'CHANNEL_SMS', 'ALARM_NOTICE', 1, 'SAFETY_ALARM_NOTIFY', 'SAFETY_MANAGE_HEAD', '安全管理(总部)', 0, 'admin', '2023-10-08 11:12:25', 'admin', '2023-10-08 11:12:25');
INSERT INTO `msg_tmp` VALUES (1710855732315127809, '总部报警事件超时未处理通知', '${marketTenantName}在${areaName}发生${eventName}报警事件已超时${timeoutProcessTime}当前还未处理，请知悉！', 'CHANNEL_SMS', 'ALARM_NOTICE', 1, 'SAFETY_ALARM_TIMEOUT_NOTIFY1', 'SAFETY_MANAGE_HEAD', '安全管理(总部)', 0, 'admin', '2023-10-08 11:12:47', 'admin', '2023-10-08 11:12:47');
INSERT INTO `msg_tmp` VALUES (1710855807602884609, '总部触发报警事件后通知', '${marketTenantName}在${areaName}发生${eventName}报警事件，请知悉！', 'CHANNEL_APP', 'ALARM_NOTICE', 1, 'SAFETY_ALARM_NOTIFY', 'SAFETY_MANAGE_HEAD', '安全管理(总部)', 0, 'admin', '2023-10-08 11:13:05', 'admin', '2023-10-08 11:13:05');
INSERT INTO `msg_tmp` VALUES (1710856054836133890, '总部报警事件超时未处理通知', '${marketTenantName}在${areaName}发生${eventName}报警事件已超时${timeoutProcessTime}当前还未处理，请知悉！', 'CHANNEL_APP', 'ALARM_NOTICE', 1, 'SAFETY_ALARM_TIMEOUT_NOTIFY1', 'SAFETY_MANAGE_HEAD', '安全管理(总部)', 0, 'admin', '2023-10-08 11:14:04', 'admin', '2023-10-08 11:14:04');

ALTER TABLE `cube_base_message2`.`msg_record_target` ADD INDEX `msg_record_target_channel_code_IDX`(`channel_code`, `user_id`, `tenant`) USING BTREE;

SET FOREIGN_KEY_CHECKS = 1;


