-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` DROP INDEX `idx_pork_arrange_detail_tenant_pork_report_code_label`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD COLUMN `transfer_number` int(2) UNSIGNED NULL DEFAULT NULL COMMENT '转存次数' AFTER `transfer_files`;

ALTER TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail` ADD UNIQUE INDEX `idx_pork_arrange_detail_tenant_pork_report_code_label`(`pork_report_code`, `label`, `tenant`) USING BTREE;

CREATE TABLE `cube-biz-meattrade`.`meattrade_pork_arrange_detail_log`  (
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_pork_arrange_detail_tenant_pork_report_code_label`(`pork_report_code`, `label`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '理货数据详情表' ROW_FORMAT = DYNAMIC;