-- liquibase formatted sql

-- changeset changsha:drone-alarm-dispatch-config-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'drone_alarm_dispatch_config') AS table_exists;
CREATE TABLE IF NOT EXISTS `drone_alarm_dispatch_config` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `enabled` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  `alarm_target_ids` text COMMENT '报警对象ID快照JSON',
  `alarm_target_names` text COMMENT '报警对象名称快照JSON',
  `alarm_level_ids` text COMMENT '报警级别ID快照JSON',
  `alarm_level_names` text COMMENT '报警级别名称快照JSON',
  `alarm_type_ids` text COMMENT '报警类型ID快照JSON',
  `alarm_type_names` text COMMENT '报警类型名称快照JSON',
  `drone_device_id` bigint(20) DEFAULT NULL COMMENT '无人机设备ID',
  `drone_device_name` varchar(255) DEFAULT NULL COMMENT '无人机设备名称快照',
  `finish_action` int(11) DEFAULT NULL COMMENT '完成动作',
  `fly_height` decimal(18,2) DEFAULT NULL COMMENT '飞行高度',
  `scene_id` bigint(20) DEFAULT NULL COMMENT '场景ID',
  `scene_code` varchar(64) DEFAULT NULL COMMENT '场景编号',
  `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant` int(11) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  KEY `idx_drone_alarm_dispatch_config_tenant` (`tenant`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='无人机报警派飞配置表';

-- changeset changsha:drone-alarm-dispatch-record-ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'drone_alarm_dispatch_record') AS table_exists;
CREATE TABLE IF NOT EXISTS `drone_alarm_dispatch_record` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `alarm_record_id` bigint(20) DEFAULT NULL COMMENT '报警记录ID',
  `alarm_code` varchar(128) DEFAULT NULL COMMENT '报警编号',
  `config_id` bigint(20) DEFAULT NULL COMMENT '匹配配置ID',
  `alarm_target_id` bigint(20) DEFAULT NULL COMMENT '报警对象ID',
  `alarm_target_name` varchar(255) DEFAULT NULL COMMENT '报警对象名称',
  `alarm_level_id` bigint(20) DEFAULT NULL COMMENT '报警级别ID',
  `alarm_level_name` varchar(255) DEFAULT NULL COMMENT '报警级别名称',
  `alarm_type_id` bigint(20) DEFAULT NULL COMMENT '报警类型ID',
  `alarm_type_name` varchar(255) DEFAULT NULL COMMENT '报警类型名称',
  `drone_device_id` bigint(20) DEFAULT NULL COMMENT '无人机设备ID',
  `drone_device_name` varchar(255) DEFAULT NULL COMMENT '无人机设备名称',
  `nest_sn` varchar(64) DEFAULT NULL COMMENT '机库编号',
  `longitude` decimal(18,8) DEFAULT NULL COMMENT '目标经度',
  `latitude` decimal(18,8) DEFAULT NULL COMMENT '目标纬度',
  `task_name` varchar(255) DEFAULT NULL COMMENT '快速任务名称',
  `dispatch_status` int(11) DEFAULT NULL COMMENT '派飞状态: 1成功 2失败',
  `fail_reason` text COMMENT '失败原因',
  `request_body` longtext COMMENT '快速任务请求体',
  `response_body` longtext COMMENT '三方响应体',
  `track_file_type` varchar(32) DEFAULT NULL COMMENT '航迹文件类型',
  `track_id` varchar(128) DEFAULT NULL COMMENT '航迹ID',
  `track_url` varchar(1024) DEFAULT NULL COMMENT '航迹URL',
  `nest_name` varchar(255) DEFAULT NULL COMMENT '机库名称',
  `dispatch_time` datetime DEFAULT NULL COMMENT '派飞时间',
  `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant` int(11) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  KEY `idx_drone_alarm_dispatch_record_alarm` (`tenant`,`alarm_record_id`,`deleted`),
  KEY `idx_drone_alarm_dispatch_record_status` (`tenant`,`dispatch_status`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='无人机报警派飞日志表';
