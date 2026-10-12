-- liquibase formatted sql

-- changeset qqq:1686103358461-1
alter table assembly_notice_obj modify column `obj_type` varchar(128) DEFAULT NULL COMMENT '对象类型';