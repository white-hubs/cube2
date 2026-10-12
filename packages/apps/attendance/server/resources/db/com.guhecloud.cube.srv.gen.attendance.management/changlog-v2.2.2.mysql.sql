-- liquibase formatted sql

-- changeset qqq:1686103358461-1


-- 请假单添加规则一天折算时长和是否计入缺勤
ALTER TABLE att_ask_leave_from ADD holiday_day_of_hour INT DEFAULT 8 NULL COMMENT '假期规则一天折算时长';
ALTER TABLE att_ask_leave_from CHANGE holiday_day_of_hour holiday_day_of_hour INT DEFAULT 8 NULL COMMENT '假期规则一天折算时长' AFTER holiday_name;

ALTER TABLE att_ask_leave_from ADD into_absence BOOL DEFAULT false NULL COMMENT '是否计入缺勤(true表示计入缺勤';
ALTER TABLE att_ask_leave_from CHANGE into_absence into_absence BOOL DEFAULT false NULL COMMENT '是否计入缺勤(true表示计入缺勤' AFTER holiday_name;

-- 日志表补偿方式改为文本
ALTER TABLE att_approve_log MODIFY COLUMN proportion TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '补偿方式';


