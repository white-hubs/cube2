-- liquibase formatted sql

-- changeset disinfection:member-device-manual-control-setting
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_device_manual_control_setting') AS column_exists;
CREATE TABLE IF NOT EXISTS `member_device_manual_control_setting` (
    `id` bigint(20) NOT NULL COMMENT '主键ID',
    `device_id` bigint(20) NOT NULL COMMENT '设备id',
    `command` varchar(50) NOT NULL COMMENT '指令枚举',
    `duration` int(11) NOT NULL COMMENT '控制时长，单位秒',

    `deleted` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '是否已删除: 0-未删, 时间戳-已删（软删除）',
    `create_by` varchar(100) CHARACTER SET utf8mb4 NOT NULL COMMENT '创建人',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant` int(11) NOT NULL COMMENT '租户ID',

    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_user_device_command` (`tenant`, `create_by`, `device_id`, `command`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户设备手动控制设置表';
