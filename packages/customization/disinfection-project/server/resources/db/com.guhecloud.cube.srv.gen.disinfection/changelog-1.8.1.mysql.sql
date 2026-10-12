-- liquibase formatted sql

-- changeset disinfection:alter-scheduled-task-plan-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_plan' AND column_name = 'day_of_week') AS column_exists;
ALTER TABLE `member_scheduled_task_plan` ADD COLUMN `day_of_week` int(11) DEFAULT NULL COMMENT '每周的第几天（1=周一 ~ 7=周日），定时任务模式为每周时使用' AFTER `end_time`;

-- changeset disinfection:alter-scheduled-task-plan-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_plan' AND column_name = 'start_time') AS column_exists;
ALTER TABLE `member_scheduled_task_plan`
    MODIFY COLUMN `start_time` varchar(50) NOT NULL COMMENT '开始时间，格式HH:mm';

-- changeset disinfection:alter-scheduled-task-plan-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'member_scheduled_task_plan' AND column_name = 'end_time') AS column_exists;
ALTER TABLE `member_scheduled_task_plan`
    MODIFY COLUMN `end_time` varchar(50) NOT NULL COMMENT '结束时间，格式HH:mm';
