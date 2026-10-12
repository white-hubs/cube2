-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE inspect_record_item MODIFY `item_name` varchar(256) DEFAULT NULL COMMENT '巡检项名称';