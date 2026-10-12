CREATE DATABASE IF NOT EXISTS`cube-srv-gen-attendance-management` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

-- TODO-db: 当前部分表创建和数据初始化未使用 liquibase 管理，后续新增的表和数据初始化均使用 liquibase 管理

USE `cube-srv-gen-attendance-management`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for arrange_work
-- ----------------------------

CREATE TABLE IF NOT EXISTS `arrange_work`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `staff_id` bigint(20) NOT NULL COMMENT '员工ID',
  `staff_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工姓名',
  `staff_mobile` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工电话',
  `staff_organization_id` bigint(20) NULL DEFAULT NULL COMMENT '员工部门',
  `arrange_work_classes_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次Ids',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
  `month` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月份',
  `week` int(1) NULL DEFAULT NULL COMMENT '周几',
  `date` date NULL DEFAULT NULL COMMENT '日期',
  `is_rest` int(1) NOT NULL DEFAULT 0 COMMENT '是否休息',
  `is_holiday` int(1) NOT NULL DEFAULT 0 COMMENT '是否节日',
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
  INDEX `month_idx`(`month`) USING BTREE,
  INDEX `date_idx`(`date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '排班' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of arrange_work
-- ----------------------------

-- ----------------------------
-- Table structure for arrange_work_classes
-- ----------------------------

CREATE TABLE IF NOT EXISTS `arrange_work_classes`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `classes_id` bigint(20) NOT NULL COMMENT '班次ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班次名称',
  `start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间',
  `start_real_time` datetime NULL DEFAULT NULL COMMENT '真实上班时间',
  `before_start_real_time` datetime NULL DEFAULT NULL COMMENT '上班时间前',
  `after_start_real_time` datetime NULL DEFAULT NULL COMMENT '上班时间后',
  `end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间',
  `end_real_time` datetime NULL DEFAULT NULL COMMENT '真实下班时间',
  `before_end_real_time` datetime NULL DEFAULT NULL COMMENT '下班时间前',
  `after_end_real_time` datetime NULL DEFAULT NULL COMMENT '下班时间后',
  `rest_start_real_time` datetime NULL DEFAULT NULL COMMENT '休息开始时间',
  `rest_end_real_time` datetime NULL DEFAULT NULL COMMENT '休息结束时间',
  `mark` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识',
  `end_time_is_morrow` int(1) NOT NULL COMMENT '下班时间是否是次日',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '排班班次' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of arrange_work_classes
-- ----------------------------

-- ----------------------------
-- Table structure for arrange_work_classes_status
-- ----------------------------

CREATE TABLE IF NOT EXISTS `arrange_work_classes_status`  (
  `id` bigint(20) NOT NULL,
  `work_id` bigint(20) NULL DEFAULT NULL COMMENT '排班ID',
  `classes_id` bigint(20) NULL DEFAULT NULL COMMENT '具体班次ID',
  `classes_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '具体班次状态',
  `trigger_action` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '触发动作：CLOCK(打卡) OVERTIME（加班） LEAVE（请假） REPAIR_CLOCK（补卡） CHANGE_CLASSES（换班）',
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
  UNIQUE INDEX `work_classes_index`(`work_id`, `classes_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of arrange_work_classes_status
-- ----------------------------

-- ----------------------------
-- Table structure for att_approve_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_approve_log`  (
  `id` bigint(20) NOT NULL,
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id（工作流）',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审批类型',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核结果',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `clock_in_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡记录id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '申请人id',
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
  `scene_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景code',
  `actual_duration` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实际时长',
  `apply_duration` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预估时长',
  `proportion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '补偿方式',
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '版本号',
  `scene_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '场景名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_approve_log
-- ----------------------------

-- ----------------------------
-- Table structure for att_ask_leave_from
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_ask_leave_from`  (
  `id` bigint(20) NOT NULL,
  `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
  `applicant_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人名称',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `post_id` bigint(20) NULL DEFAULT NULL COMMENT '职位id',
  `post_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位名称',
  `phone` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `enter_date` datetime NULL DEFAULT NULL COMMENT '入职日期',
  `holiday_id` bigint(20) NOT NULL COMMENT '假期类型(规则)id',
  `holiday_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假期类型(规则)名称',
  `into_absence` tinyint(1) NULL DEFAULT 0 COMMENT '是否计入缺勤(true表示计入缺勤',
  `holiday_day_of_hour` int(11) NULL DEFAULT 8 COMMENT '假期规则一天折算时长',
  `holiday_balance` bigint(20) NULL DEFAULT NULL COMMENT '假期余额',
  `leave_start_time` datetime NOT NULL COMMENT '租户主键',
  `leave_end_time` datetime NOT NULL COMMENT '请假结束时间',
  `leave_number` bigint(20) NULL DEFAULT NULL COMMENT '请假时长',
  `leave_location` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休假地址',
  `file_key` bigint(20) NULL DEFAULT NULL COMMENT '文件key',
  `leave_remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假备注',
  `cancel_leave_from_id` bigint(20) NULL DEFAULT NULL COMMENT '销假单id',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假单状态',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
  `deadline_ids` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扣除的记录id',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '请假单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_ask_leave_from
-- ----------------------------

-- ----------------------------
-- Table structure for att_ask_leave_ledger
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_ask_leave_ledger`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `staff_id` bigint(20) NOT NULL COMMENT '人员id',
  `staff_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员名称',
  `holiday_id` bigint(20) NOT NULL COMMENT '假期规则id',
  `holiday_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假期code(类型code)',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `day_of_hour` int(11) NULL DEFAULT 8 COMMENT '一天折算多少小时',
  `into_absence` tinyint(1) NOT NULL DEFAULT 0 COMMENT '计入缺勤(true表示计入',
  `leave_number` bigint(20) NOT NULL COMMENT '请假时间',
  `leave_date` datetime NOT NULL COMMENT '请假日期',
  `leave_from_id` bigint(20) NOT NULL COMMENT '请假单id',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '请假台账(请假单拆分为每天的数据)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_ask_leave_ledger
-- ----------------------------

-- ----------------------------
-- Table structure for att_away_business_from
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_away_business_from`  (
  `id` bigint(20) NOT NULL,
  `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
  `applicant_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人名称',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `phone` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `post_id` bigint(20) NULL DEFAULT NULL COMMENT '职位id',
  `post_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位名称',
  `away_rule_id` bigint(20) NOT NULL COMMENT '出差规则id',
  `away_rule_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `away_start_time` datetime NOT NULL COMMENT '出差开始时间',
  `away_end_time` datetime NOT NULL COMMENT '出差结束时间',
  `away_number` bigint(20) NULL DEFAULT NULL COMMENT '出差时长',
  `file_key` bigint(20) NULL DEFAULT NULL COMMENT '文件key',
  `away_remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出差单状态',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出差单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_away_business_from
-- ----------------------------

-- ----------------------------
-- Table structure for att_away_business_ledger
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_away_business_ledger`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `staff_id` bigint(20) NOT NULL COMMENT '人员id',
  `staff_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员名称',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '考勤组id',
  `away_number` bigint(20) NOT NULL COMMENT '出差时长',
  `away_date` datetime NOT NULL COMMENT '出差日期',
  `away_from_id` bigint(20) NOT NULL COMMENT '出差单id',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出差台账(出差单拆分为每天的数据)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_away_business_ledger
-- ----------------------------

-- ----------------------------
-- Table structure for att_away_business_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_away_business_rule`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '出差规则名称',
  `before_apply` tinyint(1) NULL DEFAULT 1 COMMENT '提前申请',
  `min_ask_unit` int(11) NULL DEFAULT 0 COMMENT '最小请假单位',
  `calculation_way` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间折算方式',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出差规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_away_business_rule
-- ----------------------------

-- ----------------------------
-- Table structure for att_cancel_leave_from
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_cancel_leave_from`  (
  `id` bigint(20) NOT NULL,
  `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
  `applicant_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人名称',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `holiday_id` bigint(20) NOT NULL COMMENT '假期类型(规则)id',
  `holiday_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假期类型(规则)名称',
  `ask_leave_from_id` bigint(20) NOT NULL COMMENT '请假单id',
  `ask_leave_from` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假单对象',
  `cancel_start_time` datetime NOT NULL COMMENT '销假开始时间',
  `cancel_end_time` datetime NOT NULL COMMENT '请假结束时间',
  `cancel_number` bigint(20) NULL DEFAULT NULL COMMENT '请假时长',
  `cancel_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '销假备注',
  `file_key` bigint(20) NULL DEFAULT NULL COMMENT '文件key',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假单状态',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
  `normal` tinyint(1) NULL DEFAULT 1 COMMENT '正常',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销假单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_cancel_leave_from
-- ----------------------------

-- ----------------------------
-- Table structure for att_classes
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_classes`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `classes_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班次类型',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班次名称',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班次简称',
  `start_work_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上班时间',
  `start_clock_before_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上班开始打卡时间',
  `start_clock_after_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上班结束打卡时间',
  `end_work_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下班时间',
  `end_clock_before_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下班开始打卡时间',
  `end_clock_after_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下班结束打卡时间',
  `rest_start_Time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息开始时间',
  `rest_end_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息结束时间',
  `end_time_is_morrow` tinyint(1) NULL DEFAULT 0 COMMENT '跨天班次',
  `dept_Ids` varchar(550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '建议应用范围(部门ids)',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '班次' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_classes
-- ----------------------------

-- ----------------------------
-- Table structure for att_classes_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_classes_record`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `classes_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班次类型',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班次名称',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班次简称',
  `start_work_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上班时间',
  `start_clock_before_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上班开始打卡时间',
  `start_clock_after_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上班结束打卡时间',
  `end_work_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下班时间',
  `end_clock_before_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下班开始打卡时间',
  `end_clock_after_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '下班结束打卡时间',
  `rest_start_Time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息开始时间',
  `rest_end_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息结束时间',
  `end_time_is_morrow` tinyint(1) NULL DEFAULT 0 COMMENT '跨天班次',
  `dept_Ids` varchar(550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '建议应用范围(部门ids)',
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
  `classes_id` bigint(20) NOT NULL COMMENT '班次id',
  `version` int(11) NOT NULL COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_classes_record
-- ----------------------------

-- ----------------------------
-- Table structure for att_clock_device
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_clock_device`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `rules_id` bigint(20) NOT NULL COMMENT '规则id',
  `device_id` bigint(20) NULL DEFAULT NULL COMMENT '设备id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `genre_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `model_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备型号',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备编码',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打卡设备' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_clock_device
-- ----------------------------

-- ----------------------------
-- Table structure for att_clock_in_ledger
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_clock_in_ledger`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `ledger_date` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '统计时间',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `org_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打卡台账' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_clock_in_ledger
-- ----------------------------

-- ----------------------------
-- Table structure for att_clock_in_ledger_details
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_clock_in_ledger_details`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `iedger_id` bigint(20) NOT NULL COMMENT '主表id',
  `week` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '周几',
  `today` int(10) NOT NULL COMMENT '几号',
  `state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡状态',
  `start_work_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间',
  `end_work_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间',
  `start_work_state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班状态',
  `end_work_state` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班状态',
  `end_work_details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班异常信息',
  `start_work_details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班异常信息',
  `start_work_omit` tinyint(1) NULL DEFAULT NULL COMMENT '上班是否有补卡',
  `end_work_omit` tinyint(1) NULL DEFAULT NULL COMMENT '下班是否有补卡',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `classes_id` bigint(20) NULL DEFAULT NULL COMMENT '班次id',
  `classes_version` int(10) NULL DEFAULT NULL COMMENT '班次版本',
  `clock_in_uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡记录uuid',
  `start_work_out` tinyint(1) NULL DEFAULT NULL COMMENT '上班是否为外勤',
  `end_work_out` tinyint(1) NULL DEFAULT NULL COMMENT '下班是否为外勤',
  `start_clock_in_time` datetime NULL DEFAULT NULL COMMENT '上班打卡时间（用于排序）',
  `ledger_belong_date` date NULL DEFAULT NULL COMMENT '日期',
  `user_id` bigint(20) NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `end_clock_in_time` datetime NULL DEFAULT NULL COMMENT '下班时间',
  `actual_start_clock_in_time` datetime NULL DEFAULT NULL COMMENT '上班打卡时间',
  `actual_end_clock_in_time` datetime NULL DEFAULT NULL COMMENT '下班打卡时间',
  `classes_details` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `iedger_id`(`iedger_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打卡台账详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_clock_in_ledger_details
-- ----------------------------

-- ----------------------------
-- Table structure for att_clock_in_records
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_clock_in_records`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名',
  `org_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `classes_id` bigint(20) NULL DEFAULT NULL COMMENT '班次id',
  `classes_version` int(10) NULL DEFAULT NULL COMMENT '班次版本',
  `clock_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '打卡类型（上班：start、下班：end，更新打卡：revise）',
  `uuid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '打卡记录标识',
  `ways` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡方式',
  `clock_in_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '打卡时间(时分)',
  `require_clock_in_time` datetime NOT NULL COMMENT '打卡时间(年月日时分)',
  `actual_clock_in_time` datetime NULL DEFAULT NULL COMMENT '实际打卡时间',
  `state` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '打卡状态（未打卡、正常、早退、迟到，缺卡、无效）',
  `deletion_duration` int(11) NOT NULL COMMENT '迟到、早退时间（分钟）',
  `overtime_duration` int(11) NOT NULL COMMENT '加班时长',
  `clock_in_date` date NULL DEFAULT NULL COMMENT '班次时间（年月日）',
  `clock_in_authority` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡权限',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡地址名称、wifi名称',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `clock_after_time` datetime NOT NULL COMMENT '最晚打卡时间',
  `photo_file_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡照片',
  `photo_file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡照片url',
  `sync_time` datetime NULL DEFAULT NULL COMMENT '同步时间',
  `face_recognition_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人脸识别失败结果',
  `past_data_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '旧数据的类型',
  `out_clock` tinyint(1) NOT NULL COMMENT '是否为外勤',
  `work_omit` tinyint(1) NULL DEFAULT NULL COMMENT '是否补卡',
  `clock_before_time` datetime NULL DEFAULT NULL COMMENT '最早打卡时间',
  `mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识：数据清洗用，无具体意义',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `clock_after_time`(`clock_after_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打卡记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_clock_in_records
-- ----------------------------

-- ----------------------------
-- Table structure for att_clock_location
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_clock_location`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `rules_id` bigint(20) NOT NULL COMMENT '规则id',
  `address` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
  `longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '经度',
  `latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '维度',
  `ranges` int(11) NULL DEFAULT NULL COMMENT '范围(米)',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打卡地点' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_clock_location
-- ----------------------------

-- ----------------------------
-- Table structure for att_clock_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_clock_rule`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `ways` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡方式(逗号隔开)',
  `out_clock` tinyint(1) NULL DEFAULT 0 COMMENT '是否可外勤打卡',
  `face_recognition` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否需要人脸识别',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打卡规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_clock_rule
-- ----------------------------

-- ----------------------------
-- Table structure for att_dumu_message
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_dumu_message`  (
  `id` bigint(20) NOT NULL,
  `msg_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息唯一标识',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '度目推送消息记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_dumu_message
-- ----------------------------

-- ----------------------------
-- Table structure for att_duty
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_duty`  (
  `id` bigint(20) NOT NULL,
  `duty_month` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '值班的月份',
  `duty_date` date NULL DEFAULT NULL COMMENT '值班班的日期',
  `classes_id` bigint(20) NULL DEFAULT NULL COMMENT '班次ID',
  `classes_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次名称',
  `classes_nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次简称',
  `classes_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次类型',
  `classes_end_time_is_morrow` tinyint(1) NULL DEFAULT NULL COMMENT '跨天班次',
  `classes_real_start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `classes_real_end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `staff_id` bigint(20) NULL DEFAULT NULL COMMENT '值班人id',
  `staff_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '值班人名称',
  `staff_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '值班人部门id',
  `staff_dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '值班人部门名称',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '值班表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_duty
-- ----------------------------

-- ----------------------------
-- Table structure for att_duty_staff
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_duty_staff`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `staff_id` bigint(20) NOT NULL COMMENT '人员id',
  `staff_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '人员名称',
  `duty_month` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月份',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '值班人员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_duty_staff
-- ----------------------------

-- ----------------------------
-- Table structure for att_group
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_group`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考勤组名称',
  `scheduling_rule_id` bigint(20) NOT NULL COMMENT '排班规则id',
  `clock_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡规则id',
  `card_rule_ids` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '补卡规则id',
  `overtime_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '加班规则id',
  `holiday_rule_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假规则ids',
  `leave_post_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '离岗规则id',
  `away_business_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '出差规则id',
  `ranges` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '适用范围',
  `group_class_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次ids',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤组' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_group
-- ----------------------------

-- ----------------------------
-- Table structure for att_group_staff
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_group_staff`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `staff_id` bigint(20) NULL DEFAULT NULL COMMENT '人员id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工姓名',
  `mobile` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工电话',
  `entry_date` datetime NULL DEFAULT NULL COMMENT '入职日期',
  `first_pay_social_security_date` datetime NULL DEFAULT NULL COMMENT '首次缴纳社保日期',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组id',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考勤组名称',
  `scheduling_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '排班规则id',
  `clock_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡规则id',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '最后一级部门id',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一级部门名称',
  `organizations` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '多级部门id',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤组员工' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_group_staff
-- ----------------------------

-- ----------------------------
-- Table structure for att_holiday_balance
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_holiday_balance`  (
  `id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `holiday_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '假期类型id',
  `not_limit` tinyint(1) NULL DEFAULT 1 COMMENT '不限额',
  `available_balance` bigint(20) NULL DEFAULT 0 COMMENT '可用额度',
  `occupy_balance` bigint(20) NULL DEFAULT 0 COMMENT '占用额度',
  `update_remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '余额修改备注(最后一次修改的备注)',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期余额详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_holiday_balance
-- ----------------------------

-- ----------------------------
-- Table structure for att_holiday_balance_deadline
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_holiday_balance_deadline`  (
  `id` bigint(20) NOT NULL,
  `holiday_rule_id` bigint(20) NOT NULL COMMENT '假期id',
  `staff_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员id',
  `balance` bigint(20) NOT NULL COMMENT '余额数量',
  `deadline` datetime NULL DEFAULT NULL COMMENT '截至日期',
  `normal` tinyint(1) NULL DEFAULT 1 COMMENT '正常',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期余额有效期' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_holiday_balance_deadline
-- ----------------------------

-- ----------------------------
-- Table structure for att_holiday_balance_record
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_holiday_balance_record`  (
  `id` bigint(20) NOT NULL,
  `staff_id` bigint(20) NULL DEFAULT NULL COMMENT '人员id',
  `staff_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工名称',
  `holiday_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '假期类型id',
  `add_or_deduct` tinyint(4) NULL DEFAULT 1 COMMENT '变动类型，true添加fasle扣除',
  `change_number` bigint(20) NULL DEFAULT 0 COMMENT '变动数量，扣减为负数，增加为正数',
  `before_balance` bigint(20) NULL DEFAULT NULL COMMENT '变动前余额',
  `deadline_id` bigint(20) NULL DEFAULT 0 COMMENT '余额id(att_holiday_balance_deadline表id)',
  `effective` tinyint(4) NULL DEFAULT 1 COMMENT '是否有效(false已作废，true未作废)',
  `effective_time` datetime NULL DEFAULT NULL COMMENT '作废时间',
  `effective_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作废理由',
  `deadline` datetime NULL DEFAULT NULL COMMENT '截至日期(余额有效期',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `update_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人名称',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期余额变动记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_holiday_balance_record
-- ----------------------------

-- ----------------------------
-- Table structure for att_holiday_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_holiday_rule`  (
  `id` bigint(20) NOT NULL,
  `rule_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'code',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `holiday_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假期类型(法定类型)AttLegalHolidayEnum',
  `into_absence` tinyint(1) NULL DEFAULT NULL COMMENT '是否计入缺勤(true请该假期算缺勤，false请该假期算正常出勤',
  `min_ask_unit` int(11) NULL DEFAULT 0 COMMENT '最小请假单位(低于此不能请假)',
  `day_of_hour` int(11) NULL DEFAULT 8 COMMENT '一天折算小时',
  `calculation_way` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假时间计算方式（工作日/自然日)',
  `not_limit` tinyint(4) NULL DEFAULT 1 COMMENT '不限额(true表示不限额)',
  `grant_way` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发放方式（每年发放',
  `grant_date` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发放日期（入职日发放，每年1月1发放)',
  `quota_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '额度类型（固定额度/按社会工龄/按公司工龄)',
  `fixed_number` int(11) NULL DEFAULT 0 COMMENT '固定额度数量',
  `fixed_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '固定额度单位',
  `quota` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '额度配置(json)',
  `long_term` tinyint(1) NULL DEFAULT 1 COMMENT '长期有效',
  `nature_year` int(11) NULL DEFAULT NULL COMMENT '发放日起 多少自然年有效',
  `validity_number` int(11) NULL DEFAULT NULL COMMENT '发放日起 多少个单位时间有效',
  `validity_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '有效期单位',
  `validity` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '有效期（json)',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_holiday_rule
-- ----------------------------

-- ----------------------------
-- Table structure for att_leave_post_from
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_leave_post_from`  (
  `id` bigint(20) NOT NULL,
  `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
  `applicant_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人名称',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `phone` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `post_id` bigint(20) NULL DEFAULT NULL COMMENT '职位id',
  `post_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位名称',
  `post_rule_id` bigint(20) NOT NULL COMMENT '离岗规则id',
  `post_rule_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `post_start_time` datetime NOT NULL COMMENT '离岗开始时间',
  `post_end_time` datetime NOT NULL COMMENT '请假结束时间',
  `post_number` bigint(20) NULL DEFAULT NULL COMMENT '离岗时长',
  `file_key` bigint(20) NULL DEFAULT NULL COMMENT '文件key',
  `post_remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '离岗单状态',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '离岗单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_leave_post_from
-- ----------------------------

-- ----------------------------
-- Table structure for att_leave_post_ledger
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_leave_post_ledger`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `staff_id` bigint(20) NOT NULL COMMENT '人员id',
  `staff_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员名称',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '考勤组id',
  `post_number` bigint(20) NOT NULL COMMENT '离岗时长',
  `post_date` datetime NOT NULL COMMENT '离岗日期',
  `post_from_id` bigint(20) NOT NULL COMMENT '离岗单id',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '离岗台账(请假单拆分为每天的数据)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_leave_post_ledger
-- ----------------------------

-- ----------------------------
-- Table structure for att_leave_post_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_leave_post_rule`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '离岗规则名称',
  `before_apply` tinyint(1) NULL DEFAULT 1 COMMENT '提前申请',
  `min_ask_unit` int(11) NULL DEFAULT 0 COMMENT '最小请假单位',
  `calculation_way` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间折算方式',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户id',
  `deleted` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '离岗规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_leave_post_rule
-- ----------------------------

-- ----------------------------
-- Table structure for att_over_time
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_over_time`  (
  `id` bigint(20) NOT NULL,
  `over_time` datetime NOT NULL COMMENT '加班时间',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名称',
  `overtime_work_duration` datetime NULL DEFAULT NULL COMMENT '工作日加班时长',
  `overtime_rest_duration` datetime NULL DEFAULT NULL COMMENT '休息日加班时长',
  `overtime_fest_duration` datetime NULL DEFAULT NULL COMMENT '节假日加班时长',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_over_time
-- ----------------------------

-- ----------------------------
-- Table structure for att_scheduling
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_scheduling`  (
  `id` bigint(20) NOT NULL,
  `scheduling_month` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '被排班的月份',
  `scheduling_date` date NULL DEFAULT NULL COMMENT '被排班的日期',
  `staff_id` bigint(20) NULL DEFAULT NULL COMMENT '被排班人id',
  `staff_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被排班人名称',
  `staff_group_id` bigint(20) NULL DEFAULT NULL COMMENT '被排班人考勤组id',
  `staff_group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被排班人考勤组名称',
  `classes_id` bigint(20) NULL DEFAULT NULL COMMENT '被排的班次ID',
  `classes_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '排班班次名称',
  `classes_nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '排班班次别名',
  `classes_before_start_time` datetime NULL DEFAULT NULL COMMENT '上班范围开始时间',
  `classes_real_start_time` datetime NULL DEFAULT NULL COMMENT '排班开始时间',
  `classes_after_start_time` datetime NULL DEFAULT NULL COMMENT '上班范围结束时间',
  `classes_before_end_time` datetime NULL DEFAULT NULL COMMENT '下班范围开始时间',
  `classes_real_end_time` datetime NULL DEFAULT NULL COMMENT '排班结束时间',
  `classes_after_end_time` datetime NULL DEFAULT NULL COMMENT '下班范围结束时间',
  `scheduling_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '排班规则id',
  `scheduling_ways` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '排班方式',
  `clock_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡规则id',
  `classes_rest_start_time` datetime NULL DEFAULT NULL COMMENT '休息开始时间',
  `classes_rest_end_time` datetime NULL DEFAULT NULL COMMENT '休息结束时间',
  `classes_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次类型',
  `classes_version` int(11) NULL DEFAULT NULL COMMENT '班次版本号',
  `end_time_is_morrow` tinyint(1) NULL DEFAULT 0 COMMENT '是否跨天班次',
  `staff_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '被排班人部门id',
  `staff_dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被排班人部门名称',
  `staff_group_ranges` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工考勤组适用范围(全部，部分)',
  `hand_scheduling` tinyint(1) NULL DEFAULT 0 COMMENT '手动排班',
  `started` tinyint(1) NULL DEFAULT 0 COMMENT '已开始',
  `clock_read` tinyint(1) NULL DEFAULT 0 COMMENT '是否已经生成打卡任务',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `att_scheduling_scheduling_date_IDX`(`scheduling_date`) USING BTREE,
  INDEX `att_scheduling_staff_id_IDX`(`staff_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '排班表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_scheduling
-- ----------------------------

-- ----------------------------
-- Table structure for att_scheduling_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_scheduling_rule`  (
  `id` bigint(20) NOT NULL COMMENT '规则id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则名称',
  `ways` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '排班方式(行政班/自由班)',
  `legal_holidays_rest` tinyint(1) NULL DEFAULT 0 COMMENT '法定节假日是否休息',
  `continuous_days` int(2) NOT NULL COMMENT '一个月允许最大连续排班天数',
  `interval_hour` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '一天两个班次最小间隔时间',
  `same_shift` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '一个月相同班次类型数量json',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '排班规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_scheduling_rule
-- ----------------------------

-- ----------------------------
-- Table structure for att_scheduling_rule_classes
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_scheduling_rule_classes`  (
  `rule_id` bigint(20) NOT NULL COMMENT '规则id',
  `id` bigint(20) NOT NULL COMMENT '班次id',
  `week` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '星期',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '排班规则的班次' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_scheduling_rule_classes
-- ----------------------------

-- ----------------------------
-- Table structure for att_scheduling_staff
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_scheduling_staff`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `staff_id` bigint(20) NOT NULL COMMENT '人员id',
  `staff_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '人员名称',
  `scheduling_month` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月份',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组id',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '排班人员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_scheduling_staff
-- ----------------------------

-- ----------------------------
-- Table structure for att_staff_sequential
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_staff_sequential`  (
  `id` bigint(20) NOT NULL,
  `sequential_month` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月份',
  `sequential_date` date NULL DEFAULT NULL COMMENT '日期',
  `staff_id` bigint(20) NULL DEFAULT NULL COMMENT '人员id',
  `staff_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员名称',
  `day_of_month` int(11) NULL DEFAULT NULL COMMENT '当月的第几天',
  `week` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '星期',
  `status_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '状态集合json',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `att_staff_sequential_staff_id_IDX`(`staff_id`) USING BTREE,
  INDEX `att_staff_sequential_scheduling_date_IDX`(`sequential_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '人员时序主表(状态表)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_staff_sequential
-- ----------------------------

-- ----------------------------
-- Table structure for att_staff_sequential_detail
-- ----------------------------

CREATE TABLE IF NOT EXISTS `att_staff_sequential_detail`  (
  `id` bigint(20) NOT NULL,
  `sequential_month` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月份',
  `staff_id` bigint(20) NULL DEFAULT NULL COMMENT '人员id',
  `staff_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人员名称',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `business_key` bigint(20) NULL DEFAULT NULL COMMENT '人员id',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `att_staff_sequential_detail_staff_id_IDX`(`staff_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '人员时序主详情表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_staff_sequential_detail
-- ----------------------------

-- ----------------------------
-- Table structure for attendance_group
-- ----------------------------

CREATE TABLE IF NOT EXISTS `attendance_group`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '考勤类型（排班制、值班制）',
  `ranges` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '适用范围(所有人、部分人)',
  `classes_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班次类型（固定班制、排班制）',
  `auto_rest` int(1) NULL DEFAULT NULL COMMENT '是否法定节假日自动排休',
  `is_punch_card` int(1) NOT NULL COMMENT '是否需要打卡',
  `is_supplement_card` int(1) NOT NULL COMMENT '是否允许补卡',
  `is_work_overtime` int(1) NOT NULL COMMENT '是否允许加班',
  `punch_card_way_str` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡方式',
  `classes_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '包含班次',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤组' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attendance_group
-- ----------------------------

-- ----------------------------
-- Table structure for attendance_group_classes
-- ----------------------------

CREATE TABLE IF NOT EXISTS `attendance_group_classes`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
  `week` int(5) NOT NULL COMMENT '时间（周几）',
  `classes_id` bigint(20) NULL DEFAULT NULL COMMENT '班次ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次名称',
  `start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间',
  `before_start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间前',
  `after_start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间后',
  `end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间',
  `before_end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间前',
  `after_end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间后',
  `rest_start_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息开始时间',
  `rest_end_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息结束时间',
  `mark` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识',
  `end_time_is_morrow` int(1) NOT NULL DEFAULT 0 COMMENT '下班时间是否是次日',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤组班次' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attendance_group_classes
-- ----------------------------

-- ----------------------------
-- Table structure for attendance_group_punch_card_way
-- ----------------------------

CREATE TABLE IF NOT EXISTS `attendance_group_punch_card_way`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
  `way` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '打卡方式',
  `wifi` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'wifi名称',
  `mac` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'mac地址',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考勤地址',
  `latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
  `longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `effective_range` int(10) NULL DEFAULT NULL COMMENT '考勤范围(单位：米)',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考勤机名称',
  `device_model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考勤机型号',
  `device_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考勤机编码',
  `device_genre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考勤机类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤组打卡方式' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attendance_group_punch_card_way
-- ----------------------------

-- ----------------------------
-- Table structure for attendance_group_staff
-- ----------------------------

CREATE TABLE IF NOT EXISTS `attendance_group_staff`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
  `group_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考勤组类型',
  `staff_id` bigint(20) NOT NULL COMMENT '员工ID（人事花名册）',
  `staff_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工姓名',
  `staff_mobile` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工电话',
  `staff_organization_id` bigint(20) NULL DEFAULT NULL COMMENT '员工部门',
  `tenant` int(10) NULL DEFAULT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_gsd`(`group_type`, `staff_id`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤组员工' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attendance_group_staff
-- ----------------------------

-- ----------------------------
-- Table structure for attendance_group_supplement_card_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `attendance_group_supplement_card_rule`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
  `supplement_card_rule_id` bigint(20) NOT NULL COMMENT '补卡规则ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '补卡规则名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '允许补卡类型',
  `times` int(10) NULL DEFAULT NULL COMMENT '每月允许补卡次数',
  `days` int(10) NULL DEFAULT NULL COMMENT '几日内允许补卡',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤组补卡规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attendance_group_supplement_card_rule
-- ----------------------------

-- ----------------------------
-- Table structure for attendance_group_work_overtime_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `attendance_group_work_overtime_rule`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
  `work_overtime_rule_id` bigint(20) NOT NULL COMMENT '加班规则ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加班名称',
  `units` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算单位(半小时、小时、天)',
  `day_convert_time` int(10) NULL DEFAULT NULL COMMENT '日折算时长',
  `upper_limit_cycle` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班上限周期(每天、每周、每月)',
  `upper_limit_duration` int(10) NULL DEFAULT NULL COMMENT '加班上限累计时长(小时)',
  `config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '加班规则细则配置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤组加班规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attendance_group_work_overtime_rule
-- ----------------------------

-- ----------------------------
-- Table structure for attendance_operation_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `attendance_operation_log`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `obj_id` bigint(20) NULL DEFAULT NULL COMMENT '操作对象id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作人id',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人名称',
  `business_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务应用',
  `operation_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `operation_obj` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作对象',
  `operation_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '操作内容',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考勤操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of attendance_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for change_holiday_balance_log
-- ----------------------------

CREATE TABLE IF NOT EXISTS `change_holiday_balance_log`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `staff_id` bigint(20) NULL DEFAULT NULL,
  `original_balance` float(5, 1) NULL DEFAULT NULL,
  `expected_balance` float(5, 1) NULL DEFAULT NULL,
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `holiday_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of change_holiday_balance_log
-- ----------------------------

-- ----------------------------
-- Table structure for classes
-- ----------------------------

CREATE TABLE IF NOT EXISTS `classes`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间',
  `before_start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间前',
  `after_start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间后',
  `end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间',
  `before_end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间前',
  `after_end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间后',
  `rest_start_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息开始时间',
  `rest_end_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息结束时间',
  `mark` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识',
  `end_time_is_morrow` int(1) NOT NULL DEFAULT 0 COMMENT '下班时间是否是次日',
  `suit_organization_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '建议应用部门',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '班次管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for classes_change_situation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `classes_change_situation`  (
  `id` bigint(20) NOT NULL,
  `applicant_id` bigint(20) NULL DEFAULT NULL COMMENT '申请人ID',
  `applicant_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人名称',
  `applicant_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '加班人所在部门',
  `attendance_group_id` bigint(20) NULL DEFAULT NULL COMMENT '申请人所在考情组',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `apply_done_time` datetime NULL DEFAULT NULL COMMENT '审批处理完成时间',
  `changer_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被换人ID',
  `changer_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '被换人姓名',
  `from_classes_id` bigint(20) NULL DEFAULT NULL COMMENT '要换班次',
  `to_classes_id` bigint(20) NULL DEFAULT NULL COMMENT '被换班次',
  `reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '换班原因',
  `work_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '换班类型(考勤组的值班和排班枚举)',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_classes_index`(`applicant_id`, `changer_id`, `from_classes_id`, `to_classes_id`) USING BTREE COMMENT '申请人-班次-索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '换班申请记录（审批通过后的换班记录）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of classes_change_situation
-- ----------------------------

-- ----------------------------
-- Table structure for classes_clock
-- ----------------------------

CREATE TABLE IF NOT EXISTS `classes_clock`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡人名称',
  `user_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人所在部门',
  `work_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人排班ID',
  `attendance_group_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人所在考勤组ID',
  `arrange_classes_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡班次ID',
  `clock_date` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡日期',
  `start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次开始时间',
  `end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次结束时间',
  `start_real_time` datetime NULL DEFAULT NULL COMMENT '班次真正开始时间',
  `end_real_time` datetime NULL DEFAULT NULL COMMENT '班次真正结束时间',
  `start_work_clock_time` datetime NULL DEFAULT NULL COMMENT '上班打卡时间',
  `end_work_clock_time` datetime NULL DEFAULT NULL COMMENT '下班打卡时间',
  `start_work_clock_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班打卡状态',
  `end_work_clock_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班打卡状态',
  `classes_clock_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本次上班的最终状态',
  `start_leave` int(1) NULL DEFAULT NULL COMMENT '上班卡是否请假',
  `end_leave` int(1) NULL DEFAULT NULL COMMENT '下班卡是否请假',
  `start_trigger` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班卡审批动作',
  `end_trigger` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班卡审批动作',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uiq_idx`(`user_id`, `arrange_classes_id`, `clock_date`, `tenant`) USING BTREE COMMENT '每人每天每个班次只能有一条打卡数据'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '班次打卡' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of classes_clock
-- ----------------------------

-- ----------------------------
-- Table structure for classes_clock_logs
-- ----------------------------

CREATE TABLE IF NOT EXISTS `classes_clock_logs`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `staff_id` bigint(20) NULL DEFAULT NULL COMMENT '员工ID',
  `clock_time` datetime NULL DEFAULT NULL COMMENT '打卡时间',
  `is_start_clock` int(1) NULL DEFAULT NULL COMMENT '是否上班卡',
  `work_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人排班ID',
  `tenant` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '打卡日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of classes_clock_logs
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
    'ALTER TABLE `cube-srv-gen-attendance-management`.databasechangelog ADD UNIQUE KEY unique_id_file_name (`ID`, `FILENAME`)',
    'SELECT \'Index Exists\' status');

PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ----------------------------
-- Records of databasechangelog
-- ----------------------------
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v1.9.0.mysql.sql', '2024-04-23 01:27:38', 1, 'EXECUTED', '8:68b5e461ae20ef11985106c2063b8180', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806858424');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changelog-v1.9.1.mysql.sql', '2024-04-23 01:27:38', 2, 'EXECUTED', '8:d2156a48b752132b78820d335fb1fb1b', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806858505');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.0.mysql.sql', '2024-04-23 01:27:38', 3, 'EXECUTED', '8:ddb52d30534e63b666c1f098a5b60e51', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806858568');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.1.mysql.sql', '2024-04-23 01:27:38', 4, 'EXECUTED', '8:13c2f698ceeaab53ae435c8a1e84bf8c', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806858916');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.7.mysql.sql', '2024-04-23 01:27:39', 5, 'EXECUTED', '8:d83379c5f76dc2c52153c91a5a3bf227', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806858989');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.7-2.mysql.sql', '2024-04-23 01:27:39', 6, 'EXECUTED', '8:3c5d9a706217423bce1ff41d5599b4b9', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806859353');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.7.3.mysql.sql', '2024-04-23 01:27:39', 7, 'EXECUTED', '8:0be4fe692732b0fea1f7e573373f40fd', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806859434');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.12-1.mysql.sql', '2024-04-23 01:27:39', 8, 'EXECUTED', '8:1f1e7dbb1e41f6caa142cec95a87a755', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806859489');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.12-2.mysql.sql', '2024-04-23 01:27:39', 9, 'EXECUTED', '8:e722dc3895e7a5cce7d4057cff8e193c', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806859549');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.14.mysql.sql', '2024-04-23 01:27:39', 10, 'EXECUTED', '8:dc740c0209d1b2cda9806721e75a3f0e', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806859606');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.20.mysql.sql', '2024-04-23 01:27:39', 11, 'EXECUTED', '8:393ae22c385675417003e1ae68349bb6', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806859690');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.20-1.mysql.sql', '2024-04-23 01:27:40', 12, 'EXECUTED', '8:658aa642511242f0dc0ddb19cbcae3bc', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806859902');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.20-2.mysql.sql', '2024-04-23 01:27:40', 13, 'EXECUTED', '8:e67eeab6a16ebf61e651a9aef60039b3', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860073');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.20-3.mysql.sql', '2024-04-23 01:27:40', 14, 'EXECUTED', '8:f9e502caa8e5617defb133241056d4cf', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860133');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.22.mysql.sql', '2024-04-23 01:27:40', 15, 'EXECUTED', '8:892ca685e89d3da786e7ebf52ad1312b', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860185');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.24.mysql.sql', '2024-04-23 01:27:40', 16, 'EXECUTED', '8:6847ec18a38a23d7479a093ff5dc658b', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860244');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.0.24-1.mysql.sql', '2024-04-23 01:27:40', 17, 'EXECUTED', '8:2df8752afcb5afe538e67dff570edfe2', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860310');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.2.2.mysql.sql', '2024-04-23 01:27:40', 18, 'EXECUTED', '8:43cd61da34a7bbb40d6f41d60f1b0b23', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860377');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.2.2-2.mysql.sql', '2024-04-23 01:27:40', 19, 'EXECUTED', '8:b89cf04198385fdf03de8d0a4d12238f', 'sql', '', NULL, '4.3.1', NULL, NULL, '3806860474');
INSERT IGNORE INTO `databasechangelog` VALUES ('1686103358461-1', 'qqq', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.2.17.mysql.sql', '2024-06-17 11:46:38', 20, 'EXECUTED', '8:025f05a0fbaf58a3cf95bb6b8d2ef7f5', 'sql', '', NULL, '4.3.1', NULL, NULL, '8595998344');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-1', 'chenhongxin', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.2.17.mysql.sql', '2024-06-17 11:46:38', 21, 'EXECUTED', '8:2dd41257e5dd55e04c408096f994c795', 'sql', '', NULL, '4.3.1', NULL, NULL, '8595998344');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-2', 'chenhongxin', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.2.17.mysql.sql', '2024-06-17 11:46:38', 22, 'EXECUTED', '8:79ad08da6eb4ad40bb7f956946415e99', 'sql', '', NULL, '4.3.1', NULL, NULL, '8595998344');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-3', 'chenhongxin', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.2.17.mysql.sql', '2024-06-17 11:46:38', 23, 'EXECUTED', '8:9a973e3587f3c838f53e832c11f8434b', 'sql', '', NULL, '4.3.1', NULL, NULL, '8595998344');
INSERT IGNORE INTO `databasechangelog` VALUES ('alter-4', 'chenhongxin', 'classpath:db/com.guhecloud.cube.srv.gen.attendance.management/changlog-v2.2.17.mysql.sql', '2024-06-17 11:46:38', 24, 'EXECUTED', '8:9e86a1ad4a62298655c3dcd361303850', 'sql', '', NULL, '4.3.1', NULL, NULL, '8595998344');

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
-- Table structure for duty_work
-- ----------------------------

CREATE TABLE IF NOT EXISTS `duty_work`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `staff_id` bigint(20) NOT NULL COMMENT '员工ID',
  `staff_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工姓名',
  `staff_mobile` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工电话',
  `staff_organization_id` bigint(20) NULL DEFAULT NULL COMMENT '员工部门',
  `duty_work_classes_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班次Ids',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
  `month` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月份',
  `week` int(1) NULL DEFAULT NULL COMMENT '周几',
  `date` date NULL DEFAULT NULL COMMENT '日期',
  `is_rest` int(1) NOT NULL DEFAULT 0 COMMENT '是否休息',
  `is_holiday` int(1) NOT NULL DEFAULT 0 COMMENT '是否节日',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '值班' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of duty_work
-- ----------------------------

-- ----------------------------
-- Table structure for duty_work_classes
-- ----------------------------

CREATE TABLE IF NOT EXISTS `duty_work_classes`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `classes_id` bigint(20) NOT NULL COMMENT '班次ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '班次名称',
  `start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间',
  `before_start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间前',
  `after_start_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班时间后',
  `end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间',
  `before_end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间前',
  `after_end_work_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下班时间后',
  `rest_start_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息开始时间',
  `rest_end_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '休息结束时间',
  `mark` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识',
  `end_time_is_morrow` int(1) NOT NULL COMMENT '下班时间是否是次日',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '值班班次' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of duty_work_classes
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_balance
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_balance`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `holiday_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假期类型',
  `remain_balance` float(5, 1) NULL DEFAULT NULL COMMENT '剩余余额',
  `occupy_balance` float(5, 1) NULL DEFAULT 0.0 COMMENT '占用余额',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '(修改余额时的)备注',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期余额' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of holiday_balance
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_balance_change
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_balance_change`  (
  `id` bigint(20) NOT NULL,
  `value` float(5, 1) NULL DEFAULT NULL COMMENT '余额，扣减为负数，增加为正数',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `holiday_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假期类型',
  `user_id` bigint(20) NULL DEFAULT NULL,
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `change_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实扣 预扣 发放',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期余额变化' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of holiday_balance_change
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_balance_list
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_balance_list`  (
  `id` bigint(20) NOT NULL,
  `holiday_type_id` bigint(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `employee_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工姓名',
  `employee_organization` bigint(20) NULL DEFAULT NULL COMMENT '员工组织',
  `time_of_induction` datetime NULL DEFAULT NULL COMMENT '入职时间',
  `attendance_group_id_of_arrange` bigint(20) NULL DEFAULT NULL,
  `remain_balance` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `show` bit(1) NULL DEFAULT b'1' COMMENT '1 显示 0 隐藏',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of holiday_balance_list
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_deduct_detail
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_deduct_detail`  (
  `user_id` bigint(20) NOT NULL,
  `value` float(4, 2) NULL DEFAULT NULL,
  `holiday_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of holiday_deduct_detail
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_grant
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_grant`  (
  `id` bigint(20) NOT NULL,
  `balance` float(5, 1) NULL DEFAULT NULL,
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `holiday_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假期类型',
  `user_id` bigint(20) NULL DEFAULT NULL,
  `remaining_balance` float(5, 1) NULL DEFAULT NULL COMMENT '剩余数量',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期发放记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of holiday_grant
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_grant_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_grant_rule`  (
  `id` bigint(20) NOT NULL,
  `grant_way` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发放方式',
  `grant_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发放时间',
  `quota` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `expiry_date` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期发放规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of holiday_grant_rule
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_type
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_type`  (
  `id` bigint(20) NOT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `time_of_induction_limit` int(5) NULL DEFAULT NULL COMMENT '入职时长限制',
  `leave_rule` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假规则',
  `grant_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '发放规则id',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `expiry_date` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_need_limit_quota` bit(1) NULL DEFAULT NULL COMMENT '是否限额',
  `staff_range` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '适用范围(冗余字段，为了方便查询)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of holiday_type
-- ----------------------------

-- ----------------------------
-- Table structure for holiday_user_range
-- ----------------------------

CREATE TABLE IF NOT EXISTS `holiday_user_range`  (
  `holiday_type_id` bigint(11) NOT NULL,
  `user_id` bigint(11) NOT NULL,
  `employee_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工姓名',
  `employee_organization` bigint(20) NULL DEFAULT NULL COMMENT '员工组织',
  `time_of_induction` datetime NULL DEFAULT NULL COMMENT '入职时间',
  `attendance_group_id_of_arrange` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '假期适用范围' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of holiday_user_range
-- ----------------------------

-- ----------------------------
-- Table structure for leave_form
-- ----------------------------

CREATE TABLE IF NOT EXISTS `leave_form`  (
  `id` bigint(20) NOT NULL,
  `leave_form_detail` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假单明细',
  `leave_rule` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '假期',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假单状态',
  `leave_start_time` datetime NULL DEFAULT NULL COMMENT '请假开始时间，这是不准确的。只是用于某方面的查询',
  `leave_end_time` datetime NULL DEFAULT NULL COMMENT '请假结束时间，这是不准确的。只是用于某方面的查询',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of leave_form
-- ----------------------------

-- ----------------------------
-- Table structure for leave_post_situation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `leave_post_situation`  (
  `id` bigint(20) NOT NULL,
  `applicant_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人ID',
  `applicant_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡人名称',
  `applicant_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人所在部门',
  `attendance_group_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人所在考情组ID',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `apply_done_time` datetime NULL DEFAULT NULL COMMENT '审批完成时间',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `post_id` bigint(20) NULL DEFAULT NULL COMMENT '职位ID',
  `post_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位名称',
  `start_time` datetime NULL DEFAULT NULL COMMENT '离岗时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '返岗时间',
  `reason` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外出地点',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `userId_starttime_index`(`applicant_id`, `start_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '离岗申请明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of leave_post_situation
-- ----------------------------

-- ----------------------------
-- Table structure for repair_clock_situation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `repair_clock_situation`  (
  `id` bigint(20) NOT NULL,
  `applicant_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人ID',
  `applicant_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '打卡人名称',
  `applicant_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人所在部门',
  `attendance_group_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡人所在考情组ID',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `apply_done_time` datetime NULL DEFAULT NULL COMMENT '审批完成时间',
  `date` date NULL DEFAULT NULL COMMENT '补卡日期',
  `classes_id` bigint(20) NULL DEFAULT NULL COMMENT '打卡班次ID',
  `clock_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上班/下班:start end',
  `exp_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常类型：迟到、早退、缺卡',
  `clock_time` datetime NULL DEFAULT NULL COMMENT '补卡时间',
  `reason` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '补卡理由',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `userId_classesId_clockType`(`applicant_id`, `classes_id`, `clock_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '补卡记录（审批通过后的补卡记录）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of repair_clock_situation
-- ----------------------------

-- ----------------------------
-- Table structure for staff_attendance_stat
-- ----------------------------

CREATE TABLE IF NOT EXISTS `staff_attendance_stat`  (
  `id` bigint(20) NULL DEFAULT NULL,
  `staff_id` bigint(20) NOT NULL COMMENT '员工ID',
  `staff_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工姓名',
  `staff_organization_id` bigint(20) NULL DEFAULT NULL COMMENT '员工部门',
  `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
  `month` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '月份',
  `stat_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '统计内容',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`month`, `staff_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工统计数据(截至前一天)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of staff_attendance_stat
-- ----------------------------

-- ----------------------------
-- Table structure for supplement_card_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `supplement_card_rule`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '允许补卡类型',
  `times` int(10) NULL DEFAULT NULL COMMENT '每月允许补卡次数',
  `days` int(10) NULL DEFAULT NULL COMMENT '几日内允许补卡',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '补卡规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of supplement_card_rule
-- ----------------------------

-- ----------------------------
-- Table structure for work_overtime_ledger
-- ----------------------------

CREATE TABLE IF NOT EXISTS `work_overtime_ledger`  (
  `id` bigint(20) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `org_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `work_date` date NOT NULL COMMENT '加班时间',
  `work_duration` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '工作日时长（时）',
  `work_pay_duration` int(11) NULL DEFAULT NULL COMMENT '工作时长记为加班费时长',
  `rest_duration` int(11) NULL DEFAULT NULL COMMENT '休息日加班时长',
  `rest_pat_duration` int(11) NULL DEFAULT NULL COMMENT '休息日加班记加班费时长',
  `fest_duration` int(11) NULL DEFAULT NULL COMMENT '节假日加班时长',
  `fest_oat_duration` int(11) NULL DEFAULT NULL COMMENT '节假日记加班费时长',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `work_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班类型',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `apply_done_time` datetime NULL DEFAULT NULL COMMENT '申请处理完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '加班台账' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of work_overtime_ledger
-- ----------------------------

-- ----------------------------
-- Table structure for work_overtime_ledger_advance
-- ----------------------------

CREATE TABLE IF NOT EXISTS `work_overtime_ledger_advance`  (
  `id` bigint(20) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `org_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `work_date` date NOT NULL COMMENT '加班时间',
  `work_duration` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '工作日时长（时）',
  `work_pay_duration` int(11) NULL DEFAULT NULL COMMENT '工作时长记为加班费时长',
  `rest_duration` int(11) NULL DEFAULT NULL COMMENT '休息日加班时长',
  `rest_pat_duration` int(11) NULL DEFAULT NULL COMMENT '休息日加班记加班费时长',
  `fest_duration` int(11) NULL DEFAULT NULL COMMENT '节假日加班时长',
  `fest_oat_duration` int(11) NULL DEFAULT NULL COMMENT '节假日记加班费时长',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `work_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班类型',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `apply_done_time` datetime NULL DEFAULT NULL COMMENT '申请处理完成时间',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id（工作流）',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核结果',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '加班台账预统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of work_overtime_ledger_advance
-- ----------------------------

-- ----------------------------
-- Table structure for work_overtime_rule
-- ----------------------------

CREATE TABLE IF NOT EXISTS `work_overtime_rule`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `units` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计算单位(半小时、小时、天)',
  `day_convert_time` int(10) NULL DEFAULT NULL COMMENT '日折算时长',
  `upper_limit_cycle` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班上限周期(每天、每周、每月)',
  `upper_limit_duration` int(10) NULL DEFAULT NULL COMMENT '加班上限累计时长(小时)',
  `tenant` int(10) NOT NULL COMMENT '租户主键',
  `deleted` bigint(20) UNSIGNED NOT NULL COMMENT '是否删除',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '加班规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of work_overtime_rule
-- ----------------------------

-- ----------------------------
-- Table structure for work_overtime_rule_detail
-- ----------------------------

CREATE TABLE IF NOT EXISTS `work_overtime_rule_detail`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `rule_id` bigint(20) NOT NULL COMMENT '规则ID',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加班类型(工作日、休息日、节假日)',
  `method` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '计算方式',
  `after_time` int(10) NULL DEFAULT NULL COMMENT '下班后多少分钟开始计算',
  `compensation_method` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加班补偿计算方式',
  `compensate` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '加班补偿方案',
  `proportion` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计算调休比例',
  `work_condition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加班审批条件',
  `units` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计算单位(半小时、小时、天)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '加班规则细则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of work_overtime_rule_detail
-- ----------------------------

-- ----------------------------
-- Table structure for work_overtime_situation
-- ----------------------------

CREATE TABLE IF NOT EXISTS `work_overtime_situation`  (
  `id` bigint(20) NOT NULL,
  `applicant_id` bigint(20) NULL DEFAULT NULL COMMENT '申请人ID',
  `applicant_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请人名称',
  `applicant_dept_id` bigint(20) NULL DEFAULT NULL COMMENT '加班人所在部门',
  `attendance_group_id` bigint(20) NULL DEFAULT NULL COMMENT '申请人所在考情组',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `apply_done_time` datetime NULL DEFAULT NULL COMMENT '加班处理完成时间',
  `work_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班类型',
  `start_time` datetime NULL DEFAULT NULL COMMENT '加班开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '加班结束时间',
  `work_date` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班日期',
  `work_period` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班时间段',
  `work_duration` float NULL DEFAULT NULL COMMENT '加班时长',
  `compensate` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班补偿类型',
  `reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加班原因',
  `work_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '这张表（attendance_group_work_overtime_rule）的ID',
  `tenant` int(11) NULL DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `userId_startTime_index`(`applicant_id`, `start_time`, `deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '加班审批记录（审批通过后的记录）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of work_overtime_situation
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
