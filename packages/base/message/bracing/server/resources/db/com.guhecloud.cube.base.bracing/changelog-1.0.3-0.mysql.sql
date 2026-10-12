-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'assembly_notice' AND column_name='config') AS column_exists;
ALTER TABLE `assembly_notice` ADD COLUMN `config` text DEFAULT NULL COMMENT '配置';