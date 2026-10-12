-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-base-alarm`.`alarm_record_target` ADD INDEX `alarm_record_target_tenant_IDX`(`user_id`, `tenant`) USING BTREE;