-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'sat_user_msg_ignored_config') AS column_exists;
CREATE TABLE `sat_user_msg_ignored_config` (
  `id` bigint(20) unsigned NOT NULL,
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户主键',
  `enabled` tinyint(1) unsigned DEFAULT NULL COMMENT '是否开启',
  `start_time` time DEFAULT NULL COMMENT '开始时间',
  `end_time` time DEFAULT NULL COMMENT '结束时间',
  `tomorrows` tinyint(1) unsigned DEFAULT NULL COMMENT '是否次日',
  `channels` varchar(500) DEFAULT NULL COMMENT '忽略的消息通知通道',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id_tenant_deleted` (`user_id`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息忽略配置';