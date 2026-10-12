-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'alarm_level_play_config') AS column_exists;
CREATE TABLE `alarm_level_play_config` (
  `id` bigint(20) NOT NULL,
  `alarm_level_code` varchar(50) NOT NULL COMMENT '报警级别编号',
  `looped` tinyint(1) NOT NULL COMMENT '是否为循环播放的',
  `play_num` int(11) DEFAULT NULL COMMENT '播放次数',
  `tenant` int(10) DEFAULT NULL,
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_alarm_level_code_tenant_deleted` (`alarm_level_code`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警级别播放配置';

