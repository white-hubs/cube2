-- liquibase formatted sql

-- changeset lanchun:ddl-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:1 SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'center_biz_tag_ref' AND column_name='deleted') AS column_exists;
ALTER TABLE `center_biz_tag_ref` MODIFY COLUMN `deleted` bigint(20) UNSIGNED ZEROFILL NOT NULL COMMENT '是否删除' AFTER `business_key`;