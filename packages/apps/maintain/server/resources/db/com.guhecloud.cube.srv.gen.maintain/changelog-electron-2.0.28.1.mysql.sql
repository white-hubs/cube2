-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `patrol_task` ADD COLUMN `visible_user_id` bigint(20) NULL DEFAULT NULL COMMENT '权限人员';
ALTER TABLE `patrol_task` ADD COLUMN `visible_org_id` bigint(20) NULL DEFAULT NULL COMMENT '权限部门';
