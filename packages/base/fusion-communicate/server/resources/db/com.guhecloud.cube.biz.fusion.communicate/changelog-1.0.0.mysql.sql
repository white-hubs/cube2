-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'third_fusion_account') AS column_exists;
CREATE TABLE `third_fusion_account` (
  `id` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `code` varchar(64) DEFAULT NULL COMMENT '编号',
  `number` varchar(64) DEFAULT NULL COMMENT '通讯号码',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `level` varchar(32) DEFAULT NULL COMMENT '账号等级',
  `usage_state` varchar(64) DEFAULT NULL COMMENT '账号使用状态',
  `provider_code` varchar(64) DEFAULT NULL COMMENT '厂家编号',
  `tenant` int(10) DEFAULT NULL,
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_number_manufacturer_code_tenant_deleted` (`number`,`provider_code`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通讯账号';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'third_fusion_account_allocation') AS column_exists;
CREATE TABLE `third_fusion_account_allocation` (
  `id` bigint(20) unsigned NOT NULL,
  `provider_code` varchar(64) DEFAULT NULL COMMENT '厂家编号',
  `third_account_number` varchar(64) DEFAULT NULL COMMENT '第三方账号号码',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '系统用户主键',
  `keep_alive_time` datetime DEFAULT NULL COMMENT '保活时间',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方账号分配表';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'third_fusion_file') AS column_exists;
CREATE TABLE `third_fusion_file` (
  `id` bigint(20) NOT NULL,
  `time` datetime DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL COMMENT '数据类型 视频 还是 语音',
  `size` int(10) DEFAULT NULL COMMENT '文件大小',
  `suffix` varchar(50) DEFAULT NULL COMMENT '文件后缀',
  `url` varchar(200) DEFAULT NULL COMMENT '文件路径',
  `caller_user_id` bigint(20) DEFAULT NULL COMMENT '主叫用户主键',
  `caller_name` varchar(50) DEFAULT NULL COMMENT '主叫用户名称',
  `callee_user_id` bigint(20) DEFAULT NULL COMMENT '被叫用户主键',
  `callee_name` varchar(50) DEFAULT NULL COMMENT '被叫用户名称',
  `file_time_length` bigint(20) DEFAULT NULL COMMENT '时长 秒',
  `file_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `file_end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `tenant` int(10) DEFAULT NULL,
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通讯文件';

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user_gps_history') AS column_exists;
CREATE TABLE `user_gps_history` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户主键',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `direction` int(10) DEFAULT NULL COMMENT '方向',
  `lng` double DEFAULT NULL COMMENT '经度',
  `lat` double DEFAULT NULL COMMENT '纬度',
  `address` varchar(500) DEFAULT NULL COMMENT '位置信息',
  `height` double DEFAULT NULL COMMENT '高度',
  `speed` double DEFAULT NULL COMMENT '速度',
  `last_upload_time` datetime DEFAULT NULL COMMENT '最后上传时间(当位置信息无变化时,只更新该字段)',
  `tenant` int(10) DEFAULT NULL,
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_device_id_device_code_upload_time_tenant` (`user_id`,`upload_time`,`tenant`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户坐标信息历史记录';
