-- liquibase formatted sql

-- changeset lanchun:add-index-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'linkage_event_record' AND index_name = 'idx_event_record_app_tenant_deleted_time') AS index_exists;
ALTER TABLE `linkage_event_record` ADD INDEX `idx_event_record_app_tenant_deleted_time` (`application_code`, `tenant`, `deleted`, `event_time`) USING BTREE;

-- changeset lanchun:add-index-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'linkage_event_record' AND index_name = 'idx_event_record_level_tenant_deleted_time') AS index_exists;
ALTER TABLE `linkage_event_record` ADD INDEX `idx_event_record_level_tenant_deleted_time` (`level_code`, `tenant`, `deleted`, `event_time`) USING BTREE;

-- changeset lanchun:add-index-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'linkage_event_record' AND index_name = 'idx_event_record_tenant_deleted_time') AS index_exists;
ALTER TABLE `linkage_event_record` ADD INDEX `idx_event_record_tenant_deleted_time` (`tenant`, `deleted`, `event_time`) USING BTREE;

-- changeset lanchun:add-index-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'event_ai_result' AND index_name = 'idx_event_ai_result_record_tenant_deleted_result') AS index_exists;
ALTER TABLE `event_ai_result` ADD INDEX `idx_event_ai_result_record_tenant_deleted_result` (`event_record_id`, `tenant`, `deleted`, `result`) USING BTREE;

-- changeset lanchun:drop-index-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'linkage_event_record' AND index_name = 'event_record_application_code_IDX') AS index_exists;
ALTER TABLE `linkage_event_record` DROP INDEX `event_record_application_code_IDX`;

-- changeset lanchun:drop-index-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'linkage_event_record' AND index_name = 'event_record_level_code_IDX') AS index_exists;
ALTER TABLE `linkage_event_record` DROP INDEX `event_record_level_code_IDX`;
