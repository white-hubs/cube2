-- liquibase formatted sql

-- changeset ht:add-security-device-application-code-tenant-deleted-idx
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.STATISTICS WHERE table_schema = DATABASE() AND table_name = 'security_device' AND index_name = 'application_code_deleted_tenant_idx') AS idx_exists;
ALTER TABLE `security_device`
    ADD INDEX `application_code_deleted_tenant_idx` (`application_code` ASC, `tenant` ASC, `deleted` ASC) USING BTREE;

