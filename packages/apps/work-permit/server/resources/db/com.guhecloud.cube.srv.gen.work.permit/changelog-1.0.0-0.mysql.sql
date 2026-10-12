-- liquibase formatted sql

-- changeset codex:work-permit-ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_permit_type_config') AS table_exists;
CREATE TABLE `work_permit_type_config`
(
    `id`                 bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type_name`          varchar(64)         NOT NULL COMMENT '作业类型名称',
    `work_permit_level`  varchar(32)         NOT NULL COMMENT '作业等级',
    `level_definition`   varchar(500)        NOT NULL COMMENT '等级定义',
    `permit_valid_hours` int(10) unsigned    NOT NULL COMMENT '作业票有效时长，单位小时',
    `tenant`             int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`          varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`         varchar(100)        NULL COMMENT '创建人ID',
    `creator_type`       varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`        datetime            NULL COMMENT '创建时间',
    `update_by`          varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`         varchar(100)        NULL COMMENT '更新人ID',
    `updater_type`       varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`        datetime            NULL COMMENT '更新时间',
    `deleted`            bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='作业类型配置';

-- changeset codex:work-permit-ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_permit_associated_item') AS table_exists;
CREATE TABLE `work_permit_associated_item`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `work_permit_type_id` bigint(20) unsigned NOT NULL COMMENT '作业类型配置ID',
    `item_type`    varchar(32)         NOT NULL COMMENT '关联项类型：RISK_CHECK 风险检查项，ACCEPTANCE 验收项',
    `item_name`    varchar(255)        NOT NULL COMMENT '关联项内容',
    `sort_no`      int(10) unsigned    NOT NULL DEFAULT 0 COMMENT '排序号',
    `tenant`       int(10) unsigned    NOT NULL COMMENT '租户ID',
    `create_by`    varchar(64)         NULL COMMENT '创建人名称',
    `creator_id`   varchar(100)        NULL COMMENT '创建人ID',
    `creator_type` varchar(50)         NULL COMMENT '创建用户类型',
    `create_time`  datetime            NULL COMMENT '创建时间',
    `update_by`    varchar(64)         NULL COMMENT '更新人名称',
    `updater_id`   varchar(100)        NULL COMMENT '更新人ID',
    `updater_type` varchar(50)         NULL COMMENT '更新用户类型',
    `update_time`  datetime            NULL COMMENT '更新时间',
    `deleted`      bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '逻辑删除标识',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='作业关联项';
