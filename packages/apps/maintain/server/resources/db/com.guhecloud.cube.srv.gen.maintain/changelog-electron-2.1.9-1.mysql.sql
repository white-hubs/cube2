-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-maintain`.patrol_location ADD bim_code varchar(100) NULL COMMENT '巡更点模型编码';
ALTER TABLE `cube-srv-gen-maintain`.patrol_location CHANGE bim_code bim_code varchar(100) NULL COMMENT '巡更点模型编码' AFTER nfc_code;