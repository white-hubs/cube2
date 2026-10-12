-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-meattrade`.`meattrade_order_detail` ADD COLUMN `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型主键' AFTER `pork_arrange_detail_id`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_order_detail` ADD COLUMN `goods_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型名称' AFTER `goods_category_id`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_order_detail` ADD COLUMN `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识' AFTER `goods_category_name`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_order_detail` ADD COLUMN `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品主键' AFTER `fresh_label`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_order_detail` ADD COLUMN `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称' AFTER `goods_id`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型主键' AFTER `pork_report_code`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `goods_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型名称' AFTER `goods_category_id`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识' AFTER `goods_category_name`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '商品主键' AFTER `fresh_label`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称' AFTER `goods_id`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `standard_weight` double(10, 2) NULL DEFAULT NULL COMMENT '标准重量' AFTER `weight`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `weight_compliant` tinyint(1) NULL DEFAULT NULL COMMENT '重量是否达标' AFTER `standard_weight`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `cheat_updated` tinyint(1) NULL DEFAULT NULL COMMENT '作弊状态是否更改' AFTER `cheat`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `pic_paths` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片路径' AFTER `cheat_updated`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` MODIFY COLUMN `abattoir_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '屠宰场主键' AFTER `stall_id`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` ADD COLUMN `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型主键' AFTER `report_time`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` ADD COLUMN `goods_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品类型名称' AFTER `goods_category_id`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` ADD COLUMN `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识' AFTER `goods_category_name`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` ADD COLUMN `goods_id` bigint(20) NULL DEFAULT NULL COMMENT '报备商品主键' AFTER `fresh_label`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` ADD COLUMN `goods_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报备商品名称' AFTER `goods_id`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` MODIFY COLUMN `vehicle_plate` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车辆号牌' AFTER `arrived_time`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` MODIFY COLUMN `pratique_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检疫证号' AFTER `vehicle_plate`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_report` MODIFY COLUMN `audit_remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注' AFTER `auditor_id`;

CREATE TABLE `cube-biz-meattrade`.`meattrade_goods_weight_config`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `goods_category_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品类型',
  `goods_category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品类型名称',
  `standard_weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '标准重量',
  `weight_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重量单位',
  `fresh_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冷热鲜标识',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL,
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '滑轨商品品类标准重量配置' ROW_FORMAT = Dynamic;

CREATE TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_cheat`  (
  `id` bigint(20) NOT NULL,
  `pork_arrange_detail_id` bigint(20) NULL DEFAULT NULL COMMENT '理货详情主键',
  `cheat` tinyint(1) NULL DEFAULT NULL COMMENT '作弊状态',
  `tenant` int(10) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '理货作弊状态修改记录' ROW_FORMAT = Dynamic;