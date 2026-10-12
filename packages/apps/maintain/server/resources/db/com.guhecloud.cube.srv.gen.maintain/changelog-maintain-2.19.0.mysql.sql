-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND column_name='repair_status') AS column_exists;
ALTER TABLE inspect_record ADD repair_status varchar(100) NULL COMMENT '修复状态';
ALTER TABLE inspect_record CHANGE repair_status repair_status varchar(100) NULL COMMENT '修复状态' AFTER `result`;

-- changeset chx:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND column_name='cancel_reason') AS column_exists;
ALTER TABLE inspect_record ADD cancel_reason varchar(300) NULL COMMENT '取消原因';
ALTER TABLE inspect_record CHANGE cancel_reason cancel_reason varchar(300) NULL COMMENT '取消原因' AFTER repair_status;

-- changeset chx:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND column_name='cancel_file_key') AS column_exists;
ALTER TABLE inspect_record ADD cancel_file_key BIGINT(20) NULL COMMENT '取消修复的文件key';
ALTER TABLE inspect_record CHANGE cancel_file_key cancel_file_key BIGINT(20) NULL COMMENT '取消修复的文件key' AFTER cancel_reason;

-- changeset chx:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND column_name='cancel_inspect_file_key') AS column_exists;
ALTER TABLE inspect_record ADD cancel_inspect_file_key BIGINT(20) NULL COMMENT '取消巡检文件key';
ALTER TABLE inspect_record CHANGE cancel_inspect_file_key cancel_inspect_file_key BIGINT(20) NULL COMMENT '取消巡检文件key' AFTER `result`;

-- changeset chx:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND column_name='cancel_inspect_reason') AS column_exists;
ALTER TABLE inspect_record ADD cancel_inspect_reason varchar(300) NULL COMMENT '取消巡检说明';
ALTER TABLE inspect_record CHANGE cancel_inspect_reason cancel_inspect_reason varchar(300) NULL COMMENT '取消巡检说明' AFTER cancel_inspect_file_key;

-- changeset chx:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record_repair' ) AS column_exists;
CREATE TABLE IF NOT EXISTS `inspect_record_repair` (
  `id` bigint(20) unsigned NOT NULL,
  `record_id` bigint(20) unsigned DEFAULT NULL COMMENT '巡检记录id',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '处理人ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '处理人名称',
  `repair_status` varchar(100) DEFAULT NULL COMMENT '修复状态',
  `content` varchar(300) DEFAULT NULL COMMENT '处理内容',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `tenant` int(11) DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` bigint(20) unsigned DEFAULT NULL COMMENT '删除标志',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='巡检手动修复记录';