-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'client' AND column_name = 'grant_types' ) AS column_exists;
ALTER TABLE `client` MODIFY COLUMN `grant_types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权类型（多个类型之间使用\",\"号隔开）';


