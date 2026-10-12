-- liquibase formatted sql

-- changeset qqq:1686103358461-1


-- 更新考勤组表，添加各个规则字段

ALTER TABLE `att_group` ADD holiday_rule_ids varchar(500) NULL COMMENT '请假规则ids';
ALTER TABLE `att_group` CHANGE holiday_rule_ids holiday_rule_ids varchar(500) NULL COMMENT '请假规则ids' AFTER overtime_rule_id;
ALTER TABLE `att_group` MODIFY COLUMN update_by varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '更新用户';
ALTER TABLE `att_group` MODIFY COLUMN create_by varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '创建用户';
ALTER TABLE `att_group` ADD leave_post_rule_id BIGINT(20) NULL COMMENT '离岗规则id';
ALTER TABLE `att_group` CHANGE leave_post_rule_id leave_post_rule_id BIGINT(20) NULL COMMENT '离岗规则id' AFTER holiday_rule_ids;
ALTER TABLE `att_group` ADD away_business_rule_id BIGINT(20) NULL COMMENT '出差规则id';
ALTER TABLE `att_group` CHANGE away_business_rule_id away_business_rule_id BIGINT(20) NULL COMMENT '出差规则id' AFTER leave_post_rule_id;

-- 考勤组人员表添加字段
ALTER TABLE `att_group_staff` CHANGE group_name group_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '考勤组名称' AFTER group_id;
ALTER TABLE `att_group_staff` ADD entry_date DATETIME NULL COMMENT '入职日期';
ALTER TABLE `att_group_staff` CHANGE entry_date entry_date DATETIME NULL COMMENT '入职日期' AFTER mobile;
ALTER TABLE `att_group_staff` ADD first_pay_social_security_date DATETIME NULL COMMENT '首次缴纳社保日期';
ALTER TABLE `att_group_staff` CHANGE first_pay_social_security_date first_pay_social_security_date DATETIME NULL COMMENT '首次缴纳社保日期' AFTER entry_date;



-- 创建请假相关表

CREATE TABLE `att_ask_leave_from` (
                                      `id` bigint(20) NOT NULL,
                                      `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
                                      `applicant_name` varchar(20) DEFAULT NULL COMMENT '申请人名称',
                                      `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                      `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
                                      `post_id` bigint(20) DEFAULT NULL COMMENT '职位id',
                                      `post_name` varchar(100) DEFAULT NULL COMMENT '职位名称',
                                      `phone` varchar(12) DEFAULT NULL COMMENT '电话',
                                      `enter_date` datetime DEFAULT NULL COMMENT '入职日期',
                                      `holiday_id` bigint(20) NOT NULL COMMENT '假期类型(规则)id',
                                      `holiday_name` varchar(20) DEFAULT NULL COMMENT '假期类型(规则)名称',
                                      `holiday_balance` bigint(20) DEFAULT NULL COMMENT '假期余额',
                                      `leave_start_time` datetime NOT NULL COMMENT '租户主键',
                                      `leave_end_time` datetime NOT NULL COMMENT '请假结束时间',
                                      `leave_number` bigint(20) DEFAULT NULL COMMENT '请假时长',
                                      `leave_location` varchar(500) DEFAULT NULL COMMENT '休假地址',
                                      `file_key` bigint(20) DEFAULT NULL COMMENT '文件key',
                                      `leave_remark` varchar(100) DEFAULT NULL COMMENT '请假备注',
                                      `cancel_leave_from_id` bigint(20) DEFAULT NULL COMMENT '销假单id',
                                      `status` varchar(30) DEFAULT NULL COMMENT '请假单状态',
                                      `process_instance_id` varchar(50) DEFAULT NULL COMMENT '流程实例id',
                                      `deadline_ids` varchar(300) DEFAULT NULL COMMENT '扣除的记录id',
                                      `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                      `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                                      `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                      `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='请假单';



CREATE TABLE `att_ask_leave_ledger` (
                                        `id` bigint(20) NOT NULL COMMENT 'id',
                                        `staff_id` bigint(20) NOT NULL COMMENT '人员id',
                                        `staff_name` varchar(100) DEFAULT NULL COMMENT '人员名称',
                                        `holiday_id` bigint(20) NOT NULL COMMENT '假期规则id',
                                        `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                        `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
                                        `day_of_hour` int(11) DEFAULT '8' COMMENT '一天折算多少小时',
                                        `leave_number` bigint(20) NOT NULL COMMENT '请假时间',
                                        `leave_date` datetime NOT NULL COMMENT '请假日期',
                                        `leave_from_id` bigint(20) NOT NULL COMMENT '请假单id',
                                        `tenant` int(10) NOT NULL COMMENT '租户主键',
                                        `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                                        `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='请假台账(请假单拆分为每天的数据)';



CREATE TABLE `att_cancel_leave_from` (
                                         `id` bigint(20) NOT NULL,
                                         `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
                                         `applicant_name` varchar(20) DEFAULT NULL COMMENT '申请人名称',
                                         `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                         `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
                                         `holiday_id` bigint(20) NOT NULL COMMENT '假期类型(规则)id',
                                         `holiday_name` varchar(20) DEFAULT NULL COMMENT '假期类型(规则)名称',
                                         `ask_leave_from_id` bigint(20) NOT NULL COMMENT '请假单id',
                                         `ask_leave_from` varchar(500) DEFAULT NULL COMMENT '请假单对象',
                                         `cancel_start_time` datetime NOT NULL COMMENT '销假开始时间',
                                         `cancel_end_time` datetime NOT NULL COMMENT '请假结束时间',
                                         `cancel_number` bigint(20) DEFAULT NULL COMMENT '请假时长',
                                         `cancel_remark` varchar(500) DEFAULT NULL COMMENT '销假备注',
                                         `file_key` bigint(20) DEFAULT NULL COMMENT '文件key',
                                         `status` varchar(30) DEFAULT NULL COMMENT '请假单状态',
                                         `process_instance_id` varchar(50) DEFAULT NULL COMMENT '流程实例id',
                                         `normal` tinyint(1) DEFAULT '1' COMMENT '正常',
                                         `tenant` int(10) DEFAULT NULL COMMENT '租户',
                                         `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                         `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                         `create_time` datetime NOT NULL COMMENT '创建时间',
                                         `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                         `update_time` datetime NOT NULL COMMENT '更新时间',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='销假单';



CREATE TABLE `att_holiday_balance` (
                                       `id` bigint(20) NOT NULL,
                                       `staff_id` bigint(20) DEFAULT NULL COMMENT '员工id',
                                       `holiday_rule_id` bigint(20) DEFAULT NULL COMMENT '假期类型id',
                                       `not_limit` tinyint(1) DEFAULT '1' COMMENT '不限额',
                                       `available_balance` bigint(20) DEFAULT '0' COMMENT '可用额度',
                                       `occupy_balance` bigint(20) DEFAULT '0' COMMENT '占用额度',
                                       `update_remark` varchar(300) DEFAULT NULL COMMENT '余额修改备注(最后一次修改的备注)',
                                       `tenant` int(10) NOT NULL COMMENT '租户主键',
                                       `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                                       `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='假期余额详情';



CREATE TABLE `att_holiday_balance_deadline` (
                                                `id` bigint(20) NOT NULL,
                                                `holiday_rule_id` bigint(20) NOT NULL COMMENT '假期id',
                                                `staff_id` varchar(20) DEFAULT NULL COMMENT '人员id',
                                                `balance` bigint(20) NOT NULL COMMENT '余额数量',
                                                `deadline` datetime DEFAULT NULL COMMENT '截至日期',
                                                `normal` tinyint(1) DEFAULT '1' COMMENT '正常',
                                                `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                                `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                                `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='假期余额有效期';



CREATE TABLE `att_holiday_balance_record` (
                                              `id` bigint(20) NOT NULL,
                                              `staff_id` bigint(20) DEFAULT NULL COMMENT '人员id',
                                              `staff_name` varchar(50) DEFAULT NULL COMMENT '员工名称',
                                              `holiday_rule_id` bigint(20) DEFAULT NULL COMMENT '假期类型id',
                                              `add_or_deduct` tinyint(4) DEFAULT '1' COMMENT '变动类型，true添加fasle扣除',
                                              `change_number` bigint(20) DEFAULT '0' COMMENT '变动数量，扣减为负数，增加为正数',
                                              `effective` tinyint(4) DEFAULT '1' COMMENT '是否有效(发放的，未被消费的',
                                              `deadline` datetime DEFAULT NULL COMMENT '截至日期(余额有效期',
                                              `remark` varchar(300) DEFAULT NULL COMMENT '备注',
                                              `update_name` varchar(100) DEFAULT NULL COMMENT '修改人名称',
                                              `tenant` int(10) NOT NULL COMMENT '租户主键',
                                              `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                                              `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                              `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='假期余额变动记录';



CREATE TABLE `att_holiday_rule` (
                                    `id` bigint(20) NOT NULL,
                                    `rule_code` varchar(100) DEFAULT NULL COMMENT 'code',
                                    `name` varchar(100) DEFAULT NULL COMMENT '名称',
                                    `min_ask_unit` int(11) DEFAULT '0' COMMENT '最小请假单位(低于此不能请假)',
                                    `day_of_hour` int(11) DEFAULT '8' COMMENT '一天折算小时',
                                    `calculation_way` varchar(50) DEFAULT NULL COMMENT '请假时间计算方式（工作日/自然日)',
                                    `not_limit` tinyint(4) DEFAULT '1' COMMENT '不限额(true表示不限额)',
                                    `grant_way` varchar(50) DEFAULT NULL COMMENT '发放方式（每年发放',
                                    `grant_date` varchar(50) DEFAULT NULL COMMENT '发放日期（入职日发放，每年1月1发放)',
                                    `quota_type` varchar(50) DEFAULT NULL COMMENT '额度类型（固定额度/按社会工龄/按公司工龄)',
                                    `fixed_number` int(11) DEFAULT '0' COMMENT '固定额度数量',
                                    `fixed_unit` varchar(50) DEFAULT NULL COMMENT '固定额度单位',
                                    `quota` text COMMENT '额度配置(json)',
                                    `long_term` tinyint(1) DEFAULT '1' COMMENT '长期有效',
                                    `nature_year` int(11) DEFAULT NULL COMMENT '发放日起 多少自然年有效',
                                    `validity_number` int(11) DEFAULT NULL COMMENT '发放日起 多少个单位时间有效',
                                    `validity_unit` varchar(50) DEFAULT NULL COMMENT '有效期单位',
                                    `validity` text COMMENT '有效期（json)',
                                    `tenant` int(10) NOT NULL COMMENT '租户主键',
                                    `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                    `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                    `update_time` datetime NOT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='假期规则';


-- 创建离岗相关表

CREATE TABLE `att_leave_post_from` (
                                       `id` bigint(20) NOT NULL,
                                       `applicant_id` bigint(20) NOT NULL COMMENT '申请人id',
                                       `applicant_name` varchar(20) DEFAULT NULL COMMENT '申请人名称',
                                       `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                       `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
                                       `phone` varchar(12) DEFAULT NULL COMMENT '电话',
                                       `post_id` bigint(20) DEFAULT NULL COMMENT '职位id',
                                       `post_name` varchar(100) DEFAULT NULL COMMENT '职位名称',
                                       `post_rule_id` bigint(20) NOT NULL COMMENT '离岗规则id',
                                       `post_rule_name` varchar(20) DEFAULT NULL COMMENT '规则名称',
                                       `post_start_time` datetime NOT NULL COMMENT '离岗开始时间',
                                       `post_end_time` datetime NOT NULL COMMENT '请假结束时间',
                                       `post_number` bigint(20) DEFAULT NULL COMMENT '离岗时长',
                                       `file_key` bigint(20) DEFAULT NULL COMMENT '文件key',
                                       `post_remark` varchar(100) DEFAULT NULL COMMENT '备注',
                                       `status` varchar(30) DEFAULT NULL COMMENT '离岗单状态',
                                       `process_instance_id` varchar(50) DEFAULT NULL COMMENT '流程实例id',
                                       `tenant` int(10) DEFAULT NULL COMMENT '租户id',
                                       `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                                       `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='离岗单';



CREATE TABLE `att_leave_post_ledger` (
                                         `id` bigint(20) NOT NULL COMMENT 'id',
                                         `staff_id` bigint(20) NOT NULL COMMENT '人员id',
                                         `staff_name` varchar(100) DEFAULT NULL COMMENT '人员名称',
                                         `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                         `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
                                         `group_id` bigint(20) DEFAULT NULL COMMENT '考勤组id',
                                         `post_number` bigint(20) NOT NULL COMMENT '离岗时长',
                                         `post_date` datetime NOT NULL COMMENT '离岗日期',
                                         `post_from_id` bigint(20) NOT NULL COMMENT '离岗单id',
                                         `tenant` int(10) NOT NULL COMMENT '租户主键',
                                         `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否删除',
                                         `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                         `create_time` datetime NOT NULL COMMENT '创建时间',
                                         `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                         `update_time` datetime NOT NULL COMMENT '更新时间',
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='离岗台账(请假单拆分为每天的数据)';



CREATE TABLE `att_leave_post_rule` (
                                       `id` bigint(20) NOT NULL,
                                       `name` varchar(20) DEFAULT NULL COMMENT '离岗规则名称',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='离岗规则';