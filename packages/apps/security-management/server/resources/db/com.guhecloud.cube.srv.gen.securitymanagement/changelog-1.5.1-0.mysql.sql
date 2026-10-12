-- liquibase formatted sql

-- changeset xiaofeng:ddl-1
ALTER TABLE `information_plan`
    MODIFY COLUMN `open_time` varchar(20) DEFAULT NULL COMMENT '开屏时间（精确到分）';

ALTER TABLE `information_plan`
    MODIFY COLUMN `close_time` varchar(20) DEFAULT NULL COMMENT '关屏时间（精确到分）';
