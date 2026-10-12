-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'report' AND column_name='departmentId') AS column_exists;
ALTER TABLE `report` ADD COLUMN `department_id` varchar(100) DEFAULT NULL COMMENT '部门id';



-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'report' AND column_name='processInsId') AS column_exists;
ALTER TABLE `report` ADD COLUMN `process_ins_id` varchar(100) DEFAULT NULL COMMENT '流程实例id';

-- changeset zengyadong:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'report' AND column_name='type_id') AS column_exists;
ALTER TABLE report MODIFY COLUMN type_id bigint(32) NULL;

-- changeset zengyadong:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'report' AND column_name='report_pname') AS column_exists;
ALTER TABLE report MODIFY COLUMN report_pname VARCHAR(32) NULL;

-- changeset zengyadong:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'report' AND column_name='status') AS column_exists;
ALTER TABLE report MODIFY COLUMN status varchar(11) NULL;