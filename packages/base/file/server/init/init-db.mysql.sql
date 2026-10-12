CREATE DATABASE IF NOT EXISTS`cube-base-file` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube-base-file`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file
-- ----------------------------

-- ----------------------------
-- Table structure for file_0
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_0`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_0
-- ----------------------------

-- ----------------------------
-- Table structure for file_1
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_1`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_1
-- ----------------------------

-- ----------------------------
-- Table structure for file_10
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_10`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_10
-- ----------------------------

-- ----------------------------
-- Table structure for file_11
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_11`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_11
-- ----------------------------

-- ----------------------------
-- Table structure for file_12
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_12`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_12
-- ----------------------------

-- ----------------------------
-- Table structure for file_13
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_13`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_13
-- ----------------------------

-- ----------------------------
-- Table structure for file_14
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_14`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_14
-- ----------------------------

-- ----------------------------
-- Table structure for file_15
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_15`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_15
-- ----------------------------

-- ----------------------------
-- Table structure for file_2
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_2`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_2
-- ----------------------------

-- ----------------------------
-- Table structure for file_3
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_3`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_3
-- ----------------------------

-- ----------------------------
-- Table structure for file_4
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_4`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_4
-- ----------------------------

-- ----------------------------
-- Table structure for file_5
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_5`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_5
-- ----------------------------

-- ----------------------------
-- Table structure for file_6
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_6`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_6
-- ----------------------------

-- ----------------------------
-- Table structure for file_7
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_7`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_7
-- ----------------------------

-- ----------------------------
-- Table structure for file_8
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_8`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_8
-- ----------------------------

-- ----------------------------
-- Table structure for file_9
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_9`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名后缀',
  `path` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'URL地址',
  `size` int(50) NOT NULL COMMENT '文件大小',
  `ref_count` int(50) NOT NULL COMMENT '关联数量',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `access_prefix` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件id',
  `bucket_file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '桶里文件名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_9
-- ----------------------------

-- ----------------------------
-- Table structure for file_config
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_config`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `endpoint` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'OSS域名',
  `bucket` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件存储名称',
  `access_key_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问密钥主键',
  `access_key_secret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问密钥',
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
  `access_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问前缀',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for file_expiration_policy
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_expiration_policy`  (
  `id` bigint(11) NOT NULL,
  `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件所在存储桶名称',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `expiration_policy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件过期策略枚举类型',
  `multiple_days` int(10) NOT NULL COMMENT '基于时间删除（N天）',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `config_id` bigint(11) NULL DEFAULT NULL COMMENT '配置文件的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_expiration_policy
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_0
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_0`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_0
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_1
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_1`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_1
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_10
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_10`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_10
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_11
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_11`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_11
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_12
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_12`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_12
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_13
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_13`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_13
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_14
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_14`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_14
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_15
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_15`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_15
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_2
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_2`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_2
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_3
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_3`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_3
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_4
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_4`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_4
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_5
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_5`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_5
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_6
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_6`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_6
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_7
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_7`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_7
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_8
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_8`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_8
-- ----------------------------

-- ----------------------------
-- Table structure for file_ref_9
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_ref_9`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `business_key` bigint(20) UNSIGNED NOT NULL COMMENT '业务主键',
  `file_id` bigint(20) UNSIGNED NOT NULL COMMENT '文件主键',
  `deleted` bigint(20) NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_ref_9
-- ----------------------------

-- ----------------------------
-- Table structure for file_store_sign_up
-- ----------------------------
CREATE TABLE IF NOT EXISTS  `file_store_sign_up`  (
  `id` bigint(20) NOT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `file_store` bigint(20) NULL DEFAULT NULL COMMENT '关联file_config表的主键',
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
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实现类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

INSERT IGNORE INTO `file_config` (`id`, `endpoint`, `bucket`, `access_key_id`, `access_key_secret`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `access_prefix`) VALUES (0, 'http://192.168.0.243:9091', 'guhecloud', 'Wfz7n0JEeQJ08clj', 'xG8pr7rwuTtQTvg0XJb6FUPPatoguw09', 0, 0, 'admin', '2022-01-06 17:35:47', 'admin', '2022-01-06 17:35:44', 'https://cube-test.guhecloud.com:30000/minio');
INSERT IGNORE INTO `cube-base-file`.`file_store_sign_up` (`id`, `tag`, `file_store`, `tenant`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `type`) VALUES (2, 'minio', 0, 0, 0, '0', '2022-07-22 10:12:04', '0', '2022-07-22 10:12:07', 'MINIO');


SET FOREIGN_KEY_CHECKS = 1;


ALTER TABLE `file` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_0` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_0` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_0` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_1` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_1` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_1` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_2` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_2` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_2` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_3` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_3` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_3` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_4` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_4` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_4` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_5` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_5` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_5` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_6` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_6` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_6` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_7` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_7` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_7` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_8` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_8` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_8` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_9` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_9` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_9` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_10` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_10` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_10` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_11` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_11` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_11` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_12` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_12` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_12` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_13` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_13` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_13` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_14` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_14` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_14` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_15` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code' AFTER `bucket_file_name`;
ALTER TABLE `file_15` ADD COLUMN `cleaning_time` datetime NULL DEFAULT NULL COMMENT '清理时间' AFTER `scene_code`;
ALTER TABLE `file_15` ADD COLUMN `storage_strategy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存储策略：sustain 持续；临时：temporary' AFTER `cleaning_time`;
ALTER TABLE `file_expiration_policy` DROP COLUMN `bucket_name`;
ALTER TABLE `file_expiration_policy` DROP COLUMN `file_name`;
ALTER TABLE `file_expiration_policy` DROP COLUMN `file_type`;
ALTER TABLE `file_expiration_policy` DROP COLUMN `multiple_days`;
ALTER TABLE `file_expiration_policy` DROP COLUMN `config_id`;
ALTER TABLE `file_expiration_policy` ADD COLUMN `scene_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件所在存储桶名称' AFTER `id`;
ALTER TABLE `file_expiration_policy` ADD COLUMN `config_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '过期配置详情' AFTER `scene_code`;
ALTER TABLE `file_expiration_policy` MODIFY COLUMN `create_by` varchar (100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户' AFTER `deleted`;
ALTER TABLE `file_expiration_policy` MODIFY COLUMN `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间' AFTER `create_by`;
ALTER TABLE `file_expiration_policy` MODIFY COLUMN `update_by` varchar (100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户' AFTER `create_time`;
ALTER TABLE `file_expiration_policy` MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间' AFTER `update_by`;
ALTER TABLE `file_expiration_policy` ADD COLUMN `tenant` int(11) NULL DEFAULT NULL COMMENT '租户' AFTER `update_time`;

ALTER TABLE `file` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_0` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_0` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_0` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_0` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_1` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_1` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_1` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_1` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_2` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_2` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_2` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_2` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_3` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_3` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_3` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_3` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_4` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_4` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_4` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_4` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_5` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_5` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_5` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_5` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_6` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_6` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_6` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_6` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_7` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_7` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_7` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_7` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_8` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_8` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_8` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_8` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_9` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_9` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_9` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_9` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_10` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_10` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_10` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_10` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_11` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_11` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_11` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_11` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_12` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_12` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_12` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_12` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_13` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_13` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_13` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_13` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_14` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_14` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_14` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_14` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_15` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_15` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_15` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_15` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_config` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_config` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_config` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_config` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_expiration_policy` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_expiration_policy` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_expiration_policy` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_expiration_policy` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_0` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_0` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_0` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_0` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_1` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_1` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_1` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_1` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_2` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_2` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_2` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_2` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_3` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_3` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_3` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_3` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_4` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_4` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_4` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_4` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_5` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_5` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_5` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_5` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_6` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_6` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_6` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_6` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_7` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_7` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_7` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_7` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_8` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_8` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_8` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_8` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_9` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_9` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_9` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_9` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_10` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_10` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_10` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_10` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_11` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_11` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_11` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_11` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_12` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_12` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_12` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_12` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_13` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_13` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_13` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_13` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_14` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_14` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_14` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_14` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_ref_15` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_ref_15` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_ref_15` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_ref_15` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `file_store_sign_up` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `file_store_sign_up` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `file_store_sign_up` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `file_store_sign_up` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';
ALTER TABLE `scene_info` ADD COLUMN `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id';
ALTER TABLE `scene_info` ADD COLUMN `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型';
ALTER TABLE `scene_info` ADD COLUMN `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id';
ALTER TABLE `scene_info` ADD COLUMN `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型';

CREATE TABLE IF NOT EXISTS `scene_info` (
                              `id` bigint(20) NOT NULL,
                              `scene_code` varchar(255) NOT NULL,
                              `scene_name` varchar(255) NOT NULL,
                              `config_id` bigint(20) NOT NULL,
                              `parent_id` bigint(20) DEFAULT NULL,
                              `remark` varchar(255) DEFAULT NULL,
                              `tenant` int(11) NOT NULL,
                              `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                              `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                              `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
                              `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                              `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
                              `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              UNIQUE KEY ```scene_code``, ``tenant``, ``deleted``` (`scene_code`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

