-- liquibase formatted sql
--
-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record' AND index_name = 'channel_code_application_code_tenant') AS index_exists;
ALTER TABLE `msg_record` ADD INDEX `channel_code_application_code_tenant`(`application_code`, `channel_code`, `tenant`, `deleted`) USING BTREE;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record' AND index_name = 'channel_code_tenant_deleted') AS index_exists;
ALTER TABLE `msg_record` ADD INDEX `channel_code_tenant_deleted`(`channel_code`, `tenant`, `deleted`) USING BTREE;

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_target' AND index_name = 'idx_msg_record_id_channel_type_user_id_deleted_tenant') AS index_exists;
ALTER TABLE `msg_record_target` DROP INDEX `idx_msg_record_id_channel_type_user_id_deleted_tenant`;

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_target' AND index_name = 'msg_record_target_channel_code_IDX') AS index_exists;
ALTER TABLE `msg_record_target` DROP INDEX `msg_record_target_channel_code_IDX`;

-- changeset lanchun:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_target' AND index_name = 'record_id_IDX') AS index_exists;
ALTER TABLE `msg_record_target` ADD INDEX `record_id_IDX`(`msg_record_id`) USING BTREE;

-- changeset lanchun:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_target' AND index_name = 'idx_msg_record_id_channel_code_user_id_deleted_tenant') AS index_exists;
ALTER TABLE `msg_record_target` ADD UNIQUE INDEX `idx_msg_record_id_channel_code_user_id_deleted_tenant`(`msg_record_id`, `channel_code`, `user_id`, `deleted`, `tenant`) USING BTREE;

-- changeset lanchun:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_target' AND index_name = 'idx_channel_code_user_id_deleted_tenant') AS index_exists;
ALTER TABLE `msg_record_target` ADD INDEX `idx_channel_code_user_id_deleted_tenant`(`channel_code`, `user_id`, `tenant`, `deleted`) USING BTREE;

-- changeset lanchun:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record_terminal' AND index_name = 'record_id_idx') AS index_exists;
ALTER TABLE `msg_record_terminal` ADD INDEX `record_id_idx`(`msg_record_id`) USING BTREE;

-- changeset lanchun:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'msg_record' AND index_name = 'scene_code_idx') AS index_exists;
ALTER TABLE `msg_record` ADD INDEX `scene_code_idx`(`msg_scene_code`) USING BTREE;
