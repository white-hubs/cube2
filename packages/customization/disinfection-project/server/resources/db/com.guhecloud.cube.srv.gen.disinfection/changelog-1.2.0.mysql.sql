-- liquibase formatted sql


-- changeset chx:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_group') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `member_group` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `member_user_id` bigint(20) NOT NULL COMMENT '所属人ID（创建该分组的用户ID）',
    `group_name` varchar(100) NOT NULL COMMENT '分组名称',
    `user_count` int(11) DEFAULT 0 COMMENT '分组内人数',
    `device_count` int(11) DEFAULT 0 COMMENT '分组内设备数',
    `description` varchar(255) DEFAULT NULL COMMENT '描述信息',

    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(11) NOT NULL COMMENT '租户ID',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户分组信息表';

-- changeset chx:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_group_user') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `member_group_user` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID（关联用户表）',
    `group_id` bigint(20) NOT NULL COMMENT '分组ID（关联 member_group.id）',
    `role` varchar(50) NOT NULL COMMENT '用户在分组中的角色，如：ADMIN, MEMBER（对应 MemberGroupRoleEnum）',

    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(11) NOT NULL COMMENT '租户ID',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户分组与用户关联表';

-- changeset chx:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_group_device') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `member_group_device` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `group_id` bigint(20) NOT NULL COMMENT '分组ID（关联 member_group.id）',
    `device_id` bigint(20) NOT NULL COMMENT '设备ID（关联设备表）',

    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(11) NOT NULL COMMENT '租户ID',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户分组与设备关联表';

-- changeset chx:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_device_automatic') AS column_exists;
-- ----------------------------
CREATE TABLE IF NOT EXISTS `member_device_automatic` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `group_id` bigint(20) NOT NULL COMMENT '分组ID（关联 member_group.id）',
    `name` varchar(300) NOT NULL COMMENT '指令名',
    `display` varchar(300) DEFAULT NULL COMMENT '显示的条件',
    `device_id` bigint(20) NOT NULL COMMENT '设备ID',
    `prop_write_value_list` text NOT NULL COMMENT '指令属性json',
    `loop_type` varchar(100) DEFAULT NULL COMMENT '循环类型',
    `loop_day` varchar(300) DEFAULT NULL COMMENT '循环日期(逗号隔开)',
    `loop_Hour` varchar(10) DEFAULT NULL COMMENT '循环时间',
    `task_id` varchar(100) DEFAULT NULL COMMENT '任务id',

    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(11) NOT NULL COMMENT '租户ID',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备自动指令表';



