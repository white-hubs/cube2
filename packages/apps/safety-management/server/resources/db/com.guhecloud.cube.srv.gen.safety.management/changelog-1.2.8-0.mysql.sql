-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_emergency_file' AND column_name='level_code') AS column_exists;
ALTER TABLE `safety_emergency_file` ADD COLUMN `level_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件级别' AFTER `name`;

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_emergency_file' AND column_name='expiration_status') AS column_exists;
ALTER TABLE `safety_emergency_file` ADD COLUMN `expiration_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预案文件状态' AFTER `emergency_event_type_name`;

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_emergency_file' AND column_name='expiration_date') AS column_exists;
ALTER TABLE `safety_emergency_file` ADD COLUMN `expiration_date` datetime NULL DEFAULT NULL COMMENT '过期时间' AFTER `expiration_status`;

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_emergency_file' AND column_name='document_file_name') AS column_exists;
ALTER TABLE `safety_emergency_file` ADD COLUMN `document_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文档附件名称' AFTER `document_file_id`;

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_emergency_file' AND column_name='document_file_path') AS column_exists;
ALTER TABLE `safety_emergency_file` ADD COLUMN `document_file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件附件路径' AFTER `document_file_name`;

-- changeset lanchun:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_emergency_file' AND column_name='process_file_name') AS column_exists;
ALTER TABLE `safety_emergency_file` ADD COLUMN `process_file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理流程文件名称' AFTER `process_file_id`;

-- changeset lanchun:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'safety_emergency_file' AND column_name='process_file_path') AS column_exists;
ALTER TABLE `safety_emergency_file` ADD COLUMN `process_file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理流程文件路径' AFTER `process_file_name`;
