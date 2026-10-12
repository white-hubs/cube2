-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_paper_question' AND column_name = 'name' ) AS column_exists;
ALTER TABLE `exam_paper_question` MODIFY COLUMN `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '题目名称' AFTER `exam_paper_question_category_id`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_question_bank' AND column_name = 'name' ) AS column_exists;
ALTER TABLE `exam_question_bank` MODIFY COLUMN `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '题目名称' AFTER `id`;

