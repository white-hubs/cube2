-- liquibase formatted sql

-- changeset qqq:1686103358461-1

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alarm_push_config
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`alarm_push_config`
(
    `id`               bigint(20) UNSIGNED                                          NOT NULL,
    `alarm_type_code`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警类型编号',
    `alarm_level_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警级别编号',
    `tenant`           int(10) UNSIGNED                                             NULL DEFAULT NULL,
    `deleted`          bigint(20) UNSIGNED                                          NULL DEFAULT NULL,
    `create_by`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `create_time`      datetime                                                     NULL DEFAULT NULL,
    `update_by`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `update_time`      datetime                                                     NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_alarm_type_code_alarm_level_code_tenant_deleted` (`alarm_type_code`, `alarm_level_code`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '报警类型推送配置'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for safety_alarm_record
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`safety_alarm_record`
(
    `id`                            bigint(20) UNSIGNED                                     NOT NULL,
    `code`                          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '编号',
    `alarm_level_id`                bigint(20) UNSIGNED                                     NULL DEFAULT NULL COMMENT '级别主键',
    `alarm_level_code`              varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '级别编号',
    `alarm_level_name`              varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '级别名称',
    `alarm_level_sort`              int(10) UNSIGNED                                        NULL DEFAULT NULL COMMENT '级别序号',
    `alarm_type_id`                 bigint(20) UNSIGNED                                     NULL DEFAULT NULL COMMENT '类型主键',
    `alarm_type_code`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '类型编号',
    `alarm_type_name`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '类型名称',
    `alarm_type_file_id`            bigint(20)                                              NULL DEFAULT NULL COMMENT '类型图标附件',
    `alarm_target_key`              bigint(20) UNSIGNED                                     NULL DEFAULT NULL COMMENT '对象主键',
    `alarm_target_code`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '对象编号',
    `alarm_target_name`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '对象名称',
    `alarm_target_type_id`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '对象类型主键',
    `alarm_target_type_name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '对象类型名称',
    `alarm_target_longitude`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象经纬度信息',
    `alarm_target_address`          varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对象位置信息',
    `business_type`                 varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '业务类型',
    `business_key`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '业务主键',
    `alarm_time`                    datetime                                                NULL DEFAULT NULL COMMENT '报警发生时间',
    `area`                          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警区域',
    `area_name`                     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警区域名称',
    `alarm_threshold`               varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警阀值',
    `alarm_event`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警事件主键(报警规则id)',
    `alarm_event_name`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警事件名称',
    `alarm_event_type_id`           bigint(20)                                              NULL DEFAULT NULL COMMENT '报警事件分类主键',
    `alarm_event_type_code`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警事件分类编号',
    `alarm_event_type_name`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警事件分类名称',
    `alarm_status`                  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警状态',
    `alarm_status_name`             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警状态名称',
    `alarm_status_order`            int(1)                                                  NULL DEFAULT NULL COMMENT '状态序号',
    `alarm_confirmed_by`            varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警确认人员',
    `alarm_confirmed_name`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警确认人员名称',
    `alarm_confirmed_time`          datetime                                                NULL DEFAULT NULL COMMENT '报警确认时间',
    `alarm_confirmed_result_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警确认结果描述',
    `suspend_time`                  datetime                                                NULL DEFAULT NULL COMMENT '挂起时间',
    `estimate_handler_time`         datetime                                                NULL DEFAULT NULL COMMENT '预计处理时间',
    `alarm_handler_by`              varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警处理人员',
    `alarm_handler_name`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警处理人员名称',
    `alarm_handler_time`            datetime                                                NULL DEFAULT NULL COMMENT '报警处理时间',
    `alarm_handler_result`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警处理结果状态',
    `alarm_handler_result_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '报警处理结果状态名称',
    `alarm_handler_result_remark`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警处理结果描述',
    `timeout_equipped`              tinyint(1)                                              NULL DEFAULT NULL COMMENT '是否配置超时',
    `timeout_process_time`          datetime                                                NULL DEFAULT NULL COMMENT '超时处理时间',
    `timeout_process_times`         int(11)                                                 NULL DEFAULT NULL COMMENT '超时处理时间配置',
    `timeout_process_times_unit`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '超时处理时间配置单位',
    `timeout_process_times_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '超时时限(处理时间+超时处理时间单位)',
    `processing_time`               int(11)                                                 NULL DEFAULT NULL COMMENT '处理耗时(单位秒)',
    `application_id`                bigint(20) UNSIGNED                                     NULL DEFAULT NULL COMMENT '应用主键',
    `application_code`              varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '应用编号',
    `application_name`              varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '应用名称',
    `tenant`                        int(10) UNSIGNED                                        NULL DEFAULT NULL,
    `market_tenant`                 int(10)                                                 NULL DEFAULT NULL COMMENT '市场编号',
    `market_name`                   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '市场名称',
    `deleted`                       bigint(20) UNSIGNED                                     NULL DEFAULT NULL,
    `create_by`                     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `create_time`                   datetime                                                NULL DEFAULT NULL,
    `update_by`                     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `update_time`                   datetime                                                NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_business_key_tenant_deleted` (`business_key`, `tenant`, `deleted`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8_general_ci COMMENT = '报警记录'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for alarm_type
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`alarm_type`
(
    `id`               bigint(20)                                                    NOT NULL,
    `alarm_type_code`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警分类编号',
    `alarm_type_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警分类名称',
    `application_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
    `application_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用名称',
    `remark`           varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注说明',
    `deleted`          bigint(20) UNSIGNED                                           NULL DEFAULT NULL,
    `create_by`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `create_time`      datetime                                                      NULL DEFAULT NULL,
    `update_by`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `update_time`      datetime                                                      NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '报警分类'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for safety_cultivate_task
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`safety_cultivate_task`
(
    `id`                         bigint(20)                                                    NOT NULL,
    `code`                       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '任务编号',
    `name`                       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '任务名称',
    `theme_id`                   bigint(20)                                                    NULL DEFAULT NULL COMMENT '培训主题',
    `theme_name`                 char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci     NULL DEFAULT NULL COMMENT '培训主题名称',
    `cultivate_start_time`       datetime                                                      NULL DEFAULT NULL COMMENT '培训开始时间',
    `cultivate_end_time`         datetime                                                      NULL DEFAULT NULL COMMENT '培训结束时间',
    `exam_task_id`               bigint(20)                                                    NULL DEFAULT NULL COMMENT '考试任务主键',
    `cultivate_user_number`      int(11)                                                       NULL DEFAULT NULL COMMENT '培训人数',
    `cultivate_mode`             varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '培训形式(线上、线下)',
    `cultivate_mode_name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '培训形式名称(线上,线下)',
    `cultivate_target`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '培训对象(商户、员工)',
    `cultivate_target_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '培训对象名称(商户，员工)',
    `cultivate_address`          varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训地点',
    `speaker`                    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '主讲人',
    `charge_user_id`             bigint(20)                                                    NULL DEFAULT NULL COMMENT '负责人',
    `charge_user_name`           varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '负责人名称',
    `cultivate_content`          varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '培训内容',
    `cultivate_task_status`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '培训任务状态',
    `cultivate_task_status_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '培训任务状态名称',
    `tenant`                     int(11)                                                       NULL DEFAULT NULL,
    `market_tenant`              int(10)                                                       NULL DEFAULT NULL COMMENT '市场编号',
    `market_name`                varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '市场名称',
    `deleted`                    bigint(20)                                                    NULL DEFAULT NULL,
    `create_by`                  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `create_time`                datetime                                                      NULL DEFAULT NULL,
    `update_by`                  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `update_time`                datetime                                                      NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '培训任务'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for device_manage
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`device_manage`
(
    `id`               bigint(20)                                                    NOT NULL COMMENT '设备id',
    `device_name`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
    `genre_name`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型',
    `status`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '设备状态',
    `model_name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '设备型号',
    `area_id`          varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '区域iD',
    `area_name`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '区域名称',
    `department_id`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '部门id',
    `department_name`  varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '部门名称',
    `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '应用code',
    `market_tenant`    int(11)                                                       NULL     DEFAULT NULL COMMENT '市场租户',
    `market_name`      varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '市场名称',
    `deleted`          bigint(20) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '更新用户',
    `update_time`      datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`           int(10)                                                       NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '设备管理'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for safety_exam_task
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`safety_exam_task`
(
    `id`                    bigint(20)                                                    NOT NULL,
    `name`                  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '考试任务名称',
    `theme_id`              bigint(20)                                                    NULL DEFAULT NULL COMMENT '主题主键',
    `theme_name`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '主题名称',
    `exam_start_time`       datetime                                                      NULL DEFAULT NULL COMMENT '考试开始时间',
    `exam_end_time`         datetime                                                      NULL DEFAULT NULL COMMENT '考试结束时间',
    `exam_paper_id`         bigint(20)                                                    NULL DEFAULT NULL COMMENT '试卷主键',
    `exam_paper_name`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '试卷名称',
    `exam_paper_content`    text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '试卷内容(整个大json信息)',
    `charge_user_id`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '负责人主键',
    `charge_user_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '负责人名称',
    `timed`                 tinyint(1)                                                    NULL DEFAULT NULL COMMENT '是否限时',
    `exam_duration`         int(11)                                                       NULL DEFAULT NULL COMMENT '考试时长',
    `exam_duration_unit`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '考试时长单位',
    `retake`                tinyint(1)                                                    NULL DEFAULT NULL COMMENT '是否支持复考',
    `retake_number`         int(11)                                                       NULL DEFAULT NULL COMMENT '复考次数',
    `exam_task_type`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '考试类型',
    `exam_task_type_name`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '考试类型名称',
    `exam_user_number`      int(11)                                                       NULL DEFAULT NULL COMMENT '参加考试人数',
    `exam_task_status`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '任务状态',
    `exam_task_status_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '任务状态名称',
    `published_time`        datetime                                                      NULL DEFAULT NULL COMMENT '发布时间',
    `remark`                varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '考试说明',
    `tenant`                int(11)                                                       NULL DEFAULT NULL,
    `market_tenant`         int(10)                                                       NULL DEFAULT NULL COMMENT '市场编号',
    `market_name`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '市场名称',
    `deleted`               bigint(20)                                                    NULL DEFAULT NULL,
    `create_by`             varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `create_time`           datetime                                                      NULL DEFAULT NULL,
    `update_by`             varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `update_time`           datetime                                                      NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '考试任务'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for safety_hidden_danger
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`safety_hidden_danger`
(
    `id`                     bigint(11) UNSIGNED                                           NOT NULL COMMENT '隐患单id',
    `name`                   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '隐患单名称',
    `hidden_danger_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '隐患分类',
    `hidden_danger_status`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '隐患状态',
    `level_name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '隐患级别',
    `remark`                 varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '隐患描述',
    `market_tenant`          int(11)                                                       NULL     DEFAULT NULL COMMENT '市场租户',
    `market_name`            varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '市场名称',
    `deleted`                bigint(20) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '创建用户',
    `create_time`            datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '更新用户',
    `update_time`            datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`                 int(10)                                                       NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '隐患单'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for safety_inspect_task
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`safety_inspect_task`
(
    `id`               bigint(20) UNSIGNED                                     NOT NULL COMMENT '任务id',
    `task_name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
    `task_create_time` datetime                                                NULL DEFAULT NULL COMMENT '任务创建时间',
    `type_name`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '巡检类型',
    `task_type`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '任务类型',
    `level_code`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '级别code',
    `level_name`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '级别name',
    `status_code`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '状态_code',
    `status_name`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '状态_name',
    `executor`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行人',
    `valid_end_time`   datetime                                                NULL DEFAULT NULL COMMENT '生效结束时间',
    `target_num`       int(11)                                                 NULL DEFAULT NULL COMMENT '巡检对象数',
    `progress`         double(5, 2)                                            NULL DEFAULT NULL COMMENT '执行进度',
    `market_tenant`    int(11)                                                 NULL DEFAULT NULL COMMENT '市场租户',
    `market_name`      varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市场名称',
    `tenant`           int(11)                                                 NULL DEFAULT NULL COMMENT '租户ID',
    `create_by`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '创建人',
    `create_time`      datetime                                                NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '修改人',
    `update_time`      datetime                                                NULL DEFAULT NULL COMMENT '修改时间',
    `deleted`          bigint(20) UNSIGNED                                     NULL DEFAULT NULL COMMENT '删除标志',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8_general_ci COMMENT = '巡检任务'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for loose_works
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`loose_works`
(
    `id`                  bigint(20)                                                    NOT NULL COMMENT 'id',
    `project_num`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '工程编号',
    `project_name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '小散工程名称',
    `party_b`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '乙方',
    `completion_time`     datetime                                                      NULL     DEFAULT NULL COMMENT '完成时限',
    `supervise_user`      bigint(20)                                                    NULL     DEFAULT NULL COMMENT '监管负责人',
    `supervise_user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '监管负责人名称',
    `supervise_dep`       bigint(20)                                                    NULL     DEFAULT NULL COMMENT '监管部门',
    `supervise_dep_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '监管部门名称',
    `status`              varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '工程状态',
    `has_problem`         tinyint(1) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '工程是否存在问题',
    `schedule`            int(4)                                                        NULL     DEFAULT NULL COMMENT '完成进度',
    `market_tenant`       int(11)                                                       NULL     DEFAULT NULL COMMENT '市场租户',
    `market_name`         varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '市场名称',
    `deleted`             bigint(20) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time`         datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '更新用户',
    `update_time`         datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`              int(10)                                                       NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '小散工程'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for market_config
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`market_config`
(
    `id`            bigint(20)                                                    NOT NULL,
    `market_name`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '市场名称',
    `market_code`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '市场编号',
    `executor_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '执行器编号',
    `executor_port` int(11)                                                       NULL DEFAULT NULL COMMENT '执行器端口',
    `market_tenant` int(11)                                                       NULL DEFAULT NULL COMMENT '市场方租户号',
    `url`           varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求地址',
    `account`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '登录账号',
    `password`      varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
    `coordinate`    varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市场经纬度坐标',
    `tenant`        int(11)                                                       NULL DEFAULT NULL,
    `create_by`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL DEFAULT NULL,
    `create_time`   datetime                                                      NULL DEFAULT NULL,
    `update_by`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL DEFAULT NULL,
    `update_time`   datetime                                                      NULL DEFAULT NULL,
    `deleted`       bigint(20) UNSIGNED                                           NULL DEFAULT NULL COMMENT '删除标志',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '市场配置'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for market_data_config
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`market_data_config`
(
    `id`           bigint(20)                                                    NOT NULL,
    `market_id`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '市场id',
    `job_name`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '任务名称',
    `method_path`  varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口路径',
    `method_type`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法类型',
    `method_param` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法参数',
    `cron`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'cron表达式',
    `task_id`      varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '任务ID（目前为xxljob的任务id）',
    `tenant`       int(11)                                                       NULL DEFAULT NULL COMMENT '租户编号',
    `create_by`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL DEFAULT NULL,
    `create_time`  datetime                                                      NULL DEFAULT NULL,
    `update_by`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci        NULL DEFAULT NULL,
    `update_time`  datetime                                                      NULL DEFAULT NULL,
    `deleted`      bigint(20) UNSIGNED                                           NULL DEFAULT NULL COMMENT '删除标志',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '市场数据同步配置'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for market_monthly_report
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`market_monthly_report`
(
    `id`                bigint(20)                                                    NOT NULL COMMENT '主键',
    `monthly_report_id` bigint(20)                                                    NULL     DEFAULT NULL COMMENT '总部月报主键',
    `title`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
    `monthly`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '月报月份',
    `month_date`        datetime                                                      NULL     DEFAULT NULL COMMENT '月报时间',
    `status`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '填报状态',
    `report_style`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '填报样式(前端使用)',
    `report_user_id`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '上报人',
    `report_time`       datetime                                                      NULL     DEFAULT NULL COMMENT '上报时间',
    `report_url`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '文件地址',
    `report_id`         bigint(20)                                                    NULL     DEFAULT NULL COMMENT '文件id',
    `report_name`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '文件名称',
    `market_tenant`     int(11)                                                       NULL     DEFAULT NULL COMMENT '市场租户',
    `market_name`       varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '市场名称',
    `deleted`           bigint(20) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '更新用户',
    `update_time`       datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`            int(10)                                                       NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '市场月报管理'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for market_monthly_report_url
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`market_monthly_report_url`
(
    `id`            bigint(20)                                                    NOT NULL,
    `market_id`     bigint(20)                                                    NULL DEFAULT NULL COMMENT '市场配置主键',
    `market_tenant` int(10) UNSIGNED                                              NULL DEFAULT NULL COMMENT '市场编号',
    `report_url`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '填报地址',
    `tenant`        int(10) UNSIGNED                                              NULL DEFAULT NULL COMMENT '租户编号',
    `deleted`       bigint(20) UNSIGNED                                           NULL DEFAULT NULL,
    `create_by`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `create_time`   datetime                                                      NULL DEFAULT NULL,
    `update_by`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `update_time`   datetime                                                      NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '市场月报填报地址信息'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for monthly_report
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`monthly_report`
(
    `id`             bigint(20)                                                    NOT NULL COMMENT '主键',
    `title`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
    `monthly`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '月报月份',
    `month_date`     datetime                                                      NULL     DEFAULT NULL COMMENT '月报时间',
    `status`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '填报状态',
    `report_style`   text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL COMMENT '填报样式(前端使用)',
    `report_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '上报人',
    `report_time`    datetime                                                      NULL     DEFAULT NULL COMMENT '上报时间',
    `report_url`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '文件地址',
    `report_id`      bigint(20)                                                    NULL     DEFAULT NULL COMMENT '文件id',
    `report_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '文件名称',
    `deleted`        bigint(20) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NOT NULL COMMENT '创建用户',
    `create_time`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '更新用户',
    `update_time`    datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`         int(10)                                                       NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '总部月报管理'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for safety_patrol_task
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`safety_patrol_task`
(
    `id`               bigint(11) UNSIGNED                                           NOT NULL COMMENT '任务id',
    `task_name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '巡更任务',
    `task_create_time` datetime                                                      NULL     DEFAULT NULL COMMENT '任务创建时间',
    `execute_data`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '执行时间段',
    `status_code`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '任务状态Code',
    `status_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '任务状态Name',
    `result_code`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '任务结果_code',
    `result_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '任务结果_name',
    `route_name`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '巡更路线',
    `location_num`     int(10)                                                       NULL     DEFAULT NULL COMMENT '巡更点数',
    `user_name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '执行人姓名',
    `market_tenant`    int(11)                                                       NULL     DEFAULT NULL COMMENT '市场租户',
    `market_name`      varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '市场名称',
    `deleted`          bigint(20) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '创建用户',
    `create_time`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '更新用户',
    `update_time`      datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`           int(10)                                                       NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '巡更任务'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for property_filing
-- ----------------------------
CREATE TABLE `cube_srv_safety_management`.`property_filing`
(
    `id`              bigint(11) UNSIGNED                                           NOT NULL COMMENT '资产id',
    `property_name`   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '资产名称',
    `property_type`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '资产类别',
    `put_in_time`     datetime                                                      NULL     DEFAULT NULL COMMENT '入库时间',
    `expiration_time` datetime                                                      NULL     DEFAULT NULL COMMENT '维保到期时间',
    `status`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '资产状态',
    `manager_id`      bigint(20)                                                    NULL     DEFAULT NULL COMMENT '管理员id',
    `manager_name`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '管理员名称',
    `area_id`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '区域id',
    `area_name`       varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '区域名称',
    `user_id`         bigint(20)                                                    NULL     DEFAULT NULL COMMENT '使用人',
    `username`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '使用人',
    `market_tenant`   int(11)                                                       NULL     DEFAULT NULL COMMENT '市场租户',
    `market_name`     varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '市场名称',
    `deleted`         bigint(20) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '是否已删除',
    `create_by`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '创建用户',
    `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci       NULL     DEFAULT NULL COMMENT '更新用户',
    `update_time`     datetime                                                      NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`          int(10)                                                       NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '资产建档'
  ROW_FORMAT = DYNAMIC;


ALTER TABLE `cube_srv_safety_management`.`market_config` ADD COLUMN `file_ids` varchar(256) NULL COMMENT '市场图片，多个用逗号分割' AFTER `coordinate`;
ALTER TABLE `cube_srv_safety_management`.`market_config` ADD COLUMN `address` varchar(128) NULL COMMENT '市场地址';
ALTER TABLE `cube_srv_safety_management`.`market_config` ADD COLUMN `found_time` varchar(32) NULL COMMENT '市场创立时间';


CREATE TABLE `cube_srv_safety_management`.`safety_emergency_file` (
      `id` bigint(20) NOT NULL COMMENT '预案文件id',
      `name` varchar(50) DEFAULT NULL COMMENT '名称',
      `emergency_event_type_code` varchar(50) DEFAULT NULL COMMENT '预案事件分类编号',
      `emergency_event_type_name` varchar(250) DEFAULT NULL COMMENT '预案事件分类名称',
      `remark` varchar(250) DEFAULT NULL COMMENT '描述',
      `document_file_id` varchar(20) DEFAULT NULL COMMENT '文档附件',
      `process_file_id` varchar(20) DEFAULT NULL COMMENT '处理流程附件',
      `market_tenant`   int(11)                                                       NULL  DEFAULT NULL COMMENT '市场租户',
      `market_name`     varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL  DEFAULT NULL COMMENT '市场名称',
      `tenant` int(11) DEFAULT NULL COMMENT '租户编号',
      `deleted` bigint(20) DEFAULT NULL COMMENT '是否删除',
      `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
      `update_by` varchar(20) DEFAULT NULL COMMENT '更新人',
      `update_time` datetime DEFAULT NULL COMMENT '更新时间',
      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='预案文件';


CREATE TABLE `safety_risk_index` (
    `id` bigint(11) unsigned NOT NULL COMMENT 'id',
    `risk_index` int(11) DEFAULT NULL COMMENT '风险指数',
    `statistical_period` varchar(6) DEFAULT NULL COMMENT '统计周期',
    `statistical_date` varchar(125) DEFAULT NULL COMMENT '统计时间',
    `market_tenant` int(11) DEFAULT NULL COMMENT '市场租户',
    `market_name` varchar(125) DEFAULT NULL COMMENT '市场名称',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='安全风险指数';


CREATE TABLE `weight_allot` (
    `id` bigint(11) unsigned NOT NULL COMMENT 'id',
    `index_type` varchar(128) DEFAULT NULL COMMENT '指标类型',
    `index_item` varchar(128) DEFAULT NULL COMMENT '指标项',
    `weight` int(11) DEFAULT NULL COMMENT '权重占比',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='权重分配';

CREATE TABLE `safety_risk_score` (
     `id` bigint(11) unsigned NOT NULL COMMENT 'id',
     `index_type` varchar(128) DEFAULT NULL COMMENT '指标类型',
     `section_start` DOUBLE(6,2) DEFAULT NULL COMMENT '区间开始值',
     `section_end` DOUBLE(6,2) DEFAULT NULL COMMENT '区间结束值',
     `slope` DOUBLE(6,2) DEFAULT NULL COMMENT '斜率',
     `score_start` INT(11) DEFAULT NULL COMMENT '分数开始值',
     `score_end` INT(11) DEFAULT NULL COMMENT '分值结束值',
     `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
     `create_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建用户',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
     `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `tenant` int(10) NOT NULL COMMENT '租户主键',
     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='分值区间';

ALTER table market_config add COLUMN `market_area` double(8,2) DEFAULT NULL COMMENT '市场面积';

SET FOREIGN_KEY_CHECKS = 1;
