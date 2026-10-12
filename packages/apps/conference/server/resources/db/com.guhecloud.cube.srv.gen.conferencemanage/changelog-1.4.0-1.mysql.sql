-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'room_screen_config') AS column_exists;
CREATE TABLE `room_screen_config`
(
    `id`              bigint(20) NOT NULL,
    `conference_id`   bigint(20) NOT NULL COMMENT '会议室id',
    `device_code`     varchar(255) DEFAULT NULL COMMENT '设备',
    `device_name`     varchar(255) DEFAULT NULL COMMENT '设备名称',
    `display_status`  varchar(20)  DEFAULT NULL COMMENT '展示状态',
    `cron_expression` bigint(20)   DEFAULT NULL COMMENT '刷新时间',
    `tenant`          varchar(255) DEFAULT NULL,
    `create_by`       varchar(32)  DEFAULT NULL COMMENT '创建人',
    `create_time`     datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`       varchar(32)  DEFAULT NULL COMMENT '更新人',
    `update_time`     datetime     DEFAULT NULL COMMENT '更新时间',
    `deleted`         varchar(255) DEFAULT NULL,
    `program_url`     varchar(255) DEFAULT NULL COMMENT '接口地址',
    `auth_info`       varchar(255) DEFAULT NULL COMMENT '鉴权信息',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='会议屏配置';