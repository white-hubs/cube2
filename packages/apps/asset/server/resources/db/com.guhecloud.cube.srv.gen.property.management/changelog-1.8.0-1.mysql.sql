-- liquibase formatted sql

-- changeset lijie:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'property_bim_config') AS column_exists;
CREATE TABLE `property_bim_config`
(
    `id`                  bigint(20) unsigned NOT NULL COMMENT '主键',
    `scene_type`          bigint(20) unsigned DEFAULT NULL COMMENT '场景类型',
    `scene_type_sequence` varchar(200) DEFAULT NULL COMMENT '场景类型序列号',
    `business_type`       varchar(100) DEFAULT NULL COMMENT '业务类型',
    `business_id`         bigint(20) unsigned DEFAULT NULL COMMENT '业务主键',
    `tenant`              int(10) unsigned DEFAULT NULL COMMENT '租户编号',
    `deleted`             bigint(20) unsigned DEFAULT NULL COMMENT '是否删除',
    `create_by`           varchar(100) DEFAULT NULL COMMENT '创建人',
    `create_time`         datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`           varchar(100) DEFAULT NULL COMMENT '更新人',
    `update_time`         datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_business_id_scene_type_tenant_deleted` (`scene_type`,`business_id`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='设施业务场景';

-- changeset lijie:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'risk_source_bim_config') AS column_exists;

CREATE TABLE `risk_source_bim_config`
(
    `id`                  bigint(20) unsigned NOT NULL COMMENT '主键',
    `scene_type`          bigint(20) unsigned DEFAULT NULL COMMENT '场景类型',
    `scene_type_sequence` varchar(200) DEFAULT NULL COMMENT '场景类型序列号',
    `business_type`       varchar(100) DEFAULT NULL COMMENT '业务类型',
    `business_id`         bigint(20) unsigned DEFAULT NULL COMMENT '业务主键',
    `tenant`              int(10) unsigned DEFAULT NULL COMMENT '租户编号',
    `deleted`             bigint(20) unsigned DEFAULT NULL COMMENT '是否删除',
    `create_by`           varchar(100) DEFAULT NULL COMMENT '创建人',
    `create_time`         datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`           varchar(100) DEFAULT NULL COMMENT '更新人',
    `update_time`         datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `idx_business_id_scene_type_tenant_deleted` (`scene_type`,`business_id`,`tenant`,`deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='设施业务场景';

