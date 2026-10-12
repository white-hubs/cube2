-- liquibase formatted sql

-- changeset fl:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'iot_md_product_method') AS column_exists;
CREATE TABLE `iot_md_product_method`
(
    `id`            bigint                                                 NOT NULL COMMENT '方法ID',
    `prod_id`       bigint                                                 NOT NULL COMMENT '产品ID',
    `name`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '方法名称',
    `method_code`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       DEFAULT NULL COMMENT '方法编码（产品内唯一，用于跨环境映射）',
    `description`   varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci      DEFAULT NULL COMMENT '方法描述',
    `method_type`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        DEFAULT 'sync' COMMENT '方法类型：sync-同步方法、async-异步方法',
    `input_params`  longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '输入参数JSON（详细格式，参考method-spec.json）',
    `output_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '输出参数JSON（详细格式，参考method-spec.json）',
    `timeout_ms`    int                                                          DEFAULT 10000 COMMENT '执行超时时间（毫秒）',
    `retryable`     tinyint(1)                                                   DEFAULT 0 COMMENT '是否支持重试：0-否 1-是',
    `max_retries`   int                                                          DEFAULT 0 COMMENT '最大重试次数',
    `status`        int                                                          DEFAULT 0 COMMENT '状态：1-启用 0-禁用',
    `extend_config` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '方法扩展配置（JSON字符串）',
    `remark`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       DEFAULT NULL COMMENT '备注',
    `tenant`        int unsigned                                                 DEFAULT NULL COMMENT '租户编号',
    `deleted`       bigint                                                       DEFAULT NULL COMMENT '逻辑删除标记',
    `create_by`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime                                                     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE key (`prod_id`, `method_code`, `tenant`, `deleted`) USING BTREE,
    KEY `idx_prod_id` (`prod_id`) USING BTREE,
    KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC COMMENT ='产品方法表';