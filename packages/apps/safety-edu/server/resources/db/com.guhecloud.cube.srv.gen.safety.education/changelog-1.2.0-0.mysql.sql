-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'courseware_library' AND column_name = 'duration' ) AS column_exists;
ALTER TABLE `courseware_library` ADD COLUMN `duration` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '时长' AFTER `remark`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'cultivate_task_courseware_library' AND column_name = 'duration' ) AS column_exists;
ALTER TABLE `cultivate_task_courseware_library` ADD COLUMN `duration` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '时长' AFTER `file_ids`;

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'headquarters_cultivate_task_courseware_library' AND column_name = 'duration' ) AS column_exists;
ALTER TABLE `headquarters_cultivate_task_courseware_library` ADD COLUMN `duration` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '时长' AFTER `file_ids`;
