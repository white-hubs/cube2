-- liquibase formatted sql

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'user' AND column_name='id_card') AS column_exists;
ALTER TABLE `user` ADD COLUMN `id_card` varchar(255) NULL DEFAULT NULL COMMENT '身份证号';