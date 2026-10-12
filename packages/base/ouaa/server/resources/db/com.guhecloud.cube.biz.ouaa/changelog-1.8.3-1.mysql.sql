-- liquibase formatted sql


-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'tenant' AND column_name = 'lnt_and_lat_scope_name' ) AS column_exists;
ALTER TABLE tenant ADD lnt_and_lat_scope_name varchar(100) NULL COMMENT '经纬度范围名称';
ALTER TABLE tenant CHANGE lnt_and_lat_scope_name lnt_and_lat_scope_name varchar(100) NULL COMMENT '经纬度范围名称' AFTER lnt_and_lat;


