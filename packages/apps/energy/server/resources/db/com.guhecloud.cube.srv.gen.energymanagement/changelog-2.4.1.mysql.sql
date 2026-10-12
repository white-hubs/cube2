-- liquibase formatted sql

-- changeset ghy:energy-collect-config-rollover-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'energy_collect_config' AND column_name = 'rollover_value' ) AS column_exists;
ALTER TABLE `energy_collect_config` ADD COLUMN `rollover_value` decimal(65, 2) DEFAULT NULL COMMENT '仪表回卷周期值';

-- changeset ghy:energy-collect-config-rollover-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.COLUMNS WHERE table_schema = DATABASE () AND table_name = 'energy_collect_config' AND column_name = 'max_step_usage' ) AS column_exists;
ALTER TABLE `energy_collect_config` ADD COLUMN `max_step_usage` decimal(65, 2) DEFAULT NULL COMMENT '单次最大合理用量';
