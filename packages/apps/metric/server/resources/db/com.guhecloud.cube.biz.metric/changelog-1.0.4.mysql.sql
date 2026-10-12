-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'safety_report_score' AND column_name = 'suggestion' ) AS column_exists;
ALTER TABLE `safety_report_score` ADD COLUMN `suggestion` varchar(500) NULL DEFAULT NULL COMMENT '分析建议' AFTER `score`;
