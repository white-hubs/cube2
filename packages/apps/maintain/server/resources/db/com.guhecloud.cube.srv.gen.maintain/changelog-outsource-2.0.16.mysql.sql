-- liquibase formatted sql

-- changeset qqq:1686103358461-1
-- 风险源类型长度更长需要加长
ALTER TABLE inspect_plan_relation MODIFY COLUMN target_type varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '巡检对象具体类型';
ALTER TABLE inspect_task_target_relation MODIFY COLUMN target_type varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '巡检对象具体类型';