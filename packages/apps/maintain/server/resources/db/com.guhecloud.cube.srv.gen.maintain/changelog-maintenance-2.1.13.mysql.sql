-- liquibase formatted sql

-- changeset lijie:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'maintenance_rule_condition') AS column_exists;
CREATE TABLE `maintenance_rule_condition`
(
    `id`                               bigint(20) NOT NULL,
    `emergency_rule_id`                bigint(20) DEFAULT NULL COMMENT '计划主键',
    `device_prod_category_code`        varchar(50)  DEFAULT NULL COMMENT '设备产品品类编号',
    `device_prod_category_code_concat` varchar(300) DEFAULT NULL COMMENT '设备产品品类编号序列号',
    `device_prod_category_name`        varchar(100) DEFAULT NULL COMMENT '设备产品品类名称',
    `device_prod_id`                   bigint(20) DEFAULT NULL COMMENT '设备产品主键',
    `device_prod_name`                 varchar(100) DEFAULT NULL COMMENT '设备产品名称',
    `condition_type`                   varchar(100) DEFAULT NULL COMMENT '条件类型(属性，事件)',
    `device_event_id`                  varchar(20)  DEFAULT NULL COMMENT '事件主键',
    `device_event_code`                varchar(50)  DEFAULT NULL COMMENT '事件编号',
    `device_event_name`                varchar(100) DEFAULT NULL COMMENT '事件名称',
    `device_event_type`                varchar(100) DEFAULT NULL COMMENT '事件类型',
    `logical_op`                       varchar(100) DEFAULT NULL COMMENT '逻辑运算符',
    `group_id`                         bigint(20) DEFAULT NULL COMMENT '条件组主键',
    `tenant`                           int(11) DEFAULT NULL,
    `deleted`                          bigint(20) DEFAULT NULL,
    `create_by`                        varchar(20)  DEFAULT NULL,
    `create_time`                      datetime     DEFAULT NULL,
    `update_by`                        varchar(20)  DEFAULT NULL,
    `update_time`                      datetime     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='维保规则条件';

-- changeset lijie:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'maintenance_rule_condition_exp') AS column_exists;
CREATE TABLE `maintenance_rule_condition_exp`
(
    `id`                          bigint(20) NOT NULL,
    `emergency_rule_id`           bigint(20) DEFAULT NULL COMMENT '预案主键',
    `emergency_rule_condition_id` bigint(20) DEFAULT NULL COMMENT '预案规则条件主键',
    `prop_code`                   varchar(100) DEFAULT NULL COMMENT '属性编号',
    `prop_name`                   varchar(100) DEFAULT NULL COMMENT '属性名称',
    `prop_unit`                   varchar(100) DEFAULT NULL COMMENT '属性单位',
    `op`                          varchar(50)  DEFAULT NULL COMMENT '属性操作符',
    `oprand`                      varchar(100) DEFAULT NULL COMMENT '属性值',
    `start_delay_time`            int(10) unsigned DEFAULT NULL COMMENT '延迟开始时间',
    `delay_time`                  int(11) unsigned DEFAULT NULL COMMENT '延迟时间',
    `end_delay_time`              int(10) unsigned DEFAULT NULL COMMENT '延迟结束时间',
    `group_id`                    bigint(20) DEFAULT NULL COMMENT '条件组主键',
    `tenant`                      int(11) DEFAULT NULL,
    `deleted`                     bigint(20) DEFAULT NULL,
    `create_by`                   varchar(20)  DEFAULT NULL,
    `create_time`                 datetime     DEFAULT NULL,
    `update_by`                   varchar(20)  DEFAULT NULL,
    `update_time`                 datetime     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='维保规则条件表达式';