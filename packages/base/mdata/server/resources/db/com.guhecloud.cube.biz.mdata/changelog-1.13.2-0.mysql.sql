-- liquibase formatted sql

-- changeset grid:ddl-1.13.2-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record') AS column_exists;
CREATE TABLE `grid_handover_record`
(
    `id`                 bigint(20) unsigned NOT NULL COMMENT '主键',
    `record_name`        varchar(50)                   DEFAULT NULL COMMENT '记录名称',
    `grid_area_id`       bigint(20) unsigned NOT NULL COMMENT '网格区域id',
    `shift_over_user`    bigint(20) unsigned NOT NULL COMMENT '交班人',
    `shift_over_time`    datetime             NOT NULL COMMENT '交班时间',
    `take_over_user`     bigint(20) unsigned NOT NULL COMMENT '接班人',
    `take_over_time`     datetime                      DEFAULT NULL COMMENT '接班时间',
    `status`             varchar(50)          NOT NULL COMMENT '状态',
    `remarks`            varchar(500)                  DEFAULT NULL COMMENT '备注',
    `before_manager_ids` varchar(500)                  DEFAULT NULL COMMENT '交接前负责人快照（逗号分隔）',
    `after_manager_ids`  varchar(500)                  DEFAULT NULL COMMENT '交接后负责人快照（逗号分隔）',
    `tenant`             int(10) unsigned              DEFAULT NULL COMMENT '租户',
    `deleted`            bigint(20) unsigned           DEFAULT NULL COMMENT '删除标记',
    `create_by`          varchar(100)         NOT NULL COMMENT '创建用户',
    `creator_id`         varchar(100)                  DEFAULT NULL COMMENT '创建用户id',
    `creator_type`       varchar(50)                   DEFAULT NULL COMMENT '创建用户类型',
    `create_time`        datetime             NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          varchar(100)         NOT NULL COMMENT '更新用户',
    `updater_id`         varchar(100)                  DEFAULT NULL COMMENT '更新用户id',
    `updater_type`       varchar(50)                   DEFAULT NULL COMMENT '更新用户类型',
    `update_time`        datetime             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_grid_area_status` (`grid_area_id`, `status`) USING BTREE,
    INDEX `idx_take_over_status` (`take_over_user`, `status`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='网格交接记录表';

-- changeset grid:ddl-1.13.2-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'grid_handover_record_extend') AS column_exists;
CREATE TABLE `grid_handover_record_extend`
(
    `id`          bigint(20) unsigned NOT NULL COMMENT '主键',
    `handover_id` bigint(20) unsigned NOT NULL COMMENT '网格交接记录ID',
    `att_info1`   varchar(500)                 DEFAULT NULL COMMENT '扩展描述1',
    `file1`       bigint(20) unsigned          DEFAULT NULL COMMENT '扩展附件1业务key',
    `att_info2`   varchar(500)                 DEFAULT NULL COMMENT '扩展描述2',
    `file2`       bigint(20) unsigned          DEFAULT NULL COMMENT '扩展附件2业务key',
    `att_info3`   varchar(500)                 DEFAULT NULL COMMENT '扩展描述3',
    `file3`       bigint(20) unsigned          DEFAULT NULL COMMENT '扩展附件3业务key',
    `att_info4`   varchar(500)                 DEFAULT NULL COMMENT '扩展描述4',
    `file4`       bigint(20) unsigned          DEFAULT NULL COMMENT '扩展附件4业务key',
    `tenant`      int(10) unsigned             DEFAULT NULL COMMENT '租户',
    `deleted`     bigint(20) unsigned          DEFAULT NULL COMMENT '删除标记',
    `create_by`   varchar(100)        NOT NULL COMMENT '创建用户',
    `creator_id`  varchar(100)                 DEFAULT NULL COMMENT '创建用户id',
    `creator_type` varchar(50)                 DEFAULT NULL COMMENT '创建用户类型',
    `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(100)        NOT NULL COMMENT '更新用户',
    `updater_id`  varchar(100)                 DEFAULT NULL COMMENT '更新用户id',
    `updater_type` varchar(50)                 DEFAULT NULL COMMENT '更新用户类型',
    `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_handover_id` (`handover_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT ='网格交接扩展信息表';
