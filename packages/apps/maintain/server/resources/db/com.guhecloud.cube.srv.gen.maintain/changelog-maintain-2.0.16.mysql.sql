-- liquibase formatted sql

-- changeset qqq:1686103358461-1

alter table maintenace_project add column `category_code` varchar(32) DEFAULT NULL COMMENT '品类编码';
alter table maintenace_project add column `category_name` varchar(32) DEFAULT NULL COMMENT '品类名称';
alter table maintenace_project drop column `device_type`;
alter table maintenace_project drop column `device_type_id`;
alter table maintenace_object add column `category_code` varchar(32) DEFAULT NULL COMMENT '品类编码';
alter table maintenace_object add column `category_name` varchar(32) DEFAULT NULL COMMENT '品类名称';
alter table maintenace_object drop column `object_type`;
alter table maintenace_object drop column `object_type_id`;
alter table maintenance_tasks add column `category_code` varchar(32) DEFAULT NULL COMMENT '品类编码';
alter table maintenance_tasks add column `category_name` varchar(32) DEFAULT NULL COMMENT '品类名称';
alter table maintenance_tasks drop column `object_type`;
alter table maintenance_tasks drop column `object_type_id`;

ALTER TABLE maintenace_project RENAME TO maintenance_project;
ALTER TABLE maintenace_object RENAME TO maintenance_object;
ALTER TABLE maintenance_project CHANGE COLUMN maintenace_content maintenance_content varchar(128) NOT NULL COMMENT '维保项内容';