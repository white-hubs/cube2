-- liquibase formatted sql
--
-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 0 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meattrade_pork_arrange' AND column_name = 'terminal_number' ) AS column_exists;
ALTER TABLE `meattrade_pork_arrange` ADD COLUMN `terminal_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理货设备台号' AFTER `place`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 0 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'meattrade_pork_arrange_detail' AND column_name = 'terminal_number' ) AS column_exists;
ALTER TABLE `meattrade_pork_arrange_detail` ADD COLUMN `terminal_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '理货设备台号' AFTER `place`;