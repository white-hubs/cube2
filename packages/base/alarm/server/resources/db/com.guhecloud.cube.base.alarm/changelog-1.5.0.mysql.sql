-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'alarm_record_notify_channel') AS column_exists;
CREATE TABLE `alarm_record_notify_channel` (
  `id` bigint(20) unsigned NOT NULL,
  `alarm_record_id` bigint(20) unsigned DEFAULT NULL COMMENT '报警记录主键',
  `channel_types` varchar(500) DEFAULT NULL COMMENT '通知渠道(多个,号隔开)',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警记录通知渠道';

