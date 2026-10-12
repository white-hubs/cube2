-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_plan' AND column_name='method') AS column_exists;
ALTER TABLE `daily_plan` ADD COLUMN `method` varchar(20) DEFAULT NULL COMMENT '处理方式';

-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_plan' AND column_name='is_on_duty') AS column_exists;
ALTER TABLE `daily_plan` ADD COLUMN `is_on_duty` int DEFAULT NULL COMMENT '是否发放给在岗人员';


-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_plan' AND column_name='require_progress') AS column_exists;
ALTER TABLE `daily_plan` ADD COLUMN `require_progress` decimal(3,2) DEFAULT NULL COMMENT '要求进度';

-- changeset zengyadong:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_plan' AND column_name='task_type') AS column_exists;
ALTER TABLE `daily_plan` ADD COLUMN `task_type` varchar(20) DEFAULT NULL COMMENT '任务类型';

-- changeset zengyadong:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_plan_target_relation' AND column_name='rule_id') AS column_exists;
ALTER TABLE `daily_plan_target_relation` ADD COLUMN `rule_id` bigint DEFAULT NULL COMMENT '规则id';


-- changeset zengyadong:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task' AND column_name='method') AS column_exists;
ALTER TABLE `daily_task` ADD COLUMN `method` varchar(20) DEFAULT NULL COMMENT '处理方式';

-- changeset zengyadong:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task' AND column_name='is_on_duty') AS column_exists;
ALTER TABLE `daily_task` ADD COLUMN `is_on_duty` int DEFAULT NULL COMMENT '是否发放给在岗人员';


-- changeset zengyadong:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task' AND column_name='require_progress') AS column_exists;
ALTER TABLE `daily_task` ADD COLUMN `require_progress` decimal(10,2) DEFAULT NULL COMMENT '要求进度';

-- changeset zengyadong:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task' AND column_name='task_type') AS column_exists;
ALTER TABLE `daily_task` ADD COLUMN `task_type` varchar(20) DEFAULT NULL COMMENT '任务类型';

-- changeset zengyadong:alter-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task_target' AND column_name='snapshot') AS column_exists;
ALTER TABLE `daily_task_target` ADD COLUMN `snapshot` int DEFAULT NULL COMMENT '快照标识';

-- changeset zengyadong:alter-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task_target' AND column_name='rule_id') AS column_exists;
ALTER TABLE `daily_task_target` ADD COLUMN `rule_id` bigint DEFAULT NULL COMMENT '规则id';

-- changeset zengyadong:alter-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_plan_target_relation' AND column_name='tag_ids') AS column_exists;
ALTER TABLE `daily_plan_target_relation` ADD COLUMN `tag_ids` varchar(255) DEFAULT NULL COMMENT '标签id';


-- changeset zengyadong:alter-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task_target' AND column_name='tag_ids') AS column_exists;
ALTER TABLE `daily_task_target` ADD COLUMN `tag_ids` varchar(255) DEFAULT NULL COMMENT '标签id';

-- changeset zengyadong:alter-14
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_record' AND column_name='remark') AS column_exists;
ALTER TABLE `daily_record` ADD COLUMN `remark` varchar(255) DEFAULT NULL COMMENT '内容';

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task_rule') AS column_exists;
CREATE TABLE `daily_task_rule` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `rule_type` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '规则类型',
  `ref_id` bigint DEFAULT NULL COMMENT '关联id',
  `abnormal_users` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '异常时用户',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务规则表';

-- changeset zengyadong:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_task_target_ref') AS column_exists;
CREATE TABLE `daily_task_target_ref` (
  `id` bigint NOT NULL,
  `task_id` bigint DEFAULT NULL COMMENT '任务ID',
  `target_ref_id` bigint DEFAULT NULL COMMENT '任务对象关联id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务对象关联关系表';

-- changeset zengyadong:alter-15
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_item' AND column_name='temp') AS column_exists;
ALTER TABLE `daily_item` ADD COLUMN `temp` int DEFAULT NULL COMMENT '临时添加表示';