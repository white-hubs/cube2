-- liquibase formatted sql

-- changeset lijie:alter-table-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger' AND column_name = 'bim_code' ) AS column_exists;
ALTER TABLE `risk_source_ledger` ADD COLUMN `bim_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型编码';


