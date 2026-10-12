-- liquibase formatted sql

-- changeset qqq:1686103358461-1
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for security_panel_config
-- ----------------------------
DROP TABLE IF EXISTS `security_panel_config`;
CREATE TABLE `security_panel_config`  (
  `id` bigint(20) NOT NULL,
  `business_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务主键(设备主键或产品等)',
  `scene_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用场景(如安全设备列表,设备详情等)',
  `panel_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '面板配置',
  `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属应用',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备面板配置' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;