-- liquibase formatted sql

-- changeset ht:1686103358461-1

-- cube_biz_daily_task.daily_cron definition

CREATE TABLE IF NOT EXISTS `daily_cron` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'cron表达式名称',
  `cron` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'cron表达式',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `daily_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='cron表达式表';


-- cube_biz_daily_task.daily_item definition

CREATE TABLE IF NOT EXISTS `daily_item` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行项名称',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行项内容',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签',
  `target_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '适配对象类型（多个类型用逗号分隔，如：AREA,DEVICE,PROPERTY）',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `daily_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='执行项表';


-- cube_biz_daily_task.daily_item_result_relation definition

CREATE TABLE IF NOT EXISTS `daily_item_result_relation` (
  `id` bigint NOT NULL COMMENT '主键',
  `item_id` bigint DEFAULT NULL COMMENT '执行项ID',
  `result_id` bigint DEFAULT NULL COMMENT '执行结果ID',
  `default_result_item` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '默认结果项',
  `must_take_photo_result_items` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '必须拍照的结果项',
  `must_remark_result_items` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '必须备注的结果项',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_item_id` (`item_id`) USING BTREE,
  KEY `idx_result_id` (`result_id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='执行项的结果关联表';


-- cube_biz_daily_task.daily_job_record definition

CREATE TABLE IF NOT EXISTS `daily_job_record` (
  `id` bigint NOT NULL COMMENT '主键',
  `plan_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '本次执行的计划主键（多个ID逗号分隔）',
  `execution_stat` tinyint(1) DEFAULT NULL COMMENT '执行状态（1-成功，0-失败）',
  `error_message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常信息',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除标记或时间戳）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_plan_ids` (`plan_ids`(191)) USING BTREE COMMENT '计划ID前缀索引（因varchar过长）',
  KEY `idx_execution_stat` (`execution_stat`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='计划执行记录';


-- cube_biz_daily_task.daily_plan definition

CREATE TABLE IF NOT EXISTS `daily_plan` (
  `id` bigint NOT NULL COMMENT '主键',
  `daily_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '计划状态 DailyPlanStatusEnum',
  `enable` tinyint(1) DEFAULT NULL COMMENT '启停(true/false)',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `execute_way` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行方式 DailyExecuteWayEnum，多种以逗号隔开，空表示可直接点击执行',
  `cron_id` bigint DEFAULT NULL COMMENT '表达式id',
  `specified_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '指定类型 JobLoopTypeEnum',
  `specified_days` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '指定的日期（根据指定类型，可能是月/周/号等）',
  `specified_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '指定的时间（仅 HH:mm:ss）',
  `last_time` datetime DEFAULT NULL COMMENT '上次执行时间',
  `next_time` datetime DEFAULT NULL COMMENT '下次执行时间',
  `execution_count` int DEFAULT '0' COMMENT '执行次数',
  `task_category_id` bigint DEFAULT NULL COMMENT '任务类型id',
  `urgency_id` bigint DEFAULT NULL COMMENT '紧急程度id',
  `task_day` int DEFAULT '0' COMMENT '任务有效期（天）',
  `task_hour` int DEFAULT '0' COMMENT '任务有效期（小时）',
  `task_minute` int DEFAULT '0' COMMENT '任务有效期（分钟）',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `file_ref_key` bigint DEFAULT NULL COMMENT '文件关联key（用于快速判断是否存在文件）',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `daily_plan_daily_type_IDX` (`daily_type`) USING BTREE,
  KEY `daily_plan_status_IDX` (`status`) USING BTREE,
  KEY `daily_plan_enable_IDX` (`enable`) USING BTREE,
  KEY `daily_plan_start_time_IDX` (`start_time`,`end_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常任务计划表';


-- cube_biz_daily_task.daily_plan_expand definition

CREATE TABLE IF NOT EXISTS `daily_plan_expand` (
  `id` bigint NOT NULL COMMENT '主键(同计划id)',
  `metadata` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '附加信息',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常任务计划表';


-- cube_biz_daily_task.daily_plan_standard_relation definition

CREATE TABLE IF NOT EXISTS `daily_plan_standard_relation` (
  `id` bigint NOT NULL COMMENT '主键',
  `item_id` bigint DEFAULT NULL COMMENT '计划ID',
  `standard_id` bigint DEFAULT NULL COMMENT '标准ID',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除标记或时间戳）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `plan_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_plan_id` (`item_id`) USING BTREE,
  KEY `idx_standard_id` (`standard_id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常计划与标准关联表';


-- cube_biz_daily_task.daily_plan_superintendent definition

CREATE TABLE IF NOT EXISTS `daily_plan_superintendent` (
  `id` bigint NOT NULL COMMENT '主键',
  `master_id` bigint DEFAULT NULL COMMENT '计划ID',
  `superintendent_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人类型 DailySuperintendentTypeEnum',
  `target_id` bigint DEFAULT NULL COMMENT '负责对象的ID（如类型为USER时是人员ID，为POST时是岗位ID）',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户ID',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户ID',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除标记或时间戳）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_plan_id` (`master_id`) USING BTREE,
  KEY `idx_target_id` (`target_id`) USING BTREE,
  KEY `idx_superintendent_type` (`superintendent_type`) USING BTREE,
  KEY `idx_deleted` (`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常计划负责人表';


-- cube_biz_daily_task.daily_plan_target_relation definition

CREATE TABLE IF NOT EXISTS `daily_plan_target_relation` (
  `id` bigint NOT NULL COMMENT '主键',
  `plan_id` bigint DEFAULT NULL COMMENT '计划ID',
  `target_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象类别（设备/档位/现场点/资产/风险源/小散工程）',
  `target_id` bigint DEFAULT NULL COMMENT '目标对象ID',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户ID',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户ID',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除标记或时间戳）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_plan_id` (`plan_id`) USING BTREE,
  KEY `idx_target_id` (`target_id`) USING BTREE,
  KEY `idx_target_category` (`target_category`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常计划目标对象关联表';


-- cube_biz_daily_task.daily_plan_target_standard_relation definition

CREATE TABLE IF NOT EXISTS `daily_plan_target_standard_relation` (
  `id` bigint NOT NULL COMMENT '主键',
  `plan_id` bigint DEFAULT NULL COMMENT '计划ID',
  `target_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象类别（设备/档位/现场点/资产/风险源/小散工程）',
  `target_id` bigint DEFAULT NULL COMMENT '对象ID',
  `standard_id` bigint DEFAULT NULL COMMENT '对象执行标准ID',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户ID',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户ID',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除标记或时间戳）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_plan_id` (`plan_id`) USING BTREE,
  KEY `idx_target_id_category` (`target_id`,`target_category`) USING BTREE,
  KEY `idx_standard_id` (`standard_id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常计划-目标对象-执行标准关联表';


-- cube_biz_daily_task.daily_record definition

CREATE TABLE IF NOT EXISTS `daily_record` (
  `id` bigint NOT NULL COMMENT '主键',
  `daily_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  `task_id` bigint DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务名称',
  `execution_time` datetime DEFAULT NULL COMMENT '执行时间',
  `executor_id` bigint DEFAULT NULL COMMENT '执行人员id',
  `executor_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行人员姓名',
  `execution_result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行结果(正常/异常)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_daily_type` (`daily_type`) USING BTREE,
  KEY `idx_task_id` (`task_id`) USING BTREE,
  KEY `idx_executor_id` (`executor_id`) USING BTREE,
  KEY `idx_execution_time` (`execution_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常任务执行记录表';


-- cube_biz_daily_task.daily_record_item definition

CREATE TABLE IF NOT EXISTS `daily_record_item` (
  `id` bigint NOT NULL COMMENT '主键',
  `record_id` bigint DEFAULT NULL COMMENT '关联记录ID',
  `daily_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  `task_id` bigint DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务名称',
  `standard_id` bigint DEFAULT NULL COMMENT '标准ID',
  `standard_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标准名称',
  `item_id` bigint DEFAULT NULL COMMENT '项目ID',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目名称',
  `item_tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目标签',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `result_id` bigint DEFAULT NULL COMMENT '结果ID',
  `result_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '结果名称',
  `result_item_id` bigint DEFAULT NULL COMMENT '执行结果项id',
  `result_item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行结果项名称',
  `error_flg` tinyint(1) DEFAULT NULL COMMENT '是否异常（0-否，1-是）',
  `file_ref_key` bigint DEFAULT NULL COMMENT '文件关联key（用于快速判断是否存在文件）',
  `default_result_item` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '默认结果项',
  `must_take_photo_result_items` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '必须拍照的结果项（如JSON或逗号分隔）',
  `must_remark_result_items` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '必须备注的结果项（如JSON或逗号分隔）',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_record_id` (`record_id`) USING BTREE,
  KEY `idx_task_id` (`task_id`) USING BTREE,
  KEY `idx_item_id` (`item_id`) USING BTREE,
  KEY `idx_daily_type` (`daily_type`) USING BTREE,
  KEY `idx_error_flg` (`error_flg`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常任务执行内容明细表';


-- cube_biz_daily_task.daily_record_target definition

CREATE TABLE IF NOT EXISTS `daily_record_target` (
  `id` bigint NOT NULL COMMENT '主键',
  `task_id` bigint DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务名称',
  `record_id` bigint DEFAULT NULL COMMENT '关联记录ID（指向 daily_record.id）',
  `target_category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象类别（设备/档位/现场点/资产/风险源/小散工程等）',
  `target_id` bigint DEFAULT NULL COMMENT '对象ID',
  `target_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象名称',
  `target_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象编码',
  `lnt_and_lat` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '经纬度（格式：lng,lat）',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '地理位置描述',
  `execution_result` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行结果',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_record_id` (`record_id`) USING BTREE,
  KEY `idx_task_id` (`task_id`) USING BTREE,
  KEY `idx_target_id` (`target_id`) USING BTREE,
  KEY `idx_target_category` (`target_category`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常任务记录关联的对象表';


-- cube_biz_daily_task.daily_record_target_item definition

CREATE TABLE IF NOT EXISTS `daily_record_target_item` (
  `id` bigint NOT NULL COMMENT '主键',
  `task_id` bigint DEFAULT NULL COMMENT '任务ID',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务名称',
  `record_id` bigint DEFAULT NULL COMMENT '记录ID',
  `target_id` bigint DEFAULT NULL COMMENT '对象ID（设备/档位/现场点/资产/风险源/小散工程等）',
  `target_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象编码',
  `target_category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象类别',
  `target_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象名称',
  `standard_id` bigint DEFAULT NULL COMMENT '标准ID',
  `standard_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标准名称',
  `item_id` bigint DEFAULT NULL COMMENT '检查项ID',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '检查项名称',
  `item_tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '检查项标签',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `result_id` bigint DEFAULT NULL COMMENT '结果ID',
  `result_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '结果名称',
  `result_item_id` bigint DEFAULT NULL COMMENT '执行结果项id',
  `result_item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行结果项名称',
  `error_flag` tinyint(1) DEFAULT NULL COMMENT '是否异常（0-否，1-是）',
  `file_ref_key` bigint DEFAULT NULL COMMENT '文件关联key（用于快速判断是否存在文件）',
  `default_result_item` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '默认结果项',
  `must_take_photo_result_items` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '必须拍照的结果项（如JSON或逗号分隔）',
  `must_remark_result_items` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '必须备注的结果项（如JSON或逗号分隔）',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_record_id` (`record_id`) USING BTREE,
  KEY `idx_task_id` (`task_id`) USING BTREE,
  KEY `idx_target_id` (`target_id`) USING BTREE,
  KEY `idx_item_id` (`item_id`) USING BTREE,
  KEY `idx_error_flg` (`error_flag`) USING BTREE,
  KEY `idx_target_category` (`target_category`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='日常任务记录对象执行内容明细表';


-- cube_biz_daily_task.daily_record_work_order definition

CREATE TABLE IF NOT EXISTS `daily_record_work_order` (
  `id` bigint NOT NULL COMMENT '主键',
  `task_id` bigint DEFAULT NULL COMMENT '任务ID',
  `record_id` bigint DEFAULT NULL COMMENT '记录ID',
  `target_id` bigint DEFAULT NULL COMMENT '目标ID',
  `target_category` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目标类别',
  `work_order_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工单类型(DailyWorkOrderTypeEnum)',
  `process_instance_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程实例ID',
  `process_instance_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工单名称',
  `process_instance_status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工单状态',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户ID',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户ID',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE,
  KEY `idx_task_id` (`task_id`) USING BTREE,
  KEY `idx_record_id` (`record_id`) USING BTREE,
  KEY `idx_target_id_category` (`target_id`,`target_category`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='记录工单关联关系';


-- cube_biz_daily_task.daily_result definition

CREATE TABLE IF NOT EXISTS `daily_result` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '结果项名称',
  `enable` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  `result_label_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '结果项 List<DailyResultLabelDTO>',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `daily_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='执行结果项表';


-- cube_biz_daily_task.daily_standard definition

CREATE TABLE IF NOT EXISTS `daily_standard` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标准名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `category_ids` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务种类ID',
  `enable` tinyint(1) DEFAULT NULL COMMENT '是否启用',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `daily_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_category_id` (`category_ids`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='执行标准表';


-- cube_biz_daily_task.daily_standard_item_result_relation definition

CREATE TABLE IF NOT EXISTS `daily_standard_item_result_relation` (
  `id` bigint NOT NULL COMMENT '主键',
  `standard_id` bigint DEFAULT NULL COMMENT '执行标准ID',
  `item_id` bigint DEFAULT NULL COMMENT '标准项ID',
  `result_id` bigint DEFAULT NULL COMMENT '执行结果ID',
  `default_result_item` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '默认结果项',
  `must_take_photo_result_items` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '必须拍照的结果项',
  `must_remark_result_items` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '必须备注的结果项',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_standard_id` (`standard_id`) USING BTREE,
  KEY `idx_item_id` (`item_id`) USING BTREE,
  KEY `idx_result_id` (`result_id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='执行标准的项关联关系表';


-- cube_biz_daily_task.daily_standard_target_type_relation definition

CREATE TABLE IF NOT EXISTS `daily_standard_target_type_relation` (
  `id` bigint NOT NULL COMMENT '主键',
  `standard_id` bigint DEFAULT NULL COMMENT '执行标准ID',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象类型 TaskTargetTypeEnum',
  `target_type_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象类型ID',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_standard_id` (`standard_id`) USING BTREE,
  KEY `idx_type_target_type_id` (`type`,`target_type_id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='执行标准关联对象类型表';


-- cube_biz_daily_task.daily_task definition

CREATE TABLE IF NOT EXISTS `daily_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `daily_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  `task_category_id` bigint DEFAULT NULL COMMENT '任务类型id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `plan_id` bigint DEFAULT NULL COMMENT '计划id',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务状态',
  `urgency_id` bigint DEFAULT NULL COMMENT '紧急程度id',
  `target_count` int DEFAULT '0' COMMENT '任务对象数',
  `deadline` datetime DEFAULT NULL COMMENT '任务截至日期(任务的期限)',
  `start_time` datetime DEFAULT NULL COMMENT '执行开始时间(人员开始执行任务的时间)',
  `end_time` datetime DEFAULT NULL COMMENT '执行结束时间(任务完成的时间)',
  `person_in_charge` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务负责人(任务接收时，或在任务完成时确定到具体人员)',
  `file_ref_key` bigint DEFAULT NULL COMMENT '文件关联key(不直接用id关联是因为用单独的key可以直接知道是否有文件,没有就不用去查询了)',
  `task_day` int DEFAULT '0' COMMENT '任务有效期-天',
  `task_hour` int DEFAULT '0' COMMENT '任务有效期-小时',
  `task_minute` int DEFAULT '0' COMMENT '任务有效期-分钟',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '描述',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `parent_id` bigint DEFAULT NULL,
  `sequence` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `level` int DEFAULT NULL,
  `progress` decimal(10,2) unsigned zerofill DEFAULT '00000000.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1997920940101054466 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务表';


-- cube_biz_daily_task.daily_task_category definition

CREATE TABLE IF NOT EXISTS `daily_task_category` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务类型名称',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `daily_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='任务类型表';


-- cube_biz_daily_task.daily_task_item definition

CREATE TABLE IF NOT EXISTS `daily_task_item` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `item_id` bigint DEFAULT NULL COMMENT '项目ID',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目名称',
  `item_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标准名称',
  `result_json_string` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '执行结果对象(应该是个集合)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日常任务项目表';


-- cube_biz_daily_task.daily_task_target definition

CREATE TABLE IF NOT EXISTS `daily_task_target` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `target_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目标类别',
  `target_id` bigint DEFAULT NULL COMMENT '目标ID',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象状态',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `target_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象名称',
  `target_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对象编码',
  `area_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '区域名称',
  `target_lnt_and_lat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '经纬度',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '详细地址',
  `distributed` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日常任务目标表';


-- cube_biz_daily_task.daily_task_target_item definition

CREATE TABLE IF NOT EXISTS `daily_task_target_item` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `target_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '目标类别',
  `target_id` bigint DEFAULT NULL COMMENT '目标ID',
  `standard_id` bigint DEFAULT NULL COMMENT '标准ID',
  `item_id` bigint DEFAULT NULL COMMENT '项目ID',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目名称',
  `standard_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标准名称',
  `result_json_string` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '执行结果对象(应该是个集合)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `item_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日常任务目标项表';


-- cube_biz_daily_task.daily_task_user definition

CREATE TABLE IF NOT EXISTS `daily_task_user` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `master_id` bigint NOT NULL COMMENT '任务ID',
  `task_user_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '人员类型(负责人/协同人)',
  `superintendent_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人类型',
  `target_id` bigint DEFAULT NULL COMMENT '负责对象的id(例如superintendentType类型为USER时是人员id,为POST时是岗位id)',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日常任务人员表';


-- cube_biz_daily_task.daily_urgency definition

CREATE TABLE IF NOT EXISTS `daily_urgency` (
  `id` bigint NOT NULL COMMENT '主键',
  `level` int DEFAULT NULL COMMENT '紧急程度等级',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '紧急程度名称',
  `create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint unsigned DEFAULT '0' COMMENT '是否已删除（0-未删除，其他为删除时间戳或标记）',
  `tenant` int DEFAULT NULL COMMENT '租户ID',
  `daily_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日常任务类型',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='任务紧急程度表';













