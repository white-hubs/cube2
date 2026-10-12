-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- 计划的任务表
CREATE TABLE `patrol_plan_task` (
    `id` bigint(20) NOT NULL,
    `plan_id` bigint(20) NOT NULL COMMENT '计划id',
    `time_range` varchar(100) NOT NULL COMMENT '时间范围',
    `interval_number` int NOT NULL COMMENT '间隔时间的值',
    `interval_unit` varchar(100) NOT NULL COMMENT '间隔时间的单位',
    `execute_period` text NOT NULL COMMENT '实际执行的时间段',
    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建用户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新用户',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(10) NOT NULL COMMENT '租户主键',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='巡更计划详情-任务';

-- 任务表添加时间段字段
ALTER TABLE `patrol_task` ADD interval_number INT NOT NULL COMMENT '间隔时间的值';
ALTER TABLE `patrol_task` ADD interval_unit varchar(100) NOT NULL COMMENT '间隔时间的单位';
ALTER TABLE `patrol_task` ADD execute_period TEXT NOT NULL COMMENT '实际执行的实际段';
ALTER TABLE `patrol_task` ADD execute_period_time TEXT NULL COMMENT '带日期的执行时间段';

-- 执行记录表添加执行时间段
ALTER TABLE `patrol_task_location` ADD execute_period varchar(100) NULL COMMENT '所属任务时段';
ALTER TABLE `patrol_task_location` CHANGE execute_period execute_period varchar(100) NULL COMMENT '所属任务时段' AFTER task_id;