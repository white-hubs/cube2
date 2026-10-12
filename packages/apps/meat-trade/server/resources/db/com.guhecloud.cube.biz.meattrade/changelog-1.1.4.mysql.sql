-- liquibase formatted sql
--
-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 0 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meattrade_pork_arrange_detail' AND column_name = 'transfer_status' ) AS column_exists;
ALTER TABLE `meattrade_pork_arrange_detail` ADD COLUMN `transfer_status` tinyint(1) NULL DEFAULT NULL COMMENT '转存状态' AFTER `transfer_number`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 0 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meattrade_pork_arrange_detail' AND column_name = 'transfer_paths' ) AS column_exists;
ALTER TABLE `meattrade_pork_arrange_detail` ADD COLUMN `transfer_paths` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转存后的图片地址' AFTER `transfer_status`;

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 0 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meattrade_pork_arrange_detail_log' AND column_name = 'transfer_status' ) AS column_exists;
ALTER TABLE `meattrade_pork_arrange_detail_log` ADD COLUMN `transfer_status` tinyint(1) NULL DEFAULT NULL COMMENT '转存状态' AFTER `transfer_number`;