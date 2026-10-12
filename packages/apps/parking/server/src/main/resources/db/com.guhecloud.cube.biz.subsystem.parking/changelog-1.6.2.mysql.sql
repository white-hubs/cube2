-- liquibase formatted sql

-- changeset lanchun:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'parking_access_record' AND index_name = 'idx_tenant_deleted_time_type_parking' ) AS index_exists;
-- 趋势统计覆盖索引：时间范围 + access_type/parking 过滤，避免回表 image_base
ALTER TABLE `parking_access_record` ADD INDEX `idx_tenant_deleted_time_type_parking` (`tenant`, `deleted`, `access_time`, `access_type`, `parking_info_code`);

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'parking_access_record' AND index_name = 'idx_tenant_deleted_time_identify_type' ) AS index_exists;
-- 停车时长：时间前缀覆盖（旧写法 GROUP BY identify_id 备用）
ALTER TABLE `parking_access_record` ADD INDEX `idx_tenant_deleted_time_identify_type` (`tenant`, `deleted`, `access_time`, `identify_id`, `access_type`);

-- changeset lanchun:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS ( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'parking_access_record' AND index_name = 'idx_tenant_deleted_type_time_identify' ) AS index_exists;
-- 停车时长：按 access_type + access_time range 取 identify_id，进/出两路连接，避免大临时表
ALTER TABLE `parking_access_record` ADD INDEX `idx_tenant_deleted_type_time_identify` (`tenant`, `deleted`, `access_type`, `access_time`, `identify_id`);
