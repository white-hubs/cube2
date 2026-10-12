-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'pork_arrange_push_data') AS column_exists;
CREATE TABLE `pork_arrange_push_data` (
  `id` bigint(20) unsigned NOT NULL,
  `guid` varchar(100) DEFAULT NULL COMMENT '客户端唯一 guid',
  `pork_arrange_id` bigint(20) unsigned DEFAULT NULL COMMENT '理货记录id',
  `device_id` varchar(100) DEFAULT NULL COMMENT '理货设备编号',
  `tally_id` varchar(100) DEFAULT NULL COMMENT '批次号',
  `event_type` int(1) unsigned DEFAULT NULL COMMENT '理货状态',
  `start_time` datetime DEFAULT NULL COMMENT '理货开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '理货结束时间',
  `arrange_details` text COMMENT '理货详细信息',
  `push_times` int(2) unsigned DEFAULT NULL COMMENT '推送次数',
  `status` varchar(255) DEFAULT NULL COMMENT '推送状态',
  `tenant` int(10) unsigned DEFAULT NULL,
  `deleted` bigint(20) unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;