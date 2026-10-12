-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-safety-education`.`cultivate_task` ADD COLUMN `source_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务来源编号' AFTER `theme_name`;

ALTER TABLE `cube-srv-gen-safety-education`.`cultivate_task` ADD COLUMN `source_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务来源名称' AFTER `source_code`;

ALTER TABLE `cube-srv-gen-safety-education`.`cultivate_task` ADD COLUMN `source_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务来源键' AFTER `source_name`;

ALTER TABLE `cube-srv-gen-safety-education`.`cultivate_task` ADD COLUMN `market_tenant` int(10) NULL DEFAULT NULL COMMENT '市场租户编号(当使用方为集团时可用)' AFTER `cultivate_task_status`;

ALTER TABLE `cube-srv-gen-safety-education`.`cultivate_task_user` ADD COLUMN `user_identify` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户唯一身份标识' AFTER `cultivate_task_id`;

ALTER TABLE `cube-srv-gen-safety-education`.`exam_task` ADD COLUMN `source_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务来源编号' AFTER `theme_name`;

ALTER TABLE `cube-srv-gen-safety-education`.`exam_task` ADD COLUMN `source_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务来源名称' AFTER `source_code`;

ALTER TABLE `cube-srv-gen-safety-education`.`exam_task` ADD COLUMN `source_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务来源键' AFTER `source_name`;

ALTER TABLE `cube-srv-gen-safety-education`.`exam_task` ADD COLUMN `market_tenant` int(10) NULL DEFAULT NULL COMMENT '市场租户编号(当使用方为集团时可用)' AFTER `remark`;

ALTER TABLE `cube-srv-gen-safety-education`.`exam_task_user` ADD COLUMN `user_identify` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户唯一身份标识' AFTER `exam_task_id`;

ALTER TABLE `cube-srv-gen-safety-education`.`exam_task_user_record` ADD COLUMN `user_identify` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户唯一标识' AFTER `exam_paper_content`;

ALTER TABLE `cube-srv-gen-safety-education`.`exam_task_user_record` ADD COLUMN `exam_submitted_time` datetime NULL DEFAULT NULL COMMENT '考试提交时间' AFTER `exam_time`;

CREATE TABLE `cube-srv-gen-safety-education`.`headquarters_cultivate_task`  (
  `id` bigint(20) NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务编号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `theme_id` bigint(20) NULL DEFAULT NULL COMMENT '培训主题',
  `theme_name` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训主题名称',
  `cultivate_start_time` datetime NULL DEFAULT NULL COMMENT '培训开始时间',
  `cultivate_end_time` datetime NULL DEFAULT NULL COMMENT '培训结束时间',
  `exam_task_id` bigint(20) NULL DEFAULT NULL COMMENT '考试任务主键',
  `cultivate_user_number` int(11) NULL DEFAULT NULL COMMENT '培训人数',
  `cultivate_mode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训形式(线上、线下)',
  `cultivate_target` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训对象(商户、员工)',
  `cultivate_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训地点',
  `speaker` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主讲人',
  `charge_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人',
  `charge_user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人名称',
  `cultivate_content` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训内容',
  `cultivate_task_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训任务状态',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '总部培训任务' ROW_FORMAT = DYNAMIC;

CREATE TABLE `cube-srv-gen-safety-education`.`headquarters_cultivate_task_courseware_library`  (
  `id` bigint(20) NOT NULL,
  `headquarters_cultivate_task_id` bigint(20) NULL DEFAULT NULL COMMENT '总部培训任务主键',
  `file_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_size` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  `file_ids` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件主键集合',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '总部培训任务关联课件' ROW_FORMAT = DYNAMIC;

CREATE TABLE `cube-srv-gen-safety-education`.`headquarters_cultivate_task_market`  (
  `id` bigint(20) NOT NULL,
  `headquarters_cultivate_task_id` bigint(20) NULL DEFAULT NULL COMMENT '总部培训任务主键',
  `market_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市场编号',
  `market_tenant_code` int(10) NULL DEFAULT NULL COMMENT '市场租户编号',
  `market_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市场名称',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '总部培训任务关联的市场' ROW_FORMAT = DYNAMIC;

CREATE TABLE `cube-srv-gen-safety-education`.`headquarters_exam_task`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考试任务名称',
  `theme_id` bigint(20) NULL DEFAULT NULL COMMENT '主题主键',
  `theme_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题名称',
  `exam_start_time` datetime NULL DEFAULT NULL COMMENT '考试开始时间',
  `exam_end_time` datetime NULL DEFAULT NULL COMMENT '考试结束时间',
  `exam_paper_id` bigint(20) NULL DEFAULT NULL COMMENT '试卷主键',
  `exam_paper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试卷名称',
  `exam_paper_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '试卷内容(整个大json信息)',
  `charge_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人主键',
  `charge_user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人名称',
  `timed` tinyint(1) NULL DEFAULT NULL COMMENT '是否限时',
  `exam_duration` int(11) NULL DEFAULT NULL COMMENT '考试时长',
  `exam_duration_unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考试时长单位',
  `retake` tinyint(1) NULL DEFAULT NULL COMMENT '是否支持复考',
  `retake_number` int(11) NULL DEFAULT NULL COMMENT '复考次数',
  `exam_task_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考试类型',
  `exam_user_number` int(11) NULL DEFAULT NULL COMMENT '参加考试人数',
  `exam_task_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务状态',
  `published_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考试说明',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '总部考试任务' ROW_FORMAT = DYNAMIC;

CREATE TABLE `cube-srv-gen-safety-education`.`headquarters_exam_task_market`  (
  `id` bigint(20) NOT NULL,
  `headquarters_exam_task_id` bigint(20) NULL DEFAULT NULL COMMENT '总部考试任务主键',
  `market_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市场编码',
  `market_tenant_code` int(10) NULL DEFAULT NULL COMMENT '市场租户编号',
  `market_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市场名称',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试任务人员' ROW_FORMAT = DYNAMIC;

CREATE TABLE `cube-srv-gen-safety-education`.`headquarters_exam_task_paper`  (
  `id` bigint(20) NOT NULL,
  `headquarters_exam_task_id` bigint(20) NULL DEFAULT NULL COMMENT '考试任务主键',
  `exam_paper_id` bigint(20) NULL DEFAULT NULL COMMENT '试卷主键',
  `exam_paper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试卷名称',
  `exam_paper_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '存储提交的试卷内容json',
  `exam_paper_score` decimal(5, 2) NULL DEFAULT NULL COMMENT '试卷总分',
  `exam_paper_passing_grade_score` decimal(5, 2) NULL DEFAULT NULL COMMENT '试卷及格分数',
  `exam_paper_question_number` int(11) NULL DEFAULT NULL COMMENT '题目数量',
  `exam_paper_remark` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '试卷描述',
  `tenant` int(11) NULL DEFAULT NULL,
  `deleted` bigint(20) NULL DEFAULT NULL,
  `create_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '考试任务试卷' ROW_FORMAT = DYNAMIC;