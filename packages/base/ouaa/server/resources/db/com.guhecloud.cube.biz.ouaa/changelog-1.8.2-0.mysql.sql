-- liquibase formatted sql


-- changeset ht:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'user_tenant' AND column_name = 'enabled' ) AS column_exists;
UPDATE `user_tenant` SET `enabled` = '1' WHERE `enabled` = 'ENABLE';
UPDATE `user_tenant` SET `enabled` = '0' WHERE `enabled` = 'DISABLE';

-- changeset ht:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'user_tenant' AND column_name = 'enabled' ) AS column_exists;
ALTER TABLE `user_tenant` MODIFY COLUMN `enabled` TINYINT DEFAULT NULL;