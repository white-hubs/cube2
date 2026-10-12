-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-ouaa`.`client` ADD COLUMN `secret_plain` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端密钥明文' AFTER `secret`;

ALTER TABLE `cube-biz-ouaa`.`client` MODIFY COLUMN `scopes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源范围（多个范围之间使用\",\"号隔开）' AFTER `secret_plain`;