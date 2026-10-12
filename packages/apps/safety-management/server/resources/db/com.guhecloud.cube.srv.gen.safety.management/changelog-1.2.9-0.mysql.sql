-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_exam_task_paper') AS column_exists;
CREATE TABLE `safety_exam_task_paper` (
  `id` bigint NOT NULL,
  `exam_task_id` bigint DEFAULT NULL COMMENT '考试任务主键',
  `exam_paper_id` bigint DEFAULT NULL COMMENT '试卷主键',
  `exam_paper_name` varchar(50) DEFAULT NULL COMMENT '试卷名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考试任务试卷';