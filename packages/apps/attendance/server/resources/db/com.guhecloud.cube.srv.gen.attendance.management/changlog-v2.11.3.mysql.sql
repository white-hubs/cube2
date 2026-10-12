-- liquibase formatted sql

-- changeset lijie:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger' AND column_name='work_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger` MODIFY COLUMN `work_duration` decimal(11, 2) UNSIGNED NULL DEFAULT NULL COMMENT '工作日时长（时）';

-- changeset lijie:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger' AND column_name='work_pay_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger` MODIFY COLUMN `work_pay_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '工作时长记为加班费时长';

-- changeset lijie:alter-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger' AND column_name='rest_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger` MODIFY COLUMN `rest_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '休息日加班时长';

-- changeset lijie:alter-4
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger' AND column_name='rest_pat_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger` MODIFY COLUMN `rest_pat_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '休息日加班记加班费时长';

-- changeset lijie:alter-5
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger' AND column_name='fest_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger` MODIFY COLUMN `fest_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '节假日加班时长';

-- changeset lijie:alter-6
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger' AND column_name='fest_oat_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger` MODIFY COLUMN `fest_oat_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '节假日记加班费时长';

-- changeset lijie:alter-7
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger_advance' AND column_name='work_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger_advance` MODIFY COLUMN `work_duration` decimal(11, 2) UNSIGNED NULL DEFAULT NULL COMMENT '工作日时长（时）';

-- changeset lijie:alter-8
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger_advance' AND column_name='work_pay_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger_advance` MODIFY COLUMN `work_pay_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '工作时长记为加班费时长';

-- changeset lijie:alter-9
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger_advance' AND column_name='rest_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger_advance` MODIFY COLUMN `rest_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '休息日加班时长';

-- changeset lijie:alter-10
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger_advance' AND column_name='rest_pat_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger_advance` MODIFY COLUMN `rest_pat_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '休息日加班记加班费时长';

-- changeset lijie:alter-11
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger_advance' AND column_name='fest_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger_advance` MODIFY COLUMN `fest_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '节假日加班时长';

-- changeset lijie:alter-12
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'work_overtime_ledger_advance' AND column_name='fest_oat_duration') AS column_exists;
ALTER TABLE `work_overtime_ledger_advance` MODIFY COLUMN `fest_oat_duration` decimal(11, 2) NULL DEFAULT NULL COMMENT '节假日记加班费时长';