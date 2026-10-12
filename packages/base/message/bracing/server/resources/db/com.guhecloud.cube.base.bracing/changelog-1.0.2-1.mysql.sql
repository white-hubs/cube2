-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'assembly_notice_way' AND column_name='notice_way') AS column_exists;
ALTER TABLE assembly_notice_way MODIFY COLUMN notice_way varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '通知方式';