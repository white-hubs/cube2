-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'video_command_record') AS column_exists;
CREATE TABLE `video_command_record` (
  `id` bigint(20) unsigned NOT NULL,
  `request_id` varchar(50) DEFAULT NULL COMMENT '请求id',
  `command` varchar(50) DEFAULT NULL COMMENT '命令',
  `manufacturer_code` varchar(50) DEFAULT NULL COMMENT '厂家编号',
  `request_param` text COMMENT '请求参数',
  `result_param` text COMMENT '返回参数',
  `status` varchar(50) DEFAULT NULL COMMENT '下发状态',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='监控命令下发记录';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'video_file') AS column_exists;
CREATE TABLE `video_file` (
  `id` bigint(20) unsigned NOT NULL,
  `file_id` bigint(20) unsigned DEFAULT NULL COMMENT '文件id',
  `locally_stored` tinyint(1) DEFAULT NULL COMMENT '是否本地存储',
  `store_bucket` varchar(100) DEFAULT NULL COMMENT '存储桶名',
  `size` double DEFAULT NULL COMMENT '文件大小',
  `type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `path` varchar(300) DEFAULT NULL COMMENT '文件路径',
  `locally_stored_status` varchar(50) DEFAULT NULL COMMENT '本地存储状态',
  `retry_times` int(2) DEFAULT NULL COMMENT '本次存储的次数',
  `retry_next_time` datetime DEFAULT NULL COMMENT '下次重试时间',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `store_scene_code` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
