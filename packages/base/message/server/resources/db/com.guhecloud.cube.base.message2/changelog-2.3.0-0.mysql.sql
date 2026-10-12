-- liquibase formatted sql

-- changeset zengyadong:insert-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'msg_record_target' AND column_name='source_tenant' ) AS column_exists;
ALTER TABLE `cube_base_message2`.`msg_record_target` ADD COLUMN `source_tenant` BIGINT ( 20 ) NULL COMMENT '来源租户';