-- liquibase formatted sql


-- changeset ghy:ddl-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_rule' AND column_name = 'form_group_id';
ALTER TABLE `form_appraisal_rule`
    ADD COLUMN `form_group_id` bigint DEFAULT NULL COMMENT '所属流程表单分组ID' AFTER `responsible_department_ids`;

-- changeset ghy:ddl-2
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record' AND column_name = 'first_level_form_group_id';
ALTER TABLE `form_appraisal_record`
    ADD COLUMN `first_level_form_group_id` bigint DEFAULT NULL COMMENT '一级流程表单分组ID快照' AFTER `responsible_department_names`;

-- changeset ghy:ddl-3
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record' AND column_name = 'first_level_form_group_name';
ALTER TABLE `form_appraisal_record`
    ADD COLUMN `first_level_form_group_name` varchar(200) DEFAULT NULL COMMENT '一级流程表单分组名称快照' AFTER `first_level_form_group_id`;

-- changeset ghy:ddl-4
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record' AND column_name = 'second_level_form_group_id';
ALTER TABLE `form_appraisal_record`
    ADD COLUMN `second_level_form_group_id` bigint DEFAULT NULL COMMENT '二级流程表单分组ID快照' AFTER `first_level_form_group_name`;

-- changeset ghy:ddl-5
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record' AND column_name = 'second_level_form_group_name';
ALTER TABLE `form_appraisal_record`
    ADD COLUMN `second_level_form_group_name` varchar(200) DEFAULT NULL COMMENT '二级流程表单分组名称快照' AFTER `second_level_form_group_id`;

-- changeset ghy:idx-1
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_rule' AND index_name = 'idx_far_rule_form_group_id';
CREATE INDEX `idx_far_rule_form_group_id` ON `form_appraisal_rule` (`form_group_id`);

-- changeset ghy:idx-2
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record' AND index_name = 'idx_far_record_first_group';
CREATE INDEX `idx_far_record_first_group` ON `form_appraisal_record` (`first_level_form_group_id`);

-- changeset ghy:idx-3
-- preconditions onFail:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'form_appraisal_record' AND index_name = 'idx_far_record_second_group';
CREATE INDEX `idx_far_record_second_group` ON `form_appraisal_record` (`second_level_form_group_id`);
