-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `risk_source_ledger`
(
    `id`                   bigint(20) NOT NULL COMMENT '主键',
    `serial_number`        varchar(255) NOT NULL COMMENT '风险源编码',
    `name`                 varchar(50)  NOT NULL COMMENT '风险源名称',
    `type_code`            varchar(255)                    DEFAULT NULL COMMENT '风险源类别code',
    `type_name`            varchar(50)                     DEFAULT NULL COMMENT '风险源类别名称',
    `usage_date`           date                            DEFAULT NULL COMMENT '使用到期日期',
    `area_id`              varchar(255)                    DEFAULT NULL COMMENT '区域id',
    `deleted`              bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`            varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
    `create_time`          datetime                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`            varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time`          datetime                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `number`               int(11) DEFAULT NULL COMMENT '数量',
    `tenant`               int(10) NOT NULL COMMENT '租户主键',
    `status`               varchar(20)  NOT NULL COMMENT '状态',
    `file`                 varchar(255)                    DEFAULT NULL COMMENT '资产附件',
    `remarks`              varchar(255)                    DEFAULT NULL COMMENT '备注',
    `receive_dep_id`       bigint(20) DEFAULT NULL COMMENT '接收部门id',
    `receive_dep_name`     varchar(20)                     DEFAULT NULL COMMENT '接收部门名称',
    `receive_user_name`    varchar(100)                    DEFAULT NULL COMMENT '接收人',
    `level`                int(2) DEFAULT NULL COMMENT '排序',
    `receive_user_phone`   varchar(50)                     DEFAULT NULL COMMENT '接收人电话',
    `maintenance_dep_id`   bigint(20) DEFAULT NULL COMMENT '维护部门',
    `maintenance_dep_name` varchar(255)                    DEFAULT NULL COMMENT '维护部门',
    `stall_id`             bigint(20) DEFAULT NULL COMMENT '档位id',
    `stall_name`           varchar(255)                    DEFAULT NULL COMMENT '档位名称',
    `site_point_id`        bigint(20) DEFAULT NULL COMMENT '现场点',
    `attribute`            varchar(255)                    DEFAULT NULL COMMENT '属性',
    `site_point_name`      varchar(255)                    DEFAULT NULL COMMENT '现场点名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='风险源台账';

CREATE TABLE `risk_source_parts`
(
    `id`                    bigint(20) NOT NULL,
    `code`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配件编码',
    `name`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配件名称',
    `expire_date`           date NULL DEFAULT NULL COMMENT '到期日期',
    `remarks`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `risk_source_ledger_id` bigint(20) NULL DEFAULT NULL COMMENT '风险源台账id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '风险源台账-配件信息' ROW_FORMAT = Dynamic;