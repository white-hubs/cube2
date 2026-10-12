-- liquibase formatted sql

-- changeset hetao:door-event-record-picture-file-id
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'event_record' AND column_name = 'picture_file_id' ) AS column_exists;
ALTER TABLE `event_record` ADD COLUMN `picture_file_id` bigint DEFAULT NULL COMMENT '抓拍图片文件ID' AFTER `card_no`;
