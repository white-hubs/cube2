-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'event_video_file_path' AND column_name = 'file_id') AS column_exists;
ALTER TABLE `event_video_file_path` ADD COLUMN `file_id` bigint(20)  DEFAULT NULL COMMENT '文件id';

-- changeset zengyadong:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'event_video_file_path' AND column_name = 'source') AS column_exists;
ALTER TABLE `event_video_file_path` ADD COLUMN `source` varchar(20)  DEFAULT NULL COMMENT '摄像机文件来源';