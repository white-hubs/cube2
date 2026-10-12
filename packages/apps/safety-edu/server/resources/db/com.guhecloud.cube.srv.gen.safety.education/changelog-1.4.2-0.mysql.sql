-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'exam_paper_question' AND column_name = 'name') AS column_exists;
ALTER TABLE exam_paper_question MODIFY COLUMN name varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '题目名称';
