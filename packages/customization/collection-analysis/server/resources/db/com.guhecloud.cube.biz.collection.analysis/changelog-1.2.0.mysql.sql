-- liquibase formatted sql

-- changeset ghy:create-gm-user-sync-config-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(1) FROM information_schema.TABLES WHERE table_schema = DATABASE() AND table_name = 'gm_user_sync_config';
CREATE TABLE `gm_user_sync_config` (
    `id` bigint(20) NOT NULL COMMENT '主键',
    `base_url` varchar(500) NOT NULL COMMENT '三方网关根地址',
    `app_id` varchar(200) NOT NULL COMMENT '三方接口appId',
    `app_key` varchar(200) NOT NULL COMMENT '三方接口appKey',
    `page_size` int(11) NOT NULL DEFAULT '500' COMMENT '分页大小',
    `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
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
    UNIQUE KEY `idx_tenant_deleted` (`tenant`,`deleted`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='光明人员同步配置';
