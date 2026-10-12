-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'alarm_record_push_config') AS column_exists;
CREATE TABLE `alarm_record_push_config` (
  `id` bigint(20) NOT NULL,
  `alarm_record_id` bigint(20) DEFAULT NULL COMMENT '报警记录主键',
  `turn_warning_sound` tinyint(1) DEFAULT NULL COMMENT '是否开启警告声音',
  `tenant` int(10) DEFAULT NULL,
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警记录推送配置';

