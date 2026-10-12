-- liquibase formatted sql

-- changeset qqq:1686103358461-1

use `cube-srv-gen-attendance-management`;
-- table init

--
-- Table structure for table `att_scheduling_rule_classes`
--
CREATE TABLE `att_scheduling_rule_classes` (
                                               `rule_id` bigint(20) NOT NULL COMMENT '规则id',
                                               `id` bigint(20) NOT NULL COMMENT '班次id',
                                               `week` varchar(30) NOT NULL COMMENT '星期',
                                               `tenant` int(10) NOT NULL COMMENT '租户主键',
                                               `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                               `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                               `create_time` datetime NOT NULL COMMENT '创建时间',
                                               `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                               `update_time` datetime NOT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='排班规则的班次';

--
-- Table structure for table `att_over_time`
--
CREATE TABLE `att_over_time` (
                                 `id` bigint(20) NOT NULL,
                                 `over_time` datetime NOT NULL COMMENT '加班时间',
                                 `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                 `user_name` varchar(50) NOT NULL COMMENT '用户名称',
                                 `overtime_work_duration` datetime DEFAULT NULL COMMENT '工作日加班时长',
                                 `overtime_rest_duration` datetime DEFAULT NULL COMMENT '休息日加班时长',
                                 `overtime_fest_duration` datetime DEFAULT NULL COMMENT '节假日加班时长',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `att_group`
--
CREATE TABLE `att_group` (
                             `id` bigint(20) NOT NULL COMMENT 'id',
                             `name` varchar(100) NOT NULL COMMENT '考勤组名称',
                             `scheduling_rule_id` bigint(20) NOT NULL COMMENT '排班规则id',
                             `clock_rule_id` bigint(20) DEFAULT NULL COMMENT '打卡规则id',
                             `card_rule_ids` varchar(300) DEFAULT NULL COMMENT '补卡规则id',
                             `overtime_rule_id` bigint(20) DEFAULT NULL COMMENT '加班规则id',
                             `ranges` varchar(20) NOT NULL COMMENT '适用范围',
                             `tenant` int(10) NOT NULL COMMENT '租户主键',
                             `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                             `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                             `create_time` datetime NOT NULL COMMENT '创建时间',
                             `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                             `update_time` datetime NOT NULL COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='考勤组';

--
-- Table structure for table `att_scheduling`
--
CREATE TABLE `att_scheduling` (
                                  `id` bigint(20) NOT NULL,
                                  `scheduling_month` varchar(8) NOT NULL COMMENT '被排班的月份',
                                  `scheduling_date` date DEFAULT NULL COMMENT '被排班的日期',
                                  `staff_id` bigint(20) DEFAULT NULL COMMENT '被排班人id',
                                  `staff_name` varchar(32) DEFAULT NULL COMMENT '被排班人名称',
                                  `staff_group_id` bigint(20) DEFAULT NULL COMMENT '被排班人考勤组id',
                                  `staff_group_name` varchar(100) DEFAULT NULL COMMENT '被排班人考勤组名称',
                                  `classes_id` bigint(20) DEFAULT NULL COMMENT '被排的班次ID',
                                  `classes_name` varchar(100) DEFAULT NULL COMMENT '排班班次名称',
                                  `classes_nick_name` varchar(100) DEFAULT NULL COMMENT '排班班次别名',
                                  `classes_before_start_time` datetime DEFAULT NULL COMMENT '上班范围开始时间',
                                  `classes_real_start_time` datetime DEFAULT NULL COMMENT '排班开始时间',
                                  `classes_after_start_time` datetime DEFAULT NULL COMMENT '上班范围结束时间',
                                  `classes_before_end_time` datetime DEFAULT NULL COMMENT '下班范围开始时间',
                                  `classes_real_end_time` datetime DEFAULT NULL COMMENT '排班结束时间',
                                  `classes_after_end_time` datetime DEFAULT NULL COMMENT '下班范围结束时间',
                                  `scheduling_rule_id` bigint(20) DEFAULT NULL COMMENT '排班规则id',
                                  `scheduling_ways` varchar(20) DEFAULT NULL COMMENT '排班方式',
                                  `clock_rule_id` bigint(20) DEFAULT NULL COMMENT '打卡规则id',
                                  `classes_rest_start_time` datetime DEFAULT NULL COMMENT '休息开始时间',
                                  `classes_rest_end_time` datetime DEFAULT NULL COMMENT '休息结束时间',
                                  `classes_type` varchar(50) DEFAULT NULL COMMENT '班次类型',
                                  `classes_version` int(11) DEFAULT NULL COMMENT '班次版本号',
                                  `end_time_is_morrow` tinyint(1) DEFAULT '0' COMMENT '是否跨天班次',
                                  `staff_dept_id` bigint(20) DEFAULT NULL COMMENT '被排班人部门id',
                                  `staff_dept_name` varchar(100) DEFAULT NULL COMMENT '被排班人部门名称',
                                  `staff_group_ranges` varchar(20) DEFAULT NULL COMMENT '员工考勤组适用范围(全部，部分)',
                                  `hand_scheduling` tinyint(1) DEFAULT '0' COMMENT '手动排班',
                                  `started` tinyint(1) DEFAULT '0' COMMENT '已开始',
                                  `clock_read` tinyint(1) DEFAULT '0' COMMENT '是否已经生成打卡任务',
                                  `tenant` int(11) DEFAULT NULL,
                                  `create_by` varchar(32) DEFAULT NULL,
                                  `create_time` datetime DEFAULT NULL,
                                  `update_by` varchar(32) DEFAULT NULL,
                                  `update_time` datetime DEFAULT NULL,
                                  `deleted` bigint(20) DEFAULT '0',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='排班表';

--
-- Table structure for table `att_clock_device`
--
CREATE TABLE `att_clock_device` (
                                    `id` bigint(20) NOT NULL COMMENT '主键id',
                                    `rules_id` bigint(20) NOT NULL COMMENT '规则id',
                                    `device_id` bigint(20) DEFAULT NULL COMMENT '设备id',
                                    `name` varchar(100) DEFAULT NULL COMMENT '设备名称',
                                    `genre_name` varchar(20) DEFAULT NULL COMMENT '设备类型',
                                    `model_name` varchar(100) DEFAULT NULL COMMENT '设备型号',
                                    `code` varchar(20) DEFAULT NULL COMMENT '设备编码',
                                    `tenant` int(10) NOT NULL COMMENT '租户主键',
                                    `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                    `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                    `update_time` datetime NOT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='打卡设备';

--
-- Table structure for table `att_clock_rule`
--
CREATE TABLE `att_clock_rule` (
                                  `id` bigint(20) NOT NULL COMMENT '主键',
                                  `name` varchar(30) NOT NULL COMMENT '名称',
                                  `ways` varchar(200) DEFAULT NULL COMMENT '打卡方式(逗号隔开)',
                                  `out_clock` tinyint(1) DEFAULT '0' COMMENT '是否可外勤打卡',
                                  `face_recognition` varchar(20) DEFAULT NULL COMMENT '是否需要人脸识别',
                                  `tenant` int(10) NOT NULL COMMENT '租户主键',
                                  `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                  `create_time` datetime NOT NULL COMMENT '创建时间',
                                  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                  `update_time` datetime NOT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='打卡规则';

--
-- Table structure for table `att_duty_staff`
--
CREATE TABLE `att_duty_staff` (
                                  `id` bigint(20) NOT NULL COMMENT 'id',
                                  `staff_id` bigint(20) NOT NULL COMMENT '人员id',
                                  `staff_name` varchar(100) NOT NULL COMMENT '人员名称',
                                  `duty_month` varchar(10) NOT NULL COMMENT '月份',
                                  `tenant` int(10) NOT NULL COMMENT '租户主键',
                                  `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                  `create_time` datetime NOT NULL COMMENT '创建时间',
                                  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                  `update_time` datetime NOT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='值班人员';

--
-- Table structure for table `att_approve_log`
--
CREATE TABLE `att_approve_log` (
                                   `id` bigint(20) NOT NULL,
                                   `process_instance_id` varchar(50) DEFAULT NULL COMMENT '流程id（工作流）',
                                   `business_type` varchar(50) NOT NULL COMMENT '审批类型',
                                   `status` varchar(255) NOT NULL COMMENT '审核结果',
                                   `start_time` datetime DEFAULT NULL COMMENT '开始时间',
                                   `end_time` datetime DEFAULT NULL COMMENT '结束时间',
                                   `clock_in_id` bigint(20) DEFAULT NULL COMMENT '打卡记录id',
                                   `user_id` bigint(20) DEFAULT NULL COMMENT '申请人id',
                                   `tenant` int(10) NOT NULL COMMENT '租户主键',
                                   `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                   `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `scene_code` varchar(50) DEFAULT NULL COMMENT '场景code',
                                   `actual_duration` varchar(50) DEFAULT NULL COMMENT '实际时长',
                                   `apply_duration` varchar(50) DEFAULT NULL COMMENT '预估时长',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批记录表';

--
-- Table structure for table `att_clock_location`
--
CREATE TABLE `att_clock_location` (
                                      `id` bigint(20) NOT NULL COMMENT '主键',
                                      `rules_id` bigint(20) NOT NULL COMMENT '规则id',
                                      `address` varchar(1000) NOT NULL COMMENT '地址',
                                      `longitude` varchar(20) NOT NULL COMMENT '经度',
                                      `latitude` varchar(20) NOT NULL COMMENT '维度',
                                      `ranges` int(11) DEFAULT NULL COMMENT '范围(米)',
                                      `tenant` int(10) NOT NULL COMMENT '租户主键',
                                      `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                      `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                      `create_time` datetime NOT NULL COMMENT '创建时间',
                                      `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                      `update_time` datetime NOT NULL COMMENT '更新时间',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='打卡地点';

--
-- Table structure for table `att_classes`
--
CREATE TABLE `att_classes` (
                               `id` bigint(20) NOT NULL COMMENT 'id',
                               `classes_type` varchar(30) NOT NULL COMMENT '班次类型',
                               `name` varchar(100) NOT NULL COMMENT '班次名称',
                               `nick_name` varchar(100) NOT NULL COMMENT '班次简称',
                               `start_work_time` varchar(10) NOT NULL COMMENT '上班时间',
                               `start_clock_before_time` varchar(10) NOT NULL COMMENT '上班开始打卡时间',
                               `start_clock_after_time` varchar(10) NOT NULL COMMENT '上班结束打卡时间',
                               `end_work_time` varchar(10) NOT NULL COMMENT '下班时间',
                               `end_clock_before_time` varchar(10) NOT NULL COMMENT '下班开始打卡时间',
                               `end_clock_after_time` varchar(10) NOT NULL COMMENT '下班结束打卡时间',
                               `rest_start_Time` varchar(10) DEFAULT NULL COMMENT '休息开始时间',
                               `rest_end_time` varchar(10) DEFAULT NULL COMMENT '休息结束时间',
                               `end_time_is_morrow` tinyint(1) DEFAULT '0' COMMENT '跨天班次',
                               `dept_Ids` varchar(550) DEFAULT NULL COMMENT '建议应用范围(部门ids)',
                               `tenant` int(10) NOT NULL COMMENT '租户主键',
                               `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                               `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                               `create_time` datetime NOT NULL COMMENT '创建时间',
                               `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                               `update_time` datetime NOT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='班次';


--
-- Table structure for table `att_classes_record`
--
CREATE TABLE `att_classes_record` (
                                      `id` bigint(20) NOT NULL COMMENT 'id',
                                      `classes_type` varchar(30) NOT NULL COMMENT '班次类型',
                                      `name` varchar(100) NOT NULL COMMENT '班次名称',
                                      `nick_name` varchar(100) NOT NULL COMMENT '班次简称',
                                      `start_work_time` varchar(10) NOT NULL COMMENT '上班时间',
                                      `start_clock_before_time` varchar(10) NOT NULL COMMENT '上班开始打卡时间',
                                      `start_clock_after_time` varchar(10) NOT NULL COMMENT '上班结束打卡时间',
                                      `end_work_time` varchar(10) NOT NULL COMMENT '下班时间',
                                      `end_clock_before_time` varchar(10) NOT NULL COMMENT '下班开始打卡时间',
                                      `end_clock_after_time` varchar(10) NOT NULL COMMENT '下班结束打卡时间',
                                      `rest_start_Time` varchar(10) DEFAULT NULL COMMENT '休息开始时间',
                                      `rest_end_time` varchar(10) DEFAULT NULL COMMENT '休息结束时间',
                                      `end_time_is_morrow` tinyint(1) DEFAULT '0' COMMENT '跨天班次',
                                      `dept_Ids` varchar(550) DEFAULT NULL COMMENT '建议应用范围(部门ids)',
                                      `tenant` int(10) NOT NULL COMMENT '租户主键',
                                      `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                      `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                      `create_time` datetime NOT NULL COMMENT '创建时间',
                                      `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                      `update_time` datetime NOT NULL COMMENT '更新时间',
                                      `classes_id` bigint(20) NOT NULL COMMENT '班次id',
                                      `version` int(11) NOT NULL COMMENT '版本号',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

--
-- Table structure for table `att_clock_in_ledger`
--
CREATE TABLE `att_clock_in_ledger` (
                                       `id` bigint(20) NOT NULL,
                                       `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                       `user_name` varchar(255) DEFAULT NULL COMMENT '用户名称',
                                       `ledger_date` varchar(20) NOT NULL COMMENT '统计时间',
                                       `tenant` int(10) DEFAULT NULL COMMENT '租户主键',
                                       `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                       `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `org_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='打卡台账';

--
-- Table structure for table `att_scheduling_rule`
--
CREATE TABLE `att_scheduling_rule` (
                                       `id` bigint(20) NOT NULL COMMENT '规则id',
                                       `name` varchar(100) NOT NULL COMMENT '规则名称',
                                       `ways` varchar(30) NOT NULL COMMENT '排班方式(行政班/自由班)',
                                       `legal_holidays_rest` tinyint(1) DEFAULT '0' COMMENT '法定节假日是否休息',
                                       `continuous_days` int(2) NOT NULL COMMENT '一个月允许最大连续排班天数',
                                       `interval_hour` varchar(20) NOT NULL COMMENT '一天两个班次最小间隔时间',
                                       `same_shift` text COMMENT '一个月相同班次类型数量json',
                                       `tenant` int(10) NOT NULL COMMENT '租户主键',
                                       `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                       `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                       `create_time` datetime NOT NULL COMMENT '创建时间',
                                       `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                       `update_time` datetime NOT NULL COMMENT '更新时间',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='排班规则';


--
-- Table structure for table `att_scheduling_staff`
--
CREATE TABLE `att_scheduling_staff` (
                                        `id` bigint(20) NOT NULL COMMENT 'id',
                                        `staff_id` bigint(20) NOT NULL COMMENT '人员id',
                                        `staff_name` varchar(100) NOT NULL COMMENT '人员名称',
                                        `scheduling_month` varchar(10) NOT NULL COMMENT '月份',
                                        `group_id` bigint(20) NOT NULL COMMENT '考勤组id',
                                        `tenant` int(10) NOT NULL COMMENT '租户主键',
                                        `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                        `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                        `create_time` datetime NOT NULL COMMENT '创建时间',
                                        `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                        `update_time` datetime NOT NULL COMMENT '更新时间',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='排班人员表';

--
-- Table structure for table `att_clock_in_records`
--
CREATE TABLE `att_clock_in_records` (
                                        `id` bigint(20) NOT NULL,
                                        `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                        `user_name` varchar(50) NOT NULL COMMENT '用户姓名',
                                        `org_id` bigint(20) DEFAULT NULL COMMENT '部门id',
                                        `classes_id` bigint(20) DEFAULT NULL COMMENT '班次id',
                                        `classes_version` int(10) DEFAULT NULL COMMENT '班次版本',
                                        `clock_type` varchar(50) NOT NULL COMMENT '打卡类型（上班：start、下班：end，更新打卡：revise）',
                                        `uuid` varchar(50) NOT NULL COMMENT '打卡记录标识',
                                        `ways` varchar(255) DEFAULT NULL COMMENT '打卡方式',
                                        `clock_in_time` varchar(20) NOT NULL COMMENT '打卡时间(时分)',
                                        `require_clock_in_time` datetime NOT NULL COMMENT '打卡时间(年月日时分)',
                                        `actual_clock_in_time` datetime DEFAULT NULL COMMENT '实际打卡时间',
                                        `state` varchar(50) NOT NULL COMMENT '打卡状态（未打卡、正常、早退、迟到，缺卡、无效）',
                                        `deletion_duration` int(11) NOT NULL COMMENT '迟到、早退时间（分钟）',
                                        `overtime_duration` int(11) NOT NULL COMMENT '加班时长',
                                        `clock_in_date` date DEFAULT NULL COMMENT '班次时间（年月日）',
                                        `clock_in_authority` varchar(20) DEFAULT NULL COMMENT '打卡权限',
                                        `name` varchar(255) DEFAULT NULL COMMENT '打卡地址名称、wifi名称',
                                        `tenant` int(10) DEFAULT NULL COMMENT '租户主键',
                                        `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                        `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                        `create_time` datetime NOT NULL COMMENT '创建时间',
                                        `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        `clock_after_time` datetime NOT NULL COMMENT '最晚打卡时间',
                                        `photo_file_id` bigint(20) DEFAULT NULL COMMENT '打卡照片',
                                        `photo_file_url` varchar(255) DEFAULT NULL COMMENT '打卡照片url',
                                        `sync_time` datetime DEFAULT NULL COMMENT '同步时间',
                                        `face_recognition_result` varchar(255) DEFAULT NULL COMMENT '人脸识别失败结果',
                                        `past_data_type` varchar(20) DEFAULT NULL COMMENT '旧数据的类型',
                                        `out_clock` tinyint(1) NOT NULL COMMENT '是否为外勤',
                                        `work_omit` tinyint(1) DEFAULT NULL COMMENT '是否补卡',
                                        `clock_before_time` datetime DEFAULT NULL COMMENT '最早打卡时间',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='打卡记录表';

--
-- Table structure for table `att_group_staff`
--
CREATE TABLE `att_group_staff` (
                                   `id` bigint(20) NOT NULL COMMENT 'id',
                                   `staff_id` bigint(20) DEFAULT NULL COMMENT '人员id',
                                   `name` varchar(100) DEFAULT NULL COMMENT '员工姓名',
                                   `mobile` varchar(12) DEFAULT NULL COMMENT '员工电话',
                                   `group_id` bigint(20) NOT NULL COMMENT '考勤组id',
                                   `scheduling_rule_id` bigint(20) DEFAULT NULL COMMENT '排班规则id',
                                   `clock_rule_id` bigint(20) DEFAULT NULL COMMENT '打卡规则id',
                                   `dept_id` bigint(20) DEFAULT NULL COMMENT '最后一级部门id',
                                   `dept_name` varchar(100) DEFAULT NULL COMMENT '最后一级部门名称',
                                   `organizations` varchar(500) DEFAULT NULL COMMENT '多级部门id',
                                   `tenant` int(10) NOT NULL COMMENT '租户主键',
                                   `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                   `create_by` varchar(100) NOT NULL COMMENT '创建用户',
                                   `create_time` datetime NOT NULL COMMENT '创建时间',
                                   `update_by` varchar(100) NOT NULL COMMENT '更新用户',
                                   `update_time` datetime NOT NULL COMMENT '更新时间',
                                   `group_name` varchar(50) DEFAULT NULL COMMENT '考勤组名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='考勤组员工';

--
-- Table structure for table `att_clock_in_ledger_details`
--
CREATE TABLE `att_clock_in_ledger_details` (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                               `iedger_id` bigint(20) NOT NULL COMMENT '主表id',
                                               `week` varchar(50) NOT NULL COMMENT '周几',
                                               `today` int(10) NOT NULL COMMENT '几号',
                                               `state` varchar(50) DEFAULT NULL COMMENT '打卡状态',
                                               `start_work_time` varchar(10) DEFAULT NULL COMMENT '上班时间',
                                               `end_work_time` varchar(10) DEFAULT NULL COMMENT '下班时间',
                                               `start_work_state` varchar(20) DEFAULT NULL COMMENT '上班状态',
                                               `end_work_state` varchar(20) DEFAULT NULL COMMENT '下班状态',
                                               `end_work_details` varchar(255) DEFAULT NULL COMMENT '下班异常信息',
                                               `start_work_details` varchar(255) DEFAULT NULL COMMENT '上班异常信息',
                                               `start_work_omit` tinyint(1) DEFAULT NULL COMMENT '上班是否有补卡',
                                               `end_work_omit` tinyint(1) DEFAULT NULL COMMENT '下班是否有补卡',
                                               `tenant` int(10) DEFAULT NULL COMMENT '租户主键',
                                               `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                               `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                               `create_time` datetime NOT NULL COMMENT '创建时间',
                                               `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                               `classes_id` bigint(20) DEFAULT NULL COMMENT '班次id',
                                               `classes_version` int(10) DEFAULT NULL COMMENT '班次版本',
                                               `clock_in_uuid` varchar(255) DEFAULT NULL COMMENT '打卡记录uuid',
                                               `start_work_out` tinyint(1) DEFAULT NULL COMMENT '上班是否为外勤',
                                               `end_work_out` tinyint(1) DEFAULT NULL COMMENT '下班是否为外勤',
                                               `start_clock_in_time` datetime DEFAULT NULL COMMENT '上班打卡时间（用于排序）',
                                               PRIMARY KEY (`id`) USING BTREE,
                                               KEY `iedger_id` (`iedger_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1727615745848487939 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='打卡台账详情';

--
-- Table structure for table `att_duty`
--
CREATE TABLE `att_duty` (
                            `id` bigint(20) NOT NULL,
                            `duty_month` varchar(8) NOT NULL COMMENT '值班的月份',
                            `duty_date` date DEFAULT NULL COMMENT '值班班的日期',
                            `classes_id` bigint(20) DEFAULT NULL COMMENT '班次ID',
                            `classes_name` varchar(100) DEFAULT NULL COMMENT '班次名称',
                            `classes_nick_name` varchar(100) DEFAULT NULL COMMENT '班次简称',
                            `classes_type` varchar(30) DEFAULT NULL COMMENT '班次类型',
                            `classes_end_time_is_morrow` tinyint(1) DEFAULT NULL COMMENT '跨天班次',
                            `classes_real_start_time` datetime DEFAULT NULL COMMENT '开始时间',
                            `classes_real_end_time` datetime DEFAULT NULL COMMENT '结束时间',
                            `staff_id` bigint(20) DEFAULT NULL COMMENT '值班人id',
                            `staff_name` varchar(32) DEFAULT NULL COMMENT '值班人名称',
                            `staff_dept_id` bigint(20) DEFAULT NULL COMMENT '值班人部门id',
                            `staff_dept_name` varchar(100) DEFAULT NULL COMMENT '值班人部门名称',
                            `tenant` int(11) DEFAULT NULL,
                            `create_by` varchar(32) DEFAULT NULL,
                            `create_time` datetime DEFAULT NULL,
                            `update_by` varchar(32) DEFAULT NULL,
                            `update_time` datetime DEFAULT NULL,
                            `deleted` bigint(20) DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='值班表';

--
-- Table structure for table `att_dumu_message`
--
CREATE TABLE `att_dumu_message` (
                                    `id` bigint(20) NOT NULL,
                                    `msg_id` varchar(255) NOT NULL COMMENT '消息唯一标识',
                                    `tenant` int(10) NOT NULL COMMENT '租户主键',
                                    `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
                                    `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                    `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='度目推送消息记录';
-- 人脸识别服务地址
INSERT INTO `cube-base-dictionary`.`dictionary_item` (`id`, `code`, `remarks`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1719266648601755650, 'att-face-recognition', '人脸识别服务', 0, '17311369782', '2023-10-31 16:14:46', '17311369782', '2023-10-31 16:14:46');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1719266699105370113, 'http://192.168.2.158:5001/identity-verify', 'att_identity_verify', '人脸识别服务', 58197260, 'v1.0.0', NULL, 'att-face-recognition', 0, '17311369782', '2023-10-31 16:14:58', '17311369782', '2023-10-31 16:14:58', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1719266699105370256, 'http://192.168.2.158:5001/identity-user-changed', 'att_update_staff_photograph', 'ouaa人员修改后更新考勤照片的地址', 58197260, 'v1.0.0', NULL, 'att-face-recognition', 0, '17311369782', '2023-10-31 16:14:58', '17311369782', '2023-10-31 16:14:58', 'ENABLE');
INSERT INTO `cube-base-dictionary`.`data_dictionary` (`id`, `name`, `code`, `remarks`, `tenant`, `version`, `variable`, `item_code`, `deleted`, `create_by`, `create_time`, `update_by`, `update_time`, `status`) VALUES (1727173107276652546, 'http://192.168.2.88:8080/', 'baidu_machine', '百度-考勤机地址', 58197260, 'v1.0.0', NULL, 'att-face-recognition', 0, '17311369782', '2023-11-22 11:52:13', '17311369782', '2023-11-22 11:52:13', 'ENABLE');
