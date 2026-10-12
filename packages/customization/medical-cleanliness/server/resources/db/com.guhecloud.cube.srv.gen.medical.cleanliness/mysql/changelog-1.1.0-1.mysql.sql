-- liquibase formatted sql

-- changeset lijie:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_rule') AS column_exists;
CREATE TABLE `medical_rule`
(
    `id`          bigint(20) NOT NULL,
    `name`        varchar(100)                    NOT NULL COMMENT '规则名称',
    `device_ids`  varchar(255)                             DEFAULT NULL,
    `deleted`     bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(100) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime                                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`      int(10) NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset lijie:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'rule_norm') AS column_exists;

CREATE TABLE `rule_norm`
(
    `id`          bigint(20) NOT NULL,
    `rule_id`     bigint(20) NOT NULL,
    `norm_name`   varchar(255)                             DEFAULT NULL COMMENT '指标名称',
    `unit`        varchar(255)                             DEFAULT NULL COMMENT '单位',
    `norm_code`   varchar(255)                             DEFAULT NULL COMMENT '指标编码',
    `display`     int(11) DEFAULT NULL COMMENT '是否展示',
    `upper_limit` int(11) DEFAULT NULL COMMENT '上限',
    `lower_limit` int(11) DEFAULT NULL COMMENT '下限',
    `deleted`     bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`   varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(100) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime                                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`      int(10) NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset lijie:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_device_statistics') AS column_exists;
CREATE TABLE `medical_device_statistics`
(
    `id`                        bigint(20) NOT NULL,
    `tenant_name`               varchar(255) NOT NULL COMMENT '租户名称',
    `tenant`                    int(10) NOT NULL COMMENT '租户id',
    `device_prod_category_name` varchar(32)                     DEFAULT NULL COMMENT '设备品类名称',
    `device_prod_category_code` varchar(50)                     DEFAULT NULL COMMENT '设备品类编码',
    `total_number`              int(11) DEFAULT NULL,
    `normal_number`             int(11) DEFAULT NULL,
    `offline_number`            int(11) DEFAULT NULL,
    `alarm_number`              bigint(11) DEFAULT NULL,
    `deleted`                   bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`                 varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
    `create_time`               datetime     NOT NULL           DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`                 varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time`               datetime                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `acquisition_date`          date                            DEFAULT NULL COMMENT '采集日期',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- changeset lijie:ddl-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'medical_alarm_statistics') AS column_exists;
CREATE TABLE `medical_alarm_statistics`
(
    `id`                        bigint(20) NOT NULL,
    `tenant_name`               varchar(255)                    DEFAULT NULL COMMENT '租户名称',
    `tenant`                    int(10) DEFAULT NULL COMMENT '租户id',
    `device_prod_category_name` varchar(32)                     DEFAULT NULL COMMENT '设备品类名称',
    `device_prod_category_code` varchar(50)                     DEFAULT NULL COMMENT '设备品类编码',
    `deleted`                   bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`                 varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
    `create_time`               datetime     NOT NULL           DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`                 varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time`               datetime                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `total_num`                 bigint(20) DEFAULT NULL COMMENT '报警总数',
    `un_confirmed_num`          bigint(20) DEFAULT NULL COMMENT '待接收报警数',
    `progress_confirmed_num`    bigint(20) DEFAULT NULL COMMENT '处理中报警数',
    `suspend_num`               bigint(20) DEFAULT NULL COMMENT '已挂起报警数',
    `handler_num`               bigint(20) DEFAULT NULL COMMENT '已处理报警数',
    `device_code`               varchar(50) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '对象编号',
    `device_name`               varchar(50) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '对象名称',
    `acquisition_date`          varchar(255) NOT NULL           DEFAULT '' COMMENT '采集日期',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;