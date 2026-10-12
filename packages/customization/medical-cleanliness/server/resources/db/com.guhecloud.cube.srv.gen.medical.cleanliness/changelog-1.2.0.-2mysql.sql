-- liquibase formatted sql

-- changeset liJie:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_iot_real_time_data') AS column_exists;
CREATE TABLE `medical_iot_real_time_data`
(
    `id`          bigint(20) NOT NULL,
    `status`      varchar(255)                    DEFAULT NULL COMMENT '指标状态',
    `device_id`   varchar(255)                    DEFAULT NULL COMMENT '设备id',
    `device_name` varchar(255)                    DEFAULT NULL,
    `deleted`     bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
    `create_time` datetime                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`      int(10) NOT NULL COMMENT '租户主键',
    `tenant_name` varchar(255)                    DEFAULT NULL,
    `dep_id`      bigint(20) DEFAULT NULL,
    `dep_name`    varchar(255)                    DEFAULT NULL,
    `norm_type`   varchar(255)                    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset liJie:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_iot_real_time_data_details') AS column_exists;
CREATE TABLE `medical_iot_real_time_data_details`
(
    `id`             bigint(20) NOT NULL,
    `value`          varchar(255)                    DEFAULT NULL COMMENT '指标值',
    `status`         varchar(255)                    DEFAULT NULL COMMENT '指标状态',
    `rule_norm_code` varchar(20)                     DEFAULT NULL COMMENT '指标编码',
    `deleted`        bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`      varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
    `create_time`    datetime NOT NULL               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time`    datetime                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`         int(10) NOT NULL COMMENT '租户主键',
    `rule_norm_name` varchar(20)                     DEFAULT NULL COMMENT '指标名称',
    `device_id`      varchar(255)                    DEFAULT NULL COMMENT '设备id',
    `device_name`    varchar(255)                    DEFAULT NULL,
    `unit`           varchar(255)                    DEFAULT NULL COMMENT '单位',
    `type`           varchar(255)                    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset liJie:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule_issued_iot') AS column_exists;
CREATE TABLE `medical_rule_issued_iot`
(
    `id`               bigint(20) UNSIGNED NOT NULL,
    `issued_status`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下发状态',
    `last_issued_time` datetime NULL DEFAULT NULL COMMENT '最后下发时间',
    `tenant`           int(10) NULL DEFAULT NULL,
    `deleted`          bigint(20) NULL DEFAULT NULL,
    `create_by`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `create_time`      datetime NULL DEFAULT NULL,
    `update_by`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `update_time`      datetime NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规则下发到iot记录' ROW_FORMAT = DYNAMIC;

-- changeset liJie:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule_notify_config') AS column_exists;
CREATE TABLE `medical_rule_notify_config`
(
    `id`              bigint(20) UNSIGNED NOT NULL,
    `times`           int(5) UNSIGNED NULL DEFAULT NULL COMMENT '超限时间数',
    `time_unit`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '超限时间单位',
    `notify_channels` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知通道',
    `deleted`         bigint(20) UNSIGNED NULL DEFAULT NULL,
    `create_by`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `create_time`     datetime NULL DEFAULT NULL,
    `update_by`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `update_time`     datetime NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- changeset liJie:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule_trigger') AS column_exists;
CREATE TABLE `medical_rule_trigger`
(
    `id`              bigint(20) UNSIGNED NOT NULL,
    `rule_id`         bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '规则主键',
    `rule_name`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
    `iot_instance_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'iot实例主键',
    `product_id`      bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '产品id',
    `device_id`       bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '触发设备id',
    `device_name`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '触发设备名称',
    `depart_id`       bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '触发部门主键',
    `depart_name`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '触发部门名称',
    `tenant`          int(10) NULL DEFAULT NULL,
    `deleted`         bigint(20) NULL DEFAULT NULL,
    `create_by`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `create_time`     datetime NULL DEFAULT NULL,
    `update_by`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `update_time`     datetime NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规则触发信息' ROW_FORMAT = DYNAMIC;

-- changeset liJie:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule_trigger_norm') AS column_exists;
CREATE TABLE `medical_rule_trigger_norm`
(
    `id`                      bigint(20) UNSIGNED NOT NULL,
    `medical_rule_trigger_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '触发id',
    `upload_time`             datetime NULL DEFAULT NULL COMMENT '上报时间',
    `norm_value`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指标值',
    `norm_value_description`  varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指标值描述',
    `upper_limit`             int(11) UNSIGNED NULL DEFAULT NULL COMMENT '上限值',
    `lower_limit`             int(11) UNSIGNED NULL DEFAULT NULL COMMENT '下限值',
    `tenant`                  int(10) UNSIGNED NULL DEFAULT NULL,
    `deleted`                 bigint(20) UNSIGNED NULL DEFAULT NULL,
    `create_by`               varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `create_time`             datetime NULL DEFAULT NULL,
    `update_by`               varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `update_time`             datetime NULL DEFAULT NULL,
    `norm_name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `unit`                    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `norm_code`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '规则触发指标信息' ROW_FORMAT = DYNAMIC;

-- changeset liJie:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule' AND column_name = 'iot_rule_id') AS column_exists;
ALTER TABLE `medical_rule` ADD COLUMN `iot_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '下发联动规则';

-- changeset liJie:ddl-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule_trigger_norm' AND column_name = 'upper_limit') AS column_exists;
ALTER TABLE `medical_rule_trigger_norm` MODIFY COLUMN `upper_limit` decimal(50, 10) NULL DEFAULT NULL COMMENT '上限值' ;

-- changeset liJie:ddl-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule_trigger_norm' AND column_name = 'lower_limit') AS column_exists;
ALTER TABLE `medical_rule_trigger_norm` MODIFY COLUMN `lower_limit` decimal(50, 10) NULL DEFAULT NULL COMMENT '下限值' ;

-- changeset liJie:ddl-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule' AND column_name = 'area_options') AS column_exists;
ALTER TABLE `medical_rule` ADD COLUMN `area_options` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指标区域';