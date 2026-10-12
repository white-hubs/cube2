-- liquibase formatted sql

-- changeset changsha:drone-alarm-dispatch-notify-state-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'drone_alarm_dispatch_notify_state') AS table_exists;
CREATE TABLE IF NOT EXISTS `drone_alarm_dispatch_notify_state` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `alarm_record_id` bigint(20) NOT NULL COMMENT '报警记录ID',
  `notify_user_ids` text COMMENT '报警通知人ID集合JSON',
  `assigned_received` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否已收到报警初始化指派完成消息',
  `dispatch_record_id` bigint(20) DEFAULT NULL COMMENT '关联派飞日志ID',
  `notify_status` int(11) NOT NULL DEFAULT 0 COMMENT '通知状态: 0待发送 1发送中 2发送成功 3发送失败',
  `notify_send_time` datetime DEFAULT NULL COMMENT '通知发送时间',
  `notify_fail_reason` text COMMENT '通知失败原因',
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
  UNIQUE KEY `uk_drone_alarm_dispatch_notify_state_alarm` (`tenant`,`alarm_record_id`,`deleted`),
  KEY `idx_drone_alarm_dispatch_notify_state_status` (`tenant`,`notify_status`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='无人机报警派飞结果通知状态表';
