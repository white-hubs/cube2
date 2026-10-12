-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-official-management`.`focus_work`
ADD COLUMN `focus_work_starter_id` varchar(128) NULL COMMENT '督办任务下发人ID' AFTER `second_user_name`,
ADD COLUMN `focus_work_starter` varchar(128) NULL COMMENT '督办任务下发人姓名' AFTER `focus_work_starter_id`,
ADD COLUMN `merge_refer_users` varchar(256) NULL COMMENT '目前将第一、第二责任人进行融合，方便权限控制' AFTER `focus_work_starter`;