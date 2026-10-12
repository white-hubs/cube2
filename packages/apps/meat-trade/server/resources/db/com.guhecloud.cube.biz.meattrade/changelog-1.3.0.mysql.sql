-- liquibase formatted sql
--
-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 0 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meattrade_pork_report' AND column_name = 'store_name' ) AS column_exists;
ALTER TABLE `meattrade_pork_report` ADD COLUMN `store_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺名称' AFTER `abattoir_id`;