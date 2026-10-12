-- liquibase formatted sql

-- changeset qqq:1686103358461-1

alter table `cube-srv-gen-maintain`.`fault` add column `fault_obj_type` varchar(32) default null comment '故障对象类型';
alter table `cube-srv-gen-maintain`.`fault` add column `fault_type_id` bigint(20) default null comment '故障类型id';