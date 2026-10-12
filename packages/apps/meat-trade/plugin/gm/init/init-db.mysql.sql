CREATE DATABASE IF NOT EXISTS `cube-biz-meattrade-plugin` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-biz-meattrade-plugin`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for couple
-- ----------------------------

CREATE TABLE IF NOT EXISTS `couple`  (
                           `id` bigint(20) UNSIGNED NOT NULL,
                           `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '挂钩编号',
                           `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '挂钩状态',
                           `report_time` datetime NULL DEFAULT NULL COMMENT '上报时间',
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
                           UNIQUE INDEX `idx_code_deleted`(`code`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '滑轨挂钩' ROW_FORMAT = DYNAMIC;

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
            'ALTER TABLE `cube-biz-meattrade-plugin`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
            'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `cube-biz-meattrade-plugin`.`databasechangelog` (`ID`, `AUTHOR`, `FILENAME`, `DATEEXECUTED`, `ORDEREXECUTED`, `EXECTYPE`, `MD5SUM`, `DESCRIPTION`, `COMMENTS`, `TAG`, `LIQUIBASE`, `CONTEXTS`, `LABELS`, `DEPLOYMENT_ID`) VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.gm.meattrade.plugin/changelog-1.0.0.mysql.sql', '2023-10-09 10:34:29', 1, 'EXECUTED', '8:e0172a7791e328e742c14c353b49e3ef', 'sql', '', NULL, '4.3.1', NULL, NULL, '6818869108');

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
-- Table structure for tally_push
-- ----------------------------

CREATE TABLE IF NOT EXISTS `tally_push`  (
                               `id` bigint(20) UNSIGNED NOT NULL,
                               `tally_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批次号',
                               `push_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '推送内容',
                               `push_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '推送状态',
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
                               UNIQUE INDEX `idx_tally_no_deleted`(`tally_no`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '理货数据推送信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for third_party_goods_mapping
-- ----------------------------

CREATE TABLE IF NOT EXISTS `third_party_goods_mapping`  (
                                              `id` bigint(20) NOT NULL,
                                              `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品类主键',
                                              `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识',
                                              `third_party_goods_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方商户标识',
                                              `third_party_fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '第三方冷热鲜标识',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '第三方商品映射' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
