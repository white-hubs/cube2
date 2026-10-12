-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'market_monthly_report_url' AND column_name='enabled') AS column_exists;
ALTER TABLE `cube_srv_safety_management`.`market_monthly_report_url` ADD COLUMN `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '开关状态' AFTER `report_url`;
