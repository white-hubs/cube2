-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'broadcast_play_plan') AS column_exists;
CREATE TABLE `broadcast_play_plan` (
  `id` bigint unsigned NOT NULL,
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `play_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '播放参数',
  `current_play_text_task_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '当前播放任务id',
  `tenant` int unsigned DEFAULT NULL,
  `deleted` bigint unsigned DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='广播播放计划';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'broadcast_play_record') AS column_exists;
CREATE TABLE `broadcast_play_record` (
  `id` bigint unsigned NOT NULL,
  `broadcast_time` datetime DEFAULT NULL COMMENT '广播时间',
  `broadcast_template_id` bigint unsigned DEFAULT NULL COMMENT '广播模板主键',
  `broadcast_template_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '广播模板名称',
  `broadcast_template_content` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '广播模板原内容',
  `broadcast_template_content_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'TEXT' COMMENT '内容类型(TEXT文本，AUDIO音频)',
  `broadcast_template_content_param` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '广播模板参数',
  `broadcast_play_content` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '广播内容',
  `broadcast_play_num` int unsigned DEFAULT NULL COMMENT '广播次数',
  `broadcast_play_devices` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '广播终端',
  `broadcast_play_plan_id` bigint DEFAULT NULL COMMENT '广播计划主键',
  `operator_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人',
  `operator_by_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人名称',
  `operator_by_dept_id` bigint unsigned DEFAULT NULL COMMENT '操作人部门',
  `operator_by_dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人部门名称',
  `broadcast_play_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '播放结果',
  `broadcast_play_error_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '播放异常描述',
  `tenant` int unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='广播播放记录';

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'broadcast_play_record_device') AS column_exists;
CREATE TABLE `broadcast_play_record_device` (
  `id` bigint unsigned NOT NULL,
  `broadcast_play_record_id` bigint unsigned DEFAULT NULL COMMENT '广播播放记录主键',
  `device_id` bigint unsigned DEFAULT NULL COMMENT '设备主键',
  `device_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备编号',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
  `tenant` int unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint unsigned DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='广播播放记录设备';

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'broadcast_template') AS column_exists;
CREATE TABLE `broadcast_template` (
  `id` bigint unsigned NOT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板名称',
  `content` varchar(350) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板内容',
  `content_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'TEXT' COMMENT '内容类型(TEXT文本，AUDIO音频)',
  `defaulted` tinyint unsigned DEFAULT NULL COMMENT '是否是默认模板',
  `tenant` int unsigned DEFAULT NULL COMMENT '租户编号',
  `deleted` bigint unsigned DEFAULT NULL COMMENT '是否删除',
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='广播模板';
