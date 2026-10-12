-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'goods_pricing_plan' AND column_name='xxl_task_id') AS column_exists;
ALTER TABLE goods_pricing_plan MODIFY COLUMN xxl_task_id varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT 'xxljob的任务ID';