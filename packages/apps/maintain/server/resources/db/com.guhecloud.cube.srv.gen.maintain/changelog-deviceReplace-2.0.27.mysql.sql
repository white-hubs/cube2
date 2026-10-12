-- liquibase formatted sql

-- changeset qqq:1686103358461-1

alter table fault_case add column `product_category_code` varchar(256) default null comment '设备品类';
alter table outsource_company_scope modify column `device_genre_id` varchar(512) default null comment '委外范围(设备品类code)';
alter table inspect_standard_type_refer modify column `target_type` varchar(256) default null comment '可能为资产、挡位、现场点的具体类型';
alter table inspect_record modify column `area_id` varchar(256) DEFAULT NULL COMMENT '设备区域id';