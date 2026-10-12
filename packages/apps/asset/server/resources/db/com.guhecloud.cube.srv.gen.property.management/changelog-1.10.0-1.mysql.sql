-- liquibase formatted sql

-- changeset lijie:update-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger') AS column_exists;
UPDATE `risk_source_ledger` SET number=1

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger' AND column_name='maintenance_user_id') AS column_exists;
ALTER TABLE `risk_source_ledger` ADD COLUMN `maintenance_user_id` bigint(20) NULL DEFAULT NULL COMMENT '维护人';

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger' AND column_name='maintenance_user_name') AS column_exists;
ALTER TABLE `risk_source_ledger` ADD COLUMN `maintenance_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维护人姓名';

-- changeset lijie:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger' AND column_name='address') AS column_exists;
ALTER TABLE `risk_source_ledger` ADD COLUMN `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细位置';

-- changeset lijie:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger' AND column_name='norms') AS column_exists;
ALTER TABLE `risk_source_ledger` ADD COLUMN `norms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格型号';

-- changeset lijie:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger' AND column_name='file') AS column_exists;
ALTER TABLE `risk_source_ledger` MODIFY COLUMN `file` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产附件';
