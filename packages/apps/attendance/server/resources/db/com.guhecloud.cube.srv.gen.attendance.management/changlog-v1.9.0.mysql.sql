-- liquibase formatted sql

-- changeset qqq:1686103358461-1

use `cube-srv-gen-attendance-management`;
ALTER TABLE `cube-srv-gen-attendance-management`.classes_change_situation ADD work_type varchar(20) NULL COMMENT '换班类型(考勤组的值班和排班枚举)';
ALTER TABLE `cube-srv-gen-attendance-management`.classes_change_situation CHANGE work_type work_type varchar(20) NULL COMMENT '换班类型(考勤组的值班和排班枚举)' AFTER reason;
