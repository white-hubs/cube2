-- liquibase formatted sql

-- changeset qqq:1686103358461-1

CREATE TABLE `project_apply`
(
    `id`             bigint(20)                      NOT NULL COMMENT 'id',
    `project_name`   varchar(100)                    NOT NULL COMMENT '小散工程名称',
    `process_ins_id` varchar(64)                     NOT NULL COMMENT '流程实例id',
    `start_user`     bigint(20)                               DEFAULT NULL COMMENT '申请人',
    `start_dep`      bigint(20)                               DEFAULT NULL COMMENT '申请部门',
    `pass_time`      datetime                                 DEFAULT NULL COMMENT '审批通过时间',
    `approve_result` varchar(32)                              DEFAULT NULL COMMENT '审批结果',
    `apply_num`      varchar(32)                              DEFAULT NULL COMMENT '工单编号',
    `deleted`        bigint(20) unsigned             NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`      varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time`    datetime                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(100) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT '更新用户',
    `update_time`    datetime                                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`         int(10)                         NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='工程请示';


CREATE TABLE `project_sign_contract`
(
    `id`             bigint(20)                      NOT NULL COMMENT 'id',
    `project_name`   varchar(100)                    NOT NULL COMMENT '小散工程名称',
    `process_ins_id` varchar(64)                     NOT NULL COMMENT '流程实例id',
    `apply_id`       bigint(20)                      NOT NULL COMMENT '请示id',
    `start_user`     bigint(20)                               DEFAULT NULL COMMENT '申请人',
    `start_dep`      bigint(20)                               DEFAULT NULL COMMENT '申请部门',
    `pass_time`      datetime                                 DEFAULT NULL COMMENT '审批通过时间',
    `approve_result` varchar(32)                              DEFAULT NULL COMMENT '审批结果',
    `project_num`    varchar(32)                              DEFAULT NULL COMMENT '工程编号',
    `deleted`        bigint(20) unsigned             NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`      varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time`    datetime                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(100) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT '更新用户',
    `update_time`    datetime                                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`         int(10)                         NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='工程签约';


CREATE TABLE `project_schedule`
(
    `id`                  bigint(20)                      NOT NULL COMMENT 'id',
    `apply_id`            bigint(20)                      NOT NULL COMMENT '请示id',
    `contract_id`         bigint(20)                      NOT NULL COMMENT '签约id',
    `project_num`         varchar(32)                              DEFAULT NULL COMMENT '工程编号',
    `start_user`          bigint(20)                               DEFAULT NULL COMMENT '申请人',
    `project_name`        varchar(100)                             DEFAULT NULL COMMENT '小散工程名称',
    `party_b`             varchar(64)                              DEFAULT NULL COMMENT '乙方',
    `completion_time`     datetime                                 DEFAULT NULL COMMENT '完成时限',
    `over_time`           datetime                                 DEFAULT NULL COMMENT '验收通过时间',
    `supervise_user`      bigint(20)                               DEFAULT NULL COMMENT '监管负责人',
    `supervise_user_name` varchar(32)                              DEFAULT NULL COMMENT '监管负责人名称',
    `supervise_dep`       bigint(20)                               DEFAULT NULL COMMENT '监管部门',
    `status`              varchar(16)                              DEFAULT NULL COMMENT '工程状态',
    `schedule`            int(4)                                   DEFAULT NULL COMMENT '完成进度',
    `deleted`             bigint(20) unsigned             NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`           varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time`         datetime                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`           varchar(100) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT '更新用户',
    `update_time`         datetime                                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`              int(10)                         NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='工程进度';



CREATE TABLE `project_feedback`
(
    `id`                bigint(20)                      NOT NULL COMMENT 'id',
    `schedule_id`       bigint(20)                      NOT NULL COMMENT '进度id',
    `schedule`          int(4)                                   DEFAULT NULL COMMENT '完成进度',
    `operation_type`    varchar(32)                              DEFAULT NULL COMMENT '操作类型',
    `acceptance_result` varchar(32)                              DEFAULT NULL COMMENT '验收结果',
    `description`       varchar(100)                             DEFAULT NULL COMMENT '描述',
    `files`             varchar(256)                             DEFAULT NULL COMMENT '文件',
    `has_problem`       TINYINT(1) unsigned             NOT NULL DEFAULT '0' COMMENT '工程是否存在问题',
    `problem_des`       varchar(256)                             DEFAULT NULL COMMENT '问题描述',
    `problem_files`     varchar(256)                             DEFAULT NULL COMMENT '问题文件',
    `deleted`           bigint(20) unsigned             NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by`         varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time`       datetime                        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`         varchar(100) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT '更新用户',
    `update_time`       datetime                                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant`            int(10)                         NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='工程反馈';


INSERT INTO `cube-biz-ouaa`.`application` (`id`, `code`, `product_line_id`, `application_group_id`, `name`, `icon`,
                                           `remark`, `enabled`, `deleted`, `create_by`, `create_time`, `update_by`,
                                           `update_time`)
VALUES (1703582677339570177, 'SMALLENFINEERING', NULL, 1564073141762863105, '小散工程',
        ' /resource/app-icons/消息中心/main.png', '', 1, 0, 'admin', '2023-09-18 09:32:17', 'admin',
        '2023-09-18 09:32:17');

INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703586112050921474, 1528568123400142850, 1703582677339570177, NULL, 'gcgl', '工程管理', '',
        '/portal/small-engineering/manage', 1, '', 1, 1, NULL, '1703586112050921474-', 0, 'admin',
        '2023-09-18 09:45:54', 'admin', '2023-09-19 11:05:20');
INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703593174306750466, 1528568123400142850, 1703582677339570177, NULL, 'XSGCGL', '管理', NULL,
        '/portal/samll-engineering/manage', 2, '', 1, 2, 1703586112050921474,
        '1703586112050921474-1703593174306750466-', 1695003343112, 'admin', '2023-09-18 10:13:59', 'admin',
        '2023-09-18 10:15:42');
INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703651625829498881, 1528568123400142850, 1703582677339570177, NULL, 'gcsp', '工程审批', '',
        '/portal/small-engineering/examineApprove', 3, '', 1, 1, NULL, '1703651625829498881-', 0, 'admin',
        '2023-09-18 14:06:13', 'admin', '2023-09-19 10:59:11');
INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703652045788381185, 1528568123400142850, 1703582677339570177, NULL, 'gcjg', '工程监管', '',
        '/portal/small-engineering/supervise', 4, '', 1, 1, NULL, '1703652045788381185-', 0, 'admin',
        '2023-09-18 14:07:55', 'admin', '2023-09-19 10:59:18');
INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703667417438584834, 1528568123400142850, 1703582677339570177, NULL, 'lcpz', '流程配置', '',
        '/portal/stream-engine-render/approval/config?api=loose-works/project', 5, '', 1, 1, NULL,
        '1703667417438584834-', 0, 'admin', '2023-09-18 15:08:58', 'admin', '2023-09-18 17:03:23');
INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703708157896130562, 1528568123400142850, 1703582677339570177, NULL, 'gcqs', '工程请示', '',
        '/portal/small-engineering/request', 2, '', 1, 1, NULL, '1703708157896130562-', 0, 'admin',
        '2023-09-18 17:50:53', 'admin', '2023-09-19 10:59:04');
INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703745933949632514, 1537258466647814145, 1703582677339570177, NULL, 'engineeringAllList', '工程监管', '',
        '/engineeringAllList', 1, '', 1, 1, NULL, '1703745933949632514-', 1695039673357, 'admin', '2023-09-18 20:20:58',
        'admin', '2023-09-18 20:21:12');
INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703746298120077314, 1537258466647814145, 1703582677339570177, NULL, 'PROJECT_APPLY', '工程请示', NULL,
        '/baseFlowProcessApply?businessType=PROJECT_APPLY', 1, '', 1, 1, NULL, '1703746298120077314-', 0, 'admin',
        '2023-09-18 20:22:26', 'admin', '2023-09-18 20:22:26');
INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703746419163496449, 1537258466647814145, 1703582677339570177, NULL, 'PROJECT_SIGN_CONTRACT', '工程签约', NULL,
        '/baseFlowProcessApply?businessType=PROJECT_SIGN_CONTRACT', 2, '', 1, 1, NULL, '1703746419163496449-', 0,
        'admin', '2023-09-18 20:22:55', 'admin', '2023-09-18 20:22:55');
INSERT INTO `cube-biz-ouaa`.`menu` (`id`, `terminal_id`, `application_id`, `category_id`, `code`, `name`, `icon`,
                                    `path`, `sort`, `remark`, `enabled`, `level`, `parent_id`, `sequence`, `deleted`,
                                    `create_by`, `create_time`, `update_by`, `update_time`)
VALUES (1703746569927753730, 1537258466647814145, 1703582677339570177, NULL, 'engineeringAllList', '工程监管', NULL,
        '/engineeringAllList', 3, '', 1, 1, NULL, '1703746569927753730-', 0, 'admin', '2023-09-18 20:23:31', 'admin',
        '2023-09-18 20:23:31');
