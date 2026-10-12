-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'announcement' AND column_name='viewed') AS column_exists;
ALTER TABLE `announcement` ADD COLUMN `viewed` int(20) DEFAULT NULL COMMENT '是否查看';