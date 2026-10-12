-- liquibase formatted sql

-- changeset zengyadong:ddl-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'act_hi_varinst' AND index_name = 'ACT_IDX_HI_PROCVAR_TASK_ID') AS column_exists;
DROP INDEX `ACT_IDX_HI_PROCVAR_TASK_ID` ON act_hi_varinst;

-- changeset zengyadong:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'act_hi_varinst' AND index_name = 'ACT_IDX_HI_PROCVAR_TASK_ID') AS column_exists;
CREATE INDEX `ACT_IDX_HI_PROCVAR_TASK_ID` ON act_hi_varinst (`TASK_ID_` ) USING BTREE;


-- changeset zengyadong:ddl-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'task_running_log' AND index_name = 'task_running_log_n1') AS column_exists;
CREATE INDEX `task_running_log_n1` ON task_running_log ( `process_instance_id`) USING BTREE;

-- changeset zengyadong:ddl-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'task_node' AND index_name = 'task_node_n1') AS column_exists;
CREATE INDEX `task_node_n1` ON task_node ( `process_definition_key` ) USING BTREE;

-- changeset zengyadong:ddl-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'task_log' AND index_name = 'task_log_n1') AS column_exists;
CREATE INDEX `task_log_n1` ON task_log ( `process_instance_id` ) USING BTREE;

-- changeset zengyadong:ddl-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'act_hi_varinst' AND index_name = 'ACT_IDX_HI_PROCVAR_N1') AS column_exists;
CREATE INDEX `ACT_IDX_HI_PROCVAR_N1` ON act_hi_varinst ( `EXECUTION_ID_`, `TASK_ID_` ) USING BTREE;