-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_question_rule') AS column_exists;
CREATE TABLE `exam_question_rule` (
  `id` bigint(20) NOT NULL,
  `rule_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '规则名称',
  `total_score` int(11) DEFAULT NULL COMMENT '试卷总分',
  `pass_score` int(11) DEFAULT NULL COMMENT '及格分数',
  `question_num` int(11) DEFAULT NULL COMMENT '题目数量',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '试卷说明',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='试卷考题规则' ROW_FORMAT=DYNAMIC;

-- changeset zengyadong:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_question_rule_Item') AS column_exists;
CREATE TABLE `exam_question_rule_Item` (
  `id` bigint(20) NOT NULL,
  `rule_id` bigint(20) DEFAULT NULL COMMENT '关联主表ID',
  `exam_question_type` varchar(30) DEFAULT NULL COMMENT '题目类型',
  `theme_keywords` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '题目主题',
  `extract_count` int(11) DEFAULT NULL COMMENT '抽取数量',
  `per_score` int(11) DEFAULT NULL COMMENT '每题分值',
  `tenant` int(11) DEFAULT NULL,
  `deleted` bigint(20) DEFAULT NULL,
  `create_by` varchar(100) NOT NULL COMMENT '创建用户',
  `creator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NOT NULL COMMENT '更新用户',
  `updater_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_exam_paper_question_rule_detail_rule_id` (`rule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='试卷考题规则明细' ROW_FORMAT=DYNAMIC;

-- changeset zengyadong:alert-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_paper' AND column_name = 'paper_source') AS column_exists;
ALTER TABLE `exam_paper`
  ADD COLUMN `paper_source` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'MANUAL' COMMENT '试卷来源：手动组卷/考试任务随机' AFTER `exam_paper_question_number`;

-- changeset zengyadong:alert-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_task' AND column_name = 'paper_source') AS column_exists;
ALTER TABLE `exam_task`
  ADD COLUMN `exam_paper_mode` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'UNIFIED' COMMENT '试卷模式：统一题目/随机出题' AFTER `exam_task_type`;

-- changeset zengyadong:alert-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_task_paper' AND column_name = 'rule_id') AS column_exists;
ALTER TABLE `exam_task_paper`
  ADD COLUMN `rule_id` bigint(20) NULL COMMENT '随机组卷规则主键' AFTER `exam_paper_id`;
