-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'fault_order' AND column_name='fault_describe') AS column_exists;
ALTER TABLE `fault_order` ADD COLUMN `fault_describe` varchar(500) NULL DEFAULT NULL COMMENT '故障描述';