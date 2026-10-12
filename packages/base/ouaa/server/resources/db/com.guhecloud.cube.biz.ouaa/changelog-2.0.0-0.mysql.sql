-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'tenant' AND column_name = 'complaint_hot_line' ) AS column_exists;
ALTER TABLE tenant ADD complaint_hot_line varchar(100) NULL COMMENT '投诉电话';