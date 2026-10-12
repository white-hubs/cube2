-- liquibase formatted sql

-- changeset qqq:1686103358461-1


-- 更新考勤组表，添加使用班次字段
ALTER TABLE `cube-srv-gen-attendance-management`.att_group ADD group_class_ids varchar(500) NULL COMMENT '班次ids';
ALTER TABLE `cube-srv-gen-attendance-management`.att_group CHANGE group_class_ids group_class_ids varchar(500) NULL COMMENT '班次ids' AFTER ranges;
