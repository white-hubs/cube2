-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger' AND column_name='mandatory_testing_date') AS column_exists;
ALTER TABLE `risk_source_ledger` ADD COLUMN `mandatory_testing_date` date NULL DEFAULT NULL COMMENT '强检日期';