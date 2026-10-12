-- liquibase formatted sql
--
-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'file' AND column_name = 'config_id' ) AS column_exists;
ALTER TABLE file ADD COLUMN config_id BIGINT ( 11 ) NULL COMMENT '配置文件id'