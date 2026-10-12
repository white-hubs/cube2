-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_task_user_record' AND column_name = 'exam_paper_content') AS column_exists;
ALTER TABLE `exam_task_user_record` MODIFY COLUMN exam_paper_content LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '试卷内容(整个大json信息)';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_task' AND column_name = 'exam_paper_content') AS column_exists;
ALTER TABLE `exam_task` MODIFY COLUMN exam_paper_content LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '试卷内容(整个大json信息)';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_task_paper' AND column_name = 'exam_paper_content') AS column_exists;
ALTER TABLE `exam_task_paper` MODIFY COLUMN exam_paper_content LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '存储提交的试卷内容json';
