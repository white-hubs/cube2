-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'reservation_record' AND column_name='process_id') AS column_exists;
ALTER TABLE `reservation_record` ADD COLUMN `process_id` varchar(255) NULL DEFAULT NULL COMMENT '邀约流程id';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'pass_record') AS column_exists;
CREATE TABLE `pass_record` (
                               `id` bigint(20) NOT NULL,
                               `visitor_name` varchar(100) DEFAULT NULL,
                               `person_type` varchar(50) DEFAULT NULL,
                               `enter_or_exist` bigint(20) DEFAULT NULL,
                               `visitor_pic` text,
                               `visitor_id` bigint(20) DEFAULT NULL,
                               `channel_code` varchar(100) DEFAULT NULL,
                               `device_code` varchar(100) DEFAULT NULL,
                               `device_name` varchar(100) DEFAULT NULL,
                               `swing_time` varchar(50) DEFAULT NULL,
                               `open_type` varchar(50) DEFAULT NULL,
                               `tenant` bigint(20) DEFAULT NULL,
                               `deleted` bigint(20) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'visitor_invite_record') AS column_exists;
CREATE TABLE `visitor_invite_record` (
                                         `id` bigint(20) unsigned NOT NULL COMMENT '主键',
                                         `visitor_name` varchar(100) DEFAULT NULL COMMENT '访客姓名',
                                         `visitor_id_card` varchar(20) DEFAULT NULL COMMENT '访客身份证号',
                                         `visitor_phone` varchar(20) DEFAULT NULL COMMENT '访客联系电话',
                                         `area_id` varchar(100) DEFAULT NULL COMMENT '访问区域',
                                         `access_date` varchar(50) DEFAULT NULL COMMENT '访问日期',
                                         `time_str` varchar(50) DEFAULT NULL COMMENT '访问时段',
                                         `reason` varchar(255) DEFAULT NULL COMMENT '访问事由',
                                         `plate_number` varchar(20) DEFAULT NULL COMMENT '车牌号码',
                                         `follow_visitors` text COMMENT '随访人员',
                                         `status` varchar(10) DEFAULT NULL COMMENT '邀约状态',
                                         `process_ins_id` varchar(50) DEFAULT NULL COMMENT '邀约流程id',
                                         `bar_code` varchar(100) DEFAULT NULL COMMENT '预约码',
                                         `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
                                         `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否已删除',
                                         `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         `reservation_record_id` varchar(255) DEFAULT NULL,
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='访客邀约记录表';

-- changeset zengyadong:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'system_login_info' AND column_name='second_visitor_type') AS column_exists;
ALTER TABLE `system_login_info` ADD COLUMN `second_visitor_type` bigint(20) NULL DEFAULT NULL COMMENT '访客类型';

-- changeset danke:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'reservation_record' AND column_name='v_bar_code') AS column_exists;
ALTER TABLE `reservation_record` ADD COLUMN `v_bar_code` varchar(255) NULL DEFAULT NULL COMMENT '预约码';