-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_holiday_balance_deadline' AND column_name='parent_id') AS column_exists;
ALTER TABLE att_holiday_balance_deadline ADD parent_id BIGINT(20) NULL COMMENT '父级id(表示从该条数据上拆分出来的)';
ALTER TABLE att_holiday_balance_deadline CHANGE parent_id parent_id BIGINT(20) NULL COMMENT '父级id(表示从该条数据上拆分出来的)' AFTER normal;
