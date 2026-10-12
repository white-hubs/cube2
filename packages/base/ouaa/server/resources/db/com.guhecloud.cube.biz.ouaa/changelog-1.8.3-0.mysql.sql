-- liquibase formatted sql


-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'tenant' AND column_name = 'lnt_and_lat_scope' ) AS column_exists;
ALTER TABLE tenant ADD lnt_and_lat_scope TEXT NULL COMMENT '经纬度范围';
ALTER TABLE tenant CHANGE lnt_and_lat_scope lnt_and_lat_scope TEXT NULL COMMENT '经纬度范围' AFTER lnt_and_lat;

