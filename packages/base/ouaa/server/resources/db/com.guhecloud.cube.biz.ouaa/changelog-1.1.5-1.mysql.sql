-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-ouaa`.`user` ADD photograph_key BIGINT(32) NULL COMMENT '人员照片业务主键';
ALTER TABLE `cube-biz-ouaa`.`user` CHANGE photograph_key photograph_key BIGINT(32) NULL COMMENT '人员照片业务主键' AFTER entry_date;

ALTER TABLE `cube-biz-ouaa`.`user` ADD work_status varchar(500) NULL COMMENT '工作状态';
ALTER TABLE `cube-biz-ouaa`.`user` CHANGE work_status work_status varchar(500) NULL COMMENT '工作状态' AFTER gender;

ALTER TABLE `cube-biz-ouaa`.user_tenant ADD work_status varchar(50) NULL COMMENT '租户工作状态';
ALTER TABLE `cube-biz-ouaa`.user_tenant CHANGE work_status work_status varchar(50) NULL COMMENT '租户工作状态' AFTER admin;
