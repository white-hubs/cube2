-- liquibase formatted sql
--
-- changeset qqq:1686103358461-1

ALTER TABLE `cube-biz-ouaa`.`home_application_card` MODIFY COLUMN `application_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '应用主键' AFTER `id`;

ALTER TABLE `cube-biz-ouaa`.`home_application_card` MODIFY COLUMN `application_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号' AFTER `application_id`;

ALTER TABLE `cube-biz-ouaa`.`ouaa_card` MODIFY COLUMN `application_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '应用主键' AFTER `terminal_id`;

ALTER TABLE `cube-biz-ouaa`.`ouaa_card` MODIFY COLUMN `application_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号' AFTER `application_id`;

UPDATE `ouaa_card`
SET application_id = NULL,
application_code = NULL
WHERE `code` = 'historyApp';