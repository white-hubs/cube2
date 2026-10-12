-- liquibase formatted sql

-- changeset lanchun:add-index-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE () AND table_name = 'event_video_file_path' AND index_name = 'index_event_id') AS index_exists;
ALTER TABLE `event_video_file_path` ADD INDEX `index_event_id` (`event_id`) USING BTREE;
