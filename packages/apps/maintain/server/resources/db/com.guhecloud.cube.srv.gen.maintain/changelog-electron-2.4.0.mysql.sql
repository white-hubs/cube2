-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_plan' AND column_name='exception_user_ids') AS column_exists;
ALTER TABLE patrol_plan ADD exception_user_ids varchar(255) NULL COMMENT '异常时发放人员ids';

-- changeset chx:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_plan' AND column_name='condition_type') AS column_exists;
ALTER TABLE patrol_plan ADD condition_type varchar(100) NULL COMMENT '条件类型(USER人员, CONDITION条件)';


-- changeset chx:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_task_location' AND column_name='superintendent_user_id') AS column_exists;
ALTER TABLE patrol_task_location ADD superintendent_user_id BIGINT(20) NULL COMMENT '巡更点负责人';

-- changeset chx:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'patrol_plan_superintendent') AS column_exists;
CREATE TABLE IF NOT EXISTS `patrol_plan_superintendent` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='计划负责人表';
