-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-door`.`event_record`
    ADD UNIQUE INDEX `eventId`(`event_id`) USING BTREE COMMENT '唯一索引';
