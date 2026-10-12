-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `administrative_division`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id或行政区域编号',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级区域ID',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` bit(1) NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '行政区划' ROW_FORMAT = Compact;


CREATE TABLE `goods_price_warn_category`  (
  `id` bigint(20) NOT NULL,
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '规则组ID',
  `rule_id` bigint(20) NULL DEFAULT NULL COMMENT '规则ID',
  `price_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格预警类型',
  `goods_category_id` bigint(20) NULL DEFAULT NULL COMMENT '品类ID',
  `goods_category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `goods_category_ids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级品类IDS',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `rule_pricetype_cateid_index`(`rule_id`, `price_type`, `goods_category_id`) USING BTREE COMMENT '规则类型和品类ID唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预警适用品类' ROW_FORMAT = Dynamic;


CREATE TABLE `goods_price_warn_record`  (
  `id` bigint(20) NOT NULL,
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '规则组ID',
  `rule_id` bigint(20) NULL DEFAULT NULL COMMENT '预警规则ID',
  `rule_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预警名称',
  `pricing_record_id` bigint(20) NULL DEFAULT NULL COMMENT '采价记录ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '采价员ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采价员姓名',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '所在部门ID',
  `dept_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所在部门名称',
  `pricing_time` datetime(0) NULL DEFAULT NULL COMMENT '采价时间',
  `goods_category_id` bigint(20) NULL DEFAULT NULL COMMENT '商品品类',
  `goods_category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品类名称',
  `max_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '最高价',
  `mid_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '中间价',
  `min_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '最低价',
  `price_exp_desc` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格异常描述',
  `price_exp` tinyint(4) NULL DEFAULT NULL COMMENT '价格是否异常',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `handler_id` bigint(20) NULL DEFAULT NULL COMMENT '处理人ID',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '是否处理',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '价格预警记录' ROW_FORMAT = Dynamic;


CREATE TABLE `goods_price_warn_record_log`  (
  `id` bigint(20) NOT NULL,
  `warn_id` bigint(20) NULL DEFAULT NULL COMMENT '预警记录ID',
  `handler_id` bigint(20) NULL DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人姓名',
  `old_max_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '原始最高价',
  `old_mid_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '原始中间价',
  `old_min_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '原始最低价',
  `new_max_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '修改最高价',
  `new_mid_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '修改中间价',
  `new_min_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '修改对低价',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


CREATE TABLE `goods_price_warn_rule`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `price_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格类型(maxmidmin)',
  `compare_condition` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '价格条件(>=<=)',
  `compare_value` decimal(8, 2) NULL DEFAULT NULL COMMENT '比较值',
  `compare_value_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '比较值类型(digitpercentage)',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '规则组ID',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采价预警规则' ROW_FORMAT = Dynamic;


CREATE TABLE `goods_price_warn_rule_group`  (
  `id` bigint(20) NOT NULL,
  `group_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '告警规则组名称',
  `creator_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT 'tru开启 false关闭',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报警规则组' ROW_FORMAT = Dynamic;


CREATE TABLE `goods_price_warn_user`  (
  `id` bigint(20) NOT NULL,
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '规则组ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` bigint(20) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发生预警时，需要推送的人员' ROW_FORMAT = Dynamic;


CREATE TABLE `goods_pricing_plan`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划名称',
  `urgency` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '紧急程度',
  `plan_desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划描述',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `publish_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布类型',
  `publish_period` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间段',
  `publish_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间点',
  `cron` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `enabled` tinyint(4) NULL DEFAULT NULL COMMENT '是否启用',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划状态（exec\\stop\\over）',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `xxl_task_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'xxljob的任务ID',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采集计划管理' ROW_FORMAT = Dynamic;


CREATE TABLE `goods_pricing_plan_detail`  (
  `id` bigint(20) NOT NULL,
  `plan_id` bigint(20) NULL DEFAULT NULL COMMENT '采价计划ID',
  `goods_category_ids` varchar(2046) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类IDS',
  `goods_category_names` varchar(2046) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `user_ids` varchar(2046) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采价人员IDS',
  `user_names` varchar(2046) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采价人员姓名',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采价计划详情' ROW_FORMAT = Dynamic;


CREATE TABLE `goods_pricing_record`  (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '采价任务ID',
  `task_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采价任务名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '采价员ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采价员姓名',
  `user_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采价员电话',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `goods_category_id` bigint(20) NULL DEFAULT NULL COMMENT '商品品类ID',
  `goods_category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品品类名称',
  `goods_category_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类父级IDS',
  `stall_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位编码',
  `stall_boss` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主名称',
  `stall_boss_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主联系电话',
  `goods_from` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品来源（产地）',
  `max_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '最高价',
  `mid_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '中间价',
  `min_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '最低价',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `goods_category_id_index`(`goods_category_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品采价记录' ROW_FORMAT = Dynamic;


CREATE TABLE `goods_pricing_task`  (
  `id` bigint(20) NOT NULL,
  `plan_id` bigint(20) NULL DEFAULT NULL COMMENT '采价计划ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `urgency` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '紧急程度',
  `task_desc` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `cron` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务状态 0 未完成 1已完成',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采价计划实例（采价任务）' ROW_FORMAT = Dynamic;


CREATE TABLE `goods_pricing_task_detail`  (
  `id` bigint(20) NOT NULL,
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '采价任务ID',
  `goods_category_ids` varchar(2046) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类父级IDS',
  `goods_category_names` varchar(2046) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `user_ids` varchar(2046) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采价人员IDS',
  `user_names` varchar(2046) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采价员姓名',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '接受人员ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人员名称',
  `user_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采价员联系电话',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '采价任务详情' ROW_FORMAT = Dynamic;


CREATE TABLE `incoming_quantity`  (
  `id` bigint(20) NOT NULL,
  `date_no` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来货日期',
  `cust_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户ID',
  `cust_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `cust_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户电话',
  `goods_category_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来货品类ID',
  `incoming_quantity` decimal(8, 2) NULL DEFAULT NULL COMMENT '来货量',
  `unit` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


CREATE TABLE `market_goods_category`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品分类编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品分类名称',
  `level` int(10) UNSIGNED NOT NULL COMMENT '商品分类层级',
  `parent_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '父类主键',
  `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '序列号',
  `enabled` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否启用',
  `tenant` int(10) UNSIGNED NOT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_goods_category_tenant_deleted_name`(`tenant`, `deleted`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品品类信息表' ROW_FORMAT = Dynamic;


CREATE TABLE `market_stall`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位名称',
  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '档位类型',
  `area` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位区域',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '具体位置',
  `built_up_area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '建筑面积',
  `practical_area` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实用面积',
  `dept_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门主键',
  `dept_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `lease_type` tinyint(1) NULL DEFAULT 0 COMMENT '出租类型',
  `leased` tinyint(1) NULL DEFAULT NULL COMMENT '是否出租',
  `boss_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主ID',
  `boss_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主名称',
  `boss_card_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主身份证号码',
  `boss_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档主电话',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `bim_bind` tinyint(1) NULL DEFAULT NULL COMMENT '模型绑定',
  `enabled` tinyint(3) UNSIGNED NULL DEFAULT 0 COMMENT '是否启用',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_stall_tenant_deleted_code`(`code`, `tenant`, `deleted`) USING BTREE,
  UNIQUE INDEX `idx_stall_tenant_deleted_name`(`name`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '档位信息表' ROW_FORMAT = Dynamic;