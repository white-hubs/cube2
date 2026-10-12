-- liquibase formatted sql

-- changeset disinfection:ddl-scheduled-task-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task') AS table_exists;
-- ----------------------------
-- Table structure for member_scheduled_task（定时任务信息表）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `member_scheduled_task` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `group_id` bigint(20) NOT NULL COMMENT '成员分组ID',
    `name` varchar(200) NOT NULL COMMENT '定时任务名称',
    `schedule_mode` varchar(50) NOT NULL COMMENT '定时任务模式：每天、每周',

    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(11) NOT NULL COMMENT '租户ID',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务信息表';

-- changeset disinfection:ddl-scheduled-task-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_device') AS table_exists;
-- ----------------------------
-- Table structure for member_scheduled_task_device（定时任务设备表）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `member_scheduled_task_device` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `device_id` bigint(20) NOT NULL COMMENT '设备ID',
    `scheduled_task_id` bigint(20) NOT NULL COMMENT '定时任务ID（关联 member_scheduled_task.id）',

    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(11) NOT NULL COMMENT '租户ID',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务设备表';

-- changeset disinfection:ddl-scheduled-task-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_plan') AS table_exists;
-- ----------------------------
-- Table structure for member_scheduled_task_plan（定时任务计划表）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `member_scheduled_task_plan` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `scheduled_task_id` bigint(20) NOT NULL COMMENT '定时任务ID（关联 member_scheduled_task.id）',
    `command` varchar(50) NOT NULL COMMENT '指令（ManualControlCommandEnum）：设备关闭、驱蚊指令、雾灌指令、定时任务等',
    `start_time` varchar(50) NOT NULL COMMENT '开始时间',
    `end_time` varchar(50) NOT NULL COMMENT '结束时间',

    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(11) NOT NULL COMMENT '租户ID',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务计划表';
