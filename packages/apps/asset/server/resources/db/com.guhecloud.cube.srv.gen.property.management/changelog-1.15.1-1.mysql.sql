-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'consumable_store_stock' AND column_name='occupy_num') AS column_exists;
ALTER TABLE `consumable_store_stock` ADD COLUMN `occupy_num` int(11) NULL DEFAULT NULL COMMENT '审批占用库存';
