CREATE DATABASE IF NOT EXISTS `cube-biz-meattrade` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-biz-meattrade`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for abattoir
-- ----------------------------

CREATE TABLE IF NOT EXISTS `abattoir`  (
                             `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                             `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '屠宰场编号',
                             `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '屠宰场名称',
                             `abbr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '屠宰场简称',
                             `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '屠宰场地址',
                             `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                             `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
                             `tenant` int(10) UNSIGNED NOT NULL DEFAULT 1 COMMENT '租户编号',
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
                             UNIQUE INDEX `idx_abattoir_tenant_deleted_code`(`tenant`, `deleted`, `code`) USING BTREE,
                             UNIQUE INDEX `idx_abattoir_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '屠宰场信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `config`  (
                           `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                           `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型',
                           `value` double NOT NULL COMMENT '业务数值',
                           `tenant` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编号',
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
                           UNIQUE INDEX `idx_config_tenant_type`(`tenant`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基础设置' ROW_FORMAT = DYNAMIC;

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
            'ALTER TABLE `cube-biz-meattrade`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
            'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `cube-biz-meattrade`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.meattrade/changelog-1.1.0.mysql.sql', '2023-10-09 10:34:27', 1, 'EXECUTED', '8:39e4740518e5af286845d89b5898ea63', 'sql', '', NULL, '4.3.1', NULL, NULL, '6818865420');
INSERT IGNORE INTO `cube-biz-meattrade`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.meattrade/changelog-1.1.1.mysql.sql', '2023-10-09 10:34:28', 2, 'EXECUTED', '8:07a154428726866e3d432bacb7270f32', 'sql', '', NULL, '4.3.1', NULL, NULL, '6818867948');
INSERT IGNORE INTO `cube-biz-meattrade`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.meattrade/changelog-1.1.2.mysql.sql', '2023-10-09 10:34:28', 3, 'EXECUTED', '8:97c81313409fec5f0e7c622e6600660c', 'sql', '', NULL, '4.3.1', NULL, NULL, '6818868516');
INSERT IGNORE INTO `cube-biz-meattrade`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.biz.meattrade/changelog-1.1.3.mysql.sql', '2023-12-11 10:24:55', 4, 'EXECUTED', '8:15be39b37b5c526c6823a70d74eea010', 'sql', '', NULL, '4.3.1', NULL, NULL, '2261493499');


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
-- Table structure for device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device`  (
                           `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                           `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
                           `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编号',
                           `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型',
                           `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备规格',
                           `manufacturer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产厂家',
                           `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
                           `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Token信息',
                           `mac` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Mac地址',
                           `mac_status` tinyint(1) NULL DEFAULT NULL COMMENT 'Mac有效状态',
                           `area` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域',
                           `install_time` datetime NULL DEFAULT NULL COMMENT '安装时间',
                           `install_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装地址',
                           `dept_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门主键',
                           `dept_liaison` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门联系人',
                           `liaison_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
                           `liaison_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系地址',
                           `camera` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联设备',
                           `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备状态',
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
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE INDEX `idx_device_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE,
                           UNIQUE INDEX `idx_device_tenant_deleted_code`(`tenant`, `deleted`, `code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备管理表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `device_type`  (
                                `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称',
                                `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型编号',
                                `level` int(10) NOT NULL COMMENT '类型层级',
                                `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型类别',
                                `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父类主键',
                                `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列号',
                                `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
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
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE INDEX `idx_device_type_tenant_deleted_code`(`tenant`, `deleted`, `code`) USING BTREE,
                                UNIQUE INDEX `idx_device_type_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------

CREATE TABLE IF NOT EXISTS `dictionary`  (
                               `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                               `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                               `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编号',
                               `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
                               `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                               `sort` float(10, 2) NOT NULL COMMENT '排序',
                               `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户主键',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_deposit
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_deposit`  (
                                      `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                      `short_id` int(10) UNSIGNED NOT NULL COMMENT '主键后六位',
                                      `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户主键',
                                      `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
                                      `user_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户电话号码',
                                      `supplier_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '商户主键',
                                      `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购商名称',
                                      `supplier_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家电话号码',
                                      `mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '充值方式',
                                      `amount` decimal(28, 8) UNSIGNED NOT NULL COMMENT '充值金额',
                                      `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '充值状态',
                                      `complete_time` datetime NOT NULL COMMENT '完成时间',
                                      `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
                                      `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
                             `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                             `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
                             `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                             `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
                             `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_deposit_account_id`(`supplier_id`) USING BTREE,
                                      INDEX `idx_deposit_tenant`(`tenant`) USING BTREE,
                                      INDEX `idx_deposit_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账户充值记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_goods_weight_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_goods_weight_config`  (
                                                  `id` bigint(20) UNSIGNED NOT NULL,
                                                  `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品类型',
                                                  `goods_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品类型名称',
                                                  `standard_weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '标准重量',
                                                  `weight_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重量单位',
                                                  `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识',
                                                  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                                                  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '滑轨商品品类标准重量配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_inspector
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_inspector`  (
                                        `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                        `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户主键',
                                        `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抽检员名称',
                                        `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                                        `enabled` tinyint(1) NOT NULL COMMENT '状态',
                                        `add_time` datetime NOT NULL COMMENT '添加时间',
                                        `remark` varchar(201) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                        `tenant` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编号',
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
                                        UNIQUE INDEX `idx_inspector_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '抽检人员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_order
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_order`  (
                                    `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                    `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号',
                                    `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户主键',
                                    `customer_id` bigint(20) UNSIGNED NOT NULL COMMENT '买家主键',
                                    `amount` decimal(28, 8) NOT NULL COMMENT '总金额',
                                    `weight` double(10, 2) NOT NULL COMMENT '商品总重量',
                                    `loss` double(10, 2) NOT NULL COMMENT '商品总损耗',
                                    `actual_weight` double(10, 2) NOT NULL COMMENT '实际总重量',
                                    `commission` double(10, 2) NOT NULL COMMENT '总佣金',
                                    `order_time` datetime NOT NULL COMMENT '下单时间',
                                    `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单状态',
                                    `way` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式',
                                    `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
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
                                    UNIQUE INDEX `idx_order_tenant_code`(`tenant`, `code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_order_detail
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_order_detail`  (
                                           `id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '主键',
                                           `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单主键',
                                           `pork_arrange_detail_id` bigint(20) UNSIGNED NOT NULL COMMENT '理货详情主键',
                                           `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型主键',
                                           `goods_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型名称',
                                           `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识',
                                           `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品主键',
                                           `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
                                           `settlement_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '结算主键',
                                           `loss` decimal(10, 2) NOT NULL COMMENT '损耗',
                                           `weight` decimal(10, 2) NOT NULL COMMENT '重量',
                                           `unit_price` decimal(10, 2) NOT NULL COMMENT '单价',
                                           `price` decimal(10, 2) NOT NULL COMMENT '总价',
                                           `commission` decimal(10, 2) NOT NULL COMMENT '佣金',
                                           `commission_ratio` double NOT NULL COMMENT '佣金率',
                                           `checked` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品是否取消',
                                           `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单详情表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_payment
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_payment`  (
                                      `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                      `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单主键',
                                      `mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式',
                                      `amount` decimal(28, 0) UNSIGNED NOT NULL COMMENT '支付金额',
                                      `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
                                      `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
                             `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                             `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
                             `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                             `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
                             `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_payment_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单支付表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_pork_arrange
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_pork_arrange`  (
                                           `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                           `pork_report_id` bigint(20) UNSIGNED NOT NULL COMMENT '报备主键',
                                           `pork_report_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备号',
                                           `slices` int(10) NULL DEFAULT NULL COMMENT '理货片数',
                                           `weight` double(10, 2) NULL DEFAULT NULL COMMENT '理货重量',
                                           `place` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理货地点',
                                           `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '理货状态',
                                           `pork_arrange_time` datetime NULL DEFAULT NULL COMMENT '理货完成时间',
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
                                           PRIMARY KEY (`id`) USING BTREE,
                                           UNIQUE INDEX `idx_pork_arrange_tenant_deleted_pork_report_id`(`tenant`, `deleted`, `pork_report_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '理货数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_pork_arrange_audit
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_pork_arrange_audit`  (
                                                 `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                                 `pork_report_id` bigint(20) UNSIGNED NOT NULL COMMENT '来货报备主键',
                                                 `pork_report_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来货报备批次号',
                                                 `pratique_register_id` bigint(20) UNSIGNED NOT NULL COMMENT '检疫票登记主键',
                                                 `spot_check_id` bigint(20) UNSIGNED NOT NULL COMMENT '食安检验主键',
                                                 `pork_arrange_id` bigint(20) UNSIGNED NOT NULL COMMENT '理货数据主键',
                                                 `auditor_id` bigint(20) NULL DEFAULT NULL COMMENT '审核员',
                                                 `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
                                                 `audit_status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核状态',
                                                 `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
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
                                                 PRIMARY KEY (`id`) USING BTREE,
                                                 UNIQUE INDEX `idx_pork_arrange_audit_tenant_pork_report_id_tenant_deleted`(`pork_report_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '理货审核表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_pork_arrange_audit_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_pork_arrange_audit_log`  (
                                                     `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                                     `auditor_id` bigint(20) UNSIGNED NOT NULL COMMENT '审核员主键',
                                                     `pork_arrange_audit_id` bigint(20) UNSIGNED NOT NULL COMMENT '理货审核主键',
                                                     `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核状态',
                                                     `time` datetime NOT NULL COMMENT '审核时间',
                                                     `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '不通过原因',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '理货审核记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_pork_arrange_cheat
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_pork_arrange_cheat`  (
                                                 `id` bigint(20) NOT NULL,
                                                 `pork_arrange_detail_id` bigint(20) NULL DEFAULT NULL COMMENT '理货详情主键',
                                                 `cheat` tinyint(1) NULL DEFAULT NULL COMMENT '作弊状态',
                                                 `tenant` int(10) NULL DEFAULT NULL,
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '理货作弊状态修改记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_pork_arrange_detail
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_pork_arrange_detail`  (
                                                  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                                  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理货编码',
                                                  `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '批发商主键',
                                                  `pork_report_id` bigint(20) NOT NULL COMMENT '报备主键',
                                                  `pork_report_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报备号',
                                                  `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型主键',
                                                  `goods_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型名称',
                                                  `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识',
                                                  `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品主键',
                                                  `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
                                                  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电子标签',
                                                  `foot_mark_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脚标编码',
                                                  `weight` double(10, 2) NOT NULL COMMENT '净重',
                                                  `standard_weight` double(10, 2) NULL DEFAULT NULL COMMENT '标准重量',
                                                  `weight_compliant` tinyint(1) NULL DEFAULT NULL COMMENT '重量是否达标',
                                                  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '销售价格',
                                                  `place` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '理货地点',
                                                  `pork_arrange_time` datetime NOT NULL COMMENT '理货时间',
                                                  `passed` tinyint(1) NULL DEFAULT NULL COMMENT '是否通过',
                                                  `cheat` tinyint(1) NULL DEFAULT 0 COMMENT '是否作弊',
                                                  `cheat_updated` tinyint(1) NULL DEFAULT NULL COMMENT '作弊状态是否更改',
                                                  `pic_paths` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片路径',
                                                  `transfer_files` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转存后的信息',
                                                  `transfer_number` int(2) UNSIGNED NULL DEFAULT NULL COMMENT '转存次数',
                                                  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户主键',
                                                  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
                                                  `create_time` datetime NOT NULL COMMENT '创建时间',
                                                  PRIMARY KEY (`id`) USING BTREE,
                                                  UNIQUE INDEX `idx_pork_arrange_detail_tenant_pork_report_code_label`(`pork_report_code`, `label`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '理货数据详情表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_pork_arrange_detail_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_pork_arrange_detail_log`  (
                                                      `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                                      `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理货编码',
                                                      `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '批发商主键',
                                                      `pork_report_id` bigint(20) NOT NULL COMMENT '报备主键',
                                                      `pork_report_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报备号',
                                                      `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型主键',
                                                      `goods_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型名称',
                                                      `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识',
                                                      `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品主键',
                                                      `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
                                                      `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电子标签',
                                                      `foot_mark_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脚标编码',
                                                      `weight` double(10, 2) NOT NULL COMMENT '净重',
                                                      `standard_weight` double(10, 2) NULL DEFAULT NULL COMMENT '标准重量',
                                                      `weight_compliant` tinyint(1) NULL DEFAULT NULL COMMENT '重量是否达标',
                                                      `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '销售价格',
                                                      `place` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '理货地点',
                                                      `pork_arrange_time` datetime NOT NULL COMMENT '理货时间',
                                                      `passed` tinyint(1) NULL DEFAULT NULL COMMENT '是否通过',
                                                      `cheat` tinyint(1) NULL DEFAULT 0 COMMENT '是否作弊',
                                                      `pic_paths` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片路径',
                                                      `transfer_files` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转存后的信息',
                                                      `transfer_number` int(2) UNSIGNED NULL DEFAULT NULL COMMENT '转存次数',
                                                      `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户主键',
                                                      `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
                                                      `create_time` datetime NOT NULL COMMENT '创建时间',
                                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '理货数据详情表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_pork_report
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_pork_report`  (
                                          `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                          `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报备号',
                                          `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '批发商主键',
                                          `stall_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '档位主键',
                                          `abattoir_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '屠宰场主键',
                                          `report_slices` int(10) UNSIGNED NOT NULL COMMENT '报备片数',
                                          `report_weight` double UNSIGNED NOT NULL COMMENT '报备重量',
                                          `report_time` datetime NOT NULL COMMENT '报备时间',
                                          `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型主键',
                                          `goods_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型名称',
                                          `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识',
                                          `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '报备商品主键',
                                          `goods_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品名称',
                                          `arrived_time` datetime NOT NULL COMMENT '到达时间',
                                          `vehicle_plate` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车辆号牌',
                                          `pratique_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '检疫证号',
                                          `origin_place` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产地名称',
                                          `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报备状态',
                                          `auditor_id` bigint(20) NULL DEFAULT NULL COMMENT '审核人主键',
                                          `audit_remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
                                          `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
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
                                          UNIQUE INDEX `pork_report_idx_code_tenant`(`code`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '猪肉报备信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_pratique_register
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_pratique_register`  (
                                                `id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '主键',
                                                `pork_report_id` bigint(20) UNSIGNED NOT NULL COMMENT '报备主键',
                                                `pork_report_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备号',
                                                `registrant_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '登记员主键',
                                                `pratique_slices` int(10) NULL DEFAULT NULL COMMENT '检疫片数',
                                                `pratique_tickets` int(10) NULL DEFAULT NULL COMMENT '检疫票数',
                                                `pratique_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '检疫证号',
                                                `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                `status` tinyint(1) NULL DEFAULT 0 COMMENT '是否登记',
                                                `register_time` datetime NULL DEFAULT NULL COMMENT '登记时间',
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
                                                PRIMARY KEY (`id`) USING BTREE,
                                                UNIQUE INDEX `idx_pratique_register_pork_report_id_tenant_deleted`(`pork_report_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '检疫登记表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_settlement
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_settlement`  (
                                         `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                         `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户主键',
                                         `order_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号',
                                         `settlement_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '结算编号',
                                         `account_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '账户主键',
                                         `amount` decimal(28, 8) NOT NULL COMMENT '交易金额',
                                         `commission` decimal(28, 8) NOT NULL COMMENT '交易佣金',
                                         `initiate_time` datetime NOT NULL COMMENT '发起时间',
                                         `settlement_time` datetime NULL DEFAULT NULL COMMENT '结算时间',
                                         `status` tinyint(1) NOT NULL COMMENT '结算状态',
                                         `tenant` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编号',
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
                                         UNIQUE INDEX `idx_settlement_tenant_order_code`(`order_code`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '结算表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_settlement_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_settlement_config`  (
                                                `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                                `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算名称',
                                                `way` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算方式',
                                                `start_weight` double NULL DEFAULT NULL COMMENT '起始重量',
                                                `end_weight` double NULL DEFAULT NULL COMMENT '结束重量',
                                                `value` double NULL DEFAULT NULL COMMENT '数值',
                                                `flag` tinyint(1) NOT NULL COMMENT '是否选中',
                                                `tenant` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '结算配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_spot_check
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_spot_check`  (
                                         `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                         `pork_report_id` bigint(20) UNSIGNED NOT NULL COMMENT '报备主键',
                                         `pork_report_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备批次号',
                                         `inspector_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '抽检员主键',
                                         `actual_check_slices` int(10) NULL DEFAULT NULL COMMENT '实际抽检片数',
                                         `required_check_ratio` float UNSIGNED NULL DEFAULT NULL COMMENT '抽检比例',
                                         `required_check_slices` int(10) NULL DEFAULT NULL COMMENT '规定抽检片数',
                                         `clenbuterol` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '瘦肉精',
                                         `watered` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '含水量',
                                         `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否抽检',
                                         `spot_check_time` datetime NULL DEFAULT NULL COMMENT '抽检时间',
                                         `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
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
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE INDEX `idx_spot_check_tenant_pork_report_id`(`pork_report_id`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '食安抽检表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_spot_check_detail
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_spot_check_detail`  (
                                                `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                                `spot_check_id` bigint(20) UNSIGNED NOT NULL COMMENT '食安检测主键',
                                                `spot_check_item_id` bigint(20) UNSIGNED NOT NULL COMMENT '检测项字典主键',
                                                `passed` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否通过',
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
                                                PRIMARY KEY (`id`) USING BTREE,
                                                UNIQUE INDEX `idx_spot_check_detail_tenant_spot_check_id_spot_check_item_id`(`spot_check_id`, `spot_check_item_id`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '抽检详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_spot_check_resource
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_spot_check_resource`  (
                                                  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                                  `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户主键',
                                                  `pork_report_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报备号',
                                                  `pork_arrange_detail_id` bigint(20) UNSIGNED NOT NULL COMMENT '数据详情主键',
                                                  `order_id` bigint(20) UNSIGNED NOT NULL COMMENT '订单主键',
                                                  `tenant` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '食安溯源' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_statistics
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_statistics`  (
                                         `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                         `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户主键',
                                         `pork_report_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '批次号',
                                         `average_price` decimal(10, 2) NOT NULL COMMENT '平均价格',
                                         `highest_price` decimal(10, 2) NOT NULL COMMENT '最高价',
                                         `lower_price` decimal(10, 2) NOT NULL COMMENT '最低价',
                                         `pork_arrange_slices` int(10) UNSIGNED NOT NULL COMMENT '理货片数',
                                         `deal_slices` int(10) UNSIGNED NOT NULL COMMENT '成交片数',
                                         `pork_arrange_weight` double NOT NULL COMMENT '理货重量',
                                         `deal_weight` double NOT NULL COMMENT '成交重量',
                                         `amount` decimal(10, 2) NOT NULL COMMENT '成交金额',
                                         `commission` decimal(10, 2) NOT NULL COMMENT '佣金金额',
                                         `statistics_date` datetime NOT NULL COMMENT '报备时间',
                                         `tenant` int(10) NOT NULL COMMENT '租户编号',
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
                                         UNIQUE INDEX `idx_statistics_tenant_merchant_id_statistics_date`(`tenant`, `supplier_id`, `pork_report_code`, `statistics_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售统计' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_track
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_track`  (
                                    `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                    `supplier_id` bigint(20) NOT NULL COMMENT '商户主键',
                                    `pork_report_id` bigint(20) NOT NULL COMMENT '来货报备主键',
                                    `pork_arrange_audit_id` bigint(20) NULL DEFAULT NULL COMMENT '理货数据审核主键',
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
-- Table structure for meattrade_vip_card
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_vip_card`  (
                                       `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                       `supplier_id` bigint(20) UNSIGNED NOT NULL COMMENT '商户主键',
                                       `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员卡号',
                                       `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
                                       `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
                             `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                             `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
                             `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                             `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
                             `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE INDEX `idx_vip_card_code_deleted`(`code`, `deleted`) USING BTREE,
                                       INDEX `idx_vip_card_merchant_id`(`supplier_id`) USING BTREE,
                                       INDEX `idx_vip_card_tenant`(`tenant`) USING BTREE,
                                       INDEX `idx_vip_card_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商户会员卡信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for meattrade_withdraw
-- ----------------------------

CREATE TABLE IF NOT EXISTS `meattrade_withdraw`  (
                                       `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                       `short_id` int(11) NOT NULL COMMENT '主键后六位',
                                       `user_id` bigint(20) NOT NULL COMMENT '用户主键',
                                       `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
                                       `user_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户电话',
                                       `supplier_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '商户主键',
                                       `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户名称',
                                       `supplier_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户电话',
                                       `bank_card_json` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提现银行卡信息',
                                       `amount` decimal(28, 8) UNSIGNED NOT NULL COMMENT '提现金额',
                                       `owner` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申请人',
                                       `auditor` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员',
                                       `completer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转账员',
                                       `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提现备注',
                                       `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
                                       `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
                                       `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
                                       `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提现状态',
                                       `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
                                       `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
                             `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                             `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
                             `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                             `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
                             `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       INDEX `idx_withdraw_user_id`(`user_id`) USING BTREE,
                                       INDEX `idx_withdraw_merchant_id`(`supplier_id`) USING BTREE,
                                       INDEX `idx_withdraw_short_id`(`short_id`) USING BTREE,
                                       INDEX `idx_withdraw_tenant`(`tenant`) USING BTREE,
                                       INDEX `idx_withdraw_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账户提现记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for stat_role_panel
-- ----------------------------

CREATE TABLE IF NOT EXISTS `stat_role_panel`  (
                                    `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                    `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色主键',
                                    `panel_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '面板类型',
                                    `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE INDEX `idx_stat_role_panel_role_id_panel_type`(`role_id`, `panel_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for weight_loss
-- ----------------------------

CREATE TABLE IF NOT EXISTS `weight_loss`  (
                                `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
                                `start_time` double NOT NULL COMMENT '起点时间',
                                `end_time` double NOT NULL COMMENT '终点时间',
                                `loss` double NOT NULL COMMENT '损耗重量',
                                `enabled` tinyint(1) NOT NULL COMMENT '是否启用',
                                `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                `tenant` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '重量损耗表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
