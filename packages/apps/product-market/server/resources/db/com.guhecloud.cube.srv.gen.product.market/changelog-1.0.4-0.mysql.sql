-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `goods_pricing_setting`  (
  `id` bigint(20) NOT NULL,
  `notice_minute` int(11) NOT NULL COMMENT '多少分钟后通知',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务编码',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code_index`(`code`, `tenant`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

ALTER TABLE `cube-srv-gen-product-market`.`goods_pricing_task_detail`
ADD COLUMN `notice_time` datetime NULL COMMENT '催促接受任务的时间' AFTER `user_phone`;

ALTER TABLE `cube-srv-gen-product-market`.`goods_pricing_record`
ADD UNIQUE INDEX `task_category_index`(`task_id`, `goods_category_id`, `tenant`) USING BTREE;


CREATE TABLE `goods_pricing_task_transfer`  (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `from_user_id` bigint(20) NULL DEFAULT NULL COMMENT '移交人',
  `from_user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '移交人姓名',
  `to_user_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人',
  `to_user_names` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人姓名',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态：successed 成功 refuse 拒绝 confirm 确认',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
