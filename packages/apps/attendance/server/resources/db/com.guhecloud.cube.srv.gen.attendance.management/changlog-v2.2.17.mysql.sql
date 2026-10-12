-- liquibase formatted sql

-- changeset qqq:1686103358461-1
-- 假期余额迭代(作废)

-- changeset chenhongxin:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_holiday_balance_record' AND column_name='deadline_id') AS column_exists;
ALTER TABLE att_holiday_balance_record ADD deadline_id BIGINT(20) DEFAULT 0 NULL COMMENT '余额id(att_holiday_balance_deadline表id)';
ALTER TABLE att_holiday_balance_record CHANGE deadline_id deadline_id BIGINT(20) DEFAULT 0 NULL COMMENT '余额id(att_holiday_balance_deadline表id)' AFTER change_number;
-- changeset chenhongxin:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_holiday_balance_record' AND column_name='effective') AS column_exists;
ALTER TABLE att_holiday_balance_record MODIFY COLUMN effective tinyint(4) DEFAULT 1 NULL COMMENT '是否有效(false已作废，true未作废)';
-- changeset chenhongxin:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_holiday_balance_record' AND column_name='effective_remark') AS column_exists;
ALTER TABLE att_holiday_balance_record ADD effective_remark varchar(200) NULL COMMENT '作废理由';
ALTER TABLE att_holiday_balance_record CHANGE effective_remark effective_remark varchar(200) NULL COMMENT '作废理由' AFTER effective;
-- changeset chenhongxin:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_holiday_balance_record' AND column_name='effective_time') AS column_exists;
ALTER TABLE att_holiday_balance_record ADD effective_time DATETIME NULL COMMENT '作废时间';
ALTER TABLE att_holiday_balance_record CHANGE effective_time effective_time DATETIME NULL COMMENT '作废时间' AFTER effective;