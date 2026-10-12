-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT CheckTableIsExist('safety_cultivate') AS column_exists;
CREATE TABLE `safety_cultivate`
(
    `id`                 bigint(11)                                                    NOT NULL,
    `theme_name`         varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '培训主题',
    `obj_id`             bigint(11)                                                    NOT NULL COMMENT '实体id',
    `cultivate_type`     varchar(64)                                                   NOT NULL COMMENT '培训类型',
    `person_num`         int(8)                                                        NOT NULL COMMENT '培训人数',
    `cultivate_date`     datetime                                                      NOT NULL COMMENT '培训日期',
    `cultivate_duration` int(8)                                                        NULL COMMENT '培训时长(分钟)',
    `photo`              varchar(512)                                                  NULL COMMENT '现场照片',
    `deleted`            bigint(20) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time`        datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '更新用户',
    `update_time`        datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`             int(10)                                                       NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4  COLLATE = utf8mb4_general_ci COMMENT = '安全培训(二维码中转页)'  ROW_FORMAT = DYNAMIC;
