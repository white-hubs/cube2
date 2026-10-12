-- liquibase formatted sql

-- changeset qqq:1686103358461-1


-- 请假规则添加法定假期类型和计入缺勤字段
ALTER TABLE `att_holiday_rule` ADD holiday_type varchar(100) NULL COMMENT '假期类型(法定类型)AttLegalHolidayEnum';
ALTER TABLE `att_holiday_rule` CHANGE holiday_type holiday_type varchar(100) NULL COMMENT '假期类型(法定类型)AttLegalHolidayEnum' AFTER name;
ALTER TABLE `att_holiday_rule` ADD into_absence BOOL NULL COMMENT '是否计入缺勤(true请该假期算缺勤，false请该假期算正常出勤';
ALTER TABLE `att_holiday_rule` CHANGE into_absence into_absence BOOL NULL COMMENT '是否计入缺勤(true请该假期算缺勤，false请该假期算正常出勤' AFTER holiday_type;

-- 请假台账添加假期类型code
ALTER TABLE att_ask_leave_ledger ADD holiday_code varchar(100) NULL COMMENT '假期code(类型code)';
ALTER TABLE att_ask_leave_ledger CHANGE holiday_code holiday_code varchar(100) NULL COMMENT '假期code(类型code)' AFTER holiday_id;

-- 出差规则
CREATE TABLE `att_away_business_rule` (
                                          `id` bigint(20) NOT NULL,
                                          `name` varchar(20) DEFAULT NULL COMMENT '出差规则名称',
                                          `before_apply` tinyint(1) DEFAULT '1' COMMENT '提前申请',
                                          `min_ask_unit` int(11) DEFAULT '0' COMMENT '最小请假单位',
                                          `calculation_way` varchar(50) DEFAULT NULL COMMENT '时间折算方式',
                                          `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                          `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                                          `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='出差规则';

-- 出差单
CREATE TABLE `att_away_business_from` (
                                          `id` bigint(20) NOT NULL,
                                          `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
                                          `applicant_name` varchar(20) DEFAULT NULL COMMENT '申请人名称',
                                          `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                          `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
                                          `phone` varchar(12) DEFAULT NULL COMMENT '电话',
                                          `post_id` bigint(20) DEFAULT NULL COMMENT '职位id',
                                          `post_name` varchar(100) DEFAULT NULL COMMENT '职位名称',
                                          `away_rule_id` bigint(20) NOT NULL COMMENT '出差规则id',
                                          `away_rule_name` varchar(20) DEFAULT NULL COMMENT '规则名称',
                                          `away_start_time` datetime NOT NULL COMMENT '出差开始时间',
                                          `away_end_time` datetime NOT NULL COMMENT '出差结束时间',
                                          `away_number` bigint(20) DEFAULT NULL COMMENT '出差时长',
                                          `file_key` bigint(20) DEFAULT NULL COMMENT '文件key',
                                          `away_remark` varchar(100) DEFAULT NULL COMMENT '备注',
                                          `status` varchar(30) DEFAULT NULL COMMENT '出差单状态',
                                          `process_instance_id` varchar(50) DEFAULT NULL COMMENT '流程实例id',
                                          `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                          `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                                          `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='出差单';

-- 出差台账
CREATE TABLE `att_away_business_ledger` (
                                            `id` bigint(20) NOT NULL COMMENT 'id',
                                            `staff_id` bigint(20) NOT NULL COMMENT '人员id',
                                            `staff_name` varchar(100) DEFAULT NULL COMMENT '人员名称',
                                            `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                            `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
                                            `group_id` bigint(20) DEFAULT NULL COMMENT '考勤组id',
                                            `away_number` bigint(20) NOT NULL COMMENT '出差时长',
                                            `away_date` datetime NOT NULL COMMENT '出差日期',
                                            `away_from_id` bigint(20) NOT NULL COMMENT '出差单id',
                                            `tenant` int(10) NOT NULL COMMENT '租户主键',
                                            `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                                            `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                            `create_time` datetime NOT NULL COMMENT '创建时间',
                                            `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                            `update_time` datetime NOT NULL COMMENT '更新时间',
                                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='出差台账(出差单拆分为每天的数据)';