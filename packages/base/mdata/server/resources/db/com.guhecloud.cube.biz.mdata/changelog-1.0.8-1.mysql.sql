-- liquibase formatted sql

-- 删除挡位名称唯一约束
-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'stall' AND index_name = 'idx_stall_tenant_deleted_name') AS column_exists;
ALTER TABLE stall DROP KEY idx_stall_tenant_deleted_name;