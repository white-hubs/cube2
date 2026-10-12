-- liquibase formatted sql

-- changeset ghy:add-gm-user-sync-last-sync-time-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'gm_user_sync_config' AND column_name = 'last_sync_time';
ALTER TABLE `gm_user_sync_config`
    ADD COLUMN `last_sync_time` datetime DEFAULT NULL COMMENT '最后同步时间' AFTER `enabled`;

-- changeset ghy:create-gm-user-sync-failed-record-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'gm_user_sync_failed_record';
CREATE TABLE `gm_user_sync_failed_record` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `third_user_id` bigint(20) DEFAULT NULL COMMENT '三方用户id',
    `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
    `user_name` varchar(200) DEFAULT NULL COMMENT '三方用户名',
    `update_time_begin` datetime DEFAULT NULL COMMENT '本次同步开始时间',
    `update_time_end` datetime NOT NULL COMMENT '本次同步结束时间',
    `error_message` varchar(1000) DEFAULT NULL COMMENT '失败原因',
    `error_stack` text DEFAULT NULL COMMENT '异常堆栈',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
    `creator_id` varchar(100) DEFAULT NULL COMMENT '创建用户id',
    `creator_type` varchar(50) DEFAULT NULL COMMENT '创建用户类型',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `updater_id` varchar(100) DEFAULT NULL COMMENT '更新用户id',
    `updater_type` varchar(50) DEFAULT NULL COMMENT '更新用户类型',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) DEFAULT '0' COMMENT '租户主键',
    PRIMARY KEY (`id`),
    KEY `idx_tenant_mobile` (`tenant`, `mobile`),
    KEY `idx_tenant_third_user_id` (`tenant`, `third_user_id`),
    KEY `idx_update_time_end` (`update_time_end`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='光明人员同步失败记录';
