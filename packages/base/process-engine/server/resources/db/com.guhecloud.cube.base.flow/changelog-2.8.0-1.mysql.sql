-- liquibase formatted sql

-- changeset codex:alter-form-flow-group-application-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'form_flow_group' AND column_name = 'application' ) AS column_exists;
ALTER TABLE `form_flow_group` ADD COLUMN `application` varchar(100) DEFAULT NULL COMMENT '应用';
