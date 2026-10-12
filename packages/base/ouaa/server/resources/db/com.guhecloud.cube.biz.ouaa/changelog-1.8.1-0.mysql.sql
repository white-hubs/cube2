-- liquibase formatted sql

-- changeset ht:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'user_tenant' AND column_name = 'belong_tenant' ) AS column_exists;
ALTER TABLE `user_tenant` MODIFY COLUMN `belong_tenant` VARCHAR (50) DEFAULT NULL;