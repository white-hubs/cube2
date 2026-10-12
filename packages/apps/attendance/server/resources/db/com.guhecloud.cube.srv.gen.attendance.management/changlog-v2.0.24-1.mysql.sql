-- liquibase formatted sql

-- changeset qqq:1686103358461-1


-- 请假台账添加计入缺勤字段
ALTER TABLE att_ask_leave_ledger ADD into_absence BOOL DEFAULT 0 NOT NULL COMMENT '计入缺勤(true表示计入';
ALTER TABLE att_ask_leave_ledger CHANGE into_absence into_absence BOOL DEFAULT 0 NOT NULL COMMENT '计入缺勤(true表示计入' AFTER day_of_hour;

