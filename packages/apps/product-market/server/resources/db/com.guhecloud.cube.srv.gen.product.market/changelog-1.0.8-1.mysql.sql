-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'goods_pricing_task' AND column_name='incomplete_notice_time') AS column_exists;
ALTER TABLE goods_pricing_task ADD incomplete_notice_time DATETIME NULL COMMENT '超时未完成通知时间';
ALTER TABLE goods_pricing_task CHANGE incomplete_notice_time incomplete_notice_time datetime NULL COMMENT '超时未完成通知时间' AFTER status;
-- changeset chx:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'goods_pricing_task' AND column_name='deadline') AS column_exists;
ALTER TABLE goods_pricing_task ADD deadline DATETIME NULL COMMENT '任务截止时间';
ALTER TABLE goods_pricing_task CHANGE deadline deadline DATETIME NULL COMMENT '任务截止时间' AFTER urgency;
-- changeset chx:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'goods_pricing_plan' AND column_name='task_validity') AS column_exists;
ALTER TABLE goods_pricing_plan ADD task_validity varchar(100) NULL COMMENT '任务有效期(天,时,分用逗号隔开)';
ALTER TABLE goods_pricing_plan CHANGE task_validity task_validity varchar(100) NULL COMMENT '任务有效期(天,时,分用逗号隔开)' AFTER urgency;
-- changeset chx:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'goods_pricing_task' AND column_name='task_validity') AS column_exists;
ALTER TABLE goods_pricing_task ADD task_validity varchar(100) NULL COMMENT '任务有效期';
ALTER TABLE goods_pricing_task CHANGE task_validity task_validity varchar(100) NULL COMMENT '任务有效期' AFTER urgency;