-- liquibase formatted sql

-- changeset ht:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'consumable_stock_detail' AND column_name='init_amount') AS column_exists;
ALTER TABLE `consumable_stock_detail` MODIFY COLUMN `init_amount` decimal(20, 2) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '初始金额' ;
