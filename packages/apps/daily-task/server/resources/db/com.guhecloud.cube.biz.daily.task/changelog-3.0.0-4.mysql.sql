-- liquibase formatted sql

-- changeset daily-task:add-record-exception
CREATE TABLE IF NOT EXISTS `daily_record_exception` (
  `id` bigint NOT NULL COMMENT '主键',
  `record_id` bigint NOT NULL COMMENT '首次异常记录ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `target_id` bigint DEFAULT NULL COMMENT '对象ID，普通/标准模式为空',
  `target_name` varchar(255) DEFAULT NULL COMMENT '对象名称',
  `status` varchar(50) NOT NULL COMMENT '异常状态(WAIT_REPAIR/REPAIRED)',
  `repair_user_id` bigint NOT NULL COMMENT '待修复人ID',
  `repair_user_name` varchar(100) DEFAULT NULL COMMENT '待修复人名称',
  `repaired_time` datetime DEFAULT NULL COMMENT '修复完成时间',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户ID',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户ID',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '删除标记',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_target` (`record_id`,`target_id`),
  KEY `idx_task_status` (`task_id`,`status`),
  KEY `idx_repair_user_status` (`repair_user_id`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日常任务异常记录';

-- changeset daily-task:add-record-repair
CREATE TABLE IF NOT EXISTS `daily_record_repair` (
  `id` bigint NOT NULL COMMENT '主键',
  `exception_id` bigint NOT NULL COMMENT '异常记录ID',
  `execution_result` varchar(50) NOT NULL COMMENT '处理结果(NORMAL/ABNORMAL)',
  `handler_id` bigint NOT NULL COMMENT '处理人ID',
  `handler_name` varchar(100) DEFAULT NULL COMMENT '处理人名称',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `file_ref_key` bigint DEFAULT NULL COMMENT '附件关联key',
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户ID',
  `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户ID',
  `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '删除标记',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  KEY `idx_exception_id` (`exception_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日常任务异常修复处理记录';
