-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `cube-srv-gen-content-management`.`notice_config` MODIFY COLUMN `department` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '指定部门' AFTER `user`;

ALTER TABLE `cube-srv-gen-content-management`.`notice_config` MODIFY COLUMN `post` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '指定岗位' AFTER `department`;