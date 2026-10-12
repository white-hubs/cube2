-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'universal_panel' AND column_name = 'items' ) AS column_exists;
ALTER TABLE `universal_panel` ADD COLUMN `items` text NULL DEFAULT NULL;