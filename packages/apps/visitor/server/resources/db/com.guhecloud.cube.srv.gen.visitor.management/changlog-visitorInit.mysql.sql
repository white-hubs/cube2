-- liquibase formatted sql

-- changeset chenhongxin:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'system_login_info') AS column_exists;
-- cube_srv_gen_adapter_third.system_login_info definition
CREATE TABLE `system_login_info` (
                                     `id` bigint(20) unsigned NOT NULL COMMENT '主键',
                                     `scene_code` varchar(100) NOT NULL COMMENT '场景code',
                                     `base_url` varchar(200) DEFAULT NULL COMMENT '基地址',
                                     `user_id` varchar(100) DEFAULT NULL COMMENT '用户id',
                                     `username` varchar(50) DEFAULT NULL COMMENT '账号',
                                     `password` varchar(200) DEFAULT NULL COMMENT '密码',
                                     `client_id` varchar(100) DEFAULT NULL COMMENT '凭证id',
                                     `client_secret` varchar(100) DEFAULT NULL COMMENT '凭证密钥',
                                     `access_token` varchar(200) DEFAULT NULL COMMENT '鉴权token',
                                     `token_expires` datetime DEFAULT NULL COMMENT 'token到期时间',
                                     `refresh_token` varchar(200) DEFAULT NULL COMMENT '刷新token',
                                     `callback_base_url` varchar(200) DEFAULT NULL COMMENT '回调基地址',
                                     `remark` varchar(200) DEFAULT NULL COMMENT '备注说明',
                                     `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
                                     `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否已删除',
                                     `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                     `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                     `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE KEY `system_login_info_un` (`scene_code`,`tenant`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统登录信息表';

-- changeset chenhongxin:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'blacklist') AS column_exists;
-- cube_srv_gen_adapter_third.blacklist definition
CREATE TABLE `blacklist` (
                             `id` bigint(20) unsigned NOT NULL COMMENT '主键id',
                             `third_user_code` int(11) DEFAULT NULL COMMENT '第三方人员code',
                             `name` varchar(200) DEFAULT NULL COMMENT '名称',
                             `phone` varchar(20) DEFAULT NULL COMMENT '手机',
                             `device_code` varchar(500) DEFAULT NULL COMMENT '设备唯一标识',
                             `sex` varchar(10) DEFAULT NULL COMMENT '性别',
                             `reason` varchar(500) DEFAULT NULL COMMENT '原因',
                             `pictures` varchar(500) DEFAULT NULL COMMENT '照片ids',
                             `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
                             `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否已删除',
                             `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                             `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                             `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='黑名单';

-- changeset chenhongxin:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'visit_area') AS column_exists;
-- cube_srv_gen_adapter_third.visit_area definition
CREATE TABLE `visit_area` (
                              `id` bigint(20) unsigned NOT NULL COMMENT '主键id',
                              `area_id` bigint(20) DEFAULT NULL COMMENT '区域id',
                              `area_name` varchar(100) DEFAULT NULL COMMENT '区域名称',
                              `device_number` int(11) DEFAULT NULL COMMENT '关联设备数',
                              `visit_periods` text COMMENT '可访问时段(列表展示用)',
                              `normal` tinyint(1) DEFAULT '1' COMMENT '1正常(0逻辑删除就不正常)',
                              `create_user_name` varchar(100) DEFAULT NULL COMMENT '创建人名称',
                              `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
                              `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否已删除',
                              `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                              `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                              `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='设置的区域表';

-- changeset chenhongxin:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'visit_area_detail') AS column_exists;
-- cube_srv_gen_adapter_third.visit_area_detail definition
CREATE TABLE `visit_area_detail` (
                                     `id` bigint(20) unsigned NOT NULL COMMENT '主键id',
                                     `area_id` bigint(20) DEFAULT NULL COMMENT '区域id(对应setup_area的area_id)',
                                     `area_name` varchar(100) DEFAULT NULL COMMENT '区域名称',
                                     `start_date` datetime DEFAULT NULL COMMENT '开始日期',
                                     `end_date` datetime DEFAULT NULL COMMENT '结束日期',
                                     `date_period_str` varchar(100) DEFAULT NULL COMMENT '日期段字符串',
                                     `start_date_str` varchar(20) DEFAULT NULL COMMENT '开始日期字符串',
                                     `end_date_str` varchar(20) DEFAULT NULL COMMENT '结束日期字符串',
                                     `time_str` varchar(100) DEFAULT NULL COMMENT '时间字符串',
                                     `start_time_str` varchar(10) DEFAULT NULL COMMENT '开始时间字符串(HH:mm:ss)',
                                     `end_time_str` varchar(10) DEFAULT NULL COMMENT '结束时间字符串(HH:mm:ss)',
                                     `next_day_bool` tinyint(1) DEFAULT NULL COMMENT '跨天标志true表示跨天',
                                     `total_number` int(11) DEFAULT '0' COMMENT '总数量',
                                     `normal` tinyint(4) DEFAULT '1' COMMENT '1正常(0删除就不正常)',
                                     `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
                                     `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否已删除',
                                     `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                     `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                     `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE KEY `visit_area_detail_area_id_IDX` (`area_id`,`date_period_str`,`time_str`,`deleted`,`tenant`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='设置的区域访问时间段表';

-- changeset chenhongxin:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'reservation_record') AS column_exists;
-- cube_srv_gen_adapter_third.reservation_record definition
CREATE TABLE `reservation_record` (
                                      `id` bigint(20) unsigned NOT NULL COMMENT '主键预约id',
                                      `name` varchar(100) DEFAULT NULL COMMENT '预约人名称',
                                      `phone` varchar(20) DEFAULT NULL COMMENT '预约人手机',
                                      `dw` varchar(300) DEFAULT NULL COMMENT '单位',
                                      `address` varchar(300) DEFAULT NULL COMMENT '住址',
                                      `person_sum` int(11) DEFAULT NULL COMMENT '预约人数',
                                      `reason` varchar(100) DEFAULT NULL COMMENT '来访理由',
                                      `face_file` text COMMENT '人脸图片base64图片',
                                      `face_file_id` bigint(20) DEFAULT NULL COMMENT '人脸图片文件id',
                                      `certificate_type` int(11) DEFAULT NULL COMMENT '证件类型',
                                      `certificate_number` varchar(50) DEFAULT NULL COMMENT '证件号',
                                      `area_id` bigint(20) DEFAULT NULL COMMENT '预约区域id',
                                      `area_code` varchar(100) DEFAULT NULL COMMENT '预约区域code',
                                      `area_name` varchar(300) DEFAULT NULL COMMENT '预约区域名称',
                                      `start_date_time` datetime DEFAULT NULL COMMENT '预约开始时间',
                                      `end_date_time` datetime DEFAULT NULL COMMENT '预约结束时间',
                                      `day_str` varchar(20) DEFAULT NULL COMMENT '预约日期字符串',
                                      `time_str` varchar(20) DEFAULT NULL COMMENT '预约时段字符串',
                                      `next_day_bool` tinyint(1) DEFAULT NULL COMMENT '跨天标志(true表示跨天)',
                                      `status` int(11) DEFAULT NULL COMMENT '状态 0：待审核 1：预约 2：在访 3：离访 4:预约取消',
                                      `rv_start_time` datetime DEFAULT NULL COMMENT '实际来访时间(第三方操作字段不更新勿使用)',
                                      `rv_end_time` datetime DEFAULT NULL COMMENT '实际离访时间(第三方操作字段不更新勿使用)',
                                      `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
                                      `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否已删除',
                                      `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                      `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='预约记录表';


-- changeset chenhongxin:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'visitor_status_change_record') AS column_exists;
-- cube_srv_gen_adapter_third.visitor_status_change_record definition
CREATE TABLE `visitor_status_change_record` (
                                                `id` bigint(20) unsigned NOT NULL COMMENT '主键',
                                                `reservation_id` bigint(20) NOT NULL COMMENT '预约id(访客id)',
                                                `name` varchar(100) DEFAULT NULL COMMENT '访客姓名',
                                                `code` varchar(100) DEFAULT NULL COMMENT '设备编号',
                                                `status` varchar(2) DEFAULT NULL COMMENT '访客状态 0:默认,1:预约,2:在访,3：离访,4:取消预约',
                                                `isv_id` varchar(100) DEFAULT NULL COMMENT '被访人ID',
                                                `phone` varchar(20) DEFAULT NULL COMMENT '电话',
                                                `card_number` varchar(64) DEFAULT NULL COMMENT '卡号',
                                                `appointment_array_time` varchar(50) DEFAULT NULL COMMENT '预约到达时间',
                                                `appointment_leave_time` varchar(50) DEFAULT NULL COMMENT '预约离开时间',
                                                `leave_time` varchar(50) DEFAULT NULL COMMENT '离开时间',
                                                `array_time` varchar(50) DEFAULT NULL COMMENT '到达时间',
                                                `visitor_pic` varchar(300) DEFAULT NULL COMMENT '上传的访客照片地址',
                                                `capture_pic` varchar(300) DEFAULT NULL COMMENT '抓拍的访客照片地址',
                                                `face_path` varchar(300) DEFAULT NULL COMMENT '人脸图片地址，visitorPic和capturePic哪个参数有值，facepath就取值哪个，',
                                                `certificate_number` varchar(50) DEFAULT NULL COMMENT '认证号码',
                                                `certificate_type` varchar(50) DEFAULT NULL COMMENT '认证类型',
                                                `car_number` varchar(50) DEFAULT NULL COMMENT '车牌号码',
                                                `work_place` varchar(200) DEFAULT NULL COMMENT '工作地点',
                                                `reason` varchar(200) DEFAULT NULL COMMENT '来访原因',
                                                `domain_id` varchar(50) DEFAULT NULL COMMENT '域ID',
                                                `channel_code` varchar(100) DEFAULT NULL COMMENT '通道编码',
                                                `tenant` int(10) unsigned DEFAULT NULL COMMENT '租户编号',
                                                `deleted` bigint(20) unsigned DEFAULT '0' COMMENT '是否已删除',
                                                `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
                                                `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
                                                `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='预约状态变更记录表';