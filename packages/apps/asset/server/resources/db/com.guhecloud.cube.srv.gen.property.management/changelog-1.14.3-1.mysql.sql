-- liquibase formatted sql

-- changeset lijie:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'consumable' AND column_name='amount') AS column_exists;
ALTER TABLE `consumable` MODIFY COLUMN `amount` decimal(8, 2) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '总金额' ;