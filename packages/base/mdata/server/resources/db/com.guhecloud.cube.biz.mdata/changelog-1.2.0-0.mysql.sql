-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND column_name = 'upload_file_state' ) AS column_exists;
ALTER TABLE `stall` ADD COLUMN `upload_file_state` tinyint(1) NULL DEFAULT NULL COMMENT '是否上传附件' AFTER `visualization_code`;
