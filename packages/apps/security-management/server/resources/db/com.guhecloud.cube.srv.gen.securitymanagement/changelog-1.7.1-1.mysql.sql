-- liquibase formatted sql

-- changeset xiaofeng:ddl-1
-- ----------------------------
-- Table structure for passenger_flow_record
-- ----------------------------
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0  SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'passenger_flow_record' AND column_name='record_num') AS column_exists;
ALTER TABLE `cube-srv-gen-securitymanagement`.`passenger_flow_record` ADD COLUMN `record_num` int(20) NULL COMMENT '' AFTER `in_out`;