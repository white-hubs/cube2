-- liquibase formatted sql

-- changeset xiaofeng:ddl-1
-- ----------------------------
-- Table structure for program
-- ----------------------------
CREATE TABLE IF NOT EXISTS `program` (
                                  id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                  third_program_id VARCHAR(100) DEFAULT NULL COMMENT '第三方节目id',
                                  program_name VARCHAR(100) DEFAULT NULL COMMENT '节目名称',
                                  category_name VARCHAR(30) DEFAULT NULL COMMENT '节目分类名称',
                                 `deleted` bigint(20) DEFAULT NULL,
                                 `tenant` int(11) DEFAULT NULL,
                                 `create_by` varchar(255) DEFAULT NULL,
                                 `create_time` datetime DEFAULT NULL,
                                 `update_by` varchar(255) DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                  CONSTRAINT idx_client_tenant UNIQUE (third_program_id, tenant)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='节目库实体表';

CREATE TABLE IF NOT EXISTS `info_screen_schedule_assoc` (
    id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '关联主键id',
    information_plan_id bigint(20) DEFAULT NULL COMMENT '开关屏配置管理主键id',
    security_device_id bigint(20) DEFAULT NULL COMMENT '基础设备id',
    `deleted` bigint(20) DEFAULT NULL,
    `tenant` int(11) DEFAULT NULL,
    `create_by` varchar(255) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_by` varchar(255) DEFAULT NULL,
    `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='信息发布屏与开关屏配置管理关联表';

CREATE TABLE IF NOT EXISTS `information_plan` (
                                                  id bigint(20) AUTO_INCREMENT PRIMARY KEY COMMENT '计划主键id',
    plan_name VARCHAR(100) DEFAULT NULL COMMENT '计划名称',
    plan_start_time datetime DEFAULT NULL COMMENT '计划开始时间',
    plan_end_time datetime DEFAULT NULL COMMENT '计划结束时间',
    open_time datetime DEFAULT NULL COMMENT '开屏时间（精确到分）',
    close_time datetime DEFAULT NULL COMMENT '关屏时间（精确到分）',
    is_enable int(10) DEFAULT NULL COMMENT '是否启用 0：禁用 1：启用',
    execution_cycle VARCHAR(100) DEFAULT NULL COMMENT '执行周期集合 1：周一 2：周二 3：周三 4：周四 5：周五 6：周六 7：周日 (逗号分割)',
    `open_job_task_id` int(11) DEFAULT NULL COMMENT '开屏的xxljob的任务id',
    `close_job_task_id` int(11) DEFAULT NULL COMMENT '关屏的xxljob的任务id',
    `deleted` bigint(20) DEFAULT NULL,
    `tenant` int(11) DEFAULT NULL,
    `create_by` varchar(255) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `update_by` varchar(255) DEFAULT NULL,
    `update_time` datetime DEFAULT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开关屏配置管理表';