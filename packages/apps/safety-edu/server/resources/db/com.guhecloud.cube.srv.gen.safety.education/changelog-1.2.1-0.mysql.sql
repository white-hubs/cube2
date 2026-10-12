-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'courseware_library' AND column_name = 'file_name' ) AS column_exists;
ALTER TABLE `courseware_library` MODIFY COLUMN `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称' AFTER `file_id`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cultivate_task_courseware_library' AND column_name = 'file_name' ) AS column_exists;
ALTER TABLE `cultivate_task_courseware_library` MODIFY COLUMN `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `cultivate_task_id`;
