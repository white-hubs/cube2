-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'video_file' AND index_name = 'idx_tenant_deleted_stored_status' ) AS index_exists;
-- comment: 待转存分页/COUNT：locally_stored + locally_stored_status（含租户隔离前缀）；MySQL 8 在线加索引
ALTER TABLE `video_file` ADD INDEX `idx_tenant_deleted_stored_status` (`tenant`, `deleted`, `locally_stored`, `locally_stored_status`), ALGORITHM=INPLACE, LOCK=NONE;

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'video_file' AND index_name = 'idx_deleted_stored_status_retry_tenant' ) AS index_exists;
-- comment: 后台任务无 tenant 上下文时的 COUNT/分页；deleted 前缀匹配实际 SQL，并包含 tenant；MySQL 8 在线加索引
ALTER TABLE `video_file` ADD INDEX `idx_deleted_stored_status_retry_tenant` (`deleted`, `locally_stored`, `locally_stored_status`, `retry_next_time`, `retry_times`, `tenant`), ALGORITHM=INPLACE, LOCK=NONE;
