-- liquibase formatted sql

-- changeset ht:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'consumable' AND column_name='amount') AS column_exists;
ALTER TABLE `consumable` MODIFY COLUMN `amount` decimal(20, 2) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '总金额' ;


-- changeset ht:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'consumable_store_stock' AND column_name='amount') AS column_exists;
ALTER TABLE `consumable_store_stock` MODIFY COLUMN `amount` decimal(20, 2) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '金额' ;

-- changeset ht:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'consumable_stock_detail' AND column_name='amount') AS column_exists;
ALTER TABLE `consumable_stock_detail` MODIFY COLUMN `amount` decimal(20, 2) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '金额' ;
