-- liquibase formatted sql

-- changeset lj:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'sys_site_page' AND column_name = 'status') AS column_exists;
ALTER TABLE sys_site_page ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用';