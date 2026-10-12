-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-meattrade`.`meattrade_inspector` ADD COLUMN `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户主键' AFTER `id`;