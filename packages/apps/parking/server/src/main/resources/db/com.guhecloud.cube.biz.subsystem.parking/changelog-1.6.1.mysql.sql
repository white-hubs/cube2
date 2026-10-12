-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'parking_access_record' AND index_name = 'idx_tenant_deleted_access_time' ) AS index_exists;
ALTER TABLE `parking_access_record` ADD INDEX `idx_tenant_deleted_access_time` (`tenant`, `deleted`, `access_time`);

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'parking_access_record' AND index_name = 'idx_tenant_deleted_type_time' ) AS index_exists;
ALTER TABLE `parking_access_record` ADD INDEX `idx_tenant_deleted_type_time` (`tenant`, `deleted`, `access_type`, `access_time`);

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'parking_access_record' AND index_name = 'idx_tenant_deleted_type_identify_time_id' ) AS index_exists;
ALTER TABLE `parking_access_record` ADD INDEX `idx_tenant_deleted_type_identify_time_id` (`tenant`, `deleted`, `access_type`, `identify_id`, `access_time`, `id`);

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'parking_pay_record' AND index_name = 'idx_tenant_deleted_pay_time_id' ) AS index_exists;
ALTER TABLE `parking_pay_record` ADD INDEX `idx_tenant_deleted_pay_time_id` (`tenant`, `deleted`, `pay_time`, `id`);
