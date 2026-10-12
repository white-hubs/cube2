-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'alarm_ai_result' AND INDEX_NAME = 'idx_alarm_id_tenant_deleted' ) AS index_exists;
CREATE INDEX idx_alarm_id_tenant_deleted USING BTREE ON alarm_ai_result (alarm_record_id,tenant,deleted);


