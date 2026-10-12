-- liquibase formatted sql

-- changeset iot-linkage:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'iot_link_config' AND index_name = 'uni_number') AS column_exists;
ALTER TABLE `iot_link_config` ADD UNIQUE INDEX `uni_number`(`number`) USING HASH COMMENT '联动编码唯一';