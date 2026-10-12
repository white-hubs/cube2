-- liquibase formatted sql


-- changeset ghy:ddl-deduction-reason-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record_detail' AND column_name = 'deduction_reason_json';
ALTER TABLE `form_appraisal_record_detail`
    ADD COLUMN `deduction_reason_json` text COMMENT '系统生成扣分原因JSON快照' AFTER `matched_range_text`;
