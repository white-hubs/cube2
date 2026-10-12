-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'alarm_ai_result') AS column_exists;
CREATE TABLE `alarm_ai_result`
(
    `id`              bigint(20) unsigned NOT NULL COMMENT '主键',
    `alarm_record_id` bigint(20) unsigned NOT NULL COMMENT '报警记录主键',
    `origin_result`   varchar(500) NOT NULL COMMENT '原始分析结果',
    `result`          varchar(50)  NOT NULL COMMENT '分析结果(实报、误报)',
    `tenant`          int(10) unsigned NOT NULL COMMENT '租户ID',
    `deleted`         bigint(20) unsigned DEFAULT NULL COMMENT '是否删除',
    `create_by`       varchar(20)           DEFAULT NULL COMMENT '创建人',
    `create_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`       varchar(20)           DEFAULT NULL COMMENT '更新人',
    `update_time`     datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报警ai分析结果';

