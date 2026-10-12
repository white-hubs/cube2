-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'handover_record') AS column_exists;
CREATE TABLE `handover_record` (
  `id` bigint unsigned NOT NULL,
  `record_name` varchar(50) DEFAULT NULL COMMENT '记录名称',
  `task_num` bigint DEFAULT NULL COMMENT '交接任务数',
  `shift_over_user` bigint DEFAULT NULL COMMENT '交班人',
  `shift_over_time` datetime DEFAULT NULL COMMENT '交班时间',
  `take_over_user` bigint DEFAULT NULL COMMENT '接班人',
  `take_over_time` datetime DEFAULT NULL COMMENT '接班时间',
  `status` varchar(50) DEFAULT NULL COMMENT '交班状态',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `shift_sign_file_id` bigint DEFAULT NULL COMMENT '交班人签名',
  `take_sign_file_id` bigint DEFAULT NULL COMMENT '接班人签名',
  `tenant` int unsigned DEFAULT NULL,
  `deleted` bigint unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='工作交接记录';

-- changeset zengyadong:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'handover_task_ref') AS column_exists;
CREATE TABLE `handover_task_ref` (
  `id` bigint unsigned NOT NULL,
  `handover_id` bigint DEFAULT NULL COMMENT '交接记录ID',
  `task_id` bigint DEFAULT NULL COMMENT '任务id',
  `task_type` varchar(50) DEFAULT NULL COMMENT '任务类型',
  `tenant` int unsigned DEFAULT NULL,
  `deleted` bigint unsigned DEFAULT NULL,
  `create_by` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='交接记录关联任务';

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'todo_tag' AND column_name='handover') AS column_exists;
ALTER TABLE `todo_tag` ADD COLUMN `handover` INT NULL DEFAULT NULL COMMENT '是否允许交班';