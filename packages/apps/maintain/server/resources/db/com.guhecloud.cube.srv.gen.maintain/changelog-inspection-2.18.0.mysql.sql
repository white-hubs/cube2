-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND column_name = 'snap_attachment_ids') AS column_exists;
ALTER TABLE `inspect_record` ADD COLUMN `snap_attachment_ids` varchar(512) NULL DEFAULT NULL COMMENT '抓拍附件（视频转存记录id，逗号拼接）' AFTER `business_type`;
