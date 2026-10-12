-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'company' AND column_name='stall_id') AS column_exists;
ALTER TABLE `company` ADD COLUMN `stall_id` bigint(20) NULL DEFAULT NULL COMMENT '档位id';

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'company' AND column_name='stall_name') AS column_exists;
ALTER TABLE `company` ADD COLUMN `stall_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '档位名称';
