-- liquibase formatted sql

-- changeset qqq:1686103358461-1

use `cube-srv-gen-attendance-management`;

CREATE TABLE `staff_attendance_stat` (
        `id` bigint(20) DEFAULT NULL,
        `staff_id` bigint(20) NOT NULL COMMENT '员工ID',
        `staff_name` varchar(30) NOT NULL COMMENT '员工姓名',
        `staff_organization_id` bigint(20) DEFAULT NULL COMMENT '员工部门',
        `group_id` bigint(20) NOT NULL COMMENT '考勤组ID',
        `month` varchar(10) NOT NULL COMMENT '月份',
        `stat_content` text COMMENT '统计内容',
        `tenant` int(10) NOT NULL COMMENT '租户主键',
        `deleted` bigint(20) unsigned NOT NULL COMMENT '是否删除',
        `create_by` varchar(100) DEFAULT NULL COMMENT '创建用户',
        `create_time` datetime NOT NULL COMMENT '创建时间',
        `update_by` varchar(100) DEFAULT NULL COMMENT '更新用户',
        `update_time` datetime NOT NULL COMMENT '更新时间',
        PRIMARY KEY (`month`,`staff_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='员工统计数据(截至前一天)';
