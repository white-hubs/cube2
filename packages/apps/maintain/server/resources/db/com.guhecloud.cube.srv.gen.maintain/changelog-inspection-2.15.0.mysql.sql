-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_cron' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_cron` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_item_template' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_item_template` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_plan' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_plan` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_plan_relation' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_plan_relation` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_record` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record_item' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_record_item` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record_relation' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_record_relation` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record_to_business' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_record_to_business` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_standard' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_standard` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_standard_item' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_standard_item` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_standard_type_refer' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_standard_type_refer` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_task` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-13
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task_target_relation' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_task_target_relation` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-14
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_type' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_type` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;
-- changeset zengyadong:ddl-15
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_result' AND column_name='business_type') AS column_exists;
ALTER TABLE `inspect_result` ADD COLUMN `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' ;