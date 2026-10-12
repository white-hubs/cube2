-- liquibase formatted sql

-- changeset qqq:1686103358461-1


-- 排班表添加索引
CREATE INDEX att_scheduling_scheduling_date_IDX USING BTREE ON att_scheduling (scheduling_date);
CREATE INDEX att_scheduling_staff_id_IDX USING BTREE ON att_scheduling (staff_id);

