-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger_extend_info' AND column_name='brand') AS column_exists;
ALTER TABLE `risk_source_ledger_extend_info` ADD COLUMN `brand` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌' AFTER `insurance`;

-- changeset lijie:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger_extend_info' AND column_name='electron_mobile_type') AS column_exists;
ALTER TABLE `risk_source_ledger_extend_info` ADD COLUMN `electron_mobile_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电动车分类' AFTER `brand`;

-- changeset lijie:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_ledger_extend_info' AND column_name='electron_mobile_colour') AS column_exists;
ALTER TABLE `risk_source_ledger_extend_info` ADD COLUMN `electron_mobile_colour` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电动车颜色' AFTER `electron_mobile_type`;