-- changeset attendance:add-att-holiday-balance-record-before-balance
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_holiday_balance_record' AND column_name = 'before_balance') AS column_exists;
ALTER TABLE att_holiday_balance_record ADD before_balance BIGINT(20) NULL COMMENT '变动前余额';
ALTER TABLE att_holiday_balance_record CHANGE before_balance before_balance BIGINT(20) NULL COMMENT '变动前余额' AFTER change_number;
