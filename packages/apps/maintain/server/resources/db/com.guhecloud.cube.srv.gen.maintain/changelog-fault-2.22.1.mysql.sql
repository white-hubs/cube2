-- liquibase formatted sql

-- changeset codex:fault-order-area-id-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'fault_order' AND column_name = 'area_id') AS column_exists;
ALTER TABLE `fault_order` ADD COLUMN `area_id` bigint(20) NULL DEFAULT NULL COMMENT '网格id' AFTER `obj_type`;
