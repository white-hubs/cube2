-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_task_paper' AND column_name = 'user_identify') AS column_exists;
ALTER TABLE `exam_task_paper`
  ADD COLUMN `user_identify` bigint(20) NULL COMMENT '随机组卷规则主键' AFTER `exam_paper_id`;
-- changeset zengyadong:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.STATISTICS WHERE table_schema = DATABASE() AND table_name = 'exam_task_paper' AND index_name = 'idx_exam_task_paper_task_user';
ALTER TABLE `exam_task_paper`
  ADD INDEX `idx_exam_task_paper_task_user` (`exam_task_id`, `user_identify`) USING BTREE;
