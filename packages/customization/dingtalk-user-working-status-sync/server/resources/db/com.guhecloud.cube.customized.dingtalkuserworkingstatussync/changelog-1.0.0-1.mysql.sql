-- liquibase formatted sql

-- changeset lanchun:ddl_1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'ding_talk_schedule') AS column_exists;
CREATE TABLE `ding_talk_schedule` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `ding_talk_schedule_id` bigint DEFAULT NULL COMMENT '排班ID',
  `check_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '考勤类型：Onduty：上班打卡, OffDuty：下班打卡',
  `plan_check_time` datetime DEFAULT NULL COMMENT '计划打卡时间',
  `group_id` bigint DEFAULT NULL COMMENT '考勤组ID',
  `userid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户userId',
  `approve_id` bigint DEFAULT NULL COMMENT '排班绑定的审批单ID',
  `work_date` datetime DEFAULT NULL COMMENT '工作日，代表具体哪一天的排班',
  `shift_version` bigint DEFAULT NULL COMMENT '班次版本',
  `shift_id` bigint DEFAULT NULL COMMENT '班次ID，该值为0，表明当天休息',
  `is_rest` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否休息：Y：当天排休, N：当天不休息',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` bigint DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ding_talk_schedule_ding_talk_schedule_id_IDX` (`ding_talk_schedule_id`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='钉钉排班表';

-- changeset lanchun:ddl_2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'ding_talk_user_ref') AS column_exists;
CREATE TABLE `ding_talk_user_ref` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `ding_talk_user_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '钉钉用户userId',
  `system_user_id` bigint DEFAULT NULL COMMENT '系统用户id',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` bigint DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ding_talk_user_ref_ding_talk_user_id_IDX` (`ding_talk_user_id`,`system_user_id`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='钉钉用户关联表';
