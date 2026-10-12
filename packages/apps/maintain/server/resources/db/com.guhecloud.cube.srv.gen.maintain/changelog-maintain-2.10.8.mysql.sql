-- liquibase formatted sql

-- changeset chx:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND column_name='receive_dept_ids') AS column_exists;
ALTER TABLE inspect_task MODIFY COLUMN receive_dept_ids TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接收部门';

-- changeset chx:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND column_name='receive_post_ids') AS column_exists;
ALTER TABLE inspect_task MODIFY COLUMN receive_post_ids TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接收岗位';

-- changeset chx:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_task' AND column_name='receive_role_ids') AS column_exists;
ALTER TABLE inspect_task MODIFY COLUMN receive_role_ids TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接收人角色ids';

