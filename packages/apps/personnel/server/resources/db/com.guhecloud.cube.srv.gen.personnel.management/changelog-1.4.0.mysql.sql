-- liquibase formatted sql

-- changeset lanchun:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS(SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'staff' AND column_name = 'marital_status') AS column_exists;
ALTER TABLE `staff` ADD COLUMN `marital_status` varchar(30) DEFAULT NULL COMMENT '婚姻状况' AFTER `gender`;

-- changeset lanchun:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS(SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'staff' AND column_name = 'plan_regular_worker_date') AS column_exists;
ALTER TABLE `staff` ADD COLUMN `plan_regular_worker_date` date DEFAULT NULL COMMENT '计划转正日期' AFTER `probation_period`;

-- changeset lanchun:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS(SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'staff' AND column_name = 'age') AS column_exists;
ALTER TABLE `staff` ADD COLUMN `age` int(10) DEFAULT NULL COMMENT '年龄' AFTER `date_of_birth`;

-- changeset lanchun:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS(SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'staff' AND column_name = 'company_years') AS column_exists;
ALTER TABLE `staff` ADD COLUMN `company_years` int(10) DEFAULT NULL COMMENT '司龄' AFTER `induction_date`;

-- changeset lanchun:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS(SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE() AND table_name = 'staff' AND column_name = 'working_years') AS column_exists;
ALTER TABLE `staff` ADD COLUMN `working_years` int(10) DEFAULT NULL COMMENT '工龄' AFTER `pay_social_insurance_first_date`;
