-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger' AND index_name = 'idx_tenant_deleted') AS index_exists;
ALTER TABLE `risk_source_ledger` ADD INDEX `idx_tenant_deleted`(`tenant`,`deleted`) USING BTREE;


-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger_extend_info' AND index_name = 'idx_ledger_id') AS index_exists;
ALTER TABLE `risk_source_ledger_extend_info` ADD INDEX `idx_ledger_id`(`ledger_id`) USING BTREE;
