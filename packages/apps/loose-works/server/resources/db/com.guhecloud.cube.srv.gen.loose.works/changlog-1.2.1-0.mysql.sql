-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_feedback' AND column_name='description') AS column_exists;
ALTER TABLE `project_feedback` MODIFY COLUMN `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL ;

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_apply' AND column_name='process_ins_id') AS column_exists;
ALTER TABLE `project_apply` MODIFY COLUMN `process_ins_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id';

-- changeset lijie:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule' AND column_name='apply_id') AS column_exists;
ALTER TABLE `project_schedule` MODIFY COLUMN `apply_id` bigint(20) NULL DEFAULT NULL COMMENT '请示id';

-- changeset lijie:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'project_schedule' AND column_name='contract_id') AS column_exists;
ALTER TABLE `project_schedule` MODIFY COLUMN `contract_id` bigint(20) NULL DEFAULT NULL COMMENT '签约id';
