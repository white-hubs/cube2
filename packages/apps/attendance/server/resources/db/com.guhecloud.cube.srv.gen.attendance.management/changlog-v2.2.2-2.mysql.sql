-- liquibase formatted sql

-- changeset qqq:1686103358461-1


CREATE TABLE `work_overtime_ledger_advance`
(
    `id`                  bigint(20) NOT NULL,
    `user_name`           varchar(255) DEFAULT NULL,
    `user_id`             bigint(20) NOT NULL COMMENT '用户id',
    `org_id`              bigint(20) DEFAULT NULL COMMENT '部门id',
    `work_date`           date         NOT NULL COMMENT '加班时间',
    `work_duration`       int(11) unsigned DEFAULT NULL COMMENT '工作日时长（时）',
    `work_pay_duration`   int(11) DEFAULT NULL COMMENT '工作时长记为加班费时长',
    `rest_duration`       int(11) DEFAULT NULL COMMENT '休息日加班时长',
    `rest_pat_duration`   int(11) DEFAULT NULL COMMENT '休息日加班记加班费时长',
    `fest_duration`       int(11) DEFAULT NULL COMMENT '节假日加班时长',
    `fest_oat_duration`   int(11) DEFAULT NULL COMMENT '节假日记加班费时长',
    `tenant`              int(11) DEFAULT NULL,
    `create_by`           varchar(32)  DEFAULT NULL,
    `create_time`         datetime     DEFAULT NULL,
    `update_by`           varchar(32)  DEFAULT NULL,
    `update_time`         datetime     DEFAULT NULL,
    `deleted`             bigint(20) DEFAULT NULL,
    `start_time`          datetime     DEFAULT NULL COMMENT '开始时间',
    `end_time`            datetime     DEFAULT NULL COMMENT '结束时间',
    `work_type`           varchar(255) DEFAULT NULL COMMENT '加班类型',
    `apply_time`          datetime     DEFAULT NULL COMMENT '申请时间',
    `apply_done_time`     datetime     DEFAULT NULL COMMENT '申请处理完成时间',
    `process_instance_id` varchar(50)  DEFAULT NULL COMMENT '流程id（工作流）',
    `status`              varchar(255) NOT NULL COMMENT '审核结果',
    PRIMARY KEY (`id`),
    KEY                   `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='加班台账预统计';