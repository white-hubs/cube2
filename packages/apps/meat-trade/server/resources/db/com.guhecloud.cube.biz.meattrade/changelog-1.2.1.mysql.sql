-- liquibase formatted sql
--
-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'meattrade_pork_arrange_detail' AND index_name = 'idx_transfer_status') AS index_exists;
ALTER TABLE `meattrade_pork_arrange_detail` ADD INDEX `idx_transfer_status`(`transfer_status`) USING BTREE;