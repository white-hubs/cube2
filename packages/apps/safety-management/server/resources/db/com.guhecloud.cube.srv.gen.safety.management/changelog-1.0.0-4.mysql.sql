-- liquibase formatted sql

-- changeset qqq:1686103358461-1


ALTER TABLE `cube_srv_safety_management`.`market_config`  ADD COLUMN `status` tinyint(1) NULL COMMENT '是否开启（影响统计；不影响采集）';