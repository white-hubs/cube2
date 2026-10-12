-- liquibase formatted sql

-- changeset qqq:1686103358461-1


ALTER TABLE `cube-srv-gen-maintain`.`inspect_task` ADD COLUMN `method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检方式' ;
ALTER TABLE `cube-srv-gen-maintain`.`inspect_plan` ADD COLUMN `method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '巡检方式' ;
ALTER table `cube-srv-gen-maintain`.`inspect_standard` MODIFY COLUMN `remark` varchar(256) DEFAULT NULL COMMENT '备注';
