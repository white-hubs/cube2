-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'user' AND column_name = 'source' ) AS column_exists;
ALTER TABLE `user` ADD COLUMN `source` varchar(100) DEFAULT NULL COMMENT '用户来源';