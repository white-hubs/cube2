CREATE DATABASE IF NOT EXISTS`cube-srv-gen-property-management` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-srv-gen-property-management`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for assembly_classify
-- ----------------------------

CREATE TABLE IF NOT EXISTS `assembly_classify`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（资产、仓库等）',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父级id',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通用分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of assembly_classify
-- ----------------------------

-- ----------------------------
-- Table structure for assembly_notice
-- ----------------------------

CREATE TABLE IF NOT EXISTS `assembly_notice`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `msg_id` bigint(20) NULL DEFAULT NULL COMMENT '消息id',
  `data_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息配置json',
  `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知消息配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of assembly_notice
-- ----------------------------

-- ----------------------------
-- Table structure for assembly_notice_condition
-- ----------------------------

CREATE TABLE IF NOT EXISTS `assembly_notice_condition`  (
  `id` bigint(20) NOT NULL,
  `notice_id` bigint(20) NULL DEFAULT NULL COMMENT '通知id(assembly_notice表id)',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知条件类型',
  `value` int(11) NULL DEFAULT NULL COMMENT '通知时间',
  `msg_id` bigint(20) NULL DEFAULT NULL COMMENT '消息id',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `module_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块类型',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of assembly_notice_condition
-- ----------------------------

-- ----------------------------
-- Table structure for assembly_notice_template
-- ----------------------------

CREATE TABLE IF NOT EXISTS `assembly_notice_template`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of assembly_notice_template
-- ----------------------------

-- ----------------------------
-- Table structure for assembly_notice_user
-- ----------------------------

CREATE TABLE IF NOT EXISTS `assembly_notice_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `notice_id` bigint(20) NOT NULL COMMENT '消息配置id',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `type_ids` bigint(20) NOT NULL COMMENT '类型id',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通知人员类型',
  `condition_id` bigint(20) NULL DEFAULT NULL COMMENT '条件id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of assembly_notice_user
-- ----------------------------

-- ----------------------------
-- Table structure for consumable
-- ----------------------------

CREATE TABLE IF NOT EXISTS `consumable`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '类型ID',
  `brand` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌',
  `model` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '型号',
  `goods_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品码',
  `unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `quality_period` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '质保期限',
  `stock_max` int(11) NULL DEFAULT NULL COMMENT '库存上线',
  `stock_min` int(11) NULL DEFAULT NULL COMMENT '库存下线',
  `use_limit` int(11) NULL DEFAULT NULL COMMENT '限领数量（人/月）',
  `stock` int(11) NULL DEFAULT NULL COMMENT '当前库存数量',
  `amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '总金额',
  `file_ids` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件IDS',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumable
-- ----------------------------

-- ----------------------------
-- Table structure for consumable_add_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `consumable_add_record`  (
  `id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `add_date` date NOT NULL COMMENT '入库日期',
  `supplier` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商',
  `handler` bigint(50) NULL DEFAULT NULL COMMENT '处理人',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `business_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型(审批：APPROVE 盘点：CHECK)',
  `tenant` int(11) NOT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `depository_numbers` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库单号',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumable_add_record
-- ----------------------------

-- ----------------------------
-- Table structure for consumable_bim_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `consumable_bim_config`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `scene_type` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '场景类型',
  `scene_type_sequence` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型序列号',
  `business_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '业务主键',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_business_id_scene_type_tenant_deleted`(`scene_type`, `business_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设施业务场景' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumable_bim_config
-- ----------------------------

-- ----------------------------
-- Table structure for consumable_category
-- ----------------------------

CREATE TABLE IF NOT EXISTS `consumable_category`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级ID',
  `level` int(11) NULL DEFAULT NULL COMMENT '层级',
  `sequence` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级序列',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumable_category
-- ----------------------------

-- ----------------------------
-- Table structure for consumable_check
-- ----------------------------

CREATE TABLE IF NOT EXISTS `consumable_check`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盘点单名称',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '盘点仓库',
  `start_time` date NULL DEFAULT NULL COMMENT '盘点开始时间',
  `end_time` date NULL DEFAULT NULL COMMENT '盘点结束时间',
  `checker_id` bigint(20) NULL DEFAULT NULL COMMENT '盘点负责人ID(用户ID)',
  `checker` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盘点负责人',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `previous_check_start_time` date NULL DEFAULT NULL COMMENT '上次盘点开始时间',
  `previous_check_end_time` date NULL DEFAULT NULL COMMENT '上次盘点结束时间',
  `reality_end_time` datetime NULL DEFAULT NULL COMMENT '实际盘点完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumable_check
-- ----------------------------

-- ----------------------------
-- Table structure for consumable_check_detail
-- ----------------------------

CREATE TABLE IF NOT EXISTS `consumable_check_detail`  (
  `id` bigint(20) NOT NULL,
  `check_id` bigint(20) NULL DEFAULT NULL COMMENT '盘点单ID',
  `consumable_id` bigint(20) NULL DEFAULT NULL COMMENT '耗材ID',
  `check_num` int(11) NULL DEFAULT NULL COMMENT '应盘数量',
  `num` decimal(8, 2) NULL DEFAULT NULL COMMENT '实盘数量',
  `checker_id` bigint(20) NULL DEFAULT NULL COMMENT '盘点人ID',
  `checker_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盘点人姓名',
  `check_time` datetime NULL DEFAULT NULL COMMENT '盘点时间',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumable_check_detail
-- ----------------------------

-- ----------------------------
-- Table structure for consumable_reduce_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `consumable_reduce_record`  (
  `id` bigint(20) NOT NULL,
  `store_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `dept_id` bigint(20) NOT NULL COMMENT '领用部门',
  `claimant` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '领用人',
  `reduce_date` date NOT NULL COMMENT '领用日期',
  `handler` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理人',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `business_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型(审批：APPROVE 盘点：CHECK)',
  `tenant` int(11) NOT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `depository_numbers` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库单号',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumable_reduce_record
-- ----------------------------

-- ----------------------------
-- Table structure for consumable_stock_detail
-- ----------------------------

CREATE TABLE IF NOT EXISTS `consumable_stock_detail`  (
  `id` bigint(20) NOT NULL,
  `stock_id` bigint(20) NOT NULL COMMENT '出入库记录ID',
  `stock_flag` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '出入库标记(ADDREDUCE)',
  `consumable_id` bigint(20) NOT NULL COMMENT '耗材ID',
  `num` int(11) NULL DEFAULT NULL COMMENT '数量',
  `price` decimal(8, 2) NULL DEFAULT NULL COMMENT '单价',
  `amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '总金额',
  `init_num` decimal(8, 2) NULL DEFAULT NULL COMMENT '初始数量',
  `init_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '初始金额',
  `init_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '初始单价（该字段预留）',
  `tenant` int(11) NOT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumable_stock_detail
-- ----------------------------

-- ----------------------------
-- Table structure for consumable_store_stock
-- ----------------------------

CREATE TABLE IF NOT EXISTS `consumable_store_stock`  (
  `id` bigint(20) NOT NULL,
  `store_id` bigint(20) NULL DEFAULT NULL COMMENT '仓库ID',
  `consumable_id` bigint(20) NULL DEFAULT NULL COMMENT '耗材ID',
  `consumable_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '耗材名称',
  `num` int(11) NULL DEFAULT NULL COMMENT '库存量',
  `avg_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '库存均价',
  `amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '库存金额',
  `position` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存放位置（扩展字段）',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `store_consumable_id`(`store_id`, `consumable_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '记录仓库中 每种耗材的库存量' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumable_store_stock
-- ----------------------------

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
    'ALTER TABLE `cube-srv-gen-property-management`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/test001.mysql.sql', '2023-07-24 02:03:04', 1, 'EXECUTED', '8:e01927cbba3bd524cca71e5edf2075c7', 'sql', '', NULL, '4.3.1', NULL, NULL, '0178584772');
INSERT IGNORE INTO `databasechangelog` VALUES ('raw', 'includeAll', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.1.0.mysql.sql', '2023-10-09 13:42:00', 1, 'EXECUTED', '8:3d11e48eb9b824afe0e95811752b7a43', 'sql', '', NULL, '4.3.1', NULL, NULL, '6830114694');
INSERT IGNORE INTO `databasechangelog` VALUES ('raw', 'includeAll', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.0.0.mysql.sql', '2023-10-09 13:42:00', 1, 'EXECUTED', '8:3d11e48eb9b824afe0e95811752b7a43', 'sql', '', NULL, '4.3.1', NULL, NULL, '6830114694');
INSERT IGNORE INTO `databasechangelog` VALUES ('raw', 'includeAll', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.3.0.mysql.sql', '2023-10-09 13:42:00', 1, 'EXECUTED', '8:3d11e48eb9b824afe0e95811752b7a43', 'sql', '', NULL, '4.3.1', NULL, NULL, '6830114694');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.4.0.mysql.sql', '2023-12-21 21:06:39', 2, 'EXECUTED', '8:e6b1677578c2c710871a0d9df2f9d7be', 'sql', '', NULL, '4.3.1', NULL, NULL, '3163999360');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.4.1.mysql.sql', '2023-12-21 21:06:39', 3, 'EXECUTED', '8:533d778eaa08022c4c77fbcfc7436080', 'sql', '', NULL, '4.3.1', NULL, NULL, '3163999691');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.4.1-2.mysql.sql', '2023-12-21 21:06:39', 4, 'EXECUTED', '8:da72e4a22b3f0f68b18125d547f23d2b', 'sql', '', NULL, '4.3.1', NULL, NULL, '3163999860');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.5.1-1.mysql.sql', '2024-01-25 19:18:12', 5, 'EXECUTED', '8:0aadb8f5d5a962018d805c22a05d8f26', 'sql', '', NULL, '4.3.1', NULL, NULL, '6181491936');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.5.1-2.mysql.sql', '2024-01-25 19:18:12', 6, 'EXECUTED', '8:d1f2e7ac6a4762ac7369a0be181442ac', 'sql', '', NULL, '4.3.1', NULL, NULL, '6181492105');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.6.0-1.mysql.sql', '2024-02-22 20:31:20', 7, 'EXECUTED', '8:3769d51ee87d27ad7b4a6ce9225365f1', 'sql', '', NULL, '4.3.1', NULL, NULL, '8605080689');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.6.1-1.mysql.sql', '2024-02-23 18:32:12', 8, 'EXECUTED', '8:e1e4f14dd9fe9052171d6510194f7e62', 'sql', '', NULL, '4.3.1', NULL, NULL, '8684332077');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-table-1', 'lijie', 'classpath:db/com.guhecloud.cube.srv.gen.property.management/changelog-1.7.0-1.mysql.sql', '2024-03-28 18:50:25', 9, 'EXECUTED', '8:4b2d168f2a413a42495040ea11ae2a89', 'sql', '', NULL, '4.3.1', NULL, NULL, '1623025208');

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
-- Table structure for property_allot
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_allot`  (
  `id` bigint(11) NOT NULL COMMENT '主键',
  `area_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域id',
  `place` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出库后的存放地点',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `files` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `allot_numbers` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '出库单号',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `over_dep_id` bigint(20) NULL DEFAULT NULL COMMENT '移交部门',
  `over_dep_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '移交部门名称',
  `receive_dep_id` bigint(20) NULL DEFAULT NULL COMMENT '接收部门id',
  `receive_dep_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收部门名称',
  `receive_user_id` bigint(20) NULL DEFAULT NULL COMMENT '接收人',
  `receive_time` date NOT NULL COMMENT '接收日期',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核状态',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型（派发、调拨）',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资产派发、调拨' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_allot
-- ----------------------------

-- ----------------------------
-- Table structure for property_allot_relation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_allot_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `property_id` bigint(20) NOT NULL COMMENT '资产id',
  `allot_id` bigint(20) NOT NULL COMMENT '出库id',
  `allot_numbers` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '出库单号',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_allot_relation
-- ----------------------------

-- ----------------------------
-- Table structure for property_bim_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_bim_config`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `scene_type` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '场景类型',
  `scene_type_sequence` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型序列号',
  `business_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '业务主键',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_business_id_scene_type_tenant_deleted`(`scene_type`, `business_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设施业务场景' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_bim_config
-- ----------------------------

-- ----------------------------
-- Table structure for property_check_plan
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_check_plan`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `task_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盘点计划名称',
  `duty_user_id` bigint(20) NOT NULL COMMENT '盘点责任人id',
  `check_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盘点方式',
  `choose_type` int(1) NOT NULL COMMENT '是否允许手动选择',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `estimate_end_time` date NULL DEFAULT NULL COMMENT '预计完成时间',
  `reality_end_time` date NULL DEFAULT NULL COMMENT '实际完成时间',
  `data_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据过滤条件',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `property_number` int(11) NOT NULL COMMENT '盘点资产数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盘点计划' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_check_plan
-- ----------------------------

-- ----------------------------
-- Table structure for property_check_task
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_check_task`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `plan_id` bigint(20) NOT NULL COMMENT '计划id',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务状态（已完成、未完成）',
  `plan_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盘点计划名称',
  `check_user_id` bigint(20) NOT NULL COMMENT '盘点责任人id',
  `check_total_number` int(11) NOT NULL COMMENT '盘点资产数',
  `check_complete_number` int(11) NOT NULL DEFAULT 0 COMMENT '已盘点',
  `check_win_number` int(11) NOT NULL DEFAULT 0 COMMENT '盘赢',
  `check_loss_number` int(11) NOT NULL DEFAULT 0 COMMENT '盘亏',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `estimate_end_time` date NULL DEFAULT NULL COMMENT '预计完成时间',
  `reality_end_time` date NULL DEFAULT NULL COMMENT '实际完成时间',
  `abnormal_number` int(11) NULL DEFAULT 0 COMMENT '异常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资产盘点任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_check_task
-- ----------------------------

-- ----------------------------
-- Table structure for property_check_task_admin
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_check_task_admin`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `org_id` bigint(20) NOT NULL COMMENT '部门id',
  `admin_id` bigint(20) NOT NULL COMMENT '资产管理员id',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `remarks
remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门资产管理员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_check_task_admin
-- ----------------------------

-- ----------------------------
-- Table structure for property_check_task_relation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_check_task_relation`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `property_id` bigint(20) NOT NULL COMMENT '资产id',
  `plan_id` bigint(20) NOT NULL COMMENT '盘点任务id',
  `task_id` bigint(20) NOT NULL COMMENT '具体盘点信息id',
  `check_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盘点状态',
  `check_time` datetime NULL DEFAULT NULL COMMENT '盘点时间',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `abnormal_type` bigint(50) NULL DEFAULT NULL COMMENT '异常类型',
  `abnormal_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常备注',
  `abnormal_file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常文件id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '盘点任务资产' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_check_task_relation
-- ----------------------------

-- ----------------------------
-- Table structure for property_depository_detailed
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_depository_detailed`  (
  `id` bigint(20) NOT NULL,
  `source_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '来源id',
  `source_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源名称',
  `receipt_numbers` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货单号',
  `start_time` date NOT NULL COMMENT '入库日期',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库状态',
  `depository_numbers` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '入库单号（RK+日期）',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id（工作流）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库基本信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_depository_detailed
-- ----------------------------

-- ----------------------------
-- Table structure for property_detailed
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_detailed`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `serial_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资产编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资产名称',
  `type_id` bigint(20) NULL DEFAULT NULL COMMENT '资产类别id',
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产类别名称',
  `purchase_date` date NULL DEFAULT NULL COMMENT '购入时间',
  `maintenance_time` int(6) NULL DEFAULT NULL COMMENT '维保期限（月）',
  `maintenance_date` date NULL DEFAULT NULL COMMENT '维保到期时间',
  `usage_time` int(6) NULL DEFAULT NULL COMMENT '使用期限（月）',
  `usage_date` date NULL DEFAULT NULL COMMENT '逾龄时间',
  `norms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `admin_id` bigint(20) NULL DEFAULT NULL COMMENT '管理员id',
  `area_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `depository_numbers` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库单号（RK+日期）',
  `number` int(11) NULL DEFAULT NULL COMMENT '数量',
  `price` decimal(12, 2) NULL DEFAULT NULL COMMENT '原值',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `storage_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存放地点',
  `supplier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商',
  `supplier_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商联系方式',
  `supplier_user` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商联系人',
  `maintenance_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维保说明',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资产状态',
  `authority` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据过滤条件，现用于盘点时新增资产时，其他模块不需要展示',
  `imgs` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产图片',
  `file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产附件',
  `device_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备编码',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `source_id` bigint(20) NULL DEFAULT NULL COMMENT '来源id',
  `start_time` date NULL DEFAULT NULL COMMENT '入库日期',
  `receive_dep_id` bigint(20) NULL DEFAULT NULL COMMENT '接收部门id',
  `receive_dep_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收部门名称',
  `receive_user_id` bigint(20) NULL DEFAULT NULL COMMENT '接收人',
  `level` int(2) NULL DEFAULT NULL COMMENT '排序',
  `receive_user_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人电话',
  `finance_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '财务编码',
  `maintenance_dep_id` bigint(20) NULL DEFAULT NULL COMMENT '维护部门',
  `maintenance_dep_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维护部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资产明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_detailed
-- ----------------------------

-- ----------------------------
-- Table structure for property_history
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_history`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `property_id` bigint(20) NOT NULL COMMENT '资产id',
  `handle_time` date NOT NULL COMMENT '处理时间',
  `handle_type_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '处理方式id',
  `handle_type_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '处理方式名称',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理方式',
  `department_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '处理人部门id',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资产使用历史记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_history
-- ----------------------------

-- ----------------------------
-- Table structure for property_repair_report
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_repair_report`  (
  `id` bigint(20) NOT NULL,
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id（工作流）',
  `property_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资产状态',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核结果',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(11) NOT NULL COMMENT '租户id',
  `property_id` bigint(20) NOT NULL COMMENT '资产id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资产报修记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_repair_report
-- ----------------------------

-- ----------------------------
-- Table structure for property_weed_out
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_weed_out`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `weed_out_user_id` bigint(20) NOT NULL COMMENT '处理人id',
  `manage_time` datetime NOT NULL COMMENT '清理时间',
  `cause_id` bigint(20) NOT NULL COMMENT '清理原因',
  `overdue` int(1) NULL DEFAULT NULL COMMENT '是否过期',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `files` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `weed_out_numbers` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报废单号',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批状态',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '清理报废' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_weed_out
-- ----------------------------

-- ----------------------------
-- Table structure for property_weed_out_relation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `property_weed_out_relation`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `property_id` bigint(20) NOT NULL COMMENT '资产id',
  `weed_out_id` bigint(20) NOT NULL COMMENT '报废id',
  `weed_out_numbers` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报废单号',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of property_weed_out_relation
-- ----------------------------

-- ----------------------------
-- Table structure for risk_source_bim_config
-- ----------------------------

CREATE TABLE IF NOT EXISTS `risk_source_bim_config`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `scene_type` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '场景类型',
  `scene_type_sequence` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景类型序列号',
  `business_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `business_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '业务主键',
  `tenant` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_business_id_scene_type_tenant_deleted`(`scene_type`, `business_id`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设施业务场景' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of risk_source_bim_config
-- ----------------------------

-- ----------------------------
-- Table structure for risk_source_ledger
-- ----------------------------

CREATE TABLE IF NOT EXISTS `risk_source_ledger`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `serial_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '风险源编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '风险源名称',
  `type_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '风险源类别code',
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '风险源类别名称',
  `usage_date` date NULL DEFAULT NULL COMMENT '使用到期日期',
  `area_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `number` int(11) NULL DEFAULT NULL COMMENT '数量',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产附件',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `receive_dep_id` bigint(20) NULL DEFAULT NULL COMMENT '接收部门id',
  `receive_dep_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收部门名称',
  `receive_user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人',
  `level` int(2) NULL DEFAULT NULL COMMENT '排序',
  `receive_user_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人电话',
  `maintenance_dep_id` bigint(20) NULL DEFAULT NULL COMMENT '维护部门',
  `maintenance_dep_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维护部门',
  `stall_id` bigint(20) NULL DEFAULT NULL COMMENT '档位id',
  `stall_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位名称',
  `site_point_id` bigint(20) NULL DEFAULT NULL COMMENT '现场点',
  `attribute` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性',
  `site_point_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现场点名称',
  `bim_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型编码',
  `produce_factory` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产厂家',
  `produce_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生产厂家地址',
  `delivery_company` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配送公司',
  `delivery_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配送公司地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '风险源台账' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of risk_source_ledger
-- ----------------------------

-- ----------------------------
-- Table structure for risk_source_parts
-- ----------------------------

CREATE TABLE IF NOT EXISTS `risk_source_parts`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配件编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配件名称',
  `expire_date` date NULL DEFAULT NULL COMMENT '到期日期',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `risk_source_ledger_id` bigint(20) NULL DEFAULT NULL COMMENT '风险源台账id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '风险源台账-配件信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of risk_source_parts
-- ----------------------------

-- ----------------------------
-- Table structure for storehouse
-- ----------------------------

CREATE TABLE IF NOT EXISTS `storehouse`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库编号',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库地址',
  `storeman` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '仓库管理员',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of storehouse
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
