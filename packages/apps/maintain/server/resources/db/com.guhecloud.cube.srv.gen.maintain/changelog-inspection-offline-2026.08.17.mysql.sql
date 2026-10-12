-- liquibase formatted sql

-- changeset codex:inspect-offline-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND column_name = 'revision') AS column_exists;
ALTER TABLE inspect_task ADD revision BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '离线同步版本号';

-- changeset codex:inspect-offline-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND column_name = 'revision') AS column_exists;
ALTER TABLE inspect_record ADD revision BIGINT(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '离线同步版本号';

-- changeset codex:inspect-offline-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND index_name = 'idx_inspect_task_offline_status') AS index_exists;
ALTER TABLE inspect_task ADD INDEX idx_inspect_task_offline_status(tenant, status, update_time);

-- changeset codex:inspect-offline-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND index_name = 'idx_inspect_task_offline_revision') AS index_exists;
ALTER TABLE inspect_task ADD INDEX idx_inspect_task_offline_revision(tenant, status, revision);

-- changeset codex:inspect-offline-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND index_name = 'idx_inspect_record_offline_task') AS index_exists;
ALTER TABLE inspect_record ADD INDEX idx_inspect_record_offline_task(tenant, task_id, status);

-- changeset codex:inspect-offline-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'inspect_record' AND index_name = 'idx_inspect_record_offline_revision') AS index_exists;
ALTER TABLE inspect_record ADD INDEX idx_inspect_record_offline_revision(tenant, task_id, revision);

-- changeset codex:inspect-offline-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'inspect_offline_sync_session') AS table_exists;
CREATE TABLE `inspect_offline_sync_session` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `session_id` varchar(64) NOT NULL COMMENT '同步会话ID',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `client_id` varchar(128) NULL DEFAULT NULL COMMENT '设备稳定标识',
  `app_version` varchar(32) NULL DEFAULT NULL COMMENT 'App版本',
  `offline_schema_version` int(11) NULL DEFAULT NULL COMMENT '离线协议版本',
  `base_cursor` varchar(255) NULL DEFAULT NULL COMMENT '客户端上次游标',
  `snapshot_id` varchar(64) NOT NULL COMMENT '快照ID',
  `status` varchar(32) NOT NULL COMMENT '会话状态',
  `progress` int(11) NOT NULL DEFAULT 0 COMMENT '构建进度',
  `cursor` varchar(255) NULL DEFAULT NULL COMMENT '新游标',
  `manifest_json` longtext NULL COMMENT 'manifest JSON',
  `inline_delta_json` longtext NULL COMMENT '小增量 JSON',
  `error_code` varchar(64) NULL DEFAULT NULL COMMENT '错误码',
  `error_message` varchar(500) NULL DEFAULT NULL COMMENT '错误信息',
  `expires_at` datetime NOT NULL COMMENT '过期时间',
  `create_by` varchar(100) NULL DEFAULT NULL COMMENT '创建用户',
  `creator_id` varchar(100) NULL DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) NULL DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NULL DEFAULT NULL COMMENT '更新用户',
  `updater_id` varchar(100) NULL DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) NULL DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_inspect_offline_session`(`session_id`) USING BTREE,
  INDEX `idx_inspect_offline_session_user`(`tenant`, `user_id`, `status`) USING BTREE,
  INDEX `idx_inspect_offline_session_expire`(`expires_at`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检离线同步会话' ROW_FORMAT = DYNAMIC;

-- changeset codex:inspect-offline-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'inspect_offline_sync_chunk') AS table_exists;
CREATE TABLE `inspect_offline_sync_chunk` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `chunk_id` varchar(64) NOT NULL COMMENT '分片ID',
  `session_id` varchar(64) NOT NULL COMMENT '同步会话ID',
  `snapshot_id` varchar(64) NOT NULL COMMENT '快照ID',
  `chunk_type` varchar(32) NOT NULL COMMENT '分片类型',
  `task_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '任务ID',
  `revision` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '任务版本',
  `part_no` int(11) NOT NULL COMMENT '分片序号',
  `part_count` int(11) NOT NULL COMMENT '分片总数',
  `compressed_size` bigint(20) UNSIGNED NOT NULL COMMENT '压缩后大小',
  `sha256` varchar(64) NOT NULL COMMENT '分片SHA256',
  `storage_key` varchar(500) NOT NULL COMMENT '兼容存储定位符',
  `content_gzip` longblob NULL COMMENT 'gzip JSON分片内容',
  `expires_at` datetime NOT NULL COMMENT '过期时间',
  `create_by` varchar(100) NULL DEFAULT NULL COMMENT '创建用户',
  `creator_id` varchar(100) NULL DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) NULL DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NULL DEFAULT NULL COMMENT '更新用户',
  `updater_id` varchar(100) NULL DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) NULL DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_inspect_offline_chunk`(`session_id`, `chunk_id`) USING BTREE,
  INDEX `idx_inspect_offline_chunk_session`(`session_id`, `part_no`) USING BTREE,
  INDEX `idx_inspect_offline_chunk_expire`(`expires_at`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检离线同步分片' ROW_FORMAT = DYNAMIC;

-- changeset codex:inspect-offline-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_offline_sync_chunk' AND column_name = 'content_gzip') AS column_exists;
ALTER TABLE inspect_offline_sync_chunk ADD content_gzip LONGBLOB NULL COMMENT 'gzip JSON分片内容' AFTER storage_key;

-- changeset codex:inspect-offline-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'inspect_offline_submit_operation') AS table_exists;
CREATE TABLE `inspect_offline_submit_operation` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tenant` int(11) NULL DEFAULT NULL COMMENT '租户ID',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',
  `client_id` varchar(128) NULL DEFAULT NULL COMMENT '设备稳定标识',
  `client_batch_id` varchar(64) NULL DEFAULT NULL COMMENT '客户端批次ID',
  `client_operation_id` varchar(64) NOT NULL COMMENT '客户端操作ID',
  `record_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检记录ID',
  `task_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '巡检任务ID',
  `record_revision` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '客户端记录版本',
  `payload_hash` varchar(64) NOT NULL COMMENT '请求摘要',
  `request_json` longtext NULL COMMENT '请求JSON',
  `status` varchar(32) NOT NULL COMMENT '操作状态',
  `server_record_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '服务端记录ID',
  `server_record_revision` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '服务端记录版本',
  `error_code` varchar(64) NULL DEFAULT NULL COMMENT '错误码',
  `error_message` varchar(500) NULL DEFAULT NULL COMMENT '错误信息',
  `create_by` varchar(100) NULL DEFAULT NULL COMMENT '创建用户',
  `creator_id` varchar(100) NULL DEFAULT NULL COMMENT '创建用户id',
  `creator_type` varchar(50) NULL DEFAULT NULL COMMENT '创建用户类型',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(100) NULL DEFAULT NULL COMMENT '更新用户',
  `updater_id` varchar(100) NULL DEFAULT NULL COMMENT '更新用户id',
  `updater_type` varchar(50) NULL DEFAULT NULL COMMENT '更新用户类型',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_inspect_offline_submit_operation`(`tenant`, `user_id`, `client_operation_id`) USING BTREE,
  INDEX `idx_inspect_offline_submit_record`(`tenant`, `record_id`) USING BTREE,
  INDEX `idx_inspect_offline_submit_batch`(`tenant`, `user_id`, `client_batch_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '巡检离线提交幂等操作' ROW_FORMAT = DYNAMIC;
