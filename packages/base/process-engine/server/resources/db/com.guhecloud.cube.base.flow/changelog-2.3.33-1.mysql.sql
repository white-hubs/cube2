-- liquibase formatted sql

-- changeset lijie:index-1
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE () AND table_name = 'process_instance_running' AND index_name = '`start_user_id`' ) AS column_exists;
ALTER TABLE `process_instance_running` ADD INDEX ```start_user_id```(`start_user_id`) USING BTREE;

-- changeset lijie:index-2
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE () AND table_name = 'process_instance_running' AND index_name = '`start_user_name`' ) AS column_exists;
ALTER TABLE `process_instance_running` ADD INDEX ```start_user_name```(`start_user_name`) USING BTREE;

-- changeset lijie:index-3
-- preconditions onFail:CONTINUE
-- precondition-sql-check expectedResult:0 SELECT EXISTS( SELECT 1 FROM information_schema.statistics WHERE table_schema = DATABASE () AND table_name = 'process_instance_running' AND index_name = '`organization_id`' ) AS column_exists;
ALTER TABLE `process_instance_running` ADD INDEX ```organization_id```(`organization_id`) USING BTREE;

