-- liquibase formatted sql

-- changeset zengyadong:alter-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_item_template' AND column_name = 'is_result_item') AS column_exists;
ALTER TABLE inspect_item_template ADD is_result_item TINYINT NULL DEFAULT NULL COMMENT '是否为结果巡检项';


-- changeset zengyadong:alter-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'inspect_standard_item' AND column_name = 'is_result_item') AS column_exists;
ALTER TABLE inspect_standard_item ADD is_result_item TINYINT NULL DEFAULT NULL COMMENT '是否为结果巡检项';
