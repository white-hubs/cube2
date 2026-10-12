-- liquibase formatted sql

-- changeset qqq:1686103358461-1

ALTER TABLE `tj_guide_area_manage` ADD COLUMN `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '状态：启用（enable）、禁用（disable）';
ALTER TABLE `tj_point_manage` ADD COLUMN `level` int(11) NULL DEFAULT NULL COMMENT '顺序';
ALTER TABLE `tj_point_manage` ADD COLUMN `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NULL DEFAULT NULL COMMENT '状态：启用（enable）、禁用（disable）';

