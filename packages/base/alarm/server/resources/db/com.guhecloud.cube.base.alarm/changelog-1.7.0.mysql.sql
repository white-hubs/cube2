-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'alarm_record_follow_up') AS column_exists;
CREATE TABLE `alarm_record_follow_up` (
  `id` bigint(20) unsigned NOT NULL,
  `alarm_record_id` bigint(20) unsigned DEFAULT NULL COMMENT '报警记录主键',
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `follow_up_user_id` bigint(20) unsigned DEFAULT NULL COMMENT '跟进人主键',
  `follow_up_user_name` varchar(50) DEFAULT NULL COMMENT '跟进人名称',
  `follow_up_time` datetime DEFAULT NULL COMMENT '跟进时间',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_alarm_record_id` (`alarm_record_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警记录跟进信息';

