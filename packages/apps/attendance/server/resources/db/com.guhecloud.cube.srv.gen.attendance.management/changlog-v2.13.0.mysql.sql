-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_holiday_balance_record' AND column_name='work_pay_duration' ) AS column_exists;
ALTER TABLE att_holiday_balance_record ADD process_instance_id varchar(100) NULL COMMENT '流程实例id';
ALTER TABLE att_holiday_balance_record CHANGE process_instance_id process_instance_id varchar(100) NULL COMMENT '流程实例id' AFTER update_name;

-- changeset chx:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_holiday_balance_record' AND column_name='business_type' ) AS column_exists;
ALTER TABLE att_holiday_balance_record ADD business_type varchar(100) NULL COMMENT '审批类型';
ALTER TABLE att_holiday_balance_record CHANGE business_type business_type varchar(100) NULL COMMENT '审批类型' AFTER process_instance_id;

