-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND index_name = 'idx_tenant_filter';
ALTER TABLE inspect_task
ADD INDEX idx_tenant_filter (deleted, business_type, tenant);
