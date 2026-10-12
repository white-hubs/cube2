-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND index_name = 'idx_tenant_status_split_endtime';
ALTER TABLE inspect_task ADD INDEX idx_tenant_status_split_endtime (tenant, deleted, status, is_split, valid_end_time);
