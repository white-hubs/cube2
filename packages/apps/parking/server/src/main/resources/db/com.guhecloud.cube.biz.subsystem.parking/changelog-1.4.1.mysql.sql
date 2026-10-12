-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'parking_info_device' AND index_name = 'idx_parking_id_device_tenant_deleted') AS index_exists;
CREATE UNIQUE INDEX idx_parking_id_device_tenant_deleted USING BTREE ON parking_info_device (parking_info_id,device_id,tenant,deleted);

