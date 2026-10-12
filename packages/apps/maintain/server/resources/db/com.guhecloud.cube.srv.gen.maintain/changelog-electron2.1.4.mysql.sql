-- liquibase formatted sql

-- changeset qqq:1686103358461-1

-- 巡更计划
ALTER TABLE patrol_plan MODIFY COLUMN execute_data varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '执行时间段';
