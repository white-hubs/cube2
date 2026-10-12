-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-mdata`.`scene_type`
ADD COLUMN `scenario_type` varchar(40) NULL COMMENT '类型' AFTER `update_time`;
