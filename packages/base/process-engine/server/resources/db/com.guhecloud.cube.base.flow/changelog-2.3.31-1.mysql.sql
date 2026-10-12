-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'process_instance_running' AND column_name='start_user_id') AS column_exists;
ALTER TABLE `process_instance_running` ADD COLUMN `start_user_id` bigint(20) NULL DEFAULT NULL COMMENT '发起人id';

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'process_instance_running' AND column_name='start_user_name') AS column_exists;
ALTER TABLE `process_instance_running` ADD COLUMN `start_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起人';

-- changeset lijie:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'process_instance_running' AND column_name='organization_id') AS column_exists;
ALTER TABLE `process_instance_running` ADD COLUMN `organization_id` bigint(20) NULL DEFAULT NULL COMMENT '发起人部门id';

-- changeset lijie:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'process_instance_running' AND column_name='organization_name') AS column_exists;
ALTER TABLE `process_instance_running` ADD COLUMN `organization_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起人部门';

-- changeset lijie:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_flow' AND column_name='auto_approval_printing') AS column_exists;
ALTER TABLE `form_flow` ADD COLUMN `auto_approval_printing` tinyint(1) NULL DEFAULT NULL COMMENT '自动审批打印展示';