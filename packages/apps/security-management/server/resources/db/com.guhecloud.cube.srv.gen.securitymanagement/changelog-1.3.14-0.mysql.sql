-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-securitymanagement`.`water_data_day`
    MODIFY COLUMN `data_time` datetime(0) NULL DEFAULT NULL COMMENT '数据时间' AFTER `total`;
