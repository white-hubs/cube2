-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-linkage`.`event` ADD INDEX `event_iot_event_instance_id_IDX`(`iot_event_instance_id`) USING BTREE;