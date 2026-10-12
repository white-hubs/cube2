-- liquibase formatted sql

-- changeset chenhongxin:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_clock_device' AND column_name='code') AS column_exists;
ALTER TABLE att_clock_device MODIFY COLUMN code varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '设备编码';
-- changeset chenhongxin:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_clock_device' AND column_name='model_name') AS column_exists;
ALTER TABLE att_clock_device MODIFY COLUMN model_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '设备型号';
-- changeset chenhongxin:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'att_clock_device' AND column_name='name') AS column_exists;
ALTER TABLE att_clock_device MODIFY COLUMN name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '设备名称';