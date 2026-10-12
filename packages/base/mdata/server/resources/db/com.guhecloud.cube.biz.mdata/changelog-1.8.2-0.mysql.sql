-- liquibase formatted sql

-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'operation_log') AS column_exists;
CREATE TABLE `operation_log`
(
    `id`            varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '主键',
    `user_id`       bigint(20) NULL DEFAULT NULL,
    `user_name`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作用户',
    `method`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '请求方式',
    `url`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求URL',
    `ip_address`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP',
    `parameters`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
    `abnormal_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异常信息',
    `result`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求结果',
    `create_time`   datetime                                                      NOT NULL,
    `tenant`        int(10) NOT NULL,
    `status_code`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求状态码',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志' ROW_FORMAT = COMPACT;