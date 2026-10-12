CREATE DATABASE IF NOT EXISTS `cube-biz-ouaa` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理
USE `cube-biz-ouaa`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_version
-- ----------------------------

CREATE TABLE IF NOT EXISTS `app_version`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本名称',
  `terminal` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端编号',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `version` int(10) NULL DEFAULT NULL COMMENT '版本号',
  `apk_secret` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装包加密后的密钥',
  `sort` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '序号',
  `force_updated` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否强制更新',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本备注',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'app版本记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of app_version
-- ----------------------------

-- ----------------------------
-- Table structure for application
-- ----------------------------

CREATE TABLE IF NOT EXISTS `application`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `product_line_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '产品线主键',
  `application_group_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '应用组主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用图标',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用备注',
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
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for application_group
-- ----------------------------

CREATE TABLE IF NOT EXISTS `application_group`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用组名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用组描述',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
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
-- Table structure for application_visit_history
-- ----------------------------

CREATE TABLE IF NOT EXISTS `application_visit_history`  (
  `id` bigint(20) NOT NULL,
  `application_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `application_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户主键',
  `terminal` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端编号',
  `visit_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用访问历史记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of application_visit_history
-- ----------------------------

-- ----------------------------
-- Table structure for appointment
-- ----------------------------

CREATE TABLE IF NOT EXISTS `appointment`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '职位名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位描述',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '启停用状态',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appointment
-- ----------------------------

-- ----------------------------
-- Table structure for certificate
-- ----------------------------

CREATE TABLE IF NOT EXISTS `certificate`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
  `terminal` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问端编号',
  `token` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话令牌',
  `expiration` datetime NOT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_certificate_tenant_terminal_username`(`terminal`, `account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户凭证信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of certificate
-- ----------------------------

-- ----------------------------
-- Table structure for client
-- ----------------------------

CREATE TABLE IF NOT EXISTS `client`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端名称',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端备注',
  `secret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端密钥',
  `secret_plain` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端密钥明文',
  `scopes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源范围（多个范围之间使用\",\"号隔开）',
  `grant_types` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权类型（多个类型之间使用\",\"号隔开）',
  `access_token_timeout` int(11) NULL DEFAULT NULL COMMENT '访问令牌超时时间（秒）',
  `refresh_token_timeout` int(11) NULL DEFAULT NULL COMMENT '刷新令牌超时时间（秒）',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `tenant` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编号',
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
  UNIQUE INDEX `idx_client_deleted_name`(`deleted`, `name`) USING BTREE,
  UNIQUE INDEX `idx_client_deleted_code`(`deleted`, `code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户端信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of client
-- ----------------------------
INSERT INTO `client`
(id, code, name, remark, secret, secret_plain, scopes, grant_types, access_token_timeout, refresh_token_timeout, enabled, tenant, deleted, create_by, create_time, update_by,  update_time)
VALUES (1, 'client_1', '智慧农批综合管理系统客户端', NULL, '{bcrypt}$2a$10$3njqC6Wqzfdi8GSsD4W5WeFFL5nO.Ltfk8Xu8/ImQhhUbhvtORj.6', NULL, 'select', 'password,client_credentials,refresh_token,pwd,wechat', 100000000, 100000000, 1, '1', 0, 'admin', '2021-07-19 19:23:28', 'admin', '2021-11-09 13:34:30') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `client`
(id, code, name, remark, secret, secret_plain, scopes, grant_types, access_token_timeout, refresh_token_timeout, enabled, tenant, deleted, create_by, create_time, update_by,  update_time)
VALUES (2, 'inner_server', '内部服务', '', '{bcrypt}$2a$10$qgxOHd92DS2.UD4juD4bo.CGAtQc.9nAU9AJQ8jHEkOG0lCsgmSlm', NULL, 'select', 'client_credentials', 100000000, 100000000, 1, '0', 0, '', '2021-10-25 19:08:33', '', '2021-11-09 16:48:00') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `client`
(id, code, name, remark, secret, secret_plain, scopes, grant_types, access_token_timeout, refresh_token_timeout, enabled, tenant, deleted, create_by, create_time, update_by,  update_time)
VALUES (3, 'inner_terminal', '内部终端', NULL, '{bcrypt}$2a$10$C6Nxw6Qe2shBmQf043GgKuSuw1BWoQlJRoukbgCb0157uYCWPloQW', NULL, 'select', 'password,refresh_token,pwd,wechat,pwd_simple_code,pwd_encrypt', 100000000, 100000000, 1, '0', 0, '', '2021-10-26 09:05:50', '', '2023-02-20 16:37:28') ON DUPLICATE KEY UPDATE `update_time` = NOW();
INSERT INTO `client`
(id, code, name, remark, secret, secret_plain, scopes, grant_types, access_token_timeout, refresh_token_timeout, enabled, tenant, deleted, create_by, create_time, update_by,  update_time)
VALUES (4, 'client_2', '数字建筑', NULL, '{bcrypt}$2a$10$xEKX6LcP9UWRFpy/wii59eYjmP4V5B7uDd7WvfWip4dVjCqboScPW', NULL, 'select', 'password,client_credentials,refresh_token,pwd,wechat,pwd_simple_code,exchange_token', 100000000, 100000000, 1, '0', 0, 'admin', '2022-05-18 16:13:12', 'admin', '2023-11-13 16:02:17') ON DUPLICATE KEY UPDATE `update_time` = NOW();

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;


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
    'ALTER TABLE `cube-biz-ouaa`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.ouaa/changelog-1.1.2-1.mysql.sql', '2024-04-23 01:27:35', 1, 'EXECUTED', '8:58e0d3ab49a287670cc2eec054e0ab41', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806855944');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.ouaa/changelog-1.1.5-0.mysql.sql', '2024-04-23 01:27:36', 2, 'EXECUTED', '8:120d84c8b06f0257744f1a160e18e8c8', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806855998');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.ouaa/changelog-1.1.5-1.mysql.sql', '2024-04-23 01:27:36', 3, 'EXECUTED', '8:1171b5f4b9e3d60338e593f5617fd795', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806856097');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.ouaa/changelog-1.1.6-0.mysql.sql', '2024-04-23 01:27:36', 4, 'EXECUTED', '8:4036b82a052e7ceb47e8b34690c25ee5', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806856438');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.ouaa/changelog-1.1.9-0.mysql.sql', '2024-04-23 01:27:36', 5, 'EXECUTED', '8:c829cf3a04f8f281ab992485c3f95161', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806856611');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.ouaa/changelog-1.1.11-0.mysql.sql', '2024-04-23 01:27:36', 6, 'EXECUTED', '8:477921b132d2147af1547082601941d2', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806856745');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.ouaa/changelog-1.1.13-0.mysql.sql', '2024-04-23 01:27:36', 7, 'EXECUTED', '8:0c4d38d737078b6cd9e078a3e3df30d9', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806856820');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.ouaa/changelog-1.1.14-0.mysql.sql', '2024-06-05 10:18:15', 8, 'EXECUTED', '8:23d7dfa9baef1646dd6abc7a368e01de', 'sql', '', NULL, '4.3.1', NULL, NULL, '7553895275');

-- ----------------------------
-- Table structure for databasechangeloglock
-- ----------------------------

CREATE TABLE IF NOT EXISTS `databasechangeloglock`  (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime NULL DEFAULT NULL,
  `LOCKEDBY` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of databasechangeloglock
-- ----------------------------
INSERT IGNORE INTO `databasechangeloglock` VALUES (1, b'0', NULL, NULL);

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dictionary`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `sort` float UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序数字',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典备注',
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
  UNIQUE INDEX `idx_dictionary_tenant_deleted_type_name`(`tenant`, `deleted`, `type`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for experience_page_info
-- ----------------------------

CREATE TABLE IF NOT EXISTS `experience_page_info`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `describe_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `modul_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名称',
  `img_route` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片路径',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面标识',
  `jump_route` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '古河云产品系列页数据展示' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of experience_page_info
-- ----------------------------

-- ----------------------------
-- Table structure for experience_users
-- ----------------------------

CREATE TABLE IF NOT EXISTS `experience_users`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `full_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `tel` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `enterprise_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '体验用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of experience_users
-- ----------------------------

-- ----------------------------
-- Table structure for home_application
-- ----------------------------

CREATE TABLE IF NOT EXISTS `home_application`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `application_id` bigint(20) UNSIGNED NOT NULL COMMENT '应用主键',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用编号',
  `sort` int(4) UNSIGNED NOT NULL COMMENT '序号',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `terminal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属终端',
  `tenant` int(4) NOT NULL COMMENT '租户id',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_tenant_deleted_application_code_user_id_terminal`(`tenant`, `deleted`, `application_code`, `user_id`, `terminal`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of home_application
-- ----------------------------

-- ----------------------------
-- Table structure for home_application_card
-- ----------------------------

CREATE TABLE IF NOT EXISTS `home_application_card`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `application_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '应用主键',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `card_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '卡片主键',
  `card_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡片名称',
  `card_size` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡片大小',
  `sort` int(4) UNSIGNED NOT NULL COMMENT '序号',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `terminal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属终端',
  `tenant` int(4) NOT NULL COMMENT '租户id',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_tenant_deleted_application_code_card_code_user_id_terminal`(`tenant`, `deleted`, `application_code`, `card_code`, `user_id`, `terminal`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for menu
-- ----------------------------

CREATE TABLE IF NOT EXISTS `menu`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `terminal_id` bigint(20) UNSIGNED NOT NULL COMMENT '访问端主键',
  `application_id` bigint(20) UNSIGNED NOT NULL COMMENT '应用主键',
  `category_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '分类字典主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `sort` float UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序数字',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单备注',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `level` int(10) UNSIGNED NOT NULL COMMENT '菜单层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '上级菜单主键',
  `sequence` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列号',
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
  UNIQUE INDEX `idx_menu_sequence`(`sequence`) USING BTREE,
  UNIQUE INDEX `idx_menu_terminal_id_application_id_deleted_name`(`terminal_id`, `name`, `deleted`, `application_id`) USING BTREE,
  UNIQUE INDEX `idx_menu_terminal_id_application_id_deleted_code`(`terminal_id`, `code`, `deleted`, `application_id`) USING BTREE,
  INDEX `idx_menu_terminal_id`(`terminal_id`) USING BTREE,
  INDEX `menu_icon`(`application_id`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单信息表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `menu_function`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `menu_id` bigint(20) UNSIGNED NOT NULL COMMENT '菜单主键',
  `function_id` bigint(20) UNSIGNED NOT NULL COMMENT '功能主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单功能关联表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `menu_prod`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `terminal_id` bigint(20) UNSIGNED NOT NULL COMMENT '访问端主键',
  `application_id` bigint(20) UNSIGNED NOT NULL COMMENT '应用主键',
  `category_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '分类字典主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `sort` float UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序数字',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单备注',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `level` int(10) UNSIGNED NOT NULL COMMENT '菜单层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '上级菜单主键',
  `sequence` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列号',
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
  UNIQUE INDEX `idx_menu_sequence`(`sequence`) USING BTREE,
  UNIQUE INDEX `idx_menu_terminal_id_application_id_deleted_name`(`terminal_id`, `name`, `deleted`, `application_id`) USING BTREE,
  UNIQUE INDEX `idx_menu_terminal_id_application_id_deleted_code`(`terminal_id`, `code`, `deleted`, `application_id`) USING BTREE,
  INDEX `idx_menu_terminal_id`(`terminal_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of menu_prod
-- ----------------------------

-- ----------------------------
-- Table structure for organization
-- ----------------------------

CREATE TABLE IF NOT EXISTS `organization`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织图标',
  `sort` float UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序数字',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织备注',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `extension` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展字段',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `level` int(10) UNSIGNED NOT NULL COMMENT '组织层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '上级组织主键',
  `sequence` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列号',
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
  UNIQUE INDEX `idx_organization_sequence`(`sequence`) USING BTREE,
  UNIQUE INDEX `idx_organization_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组织机构信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ouaa_card
-- ----------------------------

CREATE TABLE IF NOT EXISTS `ouaa_card`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `terminal_id` bigint(20) UNSIGNED NOT NULL COMMENT '访问端主键',
  `application_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '应用主键',
  `application_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '卡片编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '卡片名称',
  `sort` int(4) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序数字',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡片备注',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `default_card` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否默认卡片',
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
  UNIQUE INDEX `idx_menu_terminal_id_application_id_deleted_name`(`terminal_id`, `name`, `deleted`, `application_id`) USING BTREE,
  UNIQUE INDEX `idx_menu_terminal_id_application_id_deleted_code`(`terminal_id`, `code`, `deleted`, `application_id`) USING BTREE,
  INDEX `idx_menu_terminal_id`(`terminal_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '终端卡片表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for ouaa_function
-- ----------------------------

CREATE TABLE IF NOT EXISTS `ouaa_function`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能名称',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能备注',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '功能信息表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `ouaa_role_menu`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色主键',
  `terminal_id` bigint(20) UNSIGNED NOT NULL COMMENT '访问端主键',
  `application_id` bigint(20) UNSIGNED NOT NULL COMMENT '应用主键',
  `menu_id` bigint(20) UNSIGNED NOT NULL COMMENT '菜单主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `ouaa_role_menu_data_permission`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色主键',
  `terminal_id` bigint(20) NOT NULL COMMENT '访问端主键',
  `application_id` bigint(20) NOT NULL COMMENT '应用主键',
  `menu_id` bigint(20) UNSIGNED NOT NULL COMMENT '菜单主键',
  `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限编码',
  `sort_no` int(11) NULL DEFAULT NULL COMMENT '权限排序号',
  `business_keys` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义的业务键（多个用逗号拼接）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ouaa_role_menu_data_permission
-- ----------------------------

-- ----------------------------
-- Table structure for ouaa_role_menu_function
-- ----------------------------

CREATE TABLE IF NOT EXISTS `ouaa_role_menu_function`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `role_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '角色主键',
  `terminal_id` bigint(20) NULL DEFAULT NULL COMMENT '访问端主键',
  `application_id` bigint(20) NULL DEFAULT NULL COMMENT '应用主键',
  `menu_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '菜单主键',
  `function_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '功能主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `ouaa_terminal_application`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `terminal_id` bigint(20) UNSIGNED NOT NULL COMMENT '访问端主键',
  `application_id` bigint(20) UNSIGNED NOT NULL COMMENT '应用主键',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `password_setting`  (
  `id` bigint(20) NOT NULL,
  `useful_life` int(5) NULL DEFAULT NULL COMMENT '修改时间',
  `due_to_prompt_day` int(5) NULL DEFAULT NULL COMMENT '提醒时间',
  `forced` tinyint(1) NOT NULL COMMENT '是否强制修改',
  `many_login_failures_calibrated` tinyint(1) NULL DEFAULT NULL COMMENT '登录失败次数过多校验是否开启',
  `account_login_locking_duration` int(5) NULL DEFAULT NULL COMMENT '账号登录失败次数过多的锁定时长(目前默认为分钟)',
  `account_login_allowed_fail_num` int(2) NULL DEFAULT NULL COMMENT '账号登录失败在一定范围内允许重试的次数',
  `account_login_allowed_fail_duration` int(5) NULL DEFAULT NULL COMMENT '账号登录登录失败允许重试的规定时间范围(目前默认为分钟)(在一定时间内且不超过登录次数的限制下还可继续登录)',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of password_setting
-- ----------------------------

-- ----------------------------
-- Table structure for permission
-- ----------------------------

CREATE TABLE IF NOT EXISTS `permission`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `terminal_id` bigint(20) UNSIGNED NOT NULL COMMENT '访问端主键',
  `application_id` bigint(20) UNSIGNED NOT NULL COMMENT '应用主键',
  `resource_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源类型',
  `resource_id` bigint(20) UNSIGNED NOT NULL COMMENT '资源主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------

-- ----------------------------
-- Table structure for position
-- ----------------------------

CREATE TABLE IF NOT EXISTS `position`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `sort` float UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序数字',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位备注',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `extension` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展字段',
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
  UNIQUE INDEX `idx_position_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for product
-- ----------------------------

CREATE TABLE IF NOT EXISTS `product`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品编号',
  `product_line_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '产品线主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品名称',
  `domain` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问域名',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品备注',
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
  UNIQUE INDEX `idx_product_product_line_id_deleted_name`(`product_line_id`, `deleted`, `name`) USING BTREE,
  INDEX `idx_product_product_line_id`(`product_line_id`) USING BTREE,
  INDEX `idx_product_domain`(`domain`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品（系统）信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for product_application
-- ----------------------------

CREATE TABLE IF NOT EXISTS `product_application`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `product_id` bigint(20) UNSIGNED NOT NULL COMMENT '产品主键',
  `application_id` bigint(20) UNSIGNED NOT NULL COMMENT '应用主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品应用关联表' ROW_FORMAT = DYNAMIC;

CREATE TABLE IF NOT EXISTS `product_line`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品线名称',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品线备注',
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
  UNIQUE INDEX `idx_product_line_deleted_name`(`deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品线信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role
-- ----------------------------

CREATE TABLE IF NOT EXISTS `role`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `product_id` bigint(20) UNSIGNED NOT NULL COMMENT '产品主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色图标',
  `sort` float UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序数字',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色备注',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `builtin` tinyint(3) UNSIGNED NOT NULL COMMENT '是否内置',
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
  UNIQUE INDEX `idx_role_product_id_tenant_deleted_name`(`product_id`, `tenant`, `deleted`, `name`) USING BTREE,
  INDEX `idx_role_product_id`(`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role_application
-- ----------------------------

CREATE TABLE IF NOT EXISTS `role_application`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色主键',
  `terminal_id` bigint(20) UNSIGNED NOT NULL COMMENT '访问端主键',
  `application_id` bigint(20) UNSIGNED NOT NULL COMMENT '应用主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色应用关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------

CREATE TABLE IF NOT EXISTS `role_permission`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色主键',
  `permission_id` bigint(20) UNSIGNED NOT NULL COMMENT '权限主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for role_terminal
-- ----------------------------

CREATE TABLE IF NOT EXISTS `role_terminal`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `terminal_id` bigint(20) NULL DEFAULT NULL COMMENT '访问端id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
  `back_identity_card_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '身份证反面文件主键',
  `submit_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '统计时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '批发商认证表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of supplier_authentication
-- ----------------------------

-- ----------------------------
-- Table structure for tenant
-- ----------------------------

CREATE TABLE IF NOT EXISTS `tenant`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `category` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户类型：SUPPLIER\\PURCHASER\\MARKET',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '详细地址',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '所在地',
  `building` int(11) NULL DEFAULT 0 COMMENT '楼栋数',
  `area_covered` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '占地面积',
  `logo_id` bigint(100) NULL DEFAULT NULL COMMENT '企业logo',
  `lnt_and_lat` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经纬度信息',
  `file` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '宣传图',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户备注',
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
  `floor_area` decimal(10, 2) NULL DEFAULT NULL COMMENT '占地总建筑面积',
  `service_hot_line` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务热线电话',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_tenant_deleted_name`(`deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tenant_application
-- ----------------------------

CREATE TABLE IF NOT EXISTS `tenant_application`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `product_id` bigint(20) UNSIGNED NOT NULL COMMENT '产品主键',
  `application_id` bigint(20) UNSIGNED NOT NULL COMMENT '应用主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户应用关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tenant_product
-- ----------------------------

CREATE TABLE IF NOT EXISTS `tenant_product`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `product_id` bigint(20) UNSIGNED NOT NULL COMMENT '产品主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户产品关联表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for terminal
-- ----------------------------

CREATE TABLE IF NOT EXISTS `terminal`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `product_id` bigint(20) NOT NULL COMMENT '产品主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问端编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问端名称',
  `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信密钥主键',
  `app_secret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信密钥',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问端备注',
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
  INDEX `idx_terminal_code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '访问端信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '固定电话',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '移动电话',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `gender` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户性别',
  `work_status` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工作状态',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户备注',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `admin` tinyint(3) UNSIGNED NOT NULL COMMENT '是否是超级管理员',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `entry_date` datetime NULL DEFAULT NULL COMMENT '入职日期',
  `photograph_key` bigint(32) NULL DEFAULT NULL COMMENT '人员照片业务主键',
  `last_login_time` datetime NULL DEFAULT NULL,
  `last_login_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_update_password_time` datetime NULL DEFAULT NULL COMMENT '最后更改密码时间',
  `extension` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展字段',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `validity` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'FOLLOW' COMMENT '账户有效性',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_deleted_mobile`(`deleted`, `mobile`) USING BTREE,
  UNIQUE INDEX `idx_user_deleted_username`(`deleted`, `username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_managed_organization
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user_managed_organization`  (
  `id` bigint(20) NOT NULL,
  `organization_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
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
-- Records of user_managed_organization
-- ----------------------------

-- ----------------------------
-- Table structure for user_organization
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user_organization`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `position_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '岗位主键',
  `organization_id` bigint(20) UNSIGNED NOT NULL COMMENT '组织主键',
  `appointment_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_organization_user_id_organization_id_position_id`(`user_id`, `organization_id`, `position_id`, `appointment_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `organization_id`(`organization_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户组织关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user_role`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_tenant
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user_tenant`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为管理员',
  `work_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户工作状态',
  `recent_login` tinyint(1) NOT NULL DEFAULT 0 COMMENT '最近登录',
  `main_enterprise` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为主企业',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户租户关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_wechat
-- ----------------------------

CREATE TABLE IF NOT EXISTS `user_wechat`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信openid',
  `union_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信unionid',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_wechat_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_wechat
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
