-- liquibase formatted sql
--
-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_terminal' AND index_name = 'idx_terminal_tenant_deleted_msg_id') AS index_exists;
ALTER TABLE `msg_record_terminal` ADD INDEX `idx_terminal_tenant_deleted_msg_id`(`terminal`,`tenant`,`deleted`,`msg_record_id`) USING BTREE;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_terminal' AND index_name = 'record_id_idx') AS index_exists;
ALTER TABLE `msg_record_terminal` DROP INDEX `record_id_idx`;

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_target' AND index_name = 'idx_msg_record_id_channel_code_user_id_deleted_tenant') AS index_exists;
ALTER TABLE `msg_record_target` DROP INDEX `idx_msg_record_id_channel_code_user_id_deleted_tenant`;

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_target' AND index_name = 'idx_channel_code_user_id_deleted_tenant') AS index_exists;
ALTER TABLE `msg_record_target` DROP INDEX `idx_channel_code_user_id_deleted_tenant`;

-- changeset lanchun:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_target' AND index_name = 'idx_msg_record_target_user_id') AS index_exists;
ALTER TABLE `msg_record_target` ADD INDEX `idx_msg_record_target_user_id`(`user_id`,`tenant`,`deleted`,`channel_code`,`msg_record_id`,`send_status`) USING BTREE;

-- changeset lanchun:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record' AND index_name = 'business_id_idx') AS index_exists;
ALTER TABLE `msg_record` ADD INDEX `business_id_idx`(`business_id`) USING BTREE;
