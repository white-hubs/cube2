-- liquibase formatted sql

-- changeset qqq:1686103358461-1

alter table `cube_base_bracing`.`assembly_notice_obj` add column `obj_info` varchar(255) default null comment '通知对象信息';