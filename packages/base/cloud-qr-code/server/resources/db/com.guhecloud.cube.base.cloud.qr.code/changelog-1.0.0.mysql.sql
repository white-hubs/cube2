-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'cloud_qr_code_type') AS column_exists;
CREATE TABLE `cloud_qr_code_type` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `code` VARCHAR(64) NOT NULL COMMENT '类型编号',
    `name` VARCHAR(128) NOT NULL COMMENT '类型名称',
    `grant_client_id` VARCHAR(64) NOT NULL COMMENT '授权客户端编号（用于数据隔离）',
    `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
    `deleted` BIGINT(20) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_grant_client_id_code` (`grant_client_id`, `code`, `deleted`),
    KEY `idx_grant_client_id` (`grant_client_id`),
    KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='云二维码类型表';

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'cloud_qr_code') AS column_exists;
CREATE TABLE `cloud_qr_code` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `code` VARCHAR(64) NOT NULL COMMENT '二维码编号（系统生成）',
    `name` VARCHAR(128) NOT NULL COMMENT '二维码名称',
    `short_code` VARCHAR(32) NOT NULL COMMENT '短码（用于生成短链接）',
    `qr_type_id` BIGINT NOT NULL COMMENT '二维码类型ID',
    `qr_content_type` VARCHAR(32) NOT NULL COMMENT '内容类型：LINK-链接，TEXT-文本',
    `qr_content` TEXT NOT NULL COMMENT '二维码内容',
    `grant_client_id` VARCHAR(64) NOT NULL COMMENT '授权客户端编号（用于数据隔离）',
    `mode` VARCHAR(64) NOT NULL COMMENT '链接行为，ROUTE路由跳转、TEXT文本',
    `mode_content` VARCHAR(500) NOT NULL COMMENT '链接行为，文本内容',
    `enabled` TINYINT(1) DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
    `deleted` BIGINT(20) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_grant_client_id_code` (`grant_client_id`, `code`, `deleted`),
    UNIQUE KEY `uk_short_code` (`short_code`, `deleted`),
    KEY `idx_grant_client_id` (`grant_client_id`),
    KEY `idx_qr_type_id` (`qr_type_id`),
    KEY `idx_name` (`name`),
    KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='云二维码主表';

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'cloud_qr_route') AS column_exists;
CREATE TABLE `cloud_qr_route` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `qr_code_id` BIGINT NOT NULL COMMENT '关联二维码ID',
    `short_code` VARCHAR(32) NOT NULL COMMENT '短码（与cloud_qr_code.short_code关联）',
    `target_url` VARCHAR(1024) NOT NULL COMMENT '实际跳转的目标URL',
    `target_params` TEXT DEFAULT NULL COMMENT '跳转参数（JSON格式）',
    `deleted` BIGINT(20) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_short_code` (`short_code`, `deleted`),
    KEY `idx_qr_code_id` (`qr_code_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='云二维码路由配置表';

-- changeset lanchun:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'cloud_address_config') AS column_exists;
CREATE TABLE `cloud_address_config` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `ip_address` VARCHAR(256) NOT NULL COMMENT '域名地址',
    `deleted` BIGINT(20) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='云地址配置表（全局唯一配置）';

-- changeset lanchun:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'cloud_qr_code_project') AS column_exists;
CREATE TABLE `cloud_qr_code_project` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `name` VARCHAR(64) NOT NULL COMMENT '域名地址',
    `code` VARCHAR(64) DEFAULT NULL COMMENT '是否删除：0-否，1-是',
    `secret` VARCHAR(64) DEFAULT NULL COMMENT '是否删除：0-否，1-是',
    `deleted` BIGINT(20) DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目管理';
