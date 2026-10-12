-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'pork_arrange_push_data' AND column_name = 'arrange_details' ) AS column_exists;
ALTER TABLE `pork_arrange_push_data` MODIFY COLUMN `arrange_details` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '理货详细信息' AFTER `end_time`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'pork_arrange_push_data' AND index_name = 'idx_tally_id') AS index_exists;
ALTER TABLE `pork_arrange_push_data` ADD INDEX `idx_tally_id`(`tally_id`) USING BTREE;

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'pork_arrange_push_data' AND index_name = 'idx_event_type') AS index_exists;
ALTER TABLE `pork_arrange_push_data` ADD INDEX `idx_event_type`(`event_type`) USING BTREE;

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'pork_arrange_push_data' AND index_name = 'idx_status') AS index_exists;
ALTER TABLE `pork_arrange_push_data` ADD INDEX `idx_status`(`status`) USING BTREE;