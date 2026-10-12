-- liquibase formatted sql

-- changeset daily-task:rename-daily-record-item-error-flag
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'daily_record_item' AND column_name = 'error_flg';
ALTER TABLE `daily_record_item`
    CHANGE COLUMN `error_flg` `error_flag` tinyint(1) DEFAULT NULL COMMENT '是否异常（0-否，1-是）';

-- changeset daily-task:rename-daily-record-item-error-flag-index
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'daily_record_item' AND index_name = 'idx_error_flg';
ALTER TABLE `daily_record_item` RENAME INDEX `idx_error_flg` TO `idx_error_flag`;

-- changeset daily-task:rename-daily-record-target-item-error-flag-index
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'daily_record_target_item' AND index_name = 'idx_error_flg';
ALTER TABLE `daily_record_target_item` RENAME INDEX `idx_error_flg` TO `idx_error_flag`;
